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
  * Adds filler paragraph when ANY subparagraph is clicked.
   */
var paragraphTitle;

var aboutMe = "Hi! My name is Leah, and I’m a rising sophomore at Gettysburg College in "
    + "their STEM Scholar program. I am a Computer Science major and Mathematics minor, and "
    + "I am on track to complete the Engineering 4-2 program, and would like to pursue a "
    + "career in Software Engineering. I am a member of Girls Who Code, and the National "
    + "Center for Women & Information Technology. My favorite languages to work with are "
    + "Java, Javascript and Python. "
    + "\n\nCurrently, I am a Student Training in Engineering Program (STEP) intern at Google.";
var pastProjects = `Schedify \n\tSocial Networking / Planner webapp using Google Calendar API, final project for Google Computer Science Summer Institute`
    +"\n\tPYTHON HTML CSS "+
    +"\n\tKevin Cam Michael Kelly"+
    "\nGStress"+
    "\n\tHealth and wellness website designed for students of Gettysburg College"+
    "\n\tHTML CSS JS"+
    "\n\tKarla Gonzalez"+  
    "\nUnspoken"+
    "\n\tWebsite designed to teach Sign Language"+
    "\n\tMelissa Wilson Rebecca Kalapala Sabrina Ahmed Reina";
var funFacts = " - I got into CS by doing robotics in middle school!"+
    "\n - My favorite color is Yellow!"+
    "\n - I can wiggle my ears and eyebrows!"+
    "\n - I am the mother to several plants!"+
    "\n - I’m very competitive at Pictionary!";

// Opens information for whichever tab was clicked. 
function openTab(info) {
    const SUBPARAGRAPH_CONTAINER = document.getElementById('about-container');
    const BUTTON_CLICKED = document.getElementById(info);
    //Clears the paragraph text if the header for the section you're already on is clicked.
    if (paragraphTitle == info) {
        SUBPARAGRAPH_CONTAINER.innerText = "";
        paragraphTitle = "";
        BUTTON_CLICKED.className = BUTTON_CLICKED.className.replace(" activeTab", "");
    } else {
        // Clears previously active tab, changes class of newly active tab
        var alreadyOpenTab = document.getElementsByClassName("activeTab");
        if (alreadyOpenTab.length > 0) {
            alreadyOpenTab[0].className = alreadyOpenTab[0].className.replace(" activeTab", "");
        }
    	paragraphTitle = info;
        BUTTON_CLICKED.className = BUTTON_CLICKED.className + " activeTab";
        // Fills content for respective tab opened
        if (paragraphTitle == "about") {
            SUBPARAGRAPH_CONTAINER.innerText =  aboutMe;
        } else if (paragraphTitle == "projects") {
            SUBPARAGRAPH_CONTAINER.innerText = pastProjects;
        } else if (paragraphTitle == "facts") {
            SUBPARAGRAPH_CONTAINER.innerText = funFacts;
        }
    }
}

// Fetch DataServlet.java content
function getData() {
    fetch ('/data').then(response => response.text()).then((quote) => {
        document.getElementById('data-container').innerText = quote;
    });
    fetch ('/data').then(response => response.json()).then((messages) => {
        const messagesElement = document.getElementById('messages-container');
        messagesElement.innerHTML = " ";
        messagesElement.appendChild(
            createListElement('Message 1: ' + messages[0]));
        messagesElement.appendChild(
            createListElement('Message 2: ' + messages[1]));
        messagesElement.appendChild(
            createListElement('Message 3: ' + messages[2]));
        console.log("changed");
    });
}
// Creates list of mesages
function createListElement(text) {
    const liElement = document.createElement('li');
    liElement.innerText = text;
    return liElement;
}
