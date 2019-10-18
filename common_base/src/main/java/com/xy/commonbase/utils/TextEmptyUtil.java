package com.xy.commonbase.utils;

import com.xy.commonbase.base.BaseApplication;
import com.xy.commonbase.bean.StatusBean;
import com.xy.commonbase.http.ApiException;

import java.text.DecimalFormat;

public class TextEmptyUtil {

    public static String getEmptyText(String text){
        return (text == null || text.equals("null"))?"":getTypeText(text);
    }

    public static String getEmptyNum(String number){
        return (number == null || number.equals("null") || number.isEmpty()|| number.equals("."))?"0":number;
    }

    public static String getTips(StatusBean bean){
        if (bean.getStatus() == 1){
            return "";
        }
        if (getEmptyText(bean.getMessage()).isEmpty()){
            if (getEmptyText(bean.getMsg()).isEmpty()){
                if (getEmptyText(bean.getError()).isEmpty()){
                    if (getEmptyText(bean.getReturn_msg()).isEmpty()){
                        return "";
                    }else {
                        throw new ApiException(50001,bean.getReturn_msg());
                    }
                }else {
                    throw new ApiException(50001,bean.getError());
                }
            }else {
                return getEmptyText(bean.getMsg());
            }
        }else {
            return getEmptyText(bean.getMessage());
        }
    }

    public static String getTypeText(String content){
        BaseApplication application = BaseApplication.getApplication();
        char[] simplified = FileUtil.getFromAssets(application,"SimplifiedCode.txt").toCharArray();
        char[] traditional = FileUtil.getFromAssets(application,"TraditionalCode.txt").toCharArray();
        // 简体
        StringBuilder result = new StringBuilder();
        if (BaseApplication.isTypeface()){
            for (char c : content.toCharArray()) {
                boolean hasT = false;
                for (int i = 0; i < simplified.length; i++) {
                    if (c == simplified[i]) {
                        hasT = true;
                        result.append(traditional[i]);
                        break;
                    }
                }
                if (!hasT){
                    result.append(c);
                }
            }
            //繁体
        }else {
            for (char c : content.toCharArray()) {
                boolean hasT = false;
                for (int i = 0; i < traditional.length; i++) {
                    if (c == traditional[i]) {
                        hasT = true;
                        result.append(simplified[i]);
                        break;
                    }
                }
                if (!hasT){
                    result.append(c);
                }
            }
        }
        return result.toString();
    }

    public static String getFormatPrice(double price){
        DecimalFormat format = new DecimalFormat("￥0.00");
        return format.format(price);
    }

    public static String getImageUrl(String url){
        if (url.startsWith("http://")){
            url = url.replace("http://","https://");
        }
        return url;
    }

}
