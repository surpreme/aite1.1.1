package com.xy.commonbase.base;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.jakewharton.rxbinding2.view.RxView;
import com.xy.commonbase.R;
import com.xy.commonbase.R2;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public abstract class BaseTipDialogFragment<T> extends BaseDialogFragment {

    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.content)
    TextView content;
    @BindView(R2.id.confirm)
    TextView confirm;
    @BindView(R2.id.cancel)
    TextView cancel;

    private T t;

    private OnLeftClickListener<T> onLeftClickListener;

    private OnRightClickListener<T> onRightClickListener;

    public void addListener(OnLeftClickListener<T> onLeftClickListener,OnRightClickListener<T> onRightClickListener){
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
        return R.layout.base_window_tips;
    }

    @Override
    protected void initView() {
        title.setText(getTitle());
        content.setText(getContent());
        confirm.setText(getLeft());
        cancel.setText(getRight());
        disposable.add(Observable.merge(RxView.clicks(confirm).filter(o -> onLeftClickListener!=null).map(o -> 0)
                ,RxView.clicks(cancel).filter(o -> onRightClickListener!= null).map(o -> 1))
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    switch (integer){
                        case 0:
                            onLeftClickListener.onClick(t);
                            break;
                        case 1:
                            onRightClickListener.onClick(t);
                            break;
                    }
                    dismiss();
                }));
    }

    @Override
    protected void initData() {

    }

    protected abstract CharSequence getTitle();

    protected abstract CharSequence getContent();

    protected abstract CharSequence getRight();

    protected abstract CharSequence getLeft();

    public void setTitle(CharSequence title) {
        this.title.setText(title);
    }

    public void setContent(CharSequence content) {
        this.content.setText(content);
    }

    public void show(FragmentManager manager, String tag,T t) {
        super.show(manager, tag);
        this.t = t;
    }

    public interface OnLeftClickListener<T>{
        void onClick(T t);
    }

    public interface OnRightClickListener<T>{
        void onClick(T t);
    }

}
