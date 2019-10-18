package com.xy.commonbase.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.xy.commonbase.R;
import com.xy.commonbase.mvp.IPresenter;
import com.xy.commonbase.mvp.IView;

public abstract class BaseMVPFragment<P extends IPresenter> extends BaseFragment implements IView {

    protected Context mContext;

    protected P mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showNormal() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    protected abstract P createPresenter();

    protected abstract int getLayoutResId();

    protected abstract void initView();

    @Override
    protected void initData(){
        requestNetwork();
    }

    protected abstract void requestNetwork();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    public void useNightMode(boolean isNightMode) {

    }

    @Override
    public boolean isShow() {
        return isShow;
    }

    @Override
    public void showEmpty() {

    }
}
