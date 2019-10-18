package com.xy.commonbase.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import androidx.annotation.ColorInt;

public class SpannableStringUtil {

    public static void setTextColor(TextView view, String content, @ColorInt int color){
        SpannableString spannableString = new SpannableString(content);
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spannableString.setSpan(span,0,0,Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        view.setText(spannableString);
    }

    public static void setTextBgColor(TextView view, String content, @ColorInt int color){
        SpannableString spannableString = new SpannableString(content);
        BackgroundColorSpan span = new BackgroundColorSpan(color);
        spannableString.setSpan(span,0,0, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        view.setText(spannableString);
    }

    public static void setTextUnderLine(TextView view,String content){
        SpannableString spannableString = new SpannableString(content);
        UnderlineSpan span = new UnderlineSpan();
        spannableString.setSpan(span,0,content.length(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        view.setText(spannableString);
    }
}
