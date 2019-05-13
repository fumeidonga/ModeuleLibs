package com.rebeau.views.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.views.R;
import com.rebeau.views.webview.iview.IWebViewLoadListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * how to use?<br/>
 * 
 * mWebView = new DefaultWebView(this);<br/>
 * setContentView(mWebView.getWebViewLayout());<br/>
 * mWebView.setTitle(title);<br/>
 * mWebView.stopLoading(); <br/>
 * mWebView.destroy(); <br/>
 * setHtmlLoadListener(this);<br/>
 * 添加js交互接口类
 * 
 * @see () addJavascriptInterface(this, "injectedObject");
 * @JavascriptInterface
 * 
 * @author david
 * 
 */
public class DefaultWebView extends WebView {

	private static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

	private Context mContext;
	private MyWebChromeClient mWebChromeClient;
	private DefaultWebViewClient mWebViewClient;
	private WebChromeClient.CustomViewCallback mCustomViewCallback;

	/**
	 * html播放视频的viewgroup
	 * */
	private FrameLayout mHtmlVideoFrameLayout;
	/**
	 * html播放视频的viewgroup缓存view
	 * */
	private View mTempHtmlVideoFrameLayout;

	/**
	 * webview的内容
	 * */
	private FrameLayout mWebView;
	/**
	 * webview的viewgroup 包含标题跟webview内容
	 * */
	private FrameLayout mWebViewFrameLayout;
	/**
	 * 对外的webview的viewgroup mWebViewLayout.addView(mWebViewFrameLayout,
	 * COVER_SCREEN_PARAMS);
	 * */
	private FrameLayout mWebViewLayout;

	/**
	 * 加载webviw的进度条，在代码中直接new出来的,
	 * 必要时，可以自定义进度条的样式跟位置
	 * */
	private ProgressBar mWebViewprogressbar;
	/**
	 * 加载webviw的标题
	 * */
	private TextView mWebViewtitle;

	/**
	 * 标题是否ok
	 */
	private boolean isTitleFixed = false;

	// *****************************js交互之js调用andorid********************************//
	/**
	 * 添加js交互接口类，并起别名 injectedObject ,别名可以任意修改,但要确保跟js中的别名一致
	 * window.injectedObject.onReceived(src); 
	 */
	/*addJavascriptInterface(this, "injectedObject");*/
	@JavascriptInterface
	public String getString() {
		return "JsObject.getString()";
	}

	@JavascriptInterface
	public void onReceived(String html) {
		
	}

	// *************************************************************//
	// *****************************js交互之android调用js 具体见activity里面********************************//
	/*<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<script type="text/javascript">
//data数据类型为字符串，字符串里面是数组，每一个数组元素为一个json对象，例如"[{id:1,name:'张三',phone:'135656461'},{id:2,name:'李四',phone:'1896561'}]"

function setContactInfo(data) {
	var tableObj = document.getElementById("contact");
	//通过eval方法处理得到json对象数组
	var jsonObjects = eval(data);
	for (var i = 0; i < jsonObjects.length; i++) {
		//获取json对象
		var jsonObj = jsonObjects[i];
		var tr = tableObj.insertRow(tableObj.rows.length); //添加一行
		//添加三列
		var td1 = tr.insertCell(0);
		var td2 = tr.insertCell(1);
		var td3 = tr.insertCell(2);

		td1.innerHTML = jsonObj.id;
		td2.innerHTML = jsonObj.name;
		td3.innerHTML = jsonObj.phone;
	}
}
</script>
</head>
<!--调用服务器端init方法-->
<body onload="javascript:injectedObject.init()">
<table id="contact">
	<tr>
		<td>编号</td>
		<td>姓名</td>
		<td>电话</td>
	</tr>
</table>
</body>
</html>*/
		public void androidToJs(){

			loadUrl("javascript:setContactInfo('" + getJsonStr() +"')");
		}

