package com.simpledeveloper.businesssrecon.ui;


public class Question {

    private long id;
    private String question;

    public Question(long id, String question) {
        this.id = id;
        this.question = question;
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
}
