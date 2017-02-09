package com.juss.mediaplay.po;

import java.util.Date;

public class UserGuessing extends UserGuessingKey {
    private Float money;

	private Date time;

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}