package com.rebeau.commons.markdown;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_markdown_webview, null);
        ButterKnife.bind(this, view);

        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");
        //显示MarkDown文本时，将MarkDown文本从文件中读入，变为字符串data，执行
        //WebView不显示任何内容,发生这个问题，有很大概率是进行了在WebView执行加载HTML文件后，
        // 马上执行解析步骤这种操作, 这两个步骤不要同时进行
        //webView.loadUrl("javascript:marked(\'"+MarkdownUtils.getData()+"\')");
        return view;
    }

    @Override
    protected String getTitleBarName() {
        return "";
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
        handler.sendEmptyMessageDelayed(0, 1500);
    }
}
