package com.xy.commonbase.base;

import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xy.commonbase.R;
import com.xy.commonbase.R2;
import com.xy.commonbase.mvp.IPresenter;
import com.xy.commonbase.utils.StatusBarUtils;

import butterknife.BindView;

public abstract class BaseRefreshTitleActivity<P extends IPresenter> extends BaseRefreshActivity<P> {

    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.extra)
    ViewStub extra;
    @BindView(R2.id.toolbar)
    ConstraintLayout toolbar;
    @BindView(R2.id.collapse_layout)
    AppBarLayout collapseLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.base_activity_refresh_title;
    }

    @Override
    protected void initData() {
        super.initData();
        initAdapterAndData();
    }

    @Override
    protected void initRecyclerView() {
        mRefreshLayout = findViewById(R.id.normal_view);
        contentList = findViewById(R.id.content_list);
        contentList.setHasFixedSize(true);
        contentList.setLayoutManager(new LinearLayoutManager(mContext));
        if (contentList.getItemDecorationCount() < 1)
            configRecyclerItemDecorationAndAdapter();
    }

    @Override
    protected void initToolBar() {
        disposable.add(RxView.clicks(back).subscribe(o -> finish()));
        title.setText(getTitleName());
        StatusBarUtils.setHeightAndPadding(mContext, toolbar);
        configTitleExtra(extra);
    }

    protected void configTitleExtra(ViewStub extra){

    }
    protected abstract String getTitleName();

    protected abstract void initAdapterAndData();

    protected abstract void configRecyclerItemDecorationAndAdapter();
}
