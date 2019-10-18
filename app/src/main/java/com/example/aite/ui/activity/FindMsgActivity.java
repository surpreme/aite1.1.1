package com.example.aite.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aite.R;
import com.example.aite.bean.BaseData;
import com.example.aite.bean.LogInBean;
import com.example.aite.callback.AbsCallback;
import com.example.aite.utils.BeanConvertor;
import com.example.aite.utils.LogUtils;
import com.example.aite.utils.ToastUtils;
import com.example.aite.view.PopWindowsUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.aite.args.Constant.findkeyAddress;

public class FindMsgActivity extends BaseActivity implements View.OnClickListener {
    private Button sendSms_btn;
    @BindView(R.id.iv_back_findmsg)
    ImageView iv_back;
    private int recLen = 10;
    private EditText find_key_phonenumber;
    @BindView(R.id.phonesms_key)
    EditText phonesms_key;
    @BindView(R.id.key_new)
    EditText key_new;
    @BindView(R.id.key_sure)
    EditText key_sure;
    @BindView(R.id.find_key_sure_btn)
    Button find_key_sure_btn;
    @BindView(R.id.findmsg_area_front_txt_id)
    TextView findmsg_area_front_txt_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_msg_layout);
        ButterKnife.bind((Activity) context);

        initViews();
    }



    private void initViews() {
        sendSms_btn = findViewById(R.id.sendSms_btn);
        iv_back = findViewById(R.id.iv_back_findmsg);
        iv_back.setOnClickListener(this);
        find_key_phonenumber = findViewById(R.id.find_key_phonenumber);
        sendSms_btn.setOnClickListener((View.OnClickListener) context);
        findmsg_area_front_txt_id.setOnClickListener(this);
        find_key_sure_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_findmsg:
                startActivity(new Intent(this, LogInActivity.class));
                finish();
                break;
            case R.id.find_key_sure_btn:
                initSurebtn();
//                finish();
                break;
            case R.id.sendSms_btn:
                sendSms_btn.setEnabled(false);
                final Timer timer = new Timer();
                initfindkey();
                @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 1) {
                            if (!(recLen <= 0))
                                sendSms_btn.setText(recLen + " S");
                            if (recLen <= 0) {
                                timer.cancel();
                                recLen = 10;
                                sendSms_btn.setText(R.string.send);
                                sendSms_btn.setEnabled(true);
                            }
                        }
                    }
                };
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        recLen--;
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                };
                timer.schedule(task, 1000, 1000);
                break;
            case R.id.findmsg_area_front_txt_id:
                showPopWindow();
                break;
        }
    }

    private void initSurebtn() {
        String findkeyphonenumber=find_key_phonenumber.getText().toString();
        String phonesmskey=phonesms_key.getText().toString();
        String keynew=key_new.getText().toString();
        String keysure=key_sure.getText().toString();
        if (TextUtils.isEmpty(findkeyphonenumber))
            ToastUtils.showToast(this,"手机号为空");
        if (TextUtils.isEmpty(phonesmskey))
            ToastUtils.showToast(this,"验证码");
        if (TextUtils.isEmpty(keysure))
            ToastUtils.showToast(this,"请输入密码");
        if (TextUtils.isEmpty(keynew)||TextUtils.isEmpty(keysure))
            ToastUtils.showToast(this,"请检查密码");
        if (keynew.equals(keysure)&&!TextUtils.isEmpty(keynew)&&!TextUtils.isEmpty(keysure))
            ToastUtils.showToast(this,"密码相同");


    }

    private void initfindkey() {
        String phonenumber = find_key_phonenumber.getText().toString().trim();
        HttpParams params = new HttpParams();
        params.put("mobile",phonenumber);
        OkGo.<BaseData<LogInBean>>post(findkeyAddress)
                .tag(context)
                .params(params)
                .execute(new AbsCallback<BaseData<LogInBean>>() {
                    @Override
                    public BaseData<LogInBean> convertResponse(okhttp3.Response response) throws Throwable {
                        LogUtils.d(response);
                        assert response.body() != null;
                        final BaseData baseData = BeanConvertor.convertBean(response.body().toString().trim(), BaseData.class);
//                        if (baseData.getCode() == 0) {
//                            ToastUtils.showToast(context, "请填写正确手机号");
//                        }
                        assert baseData != null;
                        LogUtils.d(baseData.getCode()+"");
//                        if (baseData.getCode() == 200) {
                            ToastUtils.showToast(context, baseData.getCode() + "");
//                        }

                        return null;
                    }

                    @Override
                    public void onStart(Request<BaseData<LogInBean>, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<BaseData<LogInBean>> response) {

                    }
                });
    }

    private void showPopWindow() {
        View.OnClickListener v = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findmsg_area_front_txt_id.setText("+86");
                PopWindowsUtils.getmInstance().dismissPopWindow();

            }
        };
        PopWindowsUtils.getmInstance().showDownPopupWindow(
                this,
                findmsg_area_front_txt_id,
                R.layout.found_msg_layout,
                0.9f,
                "+86",
                v);
    }

//    @OnClick(R.id.findmsg_area_front_txt_id)
//    public void onViewClicked() {
//        showPopWindow();
//    }
}
