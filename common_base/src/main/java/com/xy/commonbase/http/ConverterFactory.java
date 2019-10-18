package com.xy.commonbase.http;

import com.google.gson.Gson;
import com.xy.commonbase.base.BaseResponse;
import com.xy.commonbase.bean.HasExtraBean;
import com.xy.commonbase.bean.PageBean;
import com.xy.commonbase.bean.StatusBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConverterFactory {

    public static <T> BaseResponse<HasExtraBean<PageBean, T>> parseHasPageBeanData(String result, Class<T> cls, String name) throws JSONException {
        BaseResponse<HasExtraBean<PageBean, T>> response = new BaseResponse<>();
        HasExtraBean<PageBean, T> bean = new HasExtraBean<>();
        JSONObject object = new JSONObject(result);
        Gson gson = new Gson();
        JSONObject data = object.optJSONObject("datas");
        JSONObject page = data.optJSONObject("0");
        JSONArray list = data.optJSONArray(name);
        PageBean pageBean = null;
        if (page != null) {
            pageBean = gson.fromJson(page.toString(), PageBean.class);
        }
        List<T> beanList = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                T t = gson.fromJson(list.get(i).toString(), cls);
                beanList.add(t);
            }
        }
        if (pageBean == null) {
            pageBean = new PageBean();
            if (beanList == null || beanList.isEmpty()) {
                pageBean.setHasmore(false);
            } else {
                pageBean.setHasmore(true);
            }
        }
        bean.setHead(pageBean);
        bean.setList(beanList);
        response.setData(bean);
        response.setErrorCode(object.optInt("code"));
        response.setErrorMsg(object.optString("error"));
        response.setLogin(object.optString("login"));
        return response;
    }

    public static <T> BaseResponse<HasExtraBean<PageBean, T>> parseSpecialPageBeanData(String result, Class<T> cls, String name) throws JSONException {
        BaseResponse<HasExtraBean<PageBean, T>> response = new BaseResponse<>();
        HasExtraBean<PageBean, T> bean = new HasExtraBean<>();
        JSONObject object = new JSONObject(result);
        Gson gson = new Gson();
        JSONObject data = object.optJSONObject("datas");
        JSONArray list = data.optJSONArray(name);
        PageBean pageBean = new PageBean();
        pageBean.setHasmore(object.optBoolean("hasmore"));
        pageBean.setPage_total(object.optInt("page_total"));
        List<T> beanList = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                T t = gson.fromJson(list.get(i).toString(), cls);
                beanList.add(t);
            }
        }
        bean.setHead(pageBean);
        bean.setList(beanList);
        response.setData(bean);
        response.setErrorCode(object.optInt("code"));
        response.setErrorMsg(object.optString("error"));
        response.setLogin(object.optString("login"));
        return response;
    }

    public static <T, E> BaseResponse<HasExtraBean<List<E>, T>> parseHasExtraListData(String result, Class<T> cls, Class<E> extraCls, String name, String extraName) throws JSONException {
        BaseResponse<HasExtraBean<List<E>, T>> response = new BaseResponse<>();
        HasExtraBean<List<E>, T> bean = new HasExtraBean<>();
        JSONObject object = new JSONObject(result);
        Gson gson = new Gson();
        JSONObject data = object.optJSONObject("datas");
        JSONArray list = data.optJSONArray(name);
        JSONArray extraList = data.optJSONArray(extraName);
        List<T> beanList = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.length(); i++) {
                T t = gson.fromJson(list.get(i).toString(), cls);
                beanList.add(t);
            }
        }
        List<E> eList = new ArrayList<>();
        if (extraList != null) {
            for (int i = 0; i < extraList.length(); i++) {
                E t = gson.fromJson(extraList.get(i).toString(), extraCls);
                eList.add(t);
            }
        }
        bean.setHead(eList);
        bean.setList(beanList);
        response.setData(bean);
        response.setErrorCode(object.optInt("code"));
        response.setErrorMsg(object.optString("error"));
        response.setLogin(object.optString("login"));
        return response;
    }

    public static <T> BaseResponse<T> parseSimpleBeanData(String result, Class<T> cls, String name) throws JSONException {
        BaseResponse<T> response = new BaseResponse<>();
        JSONObject object = new JSONObject(result);
        Gson gson = new Gson();
        JSONObject datas = object.optJSONObject("datas");
        JSONObject data = datas.optJSONObject(name);
        T bean = gson.fromJson(data.toString(), cls);
        response.setData(bean);
        response.setErrorCode(object.optInt("code"));
        response.setErrorMsg(object.optString("error"));
        response.setLogin(object.optString("login"));
        return response;
    }

    public static BaseResponse<StatusBean> parseStatusData(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        BaseResponse<StatusBean> response = new BaseResponse<>();
        Gson gson = new Gson();
        StatusBean bean = new StatusBean();
        try {
            JSONObject datas = jsonObject.getJSONObject("datas");
            bean = gson.fromJson(datas.toString(), StatusBean.class);
        } catch (Exception e) {
            String s = jsonObject.getString("datas");
            try {
                int state = Integer.parseInt(s);
                bean.setStatus(state);
            } catch (NumberFormatException e1) {
                bean.setMessage(s);
            }
        }
        response.setData(bean);
        response.setErrorCode(jsonObject.optInt("code"));
        response.setErrorMsg(jsonObject.optString("error"));
        response.setLogin(jsonObject.optString("login"));
        return response;
    }

}
