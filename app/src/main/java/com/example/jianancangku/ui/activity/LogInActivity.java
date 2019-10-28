package com.example.jianancangku.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jianancangku.R;
import com.example.jianancangku.args.Constant;
import com.example.jianancangku.bean.BaseData;
import com.example.jianancangku.bean.LogInBean;
import com.example.jianancangku.callback.ICallback;
import com.example.jianancangku.utils.BeanConvertor;
import com.example.jianancangku.utils.HttpHelper;
import com.example.jianancangku.utils.LogUtils;
import com.example.jianancangku.utils.NetworkUtils;
import com.example.jianancangku.utils.OkgoUtils;
import com.example.jianancangku.utils.PhoneDeviceMsgUtils;
import com.example.jianancangku.utils.SharePreferencesHelper;
import com.example.jianancangku.utils.StatusBarUtils;
import com.example.jianancangku.utils.ToastUtils;
import com.example.jianancangku.utils.http.HttpOkgoUtils;
import com.example.jianancangku.view.PopWindowsUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.jianancangku.App.getContext;

public class LogInActivity extends BaseActivity implements View.OnClickListener {
    private Button loginBtn;
    private TextView number_login_txt, sms_login_txt;
    private TextView phone_area_front_txt_id;
    private ImageView phone_area_front_img_id;
    private EditText number_get_edit, key_get_edit;
    private CheckBox remember_key_checkbox, see_eye_img;
    private ImageView remember_key_img;
    private TextView find_key_txt;
    private SharePreferencesHelper sharePreferencesHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        applypermission();
        initviews();
        LogUtils.d(Constant.LogInAddress);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                logInFix();
                break;
            case R.id.phone_number_login_txt:
                number_login_txt.setTextColor(getResources().getColor(R.color.yelllow));
                sms_login_txt.setTextColor(getResources().getColor(R.color.glay));
                phone_area_front_img_id.setVisibility(View.VISIBLE);
                phone_area_front_txt_id.setVisibility(View.VISIBLE);
                number_get_edit.setHint("手机号码");
                key_get_edit.setText("");
                if (number_get_edit.getText().toString().length() != 0)
                    number_get_edit.setText("");
                break;
            case R.id.sms_number_login_txt:
                number_login_txt.setTextColor(getResources().getColor(R.color.glay));
                sms_login_txt.setTextColor(getResources().getColor(R.color.yelllow));
                phone_area_front_img_id.setVisibility(View.GONE);
                phone_area_front_txt_id.setVisibility(View.GONE);
                number_get_edit.setHint("邮箱编号");
                key_get_edit.setText("");
                if (number_get_edit.getText().toString().length() != 0)
                    number_get_edit.setText("");
                break;
            case R.id.phone_area_front_img_id:
                showPopWindow();
                break;
            case R.id.find_key_txt:
                PopWindowsUtils.getmInstance().showfindkeyPopupWindow(context);
//                startActivity(new Intent(context, FindMsgActivity.class));
//                finish();
                break;
        }
    }

    private void logInFix() {
        String username = number_get_edit.getText().toString().trim();
        String userkey = key_get_edit.getText().toString().trim();
        if (TextUtils.isEmpty(userkey) || TextUtils.isEmpty(username))
            ToastUtils.showToast(context, "请检查账号信息");
        if (!NetworkUtils.isNetworkAvailable(context)) {
            ToastUtils.showToast(context, "请检查网络设置");
            return;
        }
        HttpParams params = initHttpParam(username, userkey);
        HttpOkgoUtils.getmInstance().mLogInActivity(context, params, Constant.LogInAddress, username,userkey);


    }

    @Override
    public void onBackPressed() {
//        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public HttpParams initHttpParam(String username, String userkey) {
        HttpParams params = new HttpParams();
        params.put("username", username);
        params.put("password", userkey);
        params.put("client", Constant.device);
        params.put("device_id", PhoneDeviceMsgUtils.getDeviceid(context));
        return params;
    }


    private void showPopWindow() {
        View.OnClickListener v = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_area_front_txt_id.setText("+86");
                PopWindowsUtils.getmInstance().dismissPopWindow();

            }
        };
        PopWindowsUtils.getmInstance().showDownPopupWindow(
                LogInActivity.this,
                phone_area_front_txt_id,
                R.layout.login_layout,
                0.9f,
                "+86",
                v);
    }

    private void initviews() {
        StatusBarUtils.setColor(context, Color.WHITE);

        loginBtn = findViewById(R.id.login_btn);
        find_key_txt = findViewById(R.id.find_key_txt);
        number_login_txt = findViewById(R.id.phone_number_login_txt);
        sms_login_txt = findViewById(R.id.sms_number_login_txt);
        number_get_edit = findViewById(R.id.number_get_edit);
        key_get_edit = findViewById(R.id.key_get_edit);
        remember_key_checkbox = findViewById(R.id.remember_key_txt);
        remember_key_img = findViewById(R.id.remember_key_img);
        see_eye_img = findViewById(R.id.see_eye_img);
        phone_area_front_txt_id = findViewById(R.id.phone_area_front_txt_id);
        phone_area_front_img_id = findViewById(R.id.phone_area_front_img_id);

        number_login_txt.setOnClickListener(this);
        sms_login_txt.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        find_key_txt.setOnClickListener(this);
        phone_area_front_img_id.setOnClickListener(this);

        number_get_edit.setHint("手机号码");
        key_get_edit.setHint("登录密码");

        sharePreferencesHelper = new SharePreferencesHelper(context, "USER_INFO");

        number_get_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sharePreferencesHelper.contain(number_get_edit.getText().toString().trim())) {
                    String key = (String) sharePreferencesHelper.getSharePreference(
                            number_get_edit.getText().toString().trim() + "key",
                            number_get_edit.getText().toString().trim() + "key");
                    key_get_edit.setText(key);
                    remember_key_img.setImageDrawable(getResources().getDrawable(R.drawable.correct));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        remember_key_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharePreferencesHelper = new SharePreferencesHelper(context, "USER_INFO");
                String usernumber = number_get_edit.getText().toString().trim();
                String userkey = key_get_edit.getText().toString().trim();
                if (isChecked) {
                    remember_key_img.setImageDrawable(getResources().getDrawable(R.drawable.correct));
                    if (userkey != null && !TextUtils.isEmpty(userkey)) {
                        if (usernumber != null && !TextUtils.isEmpty(usernumber)) {
                            sharePreferencesHelper.put(usernumber, usernumber);
                            sharePreferencesHelper.put(usernumber + "key", userkey);
                        }
                    }


                } else {
                    remember_key_img.setImageDrawable(getResources().getDrawable(R.drawable.corrtect_none));
                    if (sharePreferencesHelper.contain(usernumber)) {
                        sharePreferencesHelper.remove(usernumber);
                        sharePreferencesHelper.remove(usernumber + "key");
                    }

                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
