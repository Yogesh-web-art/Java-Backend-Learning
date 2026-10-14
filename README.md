# Spring Core Learning and Assessment

A small Java 25+ Maven WAR application with a Spring Core learning page, a test form, server-side JSP verification, and a results page.

## Run it

Build with JDK 25 or newer and ensure `JAVA_HOME` points to that JDK.

This project uses JSP and the `javax.servlet` API, so deploy it to a Servlet 4.0-compatible container such as Apache Tomcat 9. Build the WAR from the project directory:

```sh
mvn clean package
```

Copy `target/springcore-learning.war` into Tomcat 9's `webapps` directory, start Tomcat, and open:

```text
http://localhost:8080/springcore-learning/
```

The app's welcome page is `about.html`. Open it directly at `/springcore-learning/about.html` if needed. A static-file preview or `file://` URL cannot execute JSP; the test must be submitted through the running servlet container.

## Requirements and scoring

- Name: 1–10 ASCII letters, first letter uppercase, no spaces or punctuation.
- Email: a valid-looking address with a `@gmail.com` domain. For example, `abc@gmail.com` is valid; other domains are rejected.
- Summary: required, maximum 100 words.
- The summary must explain at least one recognized Spring Core topic; listing unrelated words or unmatched keywords is rejected with a summary-specific message.
- All submission validation is performed only in `verification.jsp`. The browser script displays a word count and submits URL-encoded fields but does not enforce the rules.
- Results are calculated by `ScoreEvaluator`: up to 8 points for assessed Spring Core concept groups, plus 1 point for 50–79 words or 2 points for 80–100 words, capped at 10.
- Concept points require both a concept term and explanatory context. This is a deterministic keyword/context rubric, not a claim of full natural-language semantic understanding or human grading.

`verification.jsp` is called asynchronously by the form and does not render a user-facing verification page. The score is stored in the server session and delivered to `result.html` through `result-data.jsp`.
