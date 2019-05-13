package com.rebeau.commons.activity.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.rebeau.commons.fragment.BaseAppFragment;

import java.util.Map;

/**
 *
 */
public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles;
    private Map<Integer, BaseAppFragment> mFragmentMap;

    public void setTabTitles(String[] tabTitles) {
        this.tabTitles = tabTitles;
    }

    public void setmFragmentMap(Map<Integer, BaseAppFragment> mFragmentMap) {
        this.mFragmentMap = mFragmentMap;
    }

    public TabViewPagerAdapter(final Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //默认会创建相邻的一个Fragment，但是切换tab后不销毁
        return;
    }

    @Override
    public Fragment getItem(int position) {
        //String type = getType(position);
        if (mFragmentMap != null && mFragmentMap.size() > position && mFragmentMap.containsKey(position)) {
            return mFragmentMap.get(position);
        }
        return new Fragment();
    }

    @Override
    public int getCount() {
        return tabTitles == null ? 0 : tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (tabTitles != null && tabTitles.length > position) ? tabTitles[position] : "";
    }

}
