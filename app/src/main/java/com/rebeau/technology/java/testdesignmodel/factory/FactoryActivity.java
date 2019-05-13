package com.rebeau.technology.java.testdesignmodel.factory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.lifecycle.RBActivityLifecycleCallbacks;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;
import com.rebeau.technology.java.testdesignmodel.factory.abstractfactory.AFactory1;
import com.rebeau.technology.java.testdesignmodel.factory.abstractfactory.AbsFactory;
import com.rebeau.technology.java.testdesignmodel.factory.abstractfactory.BFactory1;
import com.rebeau.technology.java.testdesignmodel.factory.factory.AFactory;
import com.rebeau.technology.java.testdesignmodel.factory.factory.AbstractFactory;
import com.rebeau.technology.java.testdesignmodel.factory.factory.BFactory;
import com.rebeau.technology.java.testdesignmodel.factory.simplefactory.SimpleFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FactoryActivity extends Activity {

    @BindView(R.id.button4)
    Button button4;
    @BindView(R.id.button5)
    Button button5;
    @BindView(R.id.button6)
    Button button6;

    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView6)
    TextView textView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 判断是否从后台恢复, 且时间间隔符合要求, 显示广告页面
        boolean isFromBackToFront = RBActivityLifecycleCallbacks.sAppState == RBActivityLifecycleCallbacks.STATE_BACK_TO_FRONT;
        if (isFromBackToFront) {
            RBLogUtil.e("");
        }
    }

    @OnClick(R.id.button4)
    public void button4(){
        StringBuilder sb = new StringBuilder();

        sb.append("简单工厂\n");
        sb.append(SimpleFactory.getComputer(0).toString());
        sb.append("\n\n");
        sb.append(SimpleFactory.getComputer(1).toString());

        textView4.setText(sb.toString());

    }
    @OnClick(R.id.button5)
    public void button5(){

        AbstractFactory abstractFactory = new AFactory();
        AbstractFactory abstractFactory1 = new BFactory();

        StringBuilder sb = new StringBuilder();

        sb.append("工厂\n");
        sb.append(abstractFactory.buildComputer().toString());
        sb.append("\n\n");
        sb.append(abstractFactory1.buildComputer().toString());

        textView5.setText(sb.toString());
    }
    @OnClick(R.id.button6)
    public void button6(){

        AbsFactory abstractFactory = new AFactory1();
        AbsFactory abstractFactory1 = new BFactory1();


        StringBuilder sb = new StringBuilder();

        sb.append("抽象工厂\n");
        sb.append(abstractFactory.createKey().build().toString());
        sb.append("\n");
        sb.append(abstractFactory.createMouse().build().toString());
        sb.append("\n\n");
        sb.append(abstractFactory1.createKey().build().toString());
        sb.append("\n");
        sb.append(abstractFactory1.createMouse().build().toString());

        textView6.setText(sb.toString());
    }

    @OnClick(R.id.readme1)
    public void readme1(){

        MarkdownUtils.setData(this, "testdesignmodel/factory/简单工厂.md");
    }
    @OnClick(R.id.readme2)
    public void readme2(){

        MarkdownUtils.setData(this, "testdesignmodel/factory/工厂.md");
    }
    @OnClick(R.id.readme3)
    public void readme3(){

        MarkdownUtils.setData(this, "testdesignmodel/factory/抽象工厂.md");
    }
}
