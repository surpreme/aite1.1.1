package com.xy.commonbase.window;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;
import com.xy.commonbase.R;
import com.xy.commonbase.R2;
import com.xy.commonbase.base.BaseDialogFragment;
import com.xy.commonbase.utils.DensityUtil;
import com.xy.commonbase.widget.recycler.BaseItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;

public class InfoSelectDialog extends BaseDialogFragment {

    @BindView(R2.id.content_list)
    RecyclerView contentList;
    @BindView(R2.id.button)
    TextView button;
    private ArrayList<String> mList;

    private InfoSelectAdapter mAdapter;

    @Override
    protected int getAnim() {
        return R.style.DialogBottomAnimation;
    }

    @Override
    protected int getWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int getLayout() {
        return R.layout.base_window_select;
    }

    @Override
    protected void initView() {
        contentList.setLayoutManager(new LinearLayoutManager(mContext));
        contentList.setHasFixedSize(true);
        if (contentList.getItemDecorationCount() < 1){
            contentList.addItemDecoration(new BaseItemDecoration(DensityUtil.dp2px(mContext,1),0
                    ,0,0,0,0, ContextCompat.getColor(mContext,R.color.divider_general_shallow),mContext
                    ,0,null));
        }
        contentList.setAdapter(mAdapter);
        mAdapter.addListener((position, s) -> {
            if (onItemClickListener!= null)
                onItemClickListener.click(position,s);
            dismiss();
        });
        disposable.add(RxView.clicks(button)
                .subscribe(o -> dismiss()));
    }

    @Override
    protected void initData() {
        mList = getArguments().getStringArrayList("content");
        if (mList == null)
            dismiss();
        mAdapter = new InfoSelectAdapter(mContext,disposable);
        mAdapter.setData(mList);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void click(int position,String s);
    }
}
