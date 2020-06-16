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

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// Servlet responsible for updating the greeting 

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {

    private static final String EMAIL_PARAMETER = "email";
    private static final String ID_PARAMETER = "id";
    private static final String NICKNAME_PARAMETER = "nickname";
    private static final String LOGIN_REDIRECT = "/login";
    private static final String TEXT_CONTENT_TYPE = "text/html";
    private static final String SITEUSER_PARAMETER = "SiteUser";
    private static final UserService userService = UserServiceFactory.getUserService();
    private static final User user = userService.getCurrentUser();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      
        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()){
            response.sendRedirect(LOGIN_REDIRECT);
        return;
        }

        // If user is not logged in or has not set a nickname, redirect to login page
        String nickname = getUserNickname(user.getEmail());
        if (nickname == null || nickname.contains("@")) {
            response.sendRedirect(LOGIN_REDIRECT);
            return;
        }
        response.setContentType(TEXT_CONTENT_TYPE);
        // User is logged in and has a nickname, so the request can proceed
        response.reset();
        response.getWriter().println("<h1>Welcome " + nickname + "!</h1>");
    }

    private String getUserNickname(String email) {
        // Takes in the email of the current user, compares it to emails in database to find the corresponding nickname
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(SITEUSER_PARAMETER).setFilter(new Query.FilterPredicate(ID_PARAMETER, Query.FilterOperator.EQUAL, user.getUserId()));
        PreparedQuery results = datastore.prepare(query);
        String nickname = "";
        for (Entity entity : results.asIterable()){
            if (email.equals(entity.getProperty(EMAIL_PARAMETER).toString())){
                nickname = entity.getProperty(NICKNAME_PARAMETER).toString();
            }
        }
        return nickname;
    }
}
