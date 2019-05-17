package com.rebeau.technology.android.performance.kadun;

import android.app.Activity;
import android.os.Bundle;

import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class KaDunActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ka_dun);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.show_dialog0)
    public void button0(){
        MarkdownUtils.setData(this, "android/performace/kadun/卡顿优化.MD");
    }

    @OnClick(R.id.show_dialog1)
    public void button1(){

    }


    @OnClick(R.id.android_performace_t)
    public void android_performace_t(){
        MarkdownUtils.setData(this, "android/performace/kadun/页面卡顿自检步骤.md");
    }

    @OnClick(R.id.show_dialog2)
    public void button2(){

    }

    @OnClick(R.id.show_dialog3)
    public void button3(){

    }

    @OnClick(R.id.show_dialog4)
    public void button4(){

    }

    @OnClick(R.id.show_dialog5)
    public void button5(){

    }

    @OnClick(R.id.show_dialog6)
    public void button6(){

    }

    @OnClick(R.id.show_dialog7)
    public void button7(){

    }

    @OnClick(R.id.show_dialog8)
    public void button8(){

    }

    @OnClick(R.id.show_dialog9)
    public void button9(){

    }
}
