package com.example.springjdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public final class ScoreEvaluator {
    private static final Pattern WORD = Pattern.compile(
            "[\\p{L}\\p{N}]+(?:['’\\-][\\p{L}\\p{N}]+)*");
    private static final List<Topic> TOPICS;

    static {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("JdbcTemplate and JDBC resource management",
                "(?i)\\b(jdbctemplate|jdbc template|jdbc)\\b",
                "(?i)\\b(execut\\w*|manag\\w*|handl\\w*|releas\\w*|clos\\w*|simplif\\w*|perform\\w*)\\b"));
        topics.add(new Topic("DataSource and connection pooling",
                "(?i)\\b(datasource|data source|connection pool|connection pooling)\\b",
                "(?i)\\b(provid\\w*|obtain\\w*|manag\\w*|reus\\w*|borrow\\w*)\\b"));
        topics.add(new Topic("Queries and row mapping",
                "(?i)\\b(rowmapper|row mapper|row mapping|resultsetextractor|beanpropertyrowmapper|queryforobject)\\b",
                "(?i)\\b(map\\w*|convert\\w*|extract\\w*|transform\\w*)\\b"));
        topics.add(new Topic("Named parameters and parameter binding",
                "(?i)\\b(namedparameterjdbctemplate|named parameter|namedparameter|positional parameter)\\b",
                "(?i)\\b(bind\\w*|replac\\w*|associat\\w*|supply\\w*|readab\\w*|match\\w*|saf\\w*)\\b"));
        topics.add(new Topic("SQL injection prevention",
                "(?i)\\b(sql injection|prepared statement|parameterized quer\\w*|parameter binding)\\b",
                "(?i)\\b(prevent\\w*|avoid\\w*|protect\\w*|saf\\w*|secur\\w*|bind\\w*)\\b"));
        topics.add(new Topic("Transactions and rollback",
                "(?i)\\b(transaction\\w*|@transactional|datasourcetransactionmanager|platformtransactionmanager|transactiontemplate)\\b",
                "(?i)\\b(commit\\w*|rollback\\w*|atomic\\w*|group\\w*|consistent\\w*|propagat\\w*|isolat\\w*|coordinat\\w*)\\b"));
        topics.add(new Topic("Exception translation",
                "(?i)\\b(dataaccessexception|data access exception|exception translation|sql exception translator|sqlexceptiontranslator)\\b",
                "(?i)\\b(convert\\w*|wrap\\w*|vendor\\w*|consistent\\w*|abstract\\w*)\\b"));
        topics.add(new Topic("Batch updates and generated keys",
                "(?i)\\b(batch\\w*|generated key\\w*|simplejdbcinsert|simplejdbccall|stored procedure\\w*)\\b",
                "(?i)\\b(bulk\\w*|reduc\\w*|round.?trip\\w*|return\\w*|captur\\w*|insert\\w*|execut\\w*|call\\w*)\\b"));
        TOPICS = Collections.unmodifiableList(topics);
    }

    private ScoreEvaluator() {
    }

    public static int countWords(String summary) {
        int count = 0;
        java.util.regex.Matcher matcher = WORD.matcher(summary);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static Evaluation evaluate(String summary) {
        int wordCount = countWords(summary);
        List<String> matchedTopics = new ArrayList<>();
        List<String> missedTopics = new ArrayList<>();

        for (Topic topic : TOPICS) {
            if (topic.matches(summary)) {
                matchedTopics.add(topic.name);
            } else {
                missedTopics.add(topic.name);
            }
        }

        int lengthPoints = wordCount >= 80 ? 2 : wordCount >= 50 ? 1 : 0;
        int score = Math.min(10, matchedTopics.size() + lengthPoints);
        return new Evaluation(score, wordCount, matchedTopics, missedTopics);
    }

    private static final class Topic {
        private final String name;
        private final Pattern concept;
        private final Pattern explanation;

        private Topic(String name, String concept, String explanation) {
            this.name = name;
            this.concept = Pattern.compile(concept);
            this.explanation = Pattern.compile(explanation);
        }

        private boolean matches(String summary) {
            String[] sentences = summary.split("(?<=[.!?;])\\s+|\\R+");
            for (String sentence : sentences) {
                if (concept.matcher(sentence).find() && explanation.matcher(sentence).find()) {
                    return true;
                }
            }
            return false;
        }
    }

    public static final class Evaluation {
        private final int score;
        private final int wordCount;
        private final List<String> matchedTopics;
        private final List<String> missedTopics;

        private Evaluation(int score, int wordCount, List<String> matchedTopics,
                           List<String> missedTopics) {
            this.score = score;
            this.wordCount = wordCount;
            this.matchedTopics = Collections.unmodifiableList(new ArrayList<>(matchedTopics));
            this.missedTopics = Collections.unmodifiableList(new ArrayList<>(missedTopics));
        }

        public int getScore() {
            return score;
        }

        public int getWordCount() {
            return wordCount;
        }

        public List<String> getMatchedTopics() {
            return matchedTopics;
        }

        public List<String> getMissedTopics() {
            return missedTopics;
        }
    }
}
