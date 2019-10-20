package com.example.jianancangku.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jianancangku.R;


public class PopWindowsUtils {
    private static PopWindowsUtils mInstance;
    private int popDialoglayoutId = R.layout.number_poplayout;
    private int popcenterlayoutid = R.layout.fixthing_success_pop_layout;

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


    public void showcenterPopupWindow(final Context context, int fatherLayoutId, float backAlpha) {
        @SuppressLint("InflateParams") View more_icon_view = LayoutInflater.from(context).inflate(popcenterlayoutid, null);
        setBackGroundAlpha(1.0f, context);
        popupWindow = new PopupWindow(more_icon_view, LinearLayout.LayoutParams.WRAP_CONTENT, 450, true);
        popupWindow.setContentView(more_icon_view);
        popupWindow.showAtLocation(more_icon_view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackGroundAlpha(1.0f, context);

            }
        });

    }

    public void showDownPopupWindow(final Context context, View mView, int fatherLayoutId, float backAlpha, String txt, View.OnClickListener numberClick) {
        @SuppressLint("InflateParams") View more_icon_view = LayoutInflater.from(context).inflate(popDialoglayoutId, null);
        setBackGroundAlpha(backAlpha, context);
        popupWindow = new PopupWindow(more_icon_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(more_icon_view);
        TextView topTxt = more_icon_view.findViewById(R.id.phone_header_txt);
        topTxt.setText(txt);
        topTxt.setOnClickListener(numberClick);
        popupWindow.showAsDropDown(mView);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackGroundAlpha(1.0f, context);

            }
        });

    }


    public void showfixthingPopupWindow(final Context context, View mView, int fatherLayoutId, float backAlpha, String txt, View.OnClickListener numberClick) {
        @SuppressLint("InflateParams") View more_icon_view = LayoutInflater.from(context).inflate(popDialoglayoutId, null);
        setBackGroundAlpha(1f, context);
        popupWindow = new PopupWindow(more_icon_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(more_icon_view);
        TextView topTxt = more_icon_view.findViewById(R.id.phone_header_txt);
        topTxt.setText(txt);
        topTxt.setOnClickListener(numberClick);
        popupWindow.showAsDropDown(mView);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackGroundAlpha(1.0f, context);

            }
        });

    }

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
