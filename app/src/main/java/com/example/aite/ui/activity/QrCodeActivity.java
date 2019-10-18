package com.example.aite.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.aite.R;
import com.example.aite.utils.LogUtils;
import com.example.aite.utils.StatusBarUtils;
import com.example.aite.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class QrCodeActivity extends BaseActivity implements QRCodeView.Delegate, View.OnClickListener {
    private ZXingView zXingView;
    private TextView textView;
    private static final int REQUEST_CODE = 453876;
    private Intent data;
    ImageView imageView;
    private long mLastTime = 0;
    private long mCurTime = 0;
    @BindView(R.id.iv_back_qrcode)
     ImageView iv_back;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_layout);
        ButterKnife.bind((Activity) context);
        init();


    }


    @SuppressLint("NewApi")
    private void init() {
        iv_back.setOnClickListener((View.OnClickListener) this);

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
                //如果用下面的方法 如果相册中选择的图片不属于二维码 则会报错
                // 本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
//                List<String> paths=Matisse.obtainPathResult ( data );
//                String as=paths.get ( 0 );
//                zXingView.decodeQRCode ( as );
                this.data = data;
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

    /**
     * 处理扫描结果
     *
     * @param result 摄像头扫码时只要回调了该方法 result 就一定有值，不会为 null。解析本地图片或 Bitmap 时 result 可能为 null
     */
    @Override
    public void onScanQRCodeSuccess(String result) {
        Toast.makeText(this, "扫描结果" + result, Toast.LENGTH_LONG).show();
        LogUtils.d("返回", result);
        startActivity(new Intent(QrCodeActivity.this, ThingsFixActivity.class));

    }

    /**
     * 摄像头环境亮度发生变化
     *
     * @param isDark 是否变暗
     */
    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
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
        switch (v.getId()){
            case R.id.iv_back_qrcode:
            onBackPressed();
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
                Toast.makeText(QrCodeActivity.this, s, Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }

}
