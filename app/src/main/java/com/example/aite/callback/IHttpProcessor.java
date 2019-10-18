package com.example.aite.callback;

import java.util.Map;

public interface IHttpProcessor {
    void post(String url, Map<String,Object> params, ICallback iCallback);
    void get(String url,Map<String,Object> params,ICallback iCallback);
}