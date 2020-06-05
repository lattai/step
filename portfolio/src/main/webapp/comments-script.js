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

/**
 * Fetches the comments input and builds the UI into a list.
 */

var row;

function getComments() {
    const responsePromise = fetch('/list-comments');
    responsePromise.then(handleResponse);
}

function handleResponse(response) {
    const JSON_PROMISE = response.json();
    JSON_PROMISE.then(addCommentToDom);
}

function addCommentToDom(comments) {
    const tableElement = document.getElementById('comments-table');
    tableElement.innerHTML = '<tr><th id = "thName">Name</th><th id = "thMessage">Message</th></tr>';
    
    for (var i = 0; i < comments.length; i ++) {
        row = createRowElement();
        row.appendChild(createDataElement(comments[i].name));
        row.appendChild(createDataElement(comments[i].message));
        tableElement.appendChild(row);
    }
}

// Makes each comment a table data item
function createDataElement(text) {
    const tdElement = document.createElement('td');
    tdElement.innerText = text;
    return tdElement;
}

function createRowElement() {
    const rowElement = document.createElement('tr');
    return rowElement;
}
