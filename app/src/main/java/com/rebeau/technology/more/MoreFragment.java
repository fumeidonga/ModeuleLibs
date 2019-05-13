package com.rebeau.technology.more;

import com.rebeau.commons.fragment.BaseTopTabVPFragment;
import com.rebeau.technology.java.JavaFragmentDetail;
import com.rebeau.views.loading.RBLoadStatusView;

import java.util.HashMap;

/**
 * author: david
 * date: 2019/5/10
 * description: ${Desc} .
 */
public class MoreFragment extends BaseTopTabVPFragment {

    @Override
    public void initTabUI() {

        mTitles = new String[]{"基础", "深入", "原理", "更多"};
    }

    @Override
    public void initFragments() {

        if (mFragmentMap == null) {
            mFragmentMap = new HashMap<>(mTitles.length);
        }
        mFragmentMap.put(0, JavaFragmentDetail.newInstance("java fragment 0"));
        mFragmentMap.put(1, JavaFragmentDetail.newInstance("java fragment 1"));
        mFragmentMap.put(2, JavaFragmentDetail.newInstance("java fragment 2"));
        mFragmentMap.put(3, JavaFragmentDetail.newInstance("java fragment 3"));
    }

    @Override
    protected void onLoadData() {
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }
}
