package com.xy.commonbase.base;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.JsonSyntaxException;
import com.xy.commonbase.bean.HasExtraBean;
import com.xy.commonbase.bean.PageBean;
import com.xy.commonbase.constants.Constants;
import com.xy.commonbase.helper.LogHelper;
import com.xy.commonbase.http.ApiException;
import com.xy.commonbase.http.ExceptionHandler;
import com.xy.commonbase.mvp.IView;
import com.xy.commonbase.utils.SharedPreferenceUtil;
import com.xy.commonbase.utils.ToastUtil;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

public abstract class BaseObserver<T> extends DisposableObserver<BaseResponse<T>> {

    private IView baseView;

    private boolean isShowError;

    private String errorMsg;

    public BaseObserver(@NotNull IView baseView) {
        this.baseView = baseView;
    }

    public BaseObserver(@NotNull IView baseView, boolean isShowError) {
        this(baseView);
        this.isShowError = isShowError;
    }

    public BaseObserver(@NotNull IView baseView, String errorMsg, boolean isShowError) {
        this(baseView,isShowError);
        this.errorMsg = errorMsg;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @SuppressWarnings("unchecked")
    @Override
    public void onNext(BaseResponse<T> baseResponse) {
        if (baseView != null) {
            baseView.showNormal();
        }
        int errcode = baseResponse.getErrorCode();
        String errmsg = baseResponse.getErrorMsg();
        String login = baseResponse.getLogin();
        // 兼容 gank api
        boolean isOk = !baseResponse.isError();
        if (errcode == 0 || errcode == 200) {
            if (login != null && login.equals("0") && baseView.isShow()) {
                ARouter.getInstance().build("/user/LoginActivity").navigation();
                SharedPreferenceUtil.remove(Constants.MY_SHARED_PREFERENCE, Constants.TOKEN);
                BaseApplication.token = null;
            } else {
                T data = baseResponse.getData();
                if (data == null) {
                    if (baseView != null)
                        baseView.showEmpty();
                    return;
                } else if (data instanceof List) {
                    List<Object> list = (List<Object>) data;
                    if (list.isEmpty() && baseView != null) {
                        baseView.showEmpty();
                        return;
                    }
                } else if (data instanceof HasExtraBean) {
                    if (((HasExtraBean) data).getHead() == null && ((HasExtraBean) data).getList().isEmpty() && baseView != null) {
                        baseView.showEmpty();
                        return;
                    }else if (((HasExtraBean) data).getHead() instanceof PageBean && ((HasExtraBean) data).getList().isEmpty() && baseView!= null){
                        baseView.showEmpty();
                        return;
                    }
                }
                // 将服务端获取到的正常数据传递给上层调用方
                onSuccess(data);
            }
        } else if (isOk) {   // gank api
            T data = baseResponse.getResults();
            onSuccess(data);
        } else {
            onError(new ApiException(errcode, errmsg));
        }
    }

    /**
     * 回调正常数据
     *
     * @param data
     */
    protected abstract void onSuccess(T data);

    /**
     * 异常处理，包括两方面数据：
     * (1) 服务端没有没有返回数据，HttpException，如网络异常，连接超时;
     * (2) 服务端返回了数据，但 errcode!=0,即服务端返回的data为空，如 密码错误,App登陆超时,token失效
     */
    @Override
    public void onError(Throwable e) {
        ExceptionHandler.handleException(e);
        LogHelper.e(e.toString());
        if (e instanceof JsonSyntaxException) {
            LogHelper.e(e.toString());
        }else if (e instanceof ApiException){
            ToastUtil.show(((ApiException) e).getErrmsg());
        } else if (isShowError) {
            if (errorMsg!= null && !errorMsg.isEmpty())
                baseView.showError(errorMsg);
            else
                baseView.showError(e.getMessage());
        } else {
            baseView.showError();
        }
    }
    @Override
    public void onComplete() {
        if (baseView != null) {
            baseView.hideLoading();
        }
    }


}
