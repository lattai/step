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
var commentsPerPage = 4;
var changedMaxComments = false;
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

    //Reassigns commentsPerPage from the last load only when a new comment is made
    if (!changedMaxComments && comments.length > 0){
        commentsPerPage = parseInt(comments[0].maxComments);
        document.getElementById("maxComments").value = commentsPerPage;
    }
    if (commentsPerPage <= 0) {
        commentsPerPage = comments.length;
    }

    for (var i = 0; i < commentsPerPage; i ++) {
        console.log("COMMENTSPP After = " + commentsPerPage);
        row = createRowElement();
        row.appendChild(createDataElement(comments[i].name));
        row.appendChild(createDataElement(comments[i].message));
        row.appendChild(createDeleteButton(comments[i]));
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

function changeMaxComments(){
    commentsPerPage = document.getElementById("maxComments").value;
    changedMaxComments = true;
    getComments();
}

function getMaxComments (){
    fetch('/new-comment')
        .then(response => response.text())
        .then(maxComments => {
            options = document.getElementById("maxComments").options;
            for (var i= 0, n= options.length; i < n ; i++) {
                if (parseInt(options[i].value) === parseInt(maxComments)) {
                    document.getElementById("maxComments").selectedIndex = i;
                    commentsPerPage = parseInt(maxComments);
                }
            }
        })
        .then(getComments());
}

function createDeleteButton(comment) {
    const deleteButton = document.createElement('button');
    deleteButton.innerText = 'Delete';
    deleteButton.addEventListener('click', () => {
        deleteComment(comment);
    });
    return deleteButton
}

function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-comment', {method: 'POST', body: params});
  getComments();
}
