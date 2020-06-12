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

package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  ArrayList<String> messages = new ArrayList<String>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = convertToJson(messages);
    response.setContentType("application/json;");
    response.getWriter().println(messages);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String text = getParameter(request, "comment-input", "");
    messages.add(text);
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  /**
   * Converts an ArrayList<String> instance into a JSON string using manual String concatenation.
   */
  private String convertToJson(ArrayList<String> list) {
      String json = "{";
      String attribute = "\"message ";

      json += attribute + "\": \"" + list.get(0) + "\"";

      for (int i = 1; i < list.size(); i++) {
          json += ", " + attribute + String.valueOf(i) + "\": ";
          json += "\"" + list.get(i) + "\"";
      }

      json += "}";

      return json;
  }

  /**
   * Converts an ArrayList<String> instance into a JSON string using the Gson library.
   */
  private String convertToJsonUsingGson(ArrayList<String> list) {
    Gson gson = new Gson();
    String json = gson.toJson(list);
    return json;
  }
}
