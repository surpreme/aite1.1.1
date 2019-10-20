package com.example.jianancangku.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.jianancangku.callback.AbsCallback;
import com.example.jianancangku.callback.ICallback;
import com.example.jianancangku.utils.BeanConvertor;
import com.example.jianancangku.utils.LogUtils;
import com.example.jianancangku.utils.OkgoUtils;
import com.example.jianancangku.utils.StatusBarUtils;
import com.example.jianancangku.utils.ToastUtils;
import com.example.jianancangku.view.PopWindowsUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.List;

import butterknife.ButterKnife;

public class LogInActivity extends BaseActivity implements View.OnClickListener {
    private Button loginBtn;
    private TextView number_login_txt, sms_login_txt;
    private TextView phone_area_front_txt_id;
    private ImageView phone_area_front_img_id;
    private EditText number_get_edit, key_get_edit;
    private CheckBox remember_key_checkbox, see_eye_img;
    private ImageView remember_key_img;
    private TextView find_key_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind((Activity) context);
        applypermission();
        initviews();
        LogUtils.d(Constant.LogInAddress);


    }


    private void initviews() {
        StatusBarUtils.setColor(context, Color.WHITE);

        loginBtn = findViewById(R.id.login_btn);
        find_key_txt = findViewById(R.id.find_key_txt);
        find_key_txt.setOnClickListener(this);
        number_login_txt = findViewById(R.id.phone_number_login_txt);
        sms_login_txt = findViewById(R.id.sms_number_login_txt);
        number_login_txt.setOnClickListener(this);
        sms_login_txt.setOnClickListener(this);
        phone_area_front_txt_id = findViewById(R.id.phone_area_front_txt_id);
        phone_area_front_img_id = findViewById(R.id.phone_area_front_img_id);
        phone_area_front_img_id.setOnClickListener(this);

        number_get_edit = findViewById(R.id.number_get_edit);
        key_get_edit = findViewById(R.id.key_get_edit);
        number_get_edit.setHint("手机号码");
        key_get_edit.setHint("登录密码");

        remember_key_checkbox = findViewById(R.id.remember_key_txt);
        remember_key_img = findViewById(R.id.remember_key_img);

        see_eye_img = findViewById(R.id.see_eye_img);
        see_eye_img.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else if (!isChecked) {

                }
            }
        });

        remember_key_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    remember_key_img.setImageDrawable(getResources().getDrawable(R.drawable.correct));
                } else {
                    remember_key_img.setImageDrawable(getResources().getDrawable(R.drawable.corrtect_none));
                }
            }
        });

        loginBtn.setOnClickListener(this);


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
                startActivity(new Intent(context, FindMsgActivity.class));
                finish();
                break;
        }
    }

    private void logInFix() {
        String username = number_get_edit.getText().toString().trim();
        String userkey = key_get_edit.getText().toString().trim();
        if (TextUtils.isEmpty(userkey) || TextUtils.isEmpty(username))
            ToastUtils.showToast(context, "请检查账号信息");
        HttpParams params = new HttpParams();
        params.put("username", Constant.usernumber);
        params.put("password", Constant.userkey);
        params.put("client", Constant.device);
        params.put("device_id", Constant.device_id);
        OkgoUtils<LogInBean> logInBeanOkgoUtils = new OkgoUtils<LogInBean>();
        logInBeanOkgoUtils.loginpost(Constant.LogInAddress, context, params, new ICallback() {
            @Override
            public void onSuccess(String result,List list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(context, MainActivity.class));
                        Constant.isLogin = true;

                        finish();
                    }
                });

            }

            @Override
            public void onFailure(String e) {
                ToastUtils.showToast(context, e);
            }
        });
//        OkGo.<BaseData<LogInBean>>loginpost(Constant.LogInAddress)
//                .tag(context)
//                .params(params)
//                .execute(new AbsCallback<BaseData<LogInBean>>() {
//                    @Override
//                    public BaseData<LogInBean> convertResponse(okhttp3.Response response) throws Throwable {
//                        LogUtils.d(response);
//                        final BaseData baseData = BeanConvertor.convertBean(response.body().string(), BaseData.class);
//                        final LogInBean logInBean = BeanConvertor.convertBean(baseData.getDatas(), LogInBean.class);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                LogUtils.d("getUsername: " + logInBean.getUsername());
//                                Constant.key = logInBean.getKey();
//                                ToastUtils.showToast(context,!TextUtils.isEmpty(Constant.key) ? Constant.key: "null");
//                                LogUtils.d(logInBean.getKey());
//
//                            }
//                        });
//                        if (baseData.getCode() == 200) {
//                            startActivity(new Intent(context, MainActivity.class));
//                            Constant.isLogin = true;
//                            finish();
//                        }
//
//                        return null;
//                    }
//
//                    @Override
//                    public void onStart(Request<BaseData<LogInBean>, ? extends Request> request) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(Response<BaseData<LogInBean>> response) {
//
//                    }
//                });
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
}
