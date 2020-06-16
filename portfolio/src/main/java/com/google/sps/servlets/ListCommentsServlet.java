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
import com.google.gson.Gson;
// import com.google.sps.data.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import com.google.gson.Gson;
import java.time.Instant;
import java.util.stream.*;


// To List comments
@WebServlet("/list-comments")
public class ListCommentsServlet extends HttpServlet {

    private static final String APPLICATION_TYPE = "application/josn;";
    private static final String COMMENT_PARAMETER = "comment";
    private static final String NAME_PARAMETER = "name";
    private static final String TIMESTAMP_PARAMETER = "timestamp";
    private static final String MAX_COMMENTS_PARAMETER = "maxComments";

    @Override
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query("Comment").addSort(TIMESTAMP_PARAMETER, SortDirection.DESCENDING);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        ArrayList<Comment> comments = new ArrayList<>();
        // Streams entities to iterate through
        
        Stream<Entity> stream = StreamSupport.stream(results.asIterable().spliterator(), false);
        stream.forEach(entity -> {
            if (!entity.equals(null)){
                Comment comment = newComment(entity);
                comment.setId(entity.getKey().getId());
                comments.add(comment);                
            }
        });
        response.setContentType(APPLICATION_TYPE);
        response.getWriter().println(convertToJsonWithGSon(comments));
    }

    private Comment newComment(Entity entity){
        String name = entity.getProperty(NAME_PARAMETER).toString();
        String message = entity.getProperty(COMMENT_PARAMETER).toString();
        long timestamp = (long) entity.getProperty(TIMESTAMP_PARAMETER);
        String maxComments = entity.getProperty(MAX_COMMENTS_PARAMETER).toString();
        Comment comment = new Comment (name, message, timestamp, maxComments);
        return comment;
    }

    private String convertToJsonWithGSon(ArrayList messages) {
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        return json;
    }
}