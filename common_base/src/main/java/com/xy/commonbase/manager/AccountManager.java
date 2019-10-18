package com.xy.commonbase.manager;

public class AccountManager {
    // 用户序列号
    private String id;
    // 用户账户
    private String account;
    // 用户唯一访问服务器交检
    private String token;
    // 用户登录位置
    private String state;
    // 用户登录状态
    private boolean isLogin;

    private AccountManager(){}

    private static final class AccountManagerHolder{
        private static final AccountManager instance = new AccountManager();
    }

    public AccountManager getInstance(){
        return AccountManagerHolder.instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isLogin() {
        return token!= null && !token.isEmpty();
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
