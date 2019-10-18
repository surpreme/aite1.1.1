package com.xy.commonbase.window;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.xy.commonbase.R;
import com.xy.commonbase.base.BaseRecyclerAdapter;

import io.reactivex.disposables.CompositeDisposable;

public class InfoSelectAdapter extends BaseRecyclerAdapter<String, InfoSelectAdapter.ViewHolder> {
    public InfoSelectAdapter(Context mContext, CompositeDisposable disposable) {
        super(mContext, disposable);
    }

    @Override
    protected int getItemView() {
        return R.layout.item_single_text;
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ViewHolder){
            bindData((ViewHolder)holder,mList.get(position));
        }
    }

    private void bindData(ViewHolder holder, String s) {
        holder.view.setText(s);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.item_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextAppearance(R.style.TextAppearance_AppCompat_Menu);
            }else {
               view.setTextAppearance(itemView.getContext(),R.style.TextAppearance_AppCompat_Menu);
            }
            view.setTextColor(ContextCompat.getColor(itemView.getContext(),R.color.review_second_user_color));
        }
    }
}
