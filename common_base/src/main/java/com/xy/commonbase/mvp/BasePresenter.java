package com.xy.commonbase.mvp;

import com.xy.commonbase.base.BaseObserver;
import com.xy.commonbase.base.BaseResponse;
import com.xy.commonbase.http.ApiException;
import com.xy.commonbase.utils.RxUtils;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<V extends IView> implements IPresenter<V> {

    //    protected V view;
    protected WeakReference<V> viewRef;
    // 管理订阅关系，用于取消订阅
    protected CompositeDisposable compositeDisposable;

    public BasePresenter() {

    }


    /**
     * 绑定 View ，一般在初始化时调用
     *
     * @param view
     */
    public void attachView(V view) {
        viewRef = new WeakReference<>(view);
        V v = viewRef.get();
//        this.view = view;
    }

    /**
     * 解除绑定 View,一般在 onDestroy 中调用
     */
    public void detachView() {
        this.viewRef = null;
        unsubscribe();
    }

    /**
     * 是否绑定了View,一般在发起网络请求之前调用
     *
     * @return
     */
    public boolean isViewAttached() {
        return viewRef != null &&viewRef.get() != null;
    }

    public V getView() {
        return viewRef.get();
    }


    /**
     * 添加订阅
     */
    public <T>void addSubscribe(Observable<BaseResponse<T>> observable, BaseObserver<T> observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        if (observable == null)
            return;
        BaseObserver baseObserver = observable
                .compose(RxUtils.handlerStatusBean())
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(observer);
        compositeDisposable.add(baseObserver);

    }

    /**
     * 取消订阅
     */
    public void unsubscribe() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    protected boolean fitterResponse(BaseResponse<?> response){
        if (response.getErrorCode() == 200 || response.getErrorCode() == 0)
            return true;
        else
            throw new ApiException(50001,response.getErrorMsg());
    }
}