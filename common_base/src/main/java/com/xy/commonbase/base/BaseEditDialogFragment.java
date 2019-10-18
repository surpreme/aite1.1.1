package com.xy.commonbase.base;

import android.graphics.Bitmap;
import android.os.Build;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.xy.commonbase.R;
import com.xy.commonbase.R2;
import com.xy.commonbase.mvp.IPresenter;
import com.xy.commonbase.utils.ViewTintUtil;
import com.xy.commonbase.widget.BlurringView;
import com.yalantis.ucrop.util.BitmapUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public abstract class BaseEditDialogFragment<P extends IPresenter> extends BaseMVPDialogFragment<P> {

    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.input)
    TextInputEditText input;
    @BindView(R2.id.content)
    TextInputLayout content;
    @BindView(R2.id.confirm)
    TextView confirm;
    @BindView(R2.id.cancel)
    TextView cancel;

    protected OnLeftClickListener onLeftClickListener;

    protected OnRightClickListener onRightClickListener;

    public void addListener(OnLeftClickListener onLeftClickListener, OnRightClickListener onRightClickListener) {
        this.onLeftClickListener = onLeftClickListener;
        this.onRightClickListener = onRightClickListener;
    }

    @Override
    protected int getAnim() {
        return R.style.WindowGrowCenterAnimation;
    }

    @Override
    protected int getWidth() {
        return mContext.getResources().getDisplayMetrics().widthPixels / 4 * 3;
    }

    @Override
    protected int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected int getLayout() {
        return R.layout.base_window_edit;
    }

    @Override
    protected void initView() {
        title.setText(getTitle());
        input.setHint(getHint());
        content.setError(getErrorMsg());
        content.setCounterMaxLength(getCount());
        confirm.setText(getLeft());
        cancel.setText(getRight());
        disposable.add(Observable.merge(RxView.clicks(confirm).filter(o -> onLeftClickListener != null).map(o -> 0)
                , RxView.clicks(cancel).filter(o -> onRightClickListener != null).map(o -> 1))
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    switch (integer) {
                        case 0:
                            onLeftClickListener.onClick();
                            dismiss();
                            break;
                        case 1:
                            if (!input.getText().toString().isEmpty()) {
                                requestNetwork(input.getText().toString());
                            } else {
                                onRightClickListener.onClick();
                                dismiss();
                            }
                            break;
                    }
                }));
//        //获取View 的id
//        int bv = mContext.getResources().getIdentifier("blurring_view", "id", mContext.getPackageName());
//        BlurringView mBlurringView = getView().findViewById(bv);
//
//        //给出了模糊视图并刷新模糊视图。
//        mBlurringView.setBlurredView(getView(), -1);
//        mBlurringView.invalidate();
    }

    @Override
    protected void initData() {

    }

    protected abstract void requestNetwork(String edit);

    protected abstract int getCount();

    protected abstract String getErrorMsg();

    protected abstract CharSequence getTitle();

    protected abstract CharSequence getHint();

    protected abstract CharSequence getRight();

    protected abstract CharSequence getLeft();

    public void setTitle(CharSequence title) {
        this.title.setText(title);
    }

    public void setHint(CharSequence content) {
        this.input.setHint(content);
    }

    public void setErrorMsg(CharSequence msg) {
        this.content.setError(msg);
    }

    public void setCount(int count) {
        this.content.setCounterMaxLength(count);
    }

    public interface OnLeftClickListener {
        void onClick();
    }

    public interface OnRightClickListener {
        void onClick();
    }
}
