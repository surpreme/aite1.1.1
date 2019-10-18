package com.xy.commonbase.bean;

import android.graphics.Bitmap;

public class QrCodeBean<T> {

    private T t;

    private Bitmap code;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public Bitmap getCode() {
        return code;
    }

    public void setCode(Bitmap code) {
        this.code = code;
    }
}
