package com.ramo.campuslive.bean;

/**
 * Created by ramo on 2016/7/1.
 */
public class ContributionList {
    private User user;
    private float money;
    public ContributionList(User u,float money){
        this.user=u;
        this.money=money;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User u) {
        this.user = u;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}
