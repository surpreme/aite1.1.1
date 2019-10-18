package com.xy.commonbase.http;

import android.text.TextUtils;
import android.webkit.WebSettings;

import com.orhanobut.logger.Logger;
import com.xy.commonbase.base.BaseApplication;
import com.xy.commonbase.base.BaseResponse;
import com.xy.commonbase.helper.LogHelper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.NoRouteToHostException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class InterceptorUtil {
    public static Interceptor headerInterceptor() throws NoRouteToHostException{
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException , NoRouteToHostException{
                Request originalRequest = chain.request();
                Request.Builder builder = originalRequest.newBuilder();
                builder.addHeader("X-APP-Agent", "corp_zx_app")
                        .addHeader("X-OS", "Android")
//                        .addHeader("X-APP-ID", "20181018000061")
                        .addHeader("X-APP-ID", "20181130000009")
                        .addHeader("X-DEVICE-TYPE", "USERNAME")
                        .addHeader("appId", "281")
                        .addHeader("businessType", "610001");

                if (originalRequest.url().encodedPath().contains("web/index")) {
                    builder.removeHeader("User-Agent")//移除旧的
                            .removeHeader("Accept")
                            .removeHeader("Accept-Encoding")
                            .addHeader("User-Agent", WebSettings.getDefaultUserAgent(BaseApplication.getApplication()))
                            .addHeader("Accept", "*/*")
                            .addHeader("Accept-Encoding", "gzip,default");//添加真正的头部
                }

                String token = "";
                if (!TextUtils.isEmpty(token)) {
                    builder.addHeader("Access-Token", token);
                }
                Request request = builder.build();
                return chain.proceed(request);

            }
        };
    }

    public static Interceptor logInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
