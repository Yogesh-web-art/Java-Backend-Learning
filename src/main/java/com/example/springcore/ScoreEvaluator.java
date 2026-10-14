package com.example.springcore;

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
        topics.add(new Topic("IoC container",
                "(?i)\\b(ioc|inversion of control|bean ?factory|applicationcontext|spring container)\\b",
                "(?i)\\b(manage|creates?|control|configur|provide|assemble|responsib)\\w*\\b"));
        topics.add(new Topic("Dependency injection",
                "(?i)\\b(dependency injection|\\bdi\\b|constructor injection|setter injection|field injection)\\b",
                "(?i)\\b(inject|supply|provide|wire|decoupl|dependenc)\\w*\\b"));
        topics.add(new Topic("Beans and configuration",
                "(?i)(\\bbeans?\\b|@component\\b|@service\\b|@repository\\b|@configuration\\b|@bean\\b)",
                "(?i)\\b(object|instance|register|configur|define|create|managed)\\w*\\b"));
        topics.add(new Topic("Bean lifecycle and scopes",
                "(?i)\\b(lifecycle|singleton|prototype|request scope|session scope|initializ|destroy)\\w*\\b",
                "(?i)\\b(scope|instance|callback|created|initialized|destroyed|per request)\\b"));
        topics.add(new Topic("Aspect-oriented programming",
                "(?i)\\b(aop|aspect-oriented|aspect|pointcut|join point|advice|proxy)\\b",
                "(?i)\\b(cross.?cutting|transaction|logging|before|after|around|separat)\\w*\\b"));
        topics.add(new Topic("Configuration styles",
                "(?i)\\b(xml|annotation|java.?based|java configuration|@configuration|@bean)\\b",
                "(?i)\\b(configur|define|register|declare|wire)\\w*\\b"));
        topics.add(new Topic("Core container services",
                "(?i)\\b(resource(loader)?|messagesource|internationali[sz]ation|application event|spel|type conversion)\\b",
                "(?i)\\b(load|resolve|publish|listen|translate|convert|evaluate|expression)\\w*\\b"));
        topics.add(new Topic("Testing and integration",
                "(?i)\\b(test\\w*|junit|mock|spring test|integration test)\\b",
                "(?i)\\b(context|container|mock|load|verify|integration|isolat)\\w*\\b"));
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
