package com.rebeau.technology.java.testdesignmodel.facade;

import android.app.Activity;
import android.os.Bundle;
import com.rebeau.commons.markdown.MarkdownUtils;
import com.rebeau.technology.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FacadeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facade);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.button8)
    public void readme(){

        MarkdownUtils.setData(this, "testdesignmodel/facade/外观模式.MD");
    }
}
