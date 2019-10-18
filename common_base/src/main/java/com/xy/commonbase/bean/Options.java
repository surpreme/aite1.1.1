package com.xy.commonbase.bean;

import androidx.annotation.IntegerRes;

import com.xy.commonbase.constants.Constants;

public class Options {
    @IntegerRes
    private int imageResource;

    private String[] content;

    private int imageNext;

    @Constants.OptionsType
    private int type;

    public Options(String... content) {
        this(Constants.OptionsType.TEXT,0,0,content);
    }

    public Options(@Constants.OptionsType int type,String... content) {
        this(type,0,0,content);
    }

    public Options(@Constants.OptionsType int type, int imageResource, String... content) {
        this(type,imageResource,0,content);
    }

    public Options(@Constants.OptionsType int type,int imageResource, int imageNext,String... content) {
        this.imageResource = imageResource;
        this.content = content;
        this.imageNext = imageNext;
        this.type = type;
    }


    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    public int getImageNext() {
        return imageNext;
    }

    public void setImageNext(int imageNext) {
        this.imageNext = imageNext;
    }

    @Constants.OptionsType
    public int getType() {
        return type;
    }

    public void setType(@Constants.OptionsType int type) {
        this.type = type;
    }
}
