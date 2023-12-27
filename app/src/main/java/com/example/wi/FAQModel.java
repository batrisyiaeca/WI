package com.example.wi;

public class FAQModel {

    String faqID, question, answer;

    public FAQModel(){}

    public FAQModel(String faqID, String question, String answer){
        this.faqID = faqID;
        this.question = question;
        this.answer = answer;
    }

    public String getFaqID() { return faqID;}

    public String getQuestion(){
        return question;
    }

    public String getAnswer(){
        return answer;
    }

    public void setFaqID(String faqID) {this.faqID = faqID;}

    public void setQuestion(String question){
        this.question = question;
    }

    public void setAnswer(String answer){
        this.answer = answer;
    }
}
