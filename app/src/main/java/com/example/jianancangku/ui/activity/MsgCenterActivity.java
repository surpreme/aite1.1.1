package com.example.jianancangku.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jianancangku.R;
import com.example.jianancangku.args.Constant;
import com.example.jianancangku.bean.BaseData;
import com.example.jianancangku.bean.MsgCenterbean;
import com.example.jianancangku.bean.ThingfixBean;
import com.example.jianancangku.callback.AbsCallback;
import com.example.jianancangku.callback.ICallback;
import com.example.jianancangku.utils.BeanConvertor;
import com.example.jianancangku.utils.LogUtils;
import com.example.jianancangku.utils.OkgoUtils;
import com.example.jianancangku.utils.ToastUtils;
import com.example.jianancangku.view.adpter.MsgCenterRecyAdapter;
import com.example.jianancangku.view.adpter.ThingfixAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        smartRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        smartRefreshLayout.setRefreshHeader(new WaterDropHeader(context));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDatas();
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
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
//        OkgoUtils<ThingfixBean> okgoUtils = new OkgoUtils<ThingfixBean>();
//        okgoUtils.post2(Constant.LogInAddress, context, params, new ICallback() {
//            @Override
//            public void onSuccess(String result, List list) {
//                if (list == null) {
//                    ToastUtils.showToast(context, "数据错误");
//                    return;
//                }
//                List<MsgCenterbean.ListBean> rt=(List<MsgCenterbean.ListBean>) list;
//                recyAdapter = new MsgCenterRecyAdapter(context, rt);
//                msgcenterrecy.setAdapter(recyAdapter);
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//                msgcenterrecy.setLayoutManager(linearLayoutManager);
//                msgcenterrecy.setItemAnimator(new DefaultItemAnimator());
//                LogUtils.d(recydatalist);
//            }
//
//            @Override
//            public void onFailure(String e) {
//                ToastUtils.showToast(context,e);
//
//            }
//        });
        OkGo.<BaseData<MsgCenterbean.ListBean>>post(Constant.msgcenterAddress)
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
