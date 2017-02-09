package com.juss.mediaplay.po;

import java.util.Date;

public class Planting {
    private Integer plantingId;

    private Integer userId;

    private Date destroyTime;

    private String plantingDesc;

    private Date plantingRelease;

    private byte[] plantingFirstview;

    public Integer getPlantingId() {
        return plantingId;
    }

    public void setPlantingId(Integer plantingId) {
        this.plantingId = plantingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getDestroyTime() {
        return destroyTime;
    }

    public void setDestroyTime(Date destroyTime) {
        this.destroyTime = destroyTime;
    }

    public String getPlantingDesc() {
        return plantingDesc;
    }

    public void setPlantingDesc(String plantingDesc) {
        this.plantingDesc = plantingDesc;
    }

    public Date getPlantingRelease() {
        return plantingRelease;
    }

    public void setPlantingRelease(Date plantingRelease) {
        this.plantingRelease = plantingRelease;
    }

    public byte[] getPlantingFirstview() {
        return plantingFirstview;
    }

    public void setPlantingFirstview(byte[] plantingFirstview) {
        this.plantingFirstview = plantingFirstview;
    }
}