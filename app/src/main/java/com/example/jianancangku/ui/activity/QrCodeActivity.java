package com.example.jianancangku.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.jianancangku.R;
import com.example.jianancangku.args.Constant;
import com.example.jianancangku.bean.BaseData;
import com.example.jianancangku.bean.MsgCenterbean;
import com.example.jianancangku.callback.AbsCallback;
import com.example.jianancangku.utils.BeanConvertor;
import com.example.jianancangku.utils.LogUtils;
import com.example.jianancangku.utils.StatusBarUtils;
import com.example.jianancangku.utils.ToastUtils;
import com.example.jianancangku.view.PopWindowsUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class QrCodeActivity extends BaseActivity implements QRCodeView.Delegate, View.OnClickListener {
    private ZXingView zXingView;
    private int popeditnumberrlayoutid = R.layout.qrcode_msg_layout;
    private static String overqr;
    private static final int REQUEST_CODE = 453876;
    private boolean isSuccessed = false;
    @BindView(R.id.iv_back_qrcode)
    ImageView iv_back;
    @BindView(R.id.thingnumber_txt)
    TextView thingnumber_txt;
    private Unbinder unbinder;
    private String qrinterface;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_layout);
        unbinder = ButterKnife.bind((Activity) context);
        init();



    }


    @SuppressLint("NewApi")
    private void init() {
        String a = getIntent().getStringExtra("outinterface");
        assert a != null;
        if (a.equals("go"))
            qrinterface = Constant.qrgohomeAddress;
        if (a.equals("out"))
            qrinterface = Constant.qrouthomeAddress;

        iv_back.setOnClickListener((View.OnClickListener) this);
        thingnumber_txt.setOnClickListener((View.OnClickListener) context);
        StatusBarUtils.setColor(this, getColor(R.color.glay));
        zXingView = findViewById(R.id.zxingview);
        zXingView.setDelegate(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        zXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                new NewMyTask().execute();
            }


        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        zXingView.closeFlashlight();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        zXingView.closeFlashlight();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zXingView.startCamera();
        zXingView.startSpotAndShowRect();
        zXingView.startSpot();
        zXingView.setType(BarcodeType.ALL, null);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        getDatas(result);
//        if (isSuccessed)
//            showPopwindow(1);
//        else
//        Toast.makeText(this, "扫描结果" + result, Toast.LENGTH_LONG).show();
//        LogUtils.d("返回", result);

    }

    private void showPopwindow(int type) {
        //1 success
        // 0 输入框
        // -1 fail
        if (type == 0)
            showeditnumberPopupWindow(context, R.layout.qrcode_layout, 1.0f);
        if (type == -1)
            LogUtils.d("fail");
        if (type == 1) {
            PopWindowsUtils.getmInstance().showcenterPopupWindow(context, R.layout.qrcode_layout, 1.0f);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PopWindowsUtils.getmInstance().dismissPopWindow();
                        }
                    });
                }
            }, 2000);
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        String tipText = zXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            //在这里加入了根据传感器光线暗的时候自动打开闪光灯
            if (!tipText.contains(ambientBrightnessTip)) {
                zXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
                zXingView.openFlashlight();
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                zXingView.getScanBoxView().setTipText(tipText);
            }
        }

    }

    /**
     * 处理打开相机出错
     */
    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtils.showToast(QrCodeActivity.this, "扫描二维码出错");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_qrcode:
                onBackPressed();
                break;
            case R.id.thingnumber_txt:
                showPopwindow(0);
                break;

        }
    }

    class NewMyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (TextUtils.isEmpty(s)) {
                Toast.makeText(QrCodeActivity.this, "未扫描到二维码", Toast.LENGTH_LONG).show();
            } else {
                getDatas(s);

            }
            super.onPostExecute(s);
        }
    }

    private void getDatas(String result) {
        if (TextUtils.isEmpty(qrinterface)||qrinterface==null)
            return;
        HttpParams params = new HttpParams();
        params.put("key", Constant.key);
        params.put("package_sn", result);
        OkGo.<BaseData>post(qrinterface)
                .tag(context)
                .params(params)
                .execute(new AbsCallback<BaseData>() {
                    @Override
                    public BaseData convertResponse(okhttp3.Response response) throws Throwable {
                        LogUtils.d(response);
                        assert response.body() != null;
                        final BaseData baseData = BeanConvertor.convertBean(response.body().string(), BaseData.class);
                        assert baseData != null;
                        isSuccessed = baseData.isSuccessed();
                        ToastUtils.showToast(context, baseData.isSuccessed() + "++++++++++++++");
                        if (baseData.isSuccessed())
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showPopwindow(1);
                                }
                            });
                        return baseData;
                    }

                    @Override
                    public void onStart(Request<BaseData, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<BaseData> response) {


                    }
                });

    }

    public void showeditnumberPopupWindow(final Context context, int fatherLayoutId, float backAlpha) {
        @SuppressLint("InflateParams") View more_icon_view = LayoutInflater.from(context).inflate(popeditnumberrlayoutid, null);
        setBackGroundAlpha(1.0f, context);
        PopupWindow popupWindow = new PopupWindow(more_icon_view, LinearLayout.LayoutParams.WRAP_CONTENT, 450, true);
        popupWindow.setContentView(more_icon_view);
        EditText editText = more_icon_view.findViewById(R.id.edit_number_pop);
        String t = editText.getText().toString().trim();
        Button surebtn = more_icon_view.findViewById(R.id.btn_number_pop);
        surebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getDatas(editText.getText().toString().trim());
                        popupWindow.dismiss();
                    }
                });
            }
        });
        popupWindow.showAtLocation(more_icon_view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackGroundAlpha(1.0f, context);

            }
        });


    }

}
