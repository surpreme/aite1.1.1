package com.example.jianancangku.ui.activity;

import android.app.Activity;
import android.os.Bundle;
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
import com.example.jianancangku.bean.WorkerfixBean;
import com.example.jianancangku.callback.AbsCallback;
import com.example.jianancangku.utils.BeanConvertor;
import com.example.jianancangku.utils.LogUtils;
import com.example.jianancangku.utils.ToastUtils;
import com.example.jianancangku.view.adpter.ThingfixAdapter;
import com.example.jianancangku.view.adpter.WorkerFixAdapter;
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
import butterknife.Unbinder;

public class WorkerfixActivity extends BaseActivity {
    @BindView(R.id.workerfix_recy)
    RecyclerView workerfix_recy;
    private Unbinder unbinder;
    private List<WorkerfixBean.ListBean> recydatalist;
    private WorkerFixAdapter workerFixAdapter;
    @BindView(R.id.refreshLayout)
     SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.iv_back_workerfix)
    ImageView iv_back_workerfix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_fix_layout);
        unbinder = ButterKnife.bind((Activity) context);
        init();

    }

    private void init() {
        iv_back_workerfix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
    protected void onStart() {
        super.onStart();
        getDatas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }

    private void getDatas() {
        HttpParams params = new HttpParams();
        params.put("key", Constant.key);
        params.put("p", 1);
        params.put("size", 20);
//        params.put("start_time", 10);
//        params.put("end_time", Constant.key);
//        params.put("province_id", 2);
//        params.put("city_id", 10);
//        params.put("package_sn", 2);
        OkGo.<BaseData<WorkerfixBean.ListBean>>post(Constant.workerFixAddress)
                .tag(context)
                .params(params)
                .execute(new AbsCallback<BaseData<WorkerfixBean.ListBean>>() {
                    @Override
                    public BaseData<WorkerfixBean.ListBean> convertResponse(okhttp3.Response response) throws Throwable {
                        LogUtils.d(response);
                        assert response.body() != null;
                        final BaseData baseData = BeanConvertor.convertBean(response.body().string(), BaseData.class);
                        assert baseData != null;
                        final WorkerfixBean workerfixBean = BeanConvertor.convertBean(baseData.getDatas(), WorkerfixBean.class);
                        assert workerfixBean != null;
                        recydatalist = workerfixBean.getList();
                        LogUtils.d(baseData.getDatas());
                        return null;
                    }

                    @Override
                    public void onStart(Request<BaseData<WorkerfixBean.ListBean>, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<BaseData<WorkerfixBean.ListBean>> response) {
                        if (recydatalist == null) {
                            ToastUtils.showToast(context, "数据错误");
                            return;
                        }
                        workerFixAdapter = new WorkerFixAdapter(context, recydatalist);
                        workerfix_recy.setAdapter(workerFixAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        workerfix_recy.setLayoutManager(linearLayoutManager);
                        workerfix_recy.setItemAnimator(new DefaultItemAnimator());

                    }
                });

    }

}
