package com.google.sps.comment;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import java.io.IOException;

/**
 * Represents a Comment written by a user.
 */
public class Comment {
    private String text;
    // The time the comment was submitted. 
    private long timestamp;
    /** 
     * A score given to the comment based on its overall sentiment (Sentiment Score).
     * It can be 0, 1, or 2, with 0 being the most negative and 2 being the most positive.
     */
    private int score;

    public Comment(String message) throws IOException {
        this.text = message;
        this.timestamp = System.currentTimeMillis();
        initializeSentimentScore(message);
    }

    public String getText() {
        return text;
    }

    public long getTime() {
        return timestamp;
    }

    public int getSentimentScore() {
        return score;
    }

    /**
     * Calculates and sets the Sentiment Score of the Comment.
     * The score is rounded to the nearest integer and incremented by 1.
     */
    private void initializeSentimentScore(String message) throws IOException {
        Document doc = Document.newBuilder()
                       .setContent(message)
                       .setType(Document.Type.PLAIN_TEXT)
                       .build();
        LanguageServiceClient languageService = LanguageServiceClient.create();
        Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
        int sentimentScore = Math.round(sentiment.getScore()) + 1;
        languageService.close();
        this.score = sentimentScore;
    }
}
