// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<Event> list = new ArrayList(events);
    Collections.sort(list, (eventA, eventB) -> TimeRange.ORDER_BY_START.compare(eventA.getWhen(), eventB.getWhen()));
    ArrayList<TimeRange> unavailableTimeRanges = getUnavailableTimeRanges(events, request.getAttendees());
    Collection<TimeRange> availableTimeRanges = getAvailableTimeRanges(unavailableTimeRanges, request.getDuration());
    return availableTimeRanges;
  }

  private ArrayList<TimeRange> getUnavailableTimeRanges(Collection<Event> events, Collection<String> meetingAttendees) {
    ArrayList<TimeRange> unavailableTimeRanges = new ArrayList<TimeRange>();

    for (Event event: events) {
      if (!Collections.disjoint(meetingAttendees, event.getAttendees())) {
        addTimeRange(unavailableTimeRanges, event.getWhen());
      }
    }

    return unavailableTimeRanges;
  }

  private void addTimeRange(ArrayList<TimeRange> timeRangeArrayList, TimeRange timeRange) {
    int lastPos = timeRangeArrayList.size() - 1;
    if (timeRangeArrayList.isEmpty() || !timeRange.overlaps(timeRangeArrayList.get(lastPos))) {
      timeRangeArrayList.add(timeRange);
    } else {
      TimeRange last = timeRangeArrayList.get(lastPos);
      TimeRange merged = TimeRange.fromStartEnd(last.start(), Math.max(last.end(), timeRange.end()), false);
      timeRangeArrayList.set(lastPos, merged);
    }
  }

  private Collection<TimeRange> getAvailableTimeRanges(ArrayList<TimeRange> unavailableTimeRanges, long duration) {
    Collection<TimeRange> availableTimeRanges = new ArrayList<TimeRange>();
    int start = TimeRange.START_OF_DAY;

    for (TimeRange time: unavailableTimeRanges) {
      if ((time.start() - start) >= duration) {
        availableTimeRanges.add(TimeRange.fromStartEnd(start, time.start(), false));
      }
      start = time.end();
    }
    if ((TimeRange.END_OF_DAY - start) >= duration) {
      availableTimeRanges.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, true));
    }

    return availableTimeRanges;
  }
}
