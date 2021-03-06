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
import com.google.sps.comment.Comment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content.*/
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    private static final Gson gson = new Gson();
    private static final String COMMENT_INPUT = "comment-input";
    private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<Comment> comments = getCommentList();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(comments));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the input from the form.
        String text = getParameter(request, COMMENT_INPUT, " ");
        Comment newComment = new Comment(text);
        createCommentEntity(newComment);
        response.sendRedirect("/index.html");
    }

    /**
     *  @return the request parameter, or the default value if the parameter
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
    *  Creates an Entity from the Comment and puts it in the Datastore.
    */
    private void createCommentEntity(Comment comment) {
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("text", comment.getText());
        commentEntity.setProperty("time", comment.getTime());
        datastore.put(commentEntity);
    }

    /**
    *  @return an ArrayList<Comment> created from the Entities in the Datastore.
    */
    private ArrayList<Comment> getCommentList() throws IOException {
        Query query = new Query("Comment").addSort("time", SortDirection.DESCENDING);
        PreparedQuery results = datastore.prepare(query);
        ArrayList<Comment> comments = new ArrayList<Comment>();

        for (Entity entity : results.asIterable()) {
            String text = (String) entity.getProperty("text");
            Comment newComment = new Comment(text);
            comments.add(newComment);
        }

        return comments;
    }
}
