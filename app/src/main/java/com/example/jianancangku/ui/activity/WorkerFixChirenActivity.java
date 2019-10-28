package com.example.jianancangku.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.jianancangku.R;
import com.example.jianancangku.args.Constant;
import com.example.jianancangku.bean.BaseData;
import com.example.jianancangku.bean.KeyBean;
import com.example.jianancangku.bean.WorkerAloneBean;
import com.example.jianancangku.bean.WorkerfixBean;
import com.example.jianancangku.callback.AbsCallback;
import com.example.jianancangku.utils.BeanConvertor;
import com.example.jianancangku.utils.LogUtils;
import com.example.jianancangku.utils.ToastUtils;
import com.example.jianancangku.view.PopWindowsUtils;
import com.example.jianancangku.view.adpter.WorkerFixAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WorkerFixChirenActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.worker_height_linear)
    LinearLayout worker_height_linear;
    @BindView(R.id.worker_height_txt)
    TextView worker_height_txt;

    @BindView(R.id.worker_account_linear)
    LinearLayout worker_account_linear;


    @BindView(R.id.worker_name)
    TextView worker_name;

    @BindView(R.id.sure_change_btn)
    Button sure_change_btn;

    @BindView(R.id.account_txt)
    TextView account_txt;


    Unbinder unbinder;
    @BindView(R.id.radiogroup)
    RadioGroup radioGroup;
    @BindView(R.id.radioButton_ok)
    RadioButton radioButton_ok;
    @BindView(R.id.radioButton_un)
    RadioButton radioButton_un;
    private volatile String type;
    private Bundle mBundle;
    private int iswork = 0;
    private int workertype = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_fix_itemlayout);
        unbinder = ButterKnife.bind((Activity) context);
        init();
    }

    private void init() {
        initBundle();
        worker_account_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeAlonyWorkerKeyActivity.class);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sure_change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iswork == 0)
                    return;
                changeKeyPost();
            }
        });
        worker_height_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showbottompop();
            }
        });
//        radioGroup.clearCheck();
//        RadioButton radioButton_ok = (RadioButton) findViewById(R.id.radioButton_ok);
//        radioButton_ok.setChecked(true);
//        RadioButton radioButton_un = (RadioButton) findViewById(R.id.radioButton_un);
//        radioButton_un.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                int id = group.getCheckedRadioButtonId();
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radioButton_ok:
                        iswork = 1;
                        break;
                    case R.id.radioButton_un:
                        iswork = 2;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void changeKeyPost() {
        HttpParams params = new HttpParams();
        params.put("key", Constant.key);
        if (mBundle.getString("warehouse_clerk_id") == null)
            ToastUtils.showToast(context, "数据错误");
        params.put("warehouse_clerk_id", mBundle.getString("warehouse_clerk_id"));
        params.put("type", 1);//1 修改职位和状态 2 修改员工密码
        if (iswork != 0)
            params.put("is_work", iswork);
        if (workertype != 0)
            params.put("position_type", workertype);
        OkGo.<BaseData<KeyBean>>post(Constant.workerfixkeyAdrress)
                .tag(context)
                .params(params)
                .execute(new AbsCallback<BaseData<KeyBean>>() {
                    @Override
                    public BaseData<KeyBean> convertResponse(okhttp3.Response response) throws Throwable {
                        LogUtils.d(response);
                        assert response.body() != null;
                        final BaseData baseData = BeanConvertor.convertBean(response.body().string(), BaseData.class);
                        assert baseData != null;
                        final KeyBean keyBean = BeanConvertor.convertBean(baseData.getDatas(), KeyBean.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast(
                                        context,
                                        baseData.isSuccessed() ? keyBean.getMessage() : keyBean.getError());
                            }
                        });
                        return null;
                    }

                    @Override
                    public void onStart(Request<BaseData<KeyBean>, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<BaseData<KeyBean>> response) {


                    }
                });

    }

    private void showbottompop() {
        PopWindowsUtils.getmInstance().showBottomPopupWindow(context, new PopWindowsUtils.Icallback() {
            @Override
            public String call(String msg) {
                worker_height_txt.setText(msg);
                workertype = msg.equals("入库员") ? 1 : 2;
                return null;
            }
        });
    }

    private void initBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String id = bundle.getString("id");
            if (id != null)
                getDatas(id);
            else ToastUtils.showToast(context, "数据错误 请重试");

        }

    }

    private void getDatas(String id) {
        HttpParams params = new HttpParams();
        params.put("key", Constant.key);
        params.put("warehouse_clerk_id", id);
        OkGo.<BaseData<WorkerAloneBean.InfoBean>>post(Constant.workermainAddress)
                .tag(context)
                .params(params)
                .execute(new AbsCallback<BaseData<WorkerAloneBean.InfoBean>>() {
                    @Override
                    public BaseData<WorkerAloneBean.InfoBean> convertResponse(okhttp3.Response response) throws Throwable {
                        LogUtils.d(response);
                        assert response.body() != null;
                        final BaseData baseData = BeanConvertor.convertBean(response.body().string(), BaseData.class);
                        assert baseData != null;
                        final WorkerAloneBean workerAloneBean = BeanConvertor.convertBean(baseData.getDatas(), WorkerAloneBean.class);
                        assert workerAloneBean != null;
                        String name = workerAloneBean.getInfo().getName();
                        String type = workerAloneBean.getInfo().getType();
                        String warehouse_clerk_id = workerAloneBean.getInfo().getWarehouse_clerk_id();
                        String is_work = workerAloneBean.getInfo().getIs_work();
                        String account_number = workerAloneBean.getInfo().getAccount_number();
                        mBundle = new Bundle();
                        mBundle.putString("name", name);
                        mBundle.putString("warehouse_clerk_id", warehouse_clerk_id);
//                        mBundle.putString("name",name);
//                        mBundle.putString("name",name);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                worker_name.setText(name);
                                worker_height_txt.setText(type.equals("1") ? "入库员" : "出库员");
                                if (type.equals("1"))workertype=1;else workertype=2;
                                if (is_work.equals("1"))iswork=1;else iswork=2;
                                account_txt.setText(account_number);
                                if (is_work.equals("1")) radioButton_ok.setChecked(true);
                                else radioButton_un.setChecked(true);
                                LogUtils.e(is_work);

                            }
                        });
//                        recydatalist = workerfixBean.getList();
//                        LogUtils.d(baseData.getDatas());
                        return null;
                    }

                    @Override
                    public void onStart(Request<BaseData<WorkerAloneBean.InfoBean>, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<BaseData<WorkerAloneBean.InfoBean>> response) {


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
