package com.juss.mediaplay.entity;

/**
 * Created by lenovo on 2016/7/12.
 * 问题回答实体
 */
public class QuestionAnswerEntity {

    private String user_name;//回答者用户名
    private String answer_time;//回答时间
    private String answer_content;//回答内容
    private int user_id;//回答者用户id
    private int user_head;//用户头像

    public QuestionAnswerEntity(){};
    public QuestionAnswerEntity(int user_id, String user_name, int user_head, String answer_time, String answer_content){
        this.answer_content=answer_content;
        this.answer_time=answer_time;
        this.user_head=user_head;
        this.user_id=user_id;
        this.user_name=user_name;
    }

    public void setAnswer_content(String answer_content) {
        this.answer_content = answer_content;
    }

    public void setAnswer_time(String answer_time) {
        this.answer_time = answer_time;
    }

    public void setUser_head(int user_head) {
        this.user_head = user_head;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_head() {
        return user_head;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getAnswer_content() {
        return answer_content;
    }

    public String getAnswer_time() {
        return answer_time;
    }

    public String getUser_name() {
        return user_name;
    }
}
