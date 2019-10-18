package com.xy.commonbase.base;

/**
 * 网络请求返回的数据，按格式统一包装成 BaseResponse 类
 * Created by Administrator on 2018/9/15.
 */

public class BaseResponse<T> {

    private int code = -1;
    private String errorMsg;
    private int error_code = -1;
    private T datas;
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * 兼容 gank api
     */
    private T results;
    private boolean error = false;

    public int getErrorCode() {
        if (code == -1)
            return error_code;
        else
            return code;
    }

    public void setErrorCode(int errorCode) {
        this.code = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return datas;
    }

    public void setData(T data) {
        this.datas = data;
    }

    public boolean isError() {
        return error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "errorCode=" + code +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + datas +
                '}';
    }
}
