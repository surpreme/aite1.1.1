package com.example.jianancangku.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.jianancangku.R;

import java.nio.Buffer;

import butterknife.ButterKnife;

public class CallSendThingWorkerActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_thingscar_layout);
        ButterKnife.bind((Activity) context);
    }
}
