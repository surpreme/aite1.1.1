package com.xy.commonbase.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import com.airbnb.lottie.LottieAnimationView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.xy.commonbase.R;
import com.xy.commonbase.R2;
import com.xy.commonbase.mvp.IPresenter;
import com.xy.commonbase.utils.ToastUtil;

public abstract class BaseRefreshFragment<P extends IPresenter> extends BaseLazyFragment<P> {


    private static final int NORMAL_STATE = 0;
    private static final int LOADING_STATE = 1;
    public static final int ERROR_STATE = 2;

    private LottieAnimationView mLoadingAnimation;
    private View mErrorView;
    private View mLoadingView;
    private ViewGroup mNormalView;
    private ImageView errorImage;
    private TextView errorText;
    private TextView reloadView;

    private int currentState = NORMAL_STATE;


    @BindView(R2.id.content_list)
    protected RecyclerView contentList;
    @BindView(R2.id.normal_view)
    protected RefreshLayout mRefreshLayout;

    protected int mCurrentPage;

    protected boolean isRefresh;

    protected boolean hasMore;

    @BaseRecyclerAdapter.DataAppendState
    protected int state = BaseRecyclerAdapter.DataAppendState.SHOW;

    @Override
    protected int getLayoutResId() {
        return R.layout.base_fragment_refresh;
    }

    @Override
    protected void initData() {
        initNorView();
        isRefresh = true;
        hasMore = true;
        mCurrentPage = 1;
    }

    protected void initNorView(){
        if (getView() == null)
            return;
        mNormalView = getView().findViewById(R.id.normal_view);
        if (mNormalView == null) {
            throw new IllegalStateException(
                    "The subclass of RootActivity must contain a View named 'mNormalView'.");
        }
        if (!(mNormalView.getParent() instanceof ViewGroup)) {
            throw new IllegalStateException(
                    "mNormalView's ParentView should be a ViewGroup.");
        }
        ViewGroup parent = (ViewGroup) mNormalView.getParent();
        View.inflate(getActivity(), R.layout.base_layout_loading, parent);
        View.inflate(getActivity(), R.layout.base_layout_error, parent);
        mLoadingView = parent.findViewById(R.id.loading_group);
        mErrorView = parent.findViewById(R.id.error_group);
        errorImage = mErrorView.findViewById(R.id.error_image);
        errorText = mErrorView.findViewById(R.id.error_tv);
        reloadView = mErrorView.findViewById(R.id.error_reload_tv);
        reloadView.setOnClickListener(v -> reload());
        mLoadingAnimation = mLoadingView.findViewById(R.id.loading_animation);
        mErrorView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mNormalView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        if (currentState == LOADING_STATE || mLoadingView == null) {
            return;
        }
        hideCurrentView();
        currentState = LOADING_STATE;
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingAnimation.setAnimation("preloader.json");
        mLoadingAnimation.loop(true);
        mLoadingAnimation.playAnimation();
    }

    @Override
    public void showEmpty() {
        if (mCurrentPage >1){
            return;
        }
        if (currentState == ERROR_STATE) {
            return;
        }
        hideCurrentView();
        currentState = ERROR_STATE;
        mErrorView.setVisibility(View.VISIBLE);
        if (reloadView.getVisibility() == View.VISIBLE)
            reloadView.setVisibility(View.INVISIBLE);
        errorImage.setImageResource(getErrorImageResource());
        errorText.setText(getErrorText());
    }

    @DrawableRes
    protected int getErrorImageResource(){
        return R.drawable.ic_load_empty;
    }

    protected String  getErrorText(){
        return "";
    }

    @Override
    public void showError() {
        if (currentState == ERROR_STATE) {
            return;
        }
        hideCurrentView();
        currentState = ERROR_STATE;
        mErrorView.setVisibility(View.VISIBLE);
        if (reloadView.getVisibility() == View.INVISIBLE)
            reloadView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String msg) {
        if (currentState == ERROR_STATE) {
            return;
        }
        hideCurrentView();
        currentState = ERROR_STATE;
        mErrorView.setVisibility(View.VISIBLE);
        if (reloadView.getVisibility() == View.INVISIBLE)
            reloadView.setVisibility(View.VISIBLE);
        errorText.setText(msg);
    }

    @Override
    public void showNormal() {
        if (currentState == NORMAL_STATE) {
            return;
        }
        hideCurrentView();
        currentState = NORMAL_STATE;
        mNormalView.setVisibility(View.VISIBLE);
    }

    private void hideCurrentView() {
        switch (currentState) {
            case NORMAL_STATE:
                if (mNormalView == null) {
                    return;
                }
                mNormalView.setVisibility(View.INVISIBLE);
                break;
            case LOADING_STATE:
                mLoadingAnimation.cancelAnimation();
                mLoadingView.setVisibility(View.GONE);
                break;
            case ERROR_STATE:
                mErrorView.setVisibility(View.GONE);
            default:
                break;
        }
//        mRefreshLayout.finishLoadMore();
//        mRefreshLayout.finishRefresh();
    }

    @Override
    public void reload() {
        showLoading();
        requestNetwork();
    }

    @Override
    public void hideLoading() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh();
            mRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void loadData() {
        showLoading();
        requestNetwork();
    }

    public void doRefresh(){
        mRefreshLayout.autoRefresh();
        mRefreshLayout.finishRefresh(1000);
    }

    @Override
    protected void initView() {
        initRecyclerView();
        contentList.setHasFixedSize(true);
        setRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh();
            mRefreshLayout.finishLoadMore();
        }
    }

    protected void setRefresh() {
        mCurrentPage = 1;
        if (mRefreshLayout == null) {
            return;
        }
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mCurrentPage = 1;
            isRefresh = true;
            state = BaseRecyclerAdapter.DataAppendState.SHOW;
            requestNetwork();
            refreshLayout.finishRefresh(1000);
        });
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mCurrentPage++;
            isRefresh = false;
            state = BaseRecyclerAdapter.DataAppendState.SHOW;
            if (hasMore) {
                requestNetwork();
            }else {
                ToastUtil.show(getContext(),"已经是最后一页啦！！！");
            }
            refreshLayout.finishLoadMore(1000);
        });
    }

    protected abstract void initRecyclerView();
}
