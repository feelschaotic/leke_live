package com.ramo.campuslive.bean;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable{
    private String name;
    private String msg;
    private	Type type;
    private	Date date;

    public enum Type{
        INCOME, Type, OUTCOME
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
