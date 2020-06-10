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
import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.time.Instant;

public class Comment {
    private final String name;
    private final String message;
    private final long timestamp;
    private Key key;
    private String maxComments;

    public Comment (String name, String message, long timestamp, String maxComments){
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
        this.maxComments = maxComments;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Key getKey(){
        return key;
    }

    public String getMaxComments() {
        return maxComments;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public void setMaxComments(String newMaxComments) {
        maxComments = newMaxComments;
    }

}
