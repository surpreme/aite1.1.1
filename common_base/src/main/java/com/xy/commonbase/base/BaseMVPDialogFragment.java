package com.xy.commonbase.base;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import com.xy.commonbase.R;
import com.xy.commonbase.mvp.IPresenter;
import com.xy.commonbase.mvp.IView;

/**
 * MVP模式的Base Dialog fragment
 *
 * @author quchao
 * @date 2017/11/28
 */

public abstract class BaseMVPDialogFragment<P extends IPresenter> extends BaseDialogFragment
        implements IView {

    protected P mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null) {
            mPresenter = null;
        }
    }

    @Override
    public void useNightMode(boolean isNightMode) {
    }

    @Override
    public void showNormal() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void reload() {

    }

    public void showLogoutView() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public boolean isShow(){
        return false;
    }

    @Override
    public void showEmpty() {

    }
}
