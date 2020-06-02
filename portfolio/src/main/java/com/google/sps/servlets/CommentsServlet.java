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

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;

//Servlet that encapsulates the comments form

@WebServlet("/comments")
public final class CommentsServlet extends HttpServlet {
    public ArrayList<Comment> comments = new ArrayList<>();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/josn");
        response.getWriter().println(convertToJsonWithGSon(comments));
    }
  
    private String convertToJsonWithGSon(ArrayList messages) {
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        return json;
  }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Get input from form
        Comment comment = getComment(request);
        comments.add(comment);
        //Redirect back to comments page
        response.sendRedirect("/comments.html");
    }
    private Comment getComment(HttpServletRequest request) {
        Comment comment = new Comment(request.getParameter("comment"));
        return comment;
    }
}