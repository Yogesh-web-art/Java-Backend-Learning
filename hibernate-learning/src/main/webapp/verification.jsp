<%@ page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.hibernate.ScoreEvaluator" %>
<%@ page import="com.example.hibernate.ScoreEvaluator.Evaluation" %>
<%
response.setHeader("Cache-Control", "no-store");
if (!"POST".equalsIgnoreCase(request.getMethod())) {
    response.setStatus(405);
    response.setHeader("Allow", "POST");
    out.print("{\"success\":false,\"message\":\"Submit the test using the form.\"}");
    return;
}

request.setCharacterEncoding("UTF-8");
String name = request.getParameter("name");
String email = request.getParameter("email");
String summary = request.getParameter("summary");

String error = null;
Evaluation evaluation = null;
if (name == null || name.isEmpty()) {
    error = "Please enter your name.";
} else if (name.length() > 10) {
    error = "Name must not exceed 10 characters.";
} else if (!name.matches("[A-Za-z]+")) {
    error = "Special characters are not allowed in the name; use English letters only.";
} else if (!Character.isUpperCase(name.charAt(0))) {
    error = "The first letter of the name must be capitalized.";
} else if (email == null || !email.matches("[A-Za-z0-9._%+-]+@gmail\\.com")) {
    error = "Enter a valid email address ending in @gmail.com.";
} else if (summary == null || summary.trim().isEmpty()) {
    error = "Please enter your Hibernate summary.";
} else {
    int words = ScoreEvaluator.countWords(summary);
    if (words > 100) {
        error = "Please shorten your summary to 100 words or fewer.";
    } else {
        evaluation = ScoreEvaluator.evaluate(summary);
        if (evaluation.getMatchedTopics().isEmpty()) {
            error = "Your summary does not explain any recognized Hibernate concepts. Please review the guide and try again.";
        }
    }
}

if (error != null) {
    response.setStatus(400);
    out.print("{\"success\":false,\"message\":\"" + error + "\"}");
    return;
}

session.setAttribute("hibernateResult", evaluation);
session.setAttribute("hibernateStudentName", name);
out.print("{\"success\":true}");
%>
