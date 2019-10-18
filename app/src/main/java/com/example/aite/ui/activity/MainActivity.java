package com.example.aite.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.aite.R;
import com.example.aite.args.Constant;
import com.example.aite.utils.StatusBarUtils;
import com.example.aite.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ImageView people_img;
    @BindView(R.id.code_out_icon)
    ImageView codeouticon;
    @BindView(R.id.code_go_icon)
    ImageView codegoicon;
    @BindView(R.id.things_fixing_icon)
    ImageView things_fixing_icon;
    @BindView(R.id.msgcenter_icon)
    ImageView msgcenter_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind((Activity) context);
        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Constant.isLogin) {
            startActivity(new Intent(context, LogInActivity.class));
            finish();
        }
    }

    private void initViews() {

        StatusBarUtils.setTransparent(this);
        if (!Constant.isLogin) {
            startActivity(new Intent(context, LogInActivity.class));
            finish();
        }
        if (!TextUtils.isEmpty(Constant.key))
            ToastUtils.showToast(context, Constant.key);
        people_img = findViewById(R.id.people_img);
        codegoicon.setOnClickListener(this);
        codeouticon.setOnClickListener(this);
        things_fixing_icon.setOnClickListener(this);
        msgcenter_icon.setOnClickListener((View.OnClickListener) context);


        people_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.code_out_icon:
                startActivity(new Intent(context, QrCodeActivity.class));
                break;
            case R.id.code_go_icon:
                startActivity(new Intent(context, QrCodeActivity.class));
                break;
            case R.id.things_fixing_icon:
                startActivity(new Intent(context, ThingsFixActivity.class));
                break;
            case R.id.msgcenter_icon:

                startActivity(new Intent(context,MsgCenterActivity.class));
                break;


        }
    }
}
