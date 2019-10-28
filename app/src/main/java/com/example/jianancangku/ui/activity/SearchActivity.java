package com.example.jianancangku.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jianancangku.R;
import com.example.jianancangku.args.Constant;
import com.example.jianancangku.bean.BaseData;
import com.example.jianancangku.bean.MsgCenterbean;
import com.example.jianancangku.bean.Thingbookbean;
import com.example.jianancangku.bean.ThingfixBean;
import com.example.jianancangku.utils.BeanConvertor;
import com.example.jianancangku.utils.LogUtils;
import com.example.jianancangku.utils.SPUtils;
import com.example.jianancangku.utils.ToastUtils;
import com.example.jianancangku.view.adpter.MsgCenterRecyAdapter;
import com.example.jianancangku.view.adpter.ThingbookAdapter;
import com.example.jianancangku.view.adpter.ThingfixAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class SearchActivity extends BaseActivity {
    Unbinder unbinder;
    @BindView(R.id.seach_edit)
    EditText seach_edit;
    @BindView(R.id.sure_seach_btn)
    Button sure_seach_btn;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.main_recy)
    RecyclerView main_recy;
    @BindView(R.id.seacher_recy)
    RecyclerView seacher_recy;

    private List<ThingfixBean.ListBean> recydatalist;
    private ThingfixAdapter thingfixAdapter;
    private ThingbookAdapter thingbookAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seacher_layout);
        init();
    }

    private void init() {
        unbinder = ButterKnife.bind((Activity) context);
        sure_seach_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seach_edit.getText().toString().length() != 0) {
                    getDatas(seach_edit.getText().toString().trim());
                    getWorkerDatas(seach_edit.getText().toString().trim());
                }
                SPUtils.hideKeyboard(seach_edit);
            }
        });
        seach_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                sure_seach_btn.setText("取消");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (seach_edit.getText().toString().length() == 0)
                    sure_seach_btn.setText("取消");
                else
                    sure_seach_btn.setText("搜索");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getWorkerDatas(String package_sn) {
        HttpParams params = new HttpParams();
        params.put("key", Constant.key);
        params.put("p", 1);
        params.put("size", 20);
        params.put("package_sn", package_sn);

        OkGo.<BaseData<Thingbookbean.ListBean>>post(Constant.thingbookAddress)
                .tag(context)
                .params(params)
                .execute(new com.example.jianancangku.callback.AbsCallback<BaseData<Thingbookbean.ListBean>>() {
                    @Override
                    public BaseData<Thingbookbean.ListBean> convertResponse(okhttp3.Response response) throws Throwable {
                        LogUtils.d(response);
                        final BaseData baseData = BeanConvertor.convertBean(response.body().string(), BaseData.class);
                        final Thingbookbean thingbookbean = BeanConvertor.convertBean(baseData.getDatas(), Thingbookbean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                thingbookAdapter = new ThingbookAdapter(context, thingbookbean.getList(), "others");
                                seacher_recy.setAdapter(thingbookAdapter);
                                thingbookAdapter.setGetfixSenderInterface(new ThingbookAdapter.GetfixSenderInterface() {
                                    @Override
                                    public void p(int postion, String type) {

                                    }
                                });
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                                seacher_recy.setLayoutManager(linearLayoutManager);
                                seacher_recy.setItemAnimator(new DefaultItemAnimator());
                            }
                        });

                        return null;
                    }

                    @Override
                    public void onStart(Request<BaseData<Thingbookbean.ListBean>, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<BaseData<Thingbookbean.ListBean>> response) {


                    }
                });

    }

    private void getDatas(String package_sn) {
        HttpParams params = new HttpParams();
        params.put("key", Constant.key);
        params.put("p", 1);
        params.put("size", 30);
        params.put("package_sn", package_sn);

//        workerthingAdrress
        OkGo.<BaseData<ThingfixBean.ListBean>>post(Constant.thingsFixAddress)
                .tag(context)
                .params(params)
                .execute(new AbsCallback<BaseData<ThingfixBean.ListBean>>() {
                    @Override
                    public BaseData<ThingfixBean.ListBean> convertResponse(okhttp3.Response response) throws Throwable {
                        LogUtils.d(response);
                        final BaseData baseData = BeanConvertor.convertBean(response.body().string(), BaseData.class);
                        final ThingfixBean thingfixBean = BeanConvertor.convertBean(baseData.getDatas(), ThingfixBean.class);
                        recydatalist = thingfixBean.getList();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!baseData.isSuccessed())
                                    ToastUtils.showToast(context, "未查询到本订单");
                            }
                        });

                        return null;
                    }

                    @Override
                    public void onStart(Request<BaseData<ThingfixBean.ListBean>, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<BaseData<ThingfixBean.ListBean>> response) {
                        LogUtils.d(recydatalist);
                        if (recydatalist == null) {
                            ToastUtils.showToast(context, "数据错误");
                            return;
                        }
                        thingfixAdapter = new ThingfixAdapter(context, recydatalist);
                        main_recy.setAdapter(thingfixAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        main_recy.setLayoutManager(linearLayoutManager);
                        main_recy.setItemAnimator(new DefaultItemAnimator());

                    }
                });

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
