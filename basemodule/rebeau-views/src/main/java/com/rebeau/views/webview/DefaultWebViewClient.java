package com.rebeau.views.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.views.webview.iview.IWebViewLoadListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class DefaultWebViewClient extends WebViewClient {

	IWebViewLoadListener mHtmlLoadListener;
	DefaultWebView webview;

	public DefaultWebViewClient(DefaultWebView webview) {
		this.webview = webview;
	}

	public IWebViewLoadListener getmHtmlLoadListener() {
		return mHtmlLoadListener;
	}

	public void setmHtmlLoadListener(IWebViewLoadListener listener) {
		this.mHtmlLoadListener = listener;
	}

	/**
	 * webview对用户可见，一些动画可以放这里提前关闭掉，没必要等pagefinished了
	 * @param view
	 * @param url
	 */
	@Override
	public void onPageCommitVisible(WebView view, String url) {
		super.onPageCommitVisible(view, url);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		if (mHtmlLoadListener != null){
			mHtmlLoadListener.onPageFinished(url);
		}
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		if (mHtmlLoadListener != null){
			mHtmlLoadListener.onReceivedError(view, errorCode, description, failingUrl);
		}
	}

	/**
	 * onReceivedSslError为webView处理ssl证书设置
	 */
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		super.onReceivedSslError(view, handler, error);
		if (mHtmlLoadListener != null){
			mHtmlLoadListener.onReceivedSslError(view, handler, error);
		}
	}

	/**
	 * 对网页中超链接按钮的响应。当按下某个连接时WebViewClient会调用这个方法，并传递参数：按下的url
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		//对不同类型的处理
		if (url != null && url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			view.getContext().startActivity(intent);
			return true;
		}
		if (mHtmlLoadListener != null && mHtmlLoadListener.shouldOverrideUrlLoading(view, url)) {
			return true;
		}
		// don't override URL so that stuff within iframe can work properly
		// view.loadUrl(url);
		return super.shouldOverrideUrlLoading(view, url);
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
		return super.shouldOverrideUrlLoading(view, request);
	}

	/**
	 * api 11(3.0) - api 21 (5.0)
	 * @param view
	 * @param url
	 * @return
	 */
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		RBLogUtil.d(url);
		/**
		 * 替换
 		 */
		// 步骤1:判断拦截资源的条件，即判断url里的图片资源的文件名
		// 例如图片的资源文件名为:logo.gif
		// 然后将这个logo.gif替换成本地的error.png
		if (url.contains("logo.gif")) {
			InputStream is = null;
			// 步骤2:创建一个输入流
			try {
				is = webview.getContext().getAssets().open("images/error.png");
				// 步骤3:打开需要替换的资源(存放在assets文件夹里)
				// 在app/src/main下创建一个assets文件夹
				// assets文件夹里再创建一个images文件夹,放一个error.png的图片
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 步骤4:替换资源
			WebResourceResponse response = new WebResourceResponse("image/png", "utf-8", is);
			// 参数1:http请求里该图片的Content-Type,此处图片为image/png
			// 参数2:编码类型
			// 参数3:替换资源的输入流
			return response;
		}

		/**
		 * 其他操作，例如缓存
		 */
		WebResourceResponse response = null;
		// 检查该资源是否已经提前下载完成。我采用的策略是在应用启动时，用户在 wifi 的网络环境下
		// 提前下载 H5 页面需要的资源。
		boolean resDown = isURLDownValid(url);
		if (resDown) {
			InputStream jsStr = getResInputStream(url);
			if (url.endsWith(".png")) {
				response = getWebResourceResponse(url, "image/png", ".png");
			} else if (url.endsWith(".gif")) {
				response = getWebResourceResponse(url, "image/gif", ".gif");
			} else if (url.endsWith(".jpg")) {
				response = getWebResourceResponse(url, "image/jepg", ".jpg");
			} else if (url.endsWith(".jepg")) {
				response = getWebResourceResponse(url, "image/jepg", ".jepg");
			} else if (url.endsWith(".js") && jsStr != null) {
				response = getWebResourceResponse("text/javascript", "UTF-8", ".js");
			} else if (url.endsWith(".css") && jsStr != null) {
				response = getWebResourceResponse("text/css", "UTF-8", ".css");
			} else if (url.endsWith(".html") && jsStr != null) {
				response = getWebResourceResponse("text/html", "UTF-8", ".html");
			}
		}
		// 若 response 返回为 null , WebView 会自行请求网络加载资源。
		return super.shouldInterceptRequest(view, url);
	}

	/**
	 *  >= api 21 (5.0) 添加的接口
	 * @param view
	 * @param request
	 * @return
	 */
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
		RBLogUtil.d(request.getUrl());
		// 替换
		// 步骤1:判断拦截资源的条件，即判断url里的图片资源的文件名
		// 图片的资源文件名为:logo.gif
		// 然后将这个logo.gif替换成本地的error.png
		if (request.getUrl().toString().contains("logo.gif")) {
			InputStream is = null;
			// 步骤2:创建一个输入流
			try {
				is = webview.getContext().getAssets().open("images/error.png");
				// 步骤3:打开需要替换的资源(存放在assets文件夹里)
				// 在app/src/main下创建一个assets文件夹
				// assets文件夹里再创建一个images文件夹,放一个error.png的图片
			} catch (Exception e) {
				e.printStackTrace();
			}
			//步骤4:替换资源
			WebResourceResponse response = new WebResourceResponse("image/png", "utf-8", is);
			// 参数1：http请求里该图片的Content-Type,此处图片为image/png
			// 参数2：编码类型
			// 参数3：存放着替换资源的输入流（上面创建的那个）

			return response;
		}
		return super.shouldInterceptRequest(view, request);
	}



	/**
	 * 根据url 以及类型，到本地缓存里面去查找是否存在相同的文件，
	 * @param url
	 * @param mime
	 * @param style
	 * @return
	 */
	private WebResourceResponse getWebResourceResponse(String url, String mime, String style) {
		WebResourceResponse response = null;
		try {
			response = new WebResourceResponse(mime, "UTF-8",
					new FileInputStream(new File("/缓存文件的位置/" + "md5String(url)" + style)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 判断是否下载过资源
	 * @param url
	 * @return
	 */
	public boolean isURLDownValid(String url){
		return false;
	}

	/**
	 * 获取输入流
	 * @param url
	 * @return
	 */
	public InputStream getResInputStream(String url){
		return null;
	}

}
