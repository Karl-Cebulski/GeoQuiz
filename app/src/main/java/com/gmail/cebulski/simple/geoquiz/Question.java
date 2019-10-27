package com.gmail.cebulski.simple.geoquiz;

public class Question {


    private int textResId;
    private boolean answerTrue;

    public Question(int mTextResId, boolean mAnswerTrue) {
        textResId = mTextResId;
        answerTrue = mAnswerTrue;
    }


    public int getQuestion() {
        return textResId;
    }

    public void setQuestion(int textResId) {
        this.textResId = textResId;
    }


    public boolean isAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(boolean mAnswerTrue) {
        answerTrue = mAnswerTrue;
    }
}
