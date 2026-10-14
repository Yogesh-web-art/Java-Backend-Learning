package com.example.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ScoreEvaluator {
    private static final Pattern WORD = Pattern.compile(
            "[\\p{L}\\p{N}]+(?:['’\\-][\\p{L}\\p{N}]+)*");
    private static final List<Topic> TOPICS;

    static {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("ORM and object-relational mapping",
                "(?i)\\b(hibernate orm|object[- ]relational mapping|orm)\\b",
                "(?i)\\b(map\\w*|bridg\\w*|translat\\w*|represent\\w*)\\b"));
        topics.add(new Topic("Entity and identifier mapping",
                "(?i)(@entity\\b|@table\\b|@id\\b|@generatedvalue\\b|\\bentity mapping\\b|\\bidentifier mapping\\b)",
                "(?i)\\b(mark\\w*|declar\\w*|map\\w*|identif\\w*|designat\\w*|associat\\w*)\\b"));
        topics.add(new Topic("SessionFactory and Session",
                "(?i)\\b(sessionfactory|session factory|hibernate session|\\bsession\\b)\\b",
                "(?i)\\b(creat\\w*|open\\w*|manag\\w*|provid\\w*|coordinat\\w*|reus\\w*)\\b"));
        topics.add(new Topic("Entity lifecycle and persistence context",
                "(?i)\\b(transient|persistent|detached|removed|persistence context|dirty checking)\\b",
                "(?i)\\b(track\\w*|synchroniz\\w*|detect\\w*|flush\\w*|chang\\w*|state\\w*)\\b"));
        topics.add(new Topic("CRUD operations and HQL",
                "(?i)\\b(hql|hibernate query language|crud|criteria api|criteria query|\\bjpql\\b)\\b",
                "(?i)\\b(retriev\\w*|insert\\w*|updat\\w*|delet\\w*|filter\\w*|select\\w*|execut\\w*|find\\w*)\\b"));
        topics.add(new Topic("Associations and cascading",
                "(?i)(\\b(association\\w*|relationship\\w*|onetoone|onetomany|manytoone|manytomany|cascade\\w*)\\b|@(OneToOne|OneToMany|ManyToOne|ManyToMany)\\b)",
                "(?i)\\b(connect\\w*|link\\w*|relat\\w*|propagat\\w*|coordinat\\w*|synchroniz\\w*)\\b"));
        topics.add(new Topic("Transactions and atomicity",
                "(?i)(@transactional\\b|\\b(transaction\\w*|hibernate transaction)\\b)",
                "(?i)\\b(begin\\w*|commit\\w*|atomic\\w*|consisten\\w*|group\\w*)\\b"));
        topics.add(new Topic("Fetching strategies and performance",
                "(?i)\\b(lazy loading|eager loading|n\\+1|fetch join|batch fetching|second[- ]level cache|caching)\\b",
                "(?i)\\b(reduc\\w*|avoid\\w*|optim\\w*|defer\\w*|improv\\w*|prevent\\w*)\\b"));
        topics.add(new Topic("Configuration, dialects, and schema generation",
                "(?i)\\b(hibernate\\.cfg\\.xml|hibernate\\.properties|dialect|schema generation|hibernate configuration|database configuration)\\b",
                "(?i)\\b(configur\\w*|select\\w*|adapt\\w*|connect\\w*|creat\\w*|defin\\w*)\\b"));
        TOPICS = Collections.unmodifiableList(topics);
    }

    private ScoreEvaluator() {
    }

    public static int countWords(String summary) {
        int count = 0;
        Matcher matcher = WORD.matcher(summary);
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
