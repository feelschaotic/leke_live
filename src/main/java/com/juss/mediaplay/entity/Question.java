package com.juss.mediaplay.entity;

/**
 * Created by lenovo on 2016/7/12.
 */
public class Question {

    private String question_user_name;
    private String question_time;
    private String question_content;

    public Question(){}
    public Question(String question_user_name,String question_time,String question_content){
        this.question_content=question_content;
        this.question_time=question_time;
        this.question_user_name=question_user_name;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public String getQuestion_time() {
        return question_time;
    }

    public String getQuestion_user_name() {
        return question_user_name;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public void setQuestion_time(String question_time) {
        this.question_time = question_time;
    }

    public void setQuestion_user_name(String question_user_name) {
        this.question_user_name = question_user_name;
    }
}
