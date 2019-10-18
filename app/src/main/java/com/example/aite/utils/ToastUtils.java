package com.example.aite.utils;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    private static Toast toast;
    public static void showToast(Context context, String msg){
        if (context==null&&msg==null)return;
        if (toast==null)
            toast=Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        else
            toast.setText(msg);
        toast.show();
    }


}
