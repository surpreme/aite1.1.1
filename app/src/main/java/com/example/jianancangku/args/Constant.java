package com.example.jianancangku.args;

public class Constant {
    public static final String device = "android";
    public static final String usernumber = "18270281021";
    public static final String userkey = "666666";
    public static final String device_id = "fs8sdf56s";//极光推送
    public static String key="983e34c5ea716cb6a564c9250c4c9275";

    private static String baseUrl="https://aitecc.com/mobile/";
    public static boolean isLogin=false;
    //interface
    public static final String LogInAddress = baseUrl+"index.php?act=login&op=index";
    public static final String findkeyAddress = baseUrl+"index.php?act=login&op=retrievePasswordSendCode";
    public static final String msgcenterAddress = baseUrl+"index.php?act=warehouse_clerk&op=messageCenter";
    public static final String thingsFixAddress = baseUrl+"index.php?act=warehouse_clerk&op=orderManager";
    public static final String workerFixAddress = baseUrl+"index.php?act=warehouse_clerk&op=staffManager";
    public static final String thingbookAddress = baseUrl+"index.php?act=warehouse_clerk&op=baleList";

    public static final String qrgohomeAddress = baseUrl+"index.php?act=warehouse_clerk&op=scanCodeEnterWarehouse";
    public static final String qrouthomeAddress = baseUrl+"index.php?act=warehouse_clerk&op=scanCodeOutWarehouse";



}
