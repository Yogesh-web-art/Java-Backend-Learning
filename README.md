# Spring JDBC Learning and Assessment

A Java 21 Maven WAR application with a Spring JDBC learning page, a test form, server-side JSP verification, and a results page.

## Run it

Build with JDK 21 or newer and ensure `JAVA_HOME` points to that JDK:

```sh
mvn clean package
```

This creates `target/springjdbc-learning.war`. Deploy it to the `webapps` directory of a Servlet 4.0-compatible container such as Apache Tomcat 9, start Tomcat, and open:

```text
http://localhost:8080/springjdbc-learning/
```

The welcome page is `about.html`. Open it at `/springjdbc-learning/about.html` if needed. A static-file preview or `file://` URL cannot run JSP; submit the test through the running servlet container.

## Requirements and scoring

- Name: 1–10 ASCII letters, first letter uppercase, no spaces or punctuation. The verifier returns a field-specific message for lowercase initials, special characters, and names over 10 characters.
- Email: a valid-looking address ending in `@gmail.com`; other domains are rejected.
- Summary: required and limited to 100 words. It must explain at least one recognized Spring JDBC topic.
- All submission validation and scoring happen in `verification.jsp`. The browser script only displays the word count and submits URL-encoded form values.
- `ScoreEvaluator` checks eight Spring JDBC topic groups. A topic earns recognition when its concept and explanatory context appear in the same sentence. Length adds 1 point for 50–79 words or 2 points for 80–100 words. The total is capped at 10.
- This is a transparent keyword/context rubric, not a claim of full natural-language understanding or human grading.

The verification endpoint returns JSON to the form script without displaying an intermediate verification page. The score is stored in the server session and delivered to `result.html` through `result-data.jsp`.
