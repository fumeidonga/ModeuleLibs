package com.rebeau.technology;

import android.content.Intent;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.commons.activity.BaseTabFragmentActivity;
import com.rebeau.technology.android.AndroidFragment;
import com.rebeau.technology.demo.AppAboutFragment;
import com.rebeau.technology.java.JavaFragment;
import com.rebeau.technology.threeparty.ThreePartyFragment;
import com.rebeau.views.loading.RBLoadStatusView;

import java.util.ArrayList;

public class MainActivity extends BaseTabFragmentActivity {

    ////////////////////////////////////// native begin //////////////////////////////////////////////////
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public void switchDefaultTabIndex(){
        switchTab(HOME_PAGE_TWO);
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    ////////////////////////////////////// native end //////////////////////////////////////////////////

    public void sample_text(){
        startActivity(new Intent(this, AboutActivity.class));
    }

    @Override
    protected String getTitleBarName() {
        RBLogUtil.dt(stringFromJNI());
        return null;
    }

    @Override
    protected void onLoadData() {
        RBLogUtil.dt(stringFromJNI());

        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);

    }

    @Override
    public void initTabUI() {
        RBLogUtil.dt();
        mTitles = new String[]{"Java", "Android", "ThreeParty", "更多"};
        mIconUnselectIds = new int[]{
                R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
                R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
        mIconSelectIds = new int[]{
                R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
                R.mipmap.tab_contact_select, R.mipmap.tab_more_select};
    }

    @Override
    public void initFragments() {
        RBLogUtil.dt();
        if (mFragments == null) {
            mFragments = new ArrayList<>(mTitles.length);
        }
        mFragments.add(0, new JavaFragment());
        mFragments.add(1, new AndroidFragment());
        mFragments.add(2, new ThreePartyFragment());
        mFragments.add(3, new AppAboutFragment());

    }

}
