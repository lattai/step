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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.time.Instant;

//Servlet that creates a new comment

@WebServlet("/new-comment")
public final class CommentsServlet extends HttpServlet {

    private static final String APPLICATION_TYPE = "application/josn;";
    private static final String COMMENT_PARAMETER = "comment";
    private static final String NAME_PARAMETER = "name";
    private static final String TIMESTAMP_PARAMETER = "timestamp";
    private static final String MAX_COMMENTS_PARAMETER = "maxComments";
    private static String maxComments;


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Create new Comment from input from form
        Comment comment = newComment(request); 
        // Create an Entity
        Entity task = newEntity(comment);
        // Store Entity
        comment.key = storeEntity(task);
        //Redirect back to comments page
        response.sendRedirect("/comments.html");
    }
    

    private Comment newComment(HttpServletRequest request) {
        long timestamp = Instant.now().toEpochMilli();
        maxComments = request.getParameter(MAX_COMMENTS_PARAMETER);
        System.out.println("COMMENTCOUNT " + maxComments);
        Comment comment = new Comment(request.getParameter(NAME_PARAMETER), request.getParameter(COMMENT_PARAMETER), timestamp, maxComments);
        return comment;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;");
        response.getWriter().println(maxComments);
    }

    private Entity newEntity(Comment comment) {
        Entity task = new Entity("Comment");
        task.setProperty(NAME_PARAMETER, comment.getName());
        task.setProperty(COMMENT_PARAMETER, comment.getMessage());
        task.setProperty(TIMESTAMP_PARAMETER, comment.getTimestamp());
        task.setProperty(MAX_COMMENTS_PARAMETER, maxComments);
        return task;
    }

    private Key storeEntity (Entity task) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key key = datastore.put(task);
        return key;
    }
    
}