		public String getJsonStr() {
			try {
				JSONObject object1 = new JSONObject();
				object1.put("id", 1);
				object1.put("name", "张三");
				object1.put("phone", "123456");

				JSONObject object2 = new JSONObject();
				object2.put("id", 2);
				object2.put("name", "李四");
				object2.put("phone", "456789");

				JSONArray jsonArray = new JSONArray();
				jsonArray.put(object1);
				jsonArray.put(object2);
				return jsonArray.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	// *************************************************************//

	@Override
	public void loadUrl(String url) {
		super.loadUrl(url);
		if (mWebViewtitle != null) {
			mWebViewtitle.setText(url);
		}
	}

	public void setTitle(String titleId) {
		if (mWebViewtitle != null) {
			mWebViewtitle.setText(titleId);
			isTitleFixed = true;
		}
	}

	public void setTitle(int titleId) {
		if (mWebViewtitle != null) {
			mWebViewtitle.setText(titleId);
			isTitleFixed = true;
		}
	}

	public void setTitle(CharSequence title) {
		if (mWebViewtitle != null) {
			mWebViewtitle.setText(title);
			isTitleFixed = true;
		}
	}

	/**
	 * 设置网页加载的监听
	 * 
	 * @param l
	 */
	public void setHtmlLoadListener(IWebViewLoadListener l) {
		mWebViewClient.setmHtmlLoadListener(l);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init(Context context) {

		//webview dubug
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}

		mContext = context;
		Activity a = (Activity) mContext;

		mWebViewLayout = new FrameLayout(context);

		mWebViewFrameLayout = (FrameLayout) LayoutInflater.from(a).inflate(R.layout.moduleview_activity_webview, null);
		mWebView = (FrameLayout) mWebViewFrameLayout.findViewById(R.id.main_content);
		mHtmlVideoFrameLayout = (FrameLayout) mWebViewFrameLayout.findViewById(R.id.fullscreen_custom_content);
		mWebViewtitle = (TextView) mWebViewFrameLayout.findViewById(R.id.title_bar_name);

		mWebViewLayout.addView(mWebViewFrameLayout, COVER_SCREEN_PARAMS);

		mWebChromeClient = new MyWebChromeClient();
		setWebChromeClient(mWebChromeClient);

		mWebViewClient = new DefaultWebViewClient(this);
		setWebViewClient(mWebViewClient);

		DefaultWebSettings.setDefaultWebSetting(this);

		/**
		 * 添加js交互接口类，并起别名 injectedObject ,别名可以任意修改,但要确保跟js中的别名一致
		 * window.injectedObject.onReceived(src); 
		 */
		addJavascriptInterface(this, "injectedObject");

		// 触摸焦点起作用
		requestFocus();

		setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
					long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				mContext.startActivity(intent);
			}
		});

		mWebViewprogressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		mWebViewprogressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 6, 0, 0));
		mWebViewprogressbar.setProgressDrawable(context.getResources().getDrawable(R.drawable.rb_layout_list_progress));
		addView(mWebViewprogressbar);

		mWebView.addView(this);
	}

	public DefaultWebView(Context context) {
		super(context);
		init(context);
	}

	public DefaultWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DefaultWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * Provides a hook for calling "alert" from javascript. Useful for debugging
	 * your javascript.
	 * WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
	 * 关于webview中支持视频全屏
	 * 
	 */
	public class MyWebChromeClient extends WebChromeClient {

		private Bitmap mDefaultVideoPoster;
		private View mVideoProgressView;

		/**
		 * Js调试在Native代码里面打印日志信息的API，同时这也成了其中一种Js与Native代码通信的方法。
		 * 在Js代码中调用console.log(‘xxx’)方法
		 * 就会在Native代码的WebChromeClient.consoleMessage()中得到回调。consoleMessage.message()
		 * 获得的正是Js代码console.log(‘xxx’)的内容
		 * @param consoleMessage
		 * @return
		 */
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			//JavaScript输入的Log内容
			String msg = consoleMessage.message();
			return super.onConsoleMessage(consoleMessage);
		}

		@Override
		public void onConsoleMessage(String message, int lineNumber, String sourceID) {
			super.onConsoleMessage(message, lineNumber, sourceID);
		}

		/**
		 * 视频播放退出全屏会被调用的
		 */
		@Override
		public void onHideCustomView() {
			// TODO Auto-generated method stub
			super.onHideCustomView();
			if (mTempHtmlVideoFrameLayout == null)
				return;

			// Hide the custom view.
			mTempHtmlVideoFrameLayout.setVisibility(View.GONE);

			// Remove the custom view from its container.
			mHtmlVideoFrameLayout.removeView(mTempHtmlVideoFrameLayout);
			mTempHtmlVideoFrameLayout = null;
			mHtmlVideoFrameLayout.setVisibility(View.GONE);
			mCustomViewCallback.onCustomViewHidden();

			DefaultWebView.this.setVisibility(View.VISIBLE);
		}

		/**
		 * 播放网络视频时全屏会被调用的方法
		 */
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			// TODO Auto-generated method stub
			super.onShowCustomView(view, callback);
			DefaultWebView.this.setVisibility(View.GONE);

			// if a view already exists then immediately terminate the new one
			if (mTempHtmlVideoFrameLayout != null) {
				callback.onCustomViewHidden();
				return;
			}

			mHtmlVideoFrameLayout.addView(view);
			mTempHtmlVideoFrameLayout = view;
			mCustomViewCallback = callback;
			mHtmlVideoFrameLayout.setVisibility(View.VISIBLE);
		}

		/**
		 * 播放网络视频时全屏会被调用的方法
		 */
		@SuppressLint("NewApi")
		@Override
		public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
			// TODO Auto-generated method stub
			super.onShowCustomView(view, requestedOrientation, callback);
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
		}

		/**
		 * 视频加载添加默认图标
		 */
		@Override
		public Bitmap getDefaultVideoPoster() {
			/*if (mDefaultVideoPoster == null) {
				mDefaultVideoPoster = BitmapFactory.decodeResource(getResources(),
						R.mipmap.moduleview_webview_default_video_poster);
			}*/
			return mDefaultVideoPoster;
		}

		/**
		 * 视频加载时进程loading
		 */
		@Override
		public View getVideoLoadingProgressView() {
			if (mVideoProgressView == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				mVideoProgressView = inflater.inflate(R.layout.moduleview_webview_video_loading_progress, null);
			}
			return mVideoProgressView;
		}

		/**
		 * 网页标题
		 */
		@Override
		public void onReceivedTitle(WebView view, String title) {
			if (!isTitleFixed && mWebViewtitle != null && !TextUtils.isEmpty(title)) {
				mWebViewtitle.setText(title);
			}
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			RBLogUtil.v(url + " :: " + message);
			result.confirm();
			return true;
		}

		// 设置网页加载的进度条
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			((Activity) mContext).getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
			if (newProgress >= 100) {
				mWebViewprogressbar.setVisibility(GONE);
			} else {
				if (mWebViewprogressbar.getVisibility() == GONE)
					mWebViewprogressbar.setVisibility(VISIBLE);
				mWebViewprogressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	// 是否有网
	public boolean isConnectInternet(final Context pContext) {

		final ConnectivityManager conManager = (ConnectivityManager) pContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;

	}

	/**
	 * 获取webview的布局
	 * 
	 * @return
	 */
	public FrameLayout getWebViewLayout() {
		return mWebViewLayout;
	}

	/**
	 * 是否播放html视频
	 * 
	 * @return
	 */
	public boolean isPlayHtmlVideo() {
		return (mTempHtmlVideoFrameLayout != null);
	}

	/**
	 * 隐藏html视频view
	 */
	public void hideTempHtmlVideoView() {
		mWebChromeClient.onHideCustomView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((mTempHtmlVideoFrameLayout == null) && canGoBack()) {
				goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) mWebViewprogressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		mWebViewprogressbar.setLayoutParams(lp);
		if(mWebViewClient.getmHtmlLoadListener() != null) {
			mWebViewClient.getmHtmlLoadListener().onScrollChanged(l, t, oldl, oldt);
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		mWebView.removeView(this);
		removeAllViews();
		super.destroy();
	}

	/*@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
		super.onMeasure(widthMeasureSpec, expandSpec); 
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}*/
	
}
