package com.example.aite.args;

public class Constant {
    public static final String device = "android";
    public static final String usernumber = "18270281021";
    public static final String userkey = "666666";
    public static final String device_id = "fs8sdf56s";//极光推送
    public static String key="3983015b524d8383c26465815fe707ee";

    private static String baseUrl="https://aitecc.com/mobile/";
    public static boolean isLogin=false;
    //interface
    public static final String LogInAddress = baseUrl+"index.php?act=login&op=index";
    public static final String findkeyAddress = baseUrl+"index.php?act=login&op=retrievePasswordSendCode";
    public static final String msgcenterAddress = baseUrl+"index.php?act=warehouse_clerk&op=messageCenter";


}
