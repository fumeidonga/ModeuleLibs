package com.rebeau.commons.markdown;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.R;
import com.rebeau.commons.R2;
import com.rebeau.commons.activity.BaseFragmentActivity;
import com.rebeau.views.loading.RBLoadStatusView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkdownWebviewActivity extends BaseFragmentActivity {

    @BindView(R2.id.md_webview)
    WebView webView;

    boolean isPause;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                String s = MarkdownUtils.getData(MarkdownWebviewActivity.this);
                webView.loadUrl("javascript:marked(\'"+ s +"\')");
                notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
            } catch (Exception e) {
                RBLogUtil.e(e, e.toString());
            }

        }
    };

    @Override
    protected View createSuccessView() {
        RBLogUtil.dt();
        isPause = true;
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_markdown_webview, null);
        ButterKnife.bind(this, view);

        //webview dubug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setJavaScriptEnabled(true);
        if(!MarkdownUtils.defaultWeb) {
            webView.loadUrl("file:///android_asset/index.html");
        }
        //显示MarkDown文本时，将MarkDown文本从文件中读入，变为字符串data，执行
        //WebView不显示任何内容,发生这个问题，有很大概率是进行了在WebView执行加载HTML文件后，
        // 马上执行解析步骤这种操作, 这两个步骤不要同时进行
        //webView.loadUrl("javascript:marked(\'"+MarkdownUtils.getData()+"\')");
        return view;
    }

    @Override
    protected String getTitleBarName() {

        if(TextUtils.isEmpty(MarkdownUtils.fileName)) {
            return "";
        }

        String title = "";

        try {
            title = MarkdownUtils.fileName.substring(MarkdownUtils.fileName.lastIndexOf("/") + 1);
        }catch (Exception e){
        }

        return title;
    }

    @Override
    protected void onLoadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isPause) {
            return;
        }
        if(!MarkdownUtils.defaultWeb){
            handler.sendEmptyMessageDelayed(0, 1300);
        } else {
            webView.loadUrl("file:///android_asset/" + MarkdownUtils.fileName);
            notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPause = false;
        handler.removeCallbacksAndMessages(null);
    }
}
