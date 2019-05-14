package com.rebeau.technology.android;

import com.rebeau.commons.fragment.BaseTopTabVPFragment;
import com.rebeau.views.loading.RBLoadStatusView;

import java.util.HashMap;

/**
 * author: david
 * date: 2019/5/10
 * description: ${Desc} .
 */
public class AndroidFragment extends BaseTopTabVPFragment {

    @Override
    public void initTabUI() {

        mTitles = new String[]{"基础", "深入", "原理", "优化"};
    }

    @Override
    public void initFragments() {

        if (mFragmentMap == null) {
            mFragmentMap = new HashMap<>(mTitles.length);
        }
        mFragmentMap.put(0, AndroidFragmentOne.newInstance("android fragment 0"));
        mFragmentMap.put(1, AndroidFragmentTwo.newInstance("android fragment 1"));
        mFragmentMap.put(2, AndroidFragmetThree.newInstance("android fragment 2"));
        mFragmentMap.put(3, AndroidPerformaceFragment.newInstance("android fragment 3"));
    }

    @Override
    protected void onLoadData() {
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }
}
