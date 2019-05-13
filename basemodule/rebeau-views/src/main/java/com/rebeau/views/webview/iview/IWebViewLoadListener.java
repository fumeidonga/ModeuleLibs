package com.rebeau.views.webview.iview;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

public interface IWebViewLoadListener {
	void onPageFinished(String url);

	/**
	 * 处理链接请求
	 * 
	 * @param view
	 * @param url
	 * @return <li>false 没有处理 <li>true 处理
	 * @author
	 */
	boolean shouldOverrideUrlLoading(WebView view, String url);

	void onReceivedError(WebView view, int errorCode, String description, String failingUrl);

	void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error);
	
	void onScrollChanged(int l, int t, int oldl, int oldt);

}
