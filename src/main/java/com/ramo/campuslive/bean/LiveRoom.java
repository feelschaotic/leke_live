package com.ramo.campuslive.bean;

/**
 * Created by ramo on 2016/7/8.
 */
public class LiveRoom extends LiveBase {
    private String live_title;
    private double longitude;
    private double latitude;
    private int live_state;


    public LiveRoom() {
        super();
    }
    public LiveRoom(String live_title) {

        super();
        this.live_title = live_title;
    }

    public LiveRoom(String live_title, byte[] cover, User u, int likeNum, double longitude, double latitude, int live_state) {
        super.setCover(cover);
        super.setUser(u);
        super.setLikeNum(likeNum);
        this.live_title = live_title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.live_state = live_state;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getLive_state() {
        return live_state;
    }

    public void setLive_state(int live_state) {
        this.live_state = live_state;
    }

    public String getLive_title() {
        return live_title;
    }

    public void setLive_title(String live_title) {
        this.live_title = live_title;
    }
}
