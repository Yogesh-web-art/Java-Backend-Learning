(function () {
  "use strict";

  var status = document.getElementById("result-status");
  var content = document.getElementById("result-content");

  function appendTopics(listId, topics, emptyMessage) {
    var list = document.getElementById(listId);
    if (!topics.length) {
      var empty = document.createElement("li");
      empty.textContent = emptyMessage;
      list.appendChild(empty);
      return;
    }
    topics.forEach(function (topic) {
      var item = document.createElement("li");
      item.textContent = topic;
      list.appendChild(item);
    });
  }

  fetch("result-data.jsp", { credentials: "same-origin", headers: { "Accept": "application/json" } })
    .then(function (response) {
      if (!response.ok) {
        throw new Error("Could not load the result.");
      }
      return response.json();
    })
    .then(function (data) {
      if (!data.available) {
        status.textContent = "There is no submitted result for this browser session. Complete the test first.";
        document.getElementById("return-link").href = "test.html";
        document.getElementById("return-link").textContent = "Go to the Spring JDBC Test";
        return;
      }

      document.getElementById("greeting").textContent = "Well done, " + data.name + "!";
      document.getElementById("score").textContent = data.score;
      document.getElementById("word-summary").textContent = "Summary length: " + data.wordCount + " words.";
      appendTopics("matched-topics", data.matchedTopics, "No concept groups matched yet.");
      appendTopics("missed-topics", data.missedTopics, "All assessed topic groups were recognized.");
      status.textContent = "";
      content.hidden = false;
    })
    .catch(function () {
      status.textContent = "Could not load your result. Please confirm the application server is running and try again.";
      status.classList.add("error");
    });
}());
