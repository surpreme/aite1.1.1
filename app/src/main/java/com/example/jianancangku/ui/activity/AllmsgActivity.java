package com.example.jianancangku.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.jianancangku.R;
import com.example.jianancangku.args.Constant;
import com.example.jianancangku.bean.BaseData;
import com.example.jianancangku.bean.OutedHouseBean;
import com.example.jianancangku.callback.AbsCallback;
import com.example.jianancangku.utils.BeanConvertor;
import com.example.jianancangku.utils.LogUtils;
import com.example.jianancangku.utils.Qrcode.QrCodeUtils;
import com.example.jianancangku.utils.ToastUtils;
import com.example.jianancangku.utils.http.HttpOkgoUtils;
import com.example.jianancangku.view.adpter.OutedHouseAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class AllmsgActivity extends BaseActivity implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.number_txt)
    TextView number_txt;
    @BindView(R.id.time_txt)
    TextView time_txt;
    @BindView(R.id.address_txt)
    TextView address_txt;
    @BindView(R.id.address_icon)
    ImageView address_icon;
    @BindView(R.id.iv_back)
    ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.things_allmsg_layout);
        unbinder = ButterKnife.bind((Activity) context);
        init();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }

    }

    private void init() {
        initViews();
        getData();
    }

    private void initViews() {
        iv_back.setOnClickListener((View.OnClickListener) context);
    }

    private void startAct() {
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @SuppressLint("StaticFieldLeak")
    private void createEnglishQRCode(String number) {
        QrCodeUtils.getmInstance().createEnglishQRCode(context, number, address_icon);
    }

    private void getData() {
        String number = initBundle();
        if (number == null) return;
        HttpParams params = initHttpParams(number);
        HttpOkgoUtils.getmInstance().mAllmsgActivity(context, params, Constant.sendallmsgAdrress);

    }

    private HttpParams initHttpParams(String number) {
        HttpParams params = new HttpParams();
        params.put("key", Constant.key);
        params.put("warehouse_order_id", number);
        params.put("type", 1);//1取件详情 2配送详情
        return params;
    }

    private String initBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return "";
        String number = bundle.getString("number");
        String time = bundle.getString("time");
        String addrress = bundle.getString("addrress");
        createEnglishQRCode(number);
        number_txt.setText(String
                .format(context.getString(R.string.shopitem_number)
                        , number));
        time_txt.setText(String
                .format(context.getString(R.string.shopitem_out_timer)
                        , time));
        address_txt.setText(String
                .format(context.getString(R.string.shopitem_adrress)
                        , addrress));
        if (number != null || !TextUtils.isEmpty(number))
            return number;
        else return "";
    }

}
