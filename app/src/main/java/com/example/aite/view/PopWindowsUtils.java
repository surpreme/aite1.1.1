package com.example.aite.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aite.R;

public class PopWindowsUtils {
    private static PopWindowsUtils mInstance;
    private int popDialoglayoutId = R.layout.number_poplayout;
    private PopupWindow popupWindow;

    public static PopWindowsUtils getmInstance() {
        if (mInstance == null) {
            synchronized (PopWindowsUtils.class) {
                if (mInstance == null) {
                    mInstance = new PopWindowsUtils();
                }
            }
        }
        return mInstance;
    }

    PopWindowsUtils() {

    }

    public void showDownPopupWindow(final Context context, View mView, int fatherLayoutId, float backAlpha, String txt, View.OnClickListener numberClick) {
        /**
         * 解析出使用的布局
         */
        @SuppressLint("InflateParams") View more_icon_view = LayoutInflater.from(context).inflate(popDialoglayoutId, null);
        /**
         * 设置背影 这里让父布局变背景颜色
         * 最佳是获得屏幕管理者
         */
        setBackGroundAlpha(0.5f, context);
        //如果代码和xml同时指定布局的位置高宽度等 以代码为准
        /**
         * 构造传入的参数
         */
        popupWindow = new PopupWindow(more_icon_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        /**
         * 将布局设置上去
         */
        popupWindow.setContentView(more_icon_view);
        /**
         * 布局中的ui控件
         */
        TextView topTxt = more_icon_view.findViewById(R.id.phone_header_txt);
        topTxt.setText(txt);
        topTxt.setOnClickListener(numberClick);

        popupWindow.showAsDropDown(mView);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // relativeLayout.setBackgroundColor ( getResources ().getColor ( R.color.mycolor ) );
                setBackGroundAlpha(1.0f, context);

            }
        });

    }


    public void showPhoneNumberPopupWindow(final Context context, View mView, int fatherLayoutId, float backAlpha, String txt, View.OnClickListener numberClick) {
        setBackGroundAlpha(backAlpha, context);
        View view = LayoutInflater.from(context).inflate(popDialoglayoutId, null);
        popupWindow = new PopupWindow(view, 90, 30, true);
        popupWindow.setContentView(view);
        TextView topTxt = view.findViewById(R.id.phone_header_txt);
        topTxt.setText(txt);
        topTxt.setOnClickListener(numberClick);
        View rootView = LayoutInflater.from(context).inflate(fatherLayoutId, null);
//        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        popupWindow.showAsDropDown(mView, 5, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackGroundAlpha(1.0f, context);

            }
        });


    }

    //    /**
//     *
//     * @param context
//     * @param backAlpha
//     * @param mView
//     * @param x
//     * @param y
//     * @param OneLaoyoutClick
//     * @param TwoLayoutClick
//     * @param ThreeLayoutClick
//     * @param oneBmp
//     * @param twoBmp
//     * @param threeBmp
//     * @param oneString
//     * @param twoString
//     * @param threeString
//     */
//    void showViewPopupWindow(final Context context, float backAlpha, View mView,int x,int y, View.OnClickListener OneLaoyoutClick, View.OnClickListener TwoLayoutClick, View.OnClickListener ThreeLayoutClick, Bitmap oneBmp,Bitmap twoBmp,Bitmap threeBmp,String oneString,String twoString,String threeString) {
//        setBackGroundAlpha(backAlpha, context);
//        View ContextLayoutView = LayoutInflater.from(context).inflate(popViewBottomlayoutId, null);
//        popupWindow = new PopupWindow(ContextLayoutView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setContentView(ContextLayoutView);
//        LinearLayout oneLayout=ContextLayoutView.findViewById(R.id.one_layout_id);
//        LinearLayout twoLayout=ContextLayoutView.findViewById(R.id.two_layout_id);
//        LinearLayout threeLayout=ContextLayoutView.findViewById(R.id.three_layout_id);
//        TextView oneTxt=ContextLayoutView.findViewById(R.id.one_txt_id);
//        TextView twoTxt=ContextLayoutView.findViewById(R.id.two_txt_id);
//        TextView threeTxt=ContextLayoutView.findViewById(R.id.three_txt_id);
//        ImageView oneImg=ContextLayoutView.findViewById(R.id.one_img_id);
//        ImageView twoImg=ContextLayoutView.findViewById(R.id.two_img_id);
//        ImageView threeImg=ContextLayoutView.findViewById(R.id.three_img_id);
//        oneLayout.setOnClickListener(OneLaoyoutClick);
//        twoLayout.setOnClickListener(TwoLayoutClick);
//        threeLayout.setOnClickListener(ThreeLayoutClick);
//        oneTxt.setText(oneString);
//        twoTxt.setText(twoString);
//        threeTxt.setText(threeString);
//        oneImg.setImageBitmap(oneBmp);
//        twoImg.setImageBitmap(twoBmp);
//        threeImg.setImageBitmap(threeBmp);
//        popupWindow.showAsDropDown ( mView,x,y );
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                setBackGroundAlpha(1.0f, context);
//
//            }
//        });
//
//    }
    public void dismissPopWindow() {
        if (popupWindow != null) popupWindow.dismiss();
    }

    void setBackGroundAlpha(float alpha, Context context) {

        WindowManager.LayoutParams layoutParams = ((AppCompatActivity) context).getWindow().getAttributes();
        layoutParams.alpha = alpha;
        ((AppCompatActivity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((AppCompatActivity) context).getWindow().setAttributes(layoutParams);
    }


}
