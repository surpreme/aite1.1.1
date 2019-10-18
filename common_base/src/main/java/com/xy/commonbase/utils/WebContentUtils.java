package com.xy.commonbase.utils;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.xy.commonbase.R;
import com.xy.commonbase.base.BaseApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WebContentUtils {

    public static String initContent(Context context,String content, float size) {
        try {
            InputStream inputStream = BaseApplication.getApplication().getResources().getAssets().open(
                    "discover.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream), 16 * 1024);
            StringBuilder sBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }
            String color = "color:#%s ";
            String background = "background:#%s ";
            String modelHtml = sBuilder.toString();
            inputStream.close();
            reader.close();
            String contentNew = modelHtml.replace(
                    "<--@#$%discoverContent@#$%-->", content);
            contentNew = contentNew.replace("<--@#$%colorfontsize2@#$%-->"
                    , String.format(color
                            , Integer.toHexString(ContextCompat.getColor(context,R.color.text_general_color_dark))
                                    .length() > 6
                                    ? Integer.toHexString(ContextCompat.getColor(context,R.color.text_general_color_dark)).substring(2)
                                    : Integer.toHexString(ContextCompat.getColor(context,R.color.text_general_color_dark))));
            contentNew = contentNew.replace("<--@#$%colorbackground@#$%-->"
                    , String.format(background
                            , Integer.toHexString(ContextCompat.getColor(context,R.color.primary_background_color))
                                    .length() > 6
                                    ? Integer.toHexString(ContextCompat.getColor(context,R.color.primary_background_color)).substring(2)
                                    : Integer.toHexString(ContextCompat.getColor(context,R.color.primary_background_color))));
            contentNew = contentNew.replaceAll("<--@#\\$%fontsize@#\\$%-->", size + "");
            return contentNew;

        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
