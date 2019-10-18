package com.xy.commonbase.widget.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.xy.commonbase.R;
import com.xy.commonbase.utils.DensityUtil;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;

    public DividerItemDecoration(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(context.getResources().getColor(R.color.divider_general_shallow));
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int itemCount = parent.getChildCount();
        for (int i = 0; i < itemCount-1; i++) {
            View child = parent.getChildAt(i);
            int cx = child.getWidth() / 2;
            int cy = child.getTop() +child.getHeight()/*+ DensityUtil.dp2px(parent.getContext(),35)/2 -DensityUtil.dp2px(parent.getContext(),5) /2*/ ;
            c.drawRect(0,cy,parent.getMeasuredWidth(),cy+DensityUtil.dp2px(parent.getContext(),1),mPaint);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int index = parent.getChildAdapterPosition(view);
        if (index == 0) {
            outRect.set(0,0,0,0);
        }else if (index == parent.getAdapter().getItemCount() -1){
            outRect.set(0,0,0,0);
        }else
        outRect.set(0, 0, 0, DensityUtil.dp2px(parent.getContext(),1));
    }
}
