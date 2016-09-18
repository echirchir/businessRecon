package com.simpledeveloper.businessrecon.ui;


public class Question {

    private long id;
    private String question;
    private String createdAt;

    public Question(long id, String question, String createdAt) {
        this.id = id;
        this.question = question;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
