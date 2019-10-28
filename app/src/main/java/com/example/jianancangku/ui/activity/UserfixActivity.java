package com.example.jianancangku.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jianancangku.R;
import com.example.jianancangku.args.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserfixActivity extends BaseActivity {
    @BindView(R.id.user_number)
    TextView user_number;
    Unbinder unbinder;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.changekey_btn)
    Button changekey_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userfix_mian_layout);
        unbinder = ButterKnife.bind((Activity) context);
        init();
    }

    private void init() {
        changekey_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,ChangeKeyActivity.class));
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Constant.usernumb != null)
            user_number.setText(Constant.usernumb);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {

    }
}
