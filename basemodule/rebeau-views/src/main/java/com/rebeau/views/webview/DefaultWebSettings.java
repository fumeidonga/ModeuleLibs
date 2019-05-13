package com.rebeau.views.webview;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.rebeau.base.utils.RBNetworkUtil;

/**
 * Created by Administrator on 2018/3/14.
 */

public class DefaultWebSettings {

    public static final String USERAGENT_UC = " myBrowser/11.6.4.950 ";
    public static final String AGENTWEB_VERSION = " myweb/4.0.2 ";

    public static void setDefaultWebSetting (WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 是否支持使用屏幕控件或手势进行缩放，默认是true，支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSavePassword(false);
        if (RBNetworkUtil.checkNetwork(webView.getContext())) {
            //根据cache-control获取数据。
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            //没网，则从本地获取，即离线加载
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //适配5.0不允许http和https混合使用情况
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webSettings.setTextZoom(100);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSupportMultipleWindows(false);
        // 是否阻塞加载网络图片  协议http or https
        webSettings.setBlockNetworkImage(false);
        // 允许加载本地文件html  file协议
        webSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // 通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
            webSettings.setAllowFileAccessFromFileURLs(false);
            // 允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
            webSettings.setAllowUniversalAccessFromFileURLs(false);
        }
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        } else {
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        //设置WebView是否使用预览模式加载界面。
        webSettings.setLoadWithOverviewMode(false);
        /*
		 * WebView自适应屏幕大小
		 */
        webSettings.setUseWideViewPort(true);
        // enable Web Storage: localStorage, sessionStorage 开启DOM形式存储
        webSettings.setDomStorageEnabled(true);
        webSettings.setNeedInitialFocus(true);
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //webSettings.setDefaultFontSize(16);
        //webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8
        webSettings.setGeolocationEnabled(true);

        String appCacheDir = webView.getContext().getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        // 设置 Application Caches 缓存目录
        webSettings.setAppCachePath(appCacheDir);
        webSettings.setAllowFileAccess(false);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //deprecated in API level 24.
            String dir = webView.getContext().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
            webSettings.setGeolocationDatabasePath(dir);
        }
        webSettings.setDatabasePath(webView.getContext().getApplicationContext().getDir("databases", 0).getPath());

        //缓存文件最大值
        if(Build.VERSION.SDK_INT < 18){
            //deprecated in API level 18
            // 设置 缓存模式
            webSettings.setAppCacheMaxSize(1024 * 1024 * 8);// 设置缓冲大小，我设的是8M
        }

        webSettings.setUserAgentString(webSettings
                .getUserAgentString()
                .concat(AGENTWEB_VERSION)
                .concat(USERAGENT_UC)
        );

    }
}
