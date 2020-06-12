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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class UserAuthServlet extends HttpServlet {

    private static final String TEXT_CONTENT_TYPE = "text/html";
    private static final String LOGIN_URL = "/login.html";
    private static final String INDEX_REDIRECT = "/";
    private static final String TIMESTAMP_PARAMETER = "timestamp";
    private static UserService userService = UserServiceFactory.getUserService();
    private static String logoutUrl = userService.createLogoutURL(LOGIN_URL);
    private static String loginUrl = userService.createLoginURL(INDEX_REDIRECT);
    

  @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_CONTENT_TYPE);
        response.getWriter().println(getWelcome());
        response.getWriter().println(getLoginLogoutLink());

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Comment").addSort("TIMESTAMP_PARAMETER", SortDirection.DESCENDING);
        PreparedQuery results = datastore.prepare(query);
        for (Entity entity : results.asIterable()) {
            String text = (String) entity.getProperty("text");
            String email = (String) entity.getProperty("email");

        }
    }

    public String getWelcome() {
        String userNickname = "stranger";
        if (userService.isUserLoggedIn()) {
            userNickname = userService.getCurrentUser().getNickname();
        }
        return ("<p>Hello " + userNickname + "!</p>");
    }

    public String getLoginLogoutLink() {
        String link = "<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>";
        if (userService.isUserLoggedIn()) {
            link = "<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>";
        }
        return link;
    }

}
