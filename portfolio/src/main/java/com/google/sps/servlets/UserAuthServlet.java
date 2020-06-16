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
import com.google.appengine.api.users.User;
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
    private static final String NICKNAME_PARAMETER = "nickname";
    private static final String EMAIL_PARAMETER = "email";   
    private static final String ID_PARAMETER = "id"; 
    private static final String USER_PARAMETER = "User";   
    private static final String SITEUSER_PARAMETER = "SiteUser";
    private static final String AT = "@";  
    private static final String DEFAULT_NAME = "stranger";
    private static final String NICKNAME_INPUT = "<form method=\"POST\" action=\"/login\">Choose a username<input type = \"text\" name = \"nickname\"><input type = \"submit\" onclick=\"changeNickname()\"/></form>";   
    private static UserService userService = UserServiceFactory.getUserService();
    private static String logoutUrl = userService.createLogoutURL(LOGIN_URL);
    private static String loginUrl = userService.createLoginURL(INDEX_REDIRECT);
    

  @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_CONTENT_TYPE);
        response.getWriter().println(getWelcome());
        response.getWriter().println(getLoginLogoutLink());
    }

    public String getWelcome() {
        String userNickname = DEFAULT_NAME;
        if (userService.isUserLoggedIn()) {
            userNickname = userService.getCurrentUser().getNickname();
            if (userNickname.contains(AT)){
                return(NICKNAME_INPUT);
            }
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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendRedirect(LOGIN_URL);
            return;
        }

        String nickname = request.getParameter(NICKNAME_PARAMETER);
        String id = userService.getCurrentUser().getUserId();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        // Creates new entity for user
        Entity entity = newEntity(userService.getCurrentUser(), nickname, id);

        // Stores entity
        datastore.put(entity);
        response.sendRedirect(INDEX_REDIRECT);
    }

    private Entity newEntity(User user, String nickname, String id) {
        Entity task = new Entity(SITEUSER_PARAMETER);
        task.setProperty(NICKNAME_PARAMETER, nickname);
        task.setProperty(ID_PARAMETER, id);
        task.setProperty(EMAIL_PARAMETER, user.getEmail());
        return task;
    }
}
