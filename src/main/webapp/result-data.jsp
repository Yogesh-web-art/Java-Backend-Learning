<%@ page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.springcore.ScoreEvaluator.Evaluation" %>
<%@ page import="java.util.List" %>
<%!
private String jsonEscape(String value) {
    StringBuilder escaped = new StringBuilder();
    for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);
        switch (c) {
            case '"': escaped.append("\\\""); break;
            case '\\': escaped.append("\\\\"); break;
            case '\b': escaped.append("\\b"); break;
            case '\f': escaped.append("\\f"); break;
            case '\n': escaped.append("\\n"); break;
            case '\r': escaped.append("\\r"); break;
            case '\t': escaped.append("\\t"); break;
            default:
                if (c < 0x20) {
                    escaped.append(String.format("\\u%04x", (int) c));
                } else {
                    escaped.append(c);
                }
        }
    }
    return escaped.toString();
}

private void appendArray(StringBuilder json, List<String> values) {
    json.append('[');
    for (int i = 0; i < values.size(); i++) {
        if (i > 0) json.append(',');
        json.append('"').append(jsonEscape(values.get(i))).append('"');
    }
    json.append(']');
}
%>
<%
response.setHeader("Cache-Control", "no-store");
Evaluation evaluation = (Evaluation) session.getAttribute("springCoreResult");
String studentName = (String) session.getAttribute("springCoreStudentName");
if (evaluation == null || studentName == null) {
    out.print("{\"available\":false}");
    return;
}

StringBuilder json = new StringBuilder("{\"available\":true,\"name\":\"")
        .append(jsonEscape(studentName))
        .append("\",\"score\":").append(evaluation.getScore())
        .append(",\"wordCount\":").append(evaluation.getWordCount())
        .append(",\"matchedTopics\":");
appendArray(json, evaluation.getMatchedTopics());
json.append(",\"missedTopics\":");
appendArray(json, evaluation.getMissedTopics());
json.append('}');
out.print(json.toString());
%>
