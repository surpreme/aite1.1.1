package com.xy.commonbase.bean;

import android.os.Parcelable;

import com.xy.commonbase.constants.Constants;

import java.io.Serializable;

public class MediaBean implements Serializable {

    private String imageUrl;

    private String videoLink;

    private String type;

    private String[] description;

    @Constants.ImageSizeType
    private int sizeType;

    private boolean play;

    private boolean round;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRound() {
        return round;
    }

    public void setRound(boolean round) {
        this.round = round;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String... description) {
        this.description = description;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }
    @Constants.ImageSizeType
    public int getSizeType() {
        return sizeType;
    }

    public void setSizeType(@Constants.ImageSizeType int sizeType) {
        this.sizeType = sizeType;
    }
}
