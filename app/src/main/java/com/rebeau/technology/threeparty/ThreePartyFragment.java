package com.rebeau.technology.threeparty;

import com.rebeau.commons.fragment.BaseTopSegmentTabVPFragment;
import com.rebeau.commons.fragment.BaseTopTabVPFragment;
import com.rebeau.technology.demo.AppAboutFragment;
import com.rebeau.views.loading.RBLoadStatusView;

import java.util.HashMap;

/**
 * author: david
 * date: 2019/5/10
 * description: ${Desc} .
 */
public class ThreePartyFragment extends BaseTopSegmentTabVPFragment {

    @Override
    public void initTabUI() {

        mTitles = new String[]{"简介", "原理"};
    }

    @Override
    public void initFragments() {

        if (mFragmentMap == null) {
            mFragmentMap = new HashMap<>(mTitles.length);
        }
        mFragmentMap.put(0, ThreePartyFragmentOne.newInstance());
        mFragmentMap.put(1, ThreePartyFragmentTwo.newInstance());
    }

    @Override
    protected void onLoadData() {
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }
}
