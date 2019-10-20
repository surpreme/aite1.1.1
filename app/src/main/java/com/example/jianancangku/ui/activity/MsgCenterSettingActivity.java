package com.example.jianancangku.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.jianancangku.R;

import butterknife.ButterKnife;

public class MsgCenterSettingActivity extends BaseActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msgcenter_setting_layout);
        ButterKnife.bind((Activity) context);

    }


    @Override
    public void onClick(View v) {

    }
}
