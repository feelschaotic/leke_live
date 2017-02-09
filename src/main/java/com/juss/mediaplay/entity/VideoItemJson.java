package com.juss.mediaplay.entity;

import java.util.ArrayList;

public class VideoItemJson {
	private ArrayList<WS> ws = new ArrayList();
	public class WS{
		public ArrayList<CW> cw = new ArrayList();
		
	}
	public class CW{
		private String w;
		public String getW() {
			return w;
		}
		public void setW(String w) {
			this.w = w;
		}
		
	}

	public ArrayList<WS> getWs() {
		return ws;
	}

	public void setWs(ArrayList<WS> ws) {
		this.ws = ws;
	}
}
