# Hibernate Learning and Assessment

A standalone Java 21 Maven WAR learning site about Hibernate ORM. It contains a guide, an assessment form, server-side JSP validation and scoring, and a results page. The lesson explains Hibernate; this assessment application itself does not connect to a database.

## Build and run

Build with JDK 21 or newer:

```sh
mvn clean package
```

This creates `target/hibernate-learning.war`. Deploy it to the `webapps` directory of a Servlet 4.0-compatible container such as Apache Tomcat 9, start Tomcat, and open:

```text
http://localhost:8080/hibernate-learning/
```

The welcome page is `about.html`. JSP verification requires a servlet container; a static-file preview or `file://` URL cannot process the form.

## Assessment rules

- Name: 1-10 ASCII letters, first letter uppercase, no spaces or punctuation. The verifier gives specific feedback for lowercase initials, special characters, and names longer than 10 characters.
- Email: a valid-looking address ending in `@gmail.com`.
- Summary: required, at most 100 words, and must explain at least one Hibernate concept.
- `verification.jsp` performs validation and scoring. The browser script only shows a word count and sends URL-encoded form values.
- `ScoreEvaluator` checks nine Hibernate topic groups. A topic is recognized when its concept and explanatory context appear in the same sentence. Length adds 1 point for 50-79 words or 2 points for 80-100 words; the total is capped at 10.
- This is a transparent rule-based estimate, not a human or AI semantic grader.

The verifier returns JSON to the form script. A successful assessment is stored in the server session and shown by `result.html` through `result-data.jsp`.
