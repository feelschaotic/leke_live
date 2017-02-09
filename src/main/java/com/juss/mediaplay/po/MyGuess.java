package com.juss.mediaplay.po;

import java.util.Date;

public class MyGuess {

	private Integer liveId;
	
	private Integer guessingId;
	
	private String guessingTitle;
	
	private String guessingResult;
	
	private String choice;
	
	private Date  betTime;

	public Integer getLiveId() {
		return liveId;
	}

	public void setLiveId(Integer liveId) {
		this.liveId = liveId;
	}

	public Integer getGuessingId() {
		return guessingId;
	}

	public void setGuessingId(Integer guessingId) {
		this.guessingId = guessingId;
	}

	public String getGuessingTitle() {
		return guessingTitle;
	}

	public void setGuessingTitle(String guessingTitle) {
		this.guessingTitle = guessingTitle;
	}

	public String getGuessingResult() {
		return guessingResult;
	}

	public void setGuessingResult(String guessingResult) {
		this.guessingResult = guessingResult;
	}

	
	public String getChoice() {
		return choice;
	}

	public void setChoice(String choice) {
		this.choice = choice;
	}

	public Date getBetTime() {
		return betTime;
	}

	public void setBetTime(Date betTime) {
		this.betTime = betTime;
	}
	
}
