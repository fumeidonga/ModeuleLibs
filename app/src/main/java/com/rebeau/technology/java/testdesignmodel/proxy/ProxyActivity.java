package com.rebeau.technology.java.testdesignmodel.proxy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;


import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;
import com.rebeau.technology.java.testdesignmodel.proxy.dynamic.DynamicProxy;
import com.rebeau.technology.java.testdesignmodel.proxy.statics.StaticClient;
import com.rebeau.technology.java.testdesignmodel.proxy.statics.StaticProxy;

import java.lang.reflect.Proxy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProxyActivity extends AppCompatActivity {

    @BindView(R.id.staticsbutton1)
    public Button staticButton;
    @BindView(R.id.staticsbutton)
    public Button button;
    @BindView(R.id.staticsbutton12)
    public Button staticsbutton12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);
        ButterKnife.bind(this);
    }

    //read me
    @OnClick(R.id.staticsbutton12)
    public void staticsbutton12(){
        MarkdownUtils.setData(this, "testdesignmodel/proxy/代理模式.MD");
    }
    @OnClick(R.id.staticsbutton1)
    public void staticButton(){
        IStaticProxy client = new StaticClient();
        IStaticProxy proxy = new StaticProxy(client);

        proxy.buy();
        proxy.pay();
    }

    @OnClick(R.id.staticsbutton)
    public void staticButton1(){
        IStaticProxy client = new StaticClient();

        DynamicProxy proxyMiddle = new DynamicProxy(client);

        //1
        ClassLoader classLoader = proxyMiddle.getClass().getClassLoader();
        IStaticProxy proxy = (IStaticProxy)Proxy.newProxyInstance(classLoader, new Class[]{IStaticProxy.class}, proxyMiddle);

        proxy.buy();
        proxy.pay();
        /**
         * 调用这个代理类的过程还是有些略显复杂
         * 我们可以将获取代理类再次封装一下
         *
         * 如下
         */

        //2
        IStaticProxy proxy1 = proxyMiddle.getProxy();
//        proxy1.buy();
//        proxy1.pay();

    }
}
