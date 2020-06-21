package com.google.sps.comment;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import java.io.IOException;

public class Comment {
    private String text;
    private long timestamp;
    private int score;

    public Comment(String message) throws IOException {
        this.text = message;
        this.timestamp = System.currentTimeMillis();
        setSentimentScore(message);
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

    private void setSentimentScore(String message) throws IOException {
        Document doc =
        Document.newBuilder().setContent(message).setType(Document.Type.PLAIN_TEXT).build();
        LanguageServiceClient languageService = LanguageServiceClient.create();
        Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
        int sentimentScore = Math.round(sentiment.getScore()) + 1;
        languageService.close();
        this.score = sentimentScore;
    }
}