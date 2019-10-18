package com.example.aite.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aite.R;
import com.example.aite.args.Constant;
import com.example.aite.bean.BaseData;
import com.example.aite.bean.MsgCenterbean;
import com.example.aite.callback.AbsCallback;
import com.example.aite.utils.BeanConvertor;
import com.example.aite.utils.LogUtils;
import com.example.aite.utils.ToastUtils;
import com.example.aite.view.adpter.MsgCenterRecyAdapter;
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

import static com.example.aite.args.Constant.msgcenterAddress;

public class MsgCenterSettingActivity extends BaseActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msgcenter_setting_layout);
        ButterKnife.bind((Activity) context);

    }


    @Override
    public void onClick(View v) {

    }
}
