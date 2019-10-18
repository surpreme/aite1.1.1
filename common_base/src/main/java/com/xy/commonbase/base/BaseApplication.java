package com.xy.commonbase.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatDelegate;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.location.BDLocation;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.xy.commonbase.BuildConfig;
import com.xy.commonbase.R;
import com.xy.commonbase.constants.Constants;
import com.xy.commonbase.utils.CertificateUtil;
import com.xy.commonbase.utils.SharedPreferenceUtil;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

public class BaseApplication extends Application {

    private static BaseApplication application;

    private static BDLocation bean;

    public static String token;

    private static String memberId;

    private String cityCode;

    private static boolean nightMode;

    private static boolean typeface;

    public static float WEB_SIZE = 18;

    private String account;

    private int merchantState;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public static boolean isNightMode() {
        return nightMode;
    }

    public static void setNightMode(boolean nightMode) {
        BaseApplication.nightMode = nightMode;
    }

    public static boolean isTypeface() {
        return typeface;
    }

    public static void setTypeface(boolean typeface) {
        BaseApplication.typeface = typeface;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public static String getMemberId() {
        return memberId;
    }

    public int getMerchantState() {
        return merchantState;
    }

    public void setMerchantState(int merchantState) {
        this.merchantState = merchantState;
    }

    public static void setMemberId(String memberId) {
        if (BaseApplication.memberId!= null && BaseApplication.memberId.equals(memberId)){
            return;
        }
        SharedPreferenceUtil.write(Constants.MY_SHARED_PREFERENCE,Constants.MEMBER_ID,memberId);
        BaseApplication.memberId = memberId;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, R.color.colorAccent);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CertificateUtil.handleSSLHandshake();
        application = this;
        BGASwipeBackHelper.init(this, null);
        initRouter(this);
        initLogger();
        initUser();
        initUMen();
    }

    private void initUMen() {
        UMShareAPI.get(this);//初始化sd
        UMConfigure.init(this, "5d6e3ab13fc1958c69000ec4", "Planet", UMConfigure.DEVICE_TYPE_PHONE,"");
        PlatformConfig.setWeixin("wxa573b1440cfb6317", "70166d59045ea96310b862c992f12882");
    }

    private float fontScale;

    public void setFontScale(float fontScale) {
        this.fontScale = fontScale;
        WEB_SIZE = fontScale * 18;
    }

    public float getFontScale() {
        return fontScale;
    }

    private void initUser() {
        token = SharedPreferenceUtil.read(Constants.MY_SHARED_PREFERENCE,Constants.TOKEN,null);
        typeface = SharedPreferenceUtil.read(Constants.MY_SHARED_PREFERENCE,Constants.TYPEFACE_STATE,false);
        nightMode = SharedPreferenceUtil.read(Constants.MY_SHARED_PREFERENCE,Constants.NIGHT_MODE_STATE,false);
        fontScale = SharedPreferenceUtil.read(Constants.MY_SHARED_PREFERENCE,Constants.FONT_SIZE,1f);
        cityCode = SharedPreferenceUtil.read(Constants.MY_SHARED_PREFERENCE,Constants.LAST_LOCATION_CODE,"820000");
        memberId = SharedPreferenceUtil.read(Constants.MY_SHARED_PREFERENCE,Constants.MEMBER_ID,"");
        account = SharedPreferenceUtil.read(Constants.MY_SHARED_PREFERENCE, Constants.ACCOUNT, "");
        merchantState = SharedPreferenceUtil.read(Constants.MY_SHARED_PREFERENCE,Constants.MERCHANT_STATE,0);
    }

    /**
     * 初始化 ARouter
     *
     * @param baseApplication
     */
    private void initRouter(BaseApplication baseApplication) {
        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(baseApplication); // 尽可能早，推荐在Application中初始化
    }

    public static BaseApplication getApplication() {
        return application;
    }

    private boolean isDebug() {
        try {
            ApplicationInfo info = getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void initLogger() {
        //DEBUG版本才打控制台log
        if (BuildConfig.DEBUG) {
            Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder().
                    tag(getString(R.string.app_name)).build()));
            UMConfigure.setLogEnabled(true);
        }
        //把log存到本地
//        Logger.addLogAdapter(new DiskLogAdapter(TxtFormatStrategy.newBuilder().
//                tag(getString(R.string.app_name)).build(getPackageName(), getString(R.string.app_name))));
    }

    public void toChangeLanguage(Locale locale){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = locale;
        resources.updateConfiguration(config, dm);
    }

    public void toChangeMode(boolean isNight){
        int mode = isNight ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public static void setApplication(BaseApplication application) {
        BaseApplication.application = application;
    }

    public static BDLocation getBean() {
        return bean;
    }

    public static void setBean(BDLocation bean) {
        BaseApplication.bean = bean;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            GlideApp.get(this).clearMemory();
        }
        GlideApp.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        GlideApp.get(this).clearMemory();
    }

    // 需要循环遍历退出的Activity 集合队列;(Set 集去重复)
    private Set<Activity> activities = new HashSet<>();

    // 加入队列
    public void addActivity(Activity a) {
        activities.add(a);
    }

    // 循环退出
    public void exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

}
