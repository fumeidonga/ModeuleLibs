package com.rebeau.views.webview;

import android.app.Activity;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.rebeau.views.webview.iview.IWebSettings;

/**
 * Created by Administrator on 2018/4/20.
 */

public class MVWebViewCreate {

    private Activity mActivity;
    private IWebSettings iWebSettings;
    /**
     * WebChromeClient
     */
    private WebChromeClient mWebChromeClient;
    /**
     * WebViewClient
     */
    private WebViewClient mWebViewClient;

    public MVWebViewCreate() {

    }

    public static Builder webViewBuilder(Activity mActivity){
        return new Builder(mActivity);
    }

    public static class Builder{
        private Activity mActivity;
        private IWebSettings iWebSettings;
        private WebChromeClient mWebChromeClient;
        private WebViewClient mWebViewClient;

        public Builder(Activity mActivity) {
            this.mActivity = mActivity;
        }

        public MVWebViewCreate build(){
            MVWebViewCreate mvWebViewCreate = new MVWebViewCreate();
            mvWebViewCreate.mActivity = mActivity;

            return mvWebViewCreate;
        }
    }
}
