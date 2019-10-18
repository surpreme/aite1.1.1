package com.xy.commonbase.web;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.itheima.view.BridgeWebView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xy.commonbase.R;
import com.xy.commonbase.R2;
import com.xy.commonbase.base.BaseActivity;
import com.xy.commonbase.helper.LogHelper;
import com.xy.commonbase.listener.BaseUMenShareListener;
import com.xy.commonbase.utils.ToastUtil;

import butterknife.BindView;

/**
 * @author:TQX
 * @Date: 2019/5/31
 * @description:
 */
@Route(path = "/base/GeneralWebActivity")
public class GeneralWebActivity extends BaseActivity {
    @BindView(R2.id.bdwebview)
    BridgeWebView webview;
    @BindView(R2.id.tool_bar)
    Toolbar toolbar;
    @BindView(R2.id.title)
    TextView title;
    @Autowired()
    String url;

    private String token;

    //图片选择
    private ValueCallback<Uri> mFilePathCallback;
    private ValueCallback<Uri[]> mFilePathCallbackArray;
    private ImagePick ip;

    private boolean hasTitle;
    private String titleContent;
    private int menuId;
    private int head = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.base_activity_web;
    }

    private ShareAction action;

    @Override
    protected void initData() {
        super.initData();
        hasTitle = getIntent().getBooleanExtra("hasTitle", true);
        titleContent = getIntent().getStringExtra("title");
        if (titleContent == null || titleContent.isEmpty())
            titleContent = getResources().getString(R.string.app_name);
        menuId = getIntent().getIntExtra("menuId", R.menu.menu_web_default);
        head = getIntent().getIntExtra("headType", 0);
        UMImage thumb = new UMImage(this, R.drawable.base_launcher);
        UMWeb web = new UMWeb(url);
        web.setTitle(titleContent);
        web.setThumb(thumb);
        action = new ShareAction(this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE)
                .withMedia(web)
                .setCallback(new BaseUMenShareListener());//回调监听器
    }

    @Override
    protected void initToolBar() {
        if (hasTitle) {
            toolbar.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(view -> finish());
            ActionBar bar = getSupportActionBar();
            if (bar != null) {
                bar.setDisplayHomeAsUpEnabled(true);
                bar.setDisplayShowTitleEnabled(false);
            }
            title.setText(titleContent);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menuId, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.share) {
            if (action != null)
                action.open();
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (action != null)
            action.close();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogHelper.e("onNewIntent");
        setIntent(intent);
        ARouter.getInstance().inject(this);
        setView();
    }

    @Override
    protected void initView() {
        setView();
    }

    private void setView() {
        LogHelper.e(head+"      "+url+"      ");
        if (!url.startsWith("http://") && !url.startsWith("https://")){
            url = "http://"+url;
        }
        if (head == 0) {
            webview.loadUrl(url);
        } else {
            webview.postUrl(url, null);
        }
        webview.getSettings().setJavaScriptEnabled(true);
        // 声明WebSettings子类
//        clearCookies(this);
        WebSettings webSettings = webview.getSettings();
//        setCookie();
        // 支持javascript
        // 在js中调用本地java方法
        webview.addJavascriptInterface(new AndroidWebView(this), "AndroidWebView");
        // 设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); // 将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        // 缩放操作
        webSettings.setSupportZoom(true); // 支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); // 设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); // 隐藏原生的缩放控件

        //启用数据库
        webSettings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        webSettings.setDomStorageEnabled(true);
        // 不使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.requestFocus();
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            //权限
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("提示")
                        .setMessage(message)
                        .setPositiveButton("确认", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface diaLogHelperHelper, int which) {
                                result.confirm();
                            }
                        })
                        .setNeutralButton("取消", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface diaLogHelperHelper, int which) {
                                result.cancel();
                            }
                        });
                builder.setOnCancelListener(new AlertDialog.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface diaLogHelperHelper) {
                        result.cancel();
                    }
                });

                // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
                builder.setOnKeyListener(new AlertDialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface diaLogHelperHelper, int keyCode, KeyEvent event) {
                        LogHelper.v("onJsConfirm", "keyCode==" + keyCode + "event=" + event);
                        return true;
                    }
                });
                // 禁止响应按back键的事件
                // builder.setCancelable(false);
                AlertDialog diaLogHelperHelper = builder.create();
                diaLogHelperHelper.show();
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {

                LogHelper.i("------------------", "提示   " + message);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("提示").setMessage(message).setPositiveButton("确认", null);
                builder.setCancelable(false);
                AlertDialog diaLogHelperHelper = builder.create();
                diaLogHelperHelper.show();
                result.confirm();
                return true;
            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    //加载完成
//                    myDiaLogHelperHelper.dismiss();
                    //获取焦点
                    if (webview != null) {
                        webview.setFocusableInTouchMode(true);
                        webview.setFocusable(true);
                    }

                } else {
                    //加载中
//                    if(!isFinishing()){
//                        myDiaLogHelperHelper.show();
//                    }
                }
            }

            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                // Double check that we don't have any existing callbacks
                if (mFilePathCallbackArray != null) {
                    mFilePathCallbackArray.onReceiveValue(null);
                }
                mFilePathCallbackArray = filePathCallback;
                showDiaLogHelperHelper();
                return true;
            }

            /**
             * 显示照片选取DiaLogHelperHelper
             */
            public void showDiaLogHelperHelper() {
                if (ip == null) {
                    ip = new ImagePick(GeneralWebActivity.this);
                }
                ip.setCancel(new ImagePick.MyDismiss() {
                    @Override
                    public void dismiss() {
                        handleCallback(null);
                    }
                });
                ip.show();
            }
        });
    }


    /* 改写物理按键返回的逻辑 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("-------------------", " 链接 " + webview.getUrl());
//        if (bdwebview.getUrl()!=null&&bdwebview.getUrl().contains("http://ddmall.aitecc.com/wap/")) {
//            finish();
//            return false;
//        }
        if (!webview.canGoBack()) {
            finish();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            // 返回上一页面
//            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 处理WebView的回调
     *
     * @param uri
     */
    private void handleCallback(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mFilePathCallbackArray != null) {
                if (uri != null) {
                    mFilePathCallbackArray.onReceiveValue(new Uri[]{uri});
                } else {
                    mFilePathCallbackArray.onReceiveValue(null);
                }
                mFilePathCallbackArray = null;
            }
        } else {
            if (mFilePathCallback != null) {
                if (uri != null) {
                    mFilePathCallback.onReceiveValue(uri);
                } else {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = null;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ip.onActivityResult(requestCode, resultCode, data, this::handleCallback);
    }

    /**
     * 注入Cookie
     */
    @SuppressLint("NewApi")
    void setCookie() {
        String StringCookie = "accesstoken=" + token + ";path=/";
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.flush();
        } else {
            cookieManager.removeSessionCookie();
            CookieSyncManager.getInstance().sync();
        }
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, StringCookie);

//        if (MyHttpUtil.getCookieMap()!= null){
//            for (String s : MyHttpUtil.getCookieMap().keySet()) {
//                cookieManager.setCookie(url ,s+"="+MyHttpUtil.getCookieMap().get(s)+ ";path=/");
//            }
//        }
    }

    /**
     * 清除Cookie
     *
     * @param context
     */
    public static void clearCookies(Context context) {
        @SuppressWarnings("unused")
        CookieSyncManager cookieSyncMngr =
                CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    /**
     * 监听js事件
     *
     * @author Administrator
     */
    class AndroidWebView {
        private Context mContext;

        public AndroidWebView(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void test1() {
            ToastUtil.show(mContext, "网页收到");
        }

        /**
         * 登录
         * 在js中调用window.AndroidWebView.AppLogHelperHelperin(name)，便会触发此方法。
         * 此方法名称一定要和js中AppLogHelperHelperin方法一样
         */
        @JavascriptInterface
        public void AppLogHelperHelperin(String jumpurl) {
            LogHelper.i("---------------------", "登录   " + jumpurl);
            ARouter.getInstance()
                    .build("/user/LoginActivity")
                    .navigation();
            finish();
        }

        /**
         * 返回
         */
        @JavascriptInterface
        public void AppGoBack() {
            LogHelper.i("----------------", "返回  ");
            runOnUiThread(() -> {
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
            });

        }

        @JavascriptInterface
        public void AppLogin() {
            runOnUiThread(() -> {
                ARouter.getInstance()
                        .build("/user/LoginActivity")
                        .navigation();
                finish();
            });
        }

        /**
         * 返回上个原生页面
         */
        @JavascriptInterface
        public void AppGoBack1() {
            finish();
        }

        /**
         * 授权失败
         */
        @JavascriptInterface
        public void CheckTokenApp() {
            LogHelper.i("-------------------", "授权失败");
        }

        /**
         * 直接退出
         */
        @JavascriptInterface
        public void AppFinish() {
            LogHelper.i("-------------------", "直接退出");
            finish();
        }

        /**
         * 去首页
         */
        @JavascriptInterface
        public void AppToHome(int index) {
            LogHelper.i("-------------------", "去首页  " + index);
            runOnUiThread(() -> {
                ARouter.getInstance()
                        .build("/main/MainActivity")
                        .withInt("homeIndex", index)
                        .navigation();
                finish();
            });
        }

        @JavascriptInterface
        public void AppShare(String title, String desc, String link, String img) {
            LogHelper.e("-------->" + title + "\n" + desc + "\n" + link + "\n" + img);
            runOnUiThread(() -> {
                UMImage thumb = new UMImage(GeneralWebActivity.this, img);
                UMWeb web = new UMWeb(link);
                web.setTitle(title);
                web.setDescription(desc);
                web.setThumb(thumb);
                new ShareAction(GeneralWebActivity.this)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE)
                        .withMedia(web)
                        .setCallback(new BaseUMenShareListener())
                        .open();//回调监听器
            });
        }
    }
}
