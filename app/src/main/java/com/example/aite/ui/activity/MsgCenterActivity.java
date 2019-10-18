package com.example.aite.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.aite.R;
import com.example.aite.args.Constant;
import com.example.aite.bean.BaseData;
import com.example.aite.bean.LogInBean;
import com.example.aite.bean.MsgCenterbean;
import com.example.aite.callback.AbsCallback;
import com.example.aite.eventbus.MsgCenterEventbus;
import com.example.aite.utils.BeanConvertor;
import com.example.aite.utils.LogUtils;
import com.example.aite.utils.ToastUtils;
import com.example.aite.view.adpter.MsgCenterRecyAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.aite.args.Constant.LogInAddress;
import static com.example.aite.args.Constant.msgcenterAddress;
import static com.example.aite.args.Constant.usernumber;

public class MsgCenterActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_back_msgcenter)
    ImageView ivbackmsgcenter;
    ImageView setting_icon_msgcenter;
    //    @BindView(R.id.msgcenter_recy)
    RecyclerView msgcenterrecy;
    private List<MsgCenterbean.ListBean> recydatalist;
    private MsgCenterRecyAdapter recyAdapter;
    private SmartRefreshLayout smartRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msgcenter_layout);
        ButterKnife.bind((Activity) context);
//        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        smartRefreshLayout=findViewById(R.id.refreshLayout);
        msgcenterrecy = findViewById(R.id.msgcenter_recy);
        ivbackmsgcenter.setOnClickListener(this);
        setting_icon_msgcenter = findViewById(R.id.setting_icon_msgcenter);
        setting_icon_msgcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(context,MsgCenterSettingActivity.class));
            }
        });
        ToastUtils.showToast(context, !TextUtils.isEmpty(Constant.key) ? Constant.key : "null");


        //下拉刷新
        smartRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        smartRefreshLayout.setRefreshHeader(new WaterDropHeader(context));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDatas();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getDatas();
    }

    private void getDatas() {
                    HttpParams params = new HttpParams();
                    params.put("key", Constant.key);
                    params.put("p", 1);
                    params.put("size", 50);
                    OkGo.<BaseData<MsgCenterbean.ListBean>>post(msgcenterAddress)
                            .tag(context)
                            .params(params)
                            .execute(new AbsCallback<BaseData<MsgCenterbean.ListBean>>() {
                                @Override
                                public BaseData<MsgCenterbean.ListBean> convertResponse(okhttp3.Response response) throws Throwable {
                                    LogUtils.d(response);
                                    final BaseData baseData = BeanConvertor.convertBean(response.body().string(), BaseData.class);
                                    final MsgCenterbean msgCenterbean = BeanConvertor.convertBean(baseData.getDatas(), MsgCenterbean.class);
                                    recydatalist = msgCenterbean.getList();
                                    LogUtils.d(msgCenterbean.getList().get(1).getAdd_time());
                                    return baseData;
                                }

                                @Override
                                public void onStart(Request<BaseData<MsgCenterbean.ListBean>, ? extends Request> request) {

                                }

                                @Override
                                public void onSuccess(Response<BaseData<MsgCenterbean.ListBean>> response) {
                                    LogUtils.d(recydatalist);
                                    if (recydatalist == null) {
                                        ToastUtils.showToast(context, "数据错误");
                                        return;
                                    }
                                    recyAdapter = new MsgCenterRecyAdapter(MsgCenterActivity.this, recydatalist);
                                    msgcenterrecy.setAdapter(recyAdapter);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                                    msgcenterrecy.setLayoutManager(linearLayoutManager);
                                    msgcenterrecy.setItemAnimator(new DefaultItemAnimator());

                                }
                            });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_msgcenter:
                onBackPressed();
                break;
        }
    }

}
