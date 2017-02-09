package com.ramo.campuslive.bean;

import java.util.Date;

/**
 * Created by ramo on 2016/6/20.
 */
public class PaymentsBalance {
    private Date record_date;
    private float record_num;
    private int balance_state;
    private String introduction;

    public PaymentsBalance(float record_num, int balance_state, String introduction, Date record_date) {
        this.record_num = record_num;
        this.balance_state = balance_state;
        this.introduction = introduction;
        this.record_date = record_date;
    }

    public Date getRecord_date() {
        return record_date;
    }

    public void setRecord_date(Date record_date) {
        this.record_date = record_date;
    }

    public float getRecord_num() {
        return record_num;
    }

    public void setRecord_num(float record_num) {
        this.record_num = record_num;
    }

    public int getBalance_state() {
        return balance_state;
    }

    public void setBalance_state(int balance_state) {
        this.balance_state = balance_state;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
