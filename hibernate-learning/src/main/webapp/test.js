(function () {
  "use strict";

  var form = document.getElementById("test-form");
  var summary = document.getElementById("summary");
  var wordCount = document.getElementById("word-count");
  var status = document.getElementById("form-status");
  var wordPattern = /[\p{L}\p{N}]+(?:['’-][\p{L}\p{N}]+)*/gu;

  function countWords(text) {
    return (text.match(wordPattern) || []).length;
  }

  function updateWordCount() {
    wordCount.textContent = countWords(summary.value) + " / 100 words";
  }

  summary.addEventListener("input", updateWordCount);
  updateWordCount();

  form.addEventListener("submit", function (event) {
    event.preventDefault();
    status.textContent = "";
    status.className = "status";

    var submitButton = form.querySelector('button[type="submit"]');
    submitButton.disabled = true;
    status.textContent = "Checking your submission...";
    var requestBody = new URLSearchParams({
      name: document.getElementById("name").value,
      email: document.getElementById("email").value,
      summary: summary.value
    });

    fetch(form.action, {
      method: "POST",
      body: requestBody,
      credentials: "same-origin",
      headers: { "Accept": "application/json" }
    }).then(function (response) {
      return response.json().then(function (data) {
        if (!response.ok && data.success !== false) {
          throw new Error("The server could not process the submission. Please try again.");
        }
        return data;
      });
    }).then(function (data) {
      if (data.success) {
        window.location.assign("result.html");
        return;
      }
      status.textContent = data.message || "Please check the form and try again.";
      status.classList.add("error");
      submitButton.disabled = false;
    }).catch(function () {
      status.textContent = "Could not reach the verifier. Check that the application server is running, then try again.";
      status.classList.add("error");
      submitButton.disabled = false;
    });
  });
}());
