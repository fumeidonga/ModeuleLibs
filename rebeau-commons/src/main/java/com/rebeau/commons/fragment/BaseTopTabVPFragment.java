package com.rebeau.commons.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;
import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.base.utils.RBScreenUtil;
import com.rebeau.commons.R;
import com.rebeau.commons.R2;
import com.rebeau.commons.activity.adapter.TabViewPagerAdapter;
import com.rebeau.views.titlebar.RBBaseTitleBar;
import com.rebeau.views.titlebar.RBNOTitleBar;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: david
 * date: 2019/5/9
 * description: ${参考下面的开源库做} .
 * @see <a href="https://github.com/H07000223/FlycoTabLayout">App 各种 Tab实现方法</a>
 * tab 关联viewpager实现
 */
public abstract class BaseTopTabVPFragment extends BaseAppFragment {

    public Map<Integer, BaseAppFragment> mFragmentMap;

    @BindView(R2.id.base_tab_sliding_layout)
    SlidingTabLayout mCommonTabLayout;

    @BindView(R2.id.base_tab_view_pager)
    ViewPager mViewPager;

    TabViewPagerAdapter mPagerAdapter;

    /**
     * tab的标题
     */
    public String[] mTitles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RBLogUtil.dt();
    }

    @Override
    protected View createSuccessView(@Nullable ViewGroup container) {
        RBLogUtil.et("");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.base_tab_top_view_pager_fragment_layout, container, false);
        ButterKnife.bind(this, view);

        initTabUI();
        initFragments();
        initTab();
        initTabListener();
        RBLogUtil.et("");
        return view;
    }

    /**
     * 初始化tab的标题、图片
     */
    public abstract void initTabUI();

    /**
     *
     */
    public abstract void initFragments();

    /**
     * 设置tab的数据
     */
    private void initTab(){
        mPagerAdapter = new TabViewPagerAdapter(mActivity, getChildFragmentManager());
        mPagerAdapter.setTabTitles(mTitles);
        mPagerAdapter.setmFragmentMap(mFragmentMap);

        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                RBLogUtil.dt();
            }

            @Override
            public void onPageSelected(int position) {
                RBLogUtil.dt();
                mCommonTabLayout.setCurrentTab(position);


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                RBLogUtil.dt();

            }
        });
        mViewPager.setCurrentItem(0);

        mCommonTabLayout.setViewPager(mViewPager);
    }

    public void initTabListener(){
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                RBLogUtil.dt();
                mViewPager.setCurrentItem(position);

            }

            /**
             * 重复的选择同一个tab
             * @param position
             */
            @Override
            public void onTabReselect(int position) {
                RBLogUtil.dt();
                if (position == 0) {

                }
            }
        });
    }

    @Override
    protected RBBaseTitleBar createTitleBar() {
        return new RBNOTitleBar(mActivity);
    }
    /**
     * @param postion
     * 提示的小红点
     */
    public void setRedPoint(int postion){
        mCommonTabLayout.showDot(postion);
    }
    /**
     * @param postion
     * @param redsize  设置小红点的大小
     * 提示的小红点
     */
    public void setRedPoint(int postion, float redsize){
        mCommonTabLayout.showDot(postion);
        MsgView rtv_2_2 = mCommonTabLayout.getMsgView(2);
        if (rtv_2_2 != null) {
            //UnreadMsgUtils.setSize(rtv_2_2, dp2px(redsize));
            UnreadMsgUtils.setSize(rtv_2_2, RBScreenUtil.dpToPx(mActivity, 7.5f));
        }
    }


    /**
     * 提示的小红点, 显示数量
     * @param postion
     * @param num num小于等于0显示红点,num大于0显示数字
     */
    public void setRedPointNum(int postion, int num){
        mCommonTabLayout.showMsg(postion, num);
        mCommonTabLayout.setMsgMargin(postion, -5, 5);

        //设置未读消息背景
        /*MsgView rtv_2_3 = mCommonTabLayout.getMsgView(postion);
        if (rtv_2_3 != null) {
            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }*/
    }


    /**
     * 是否开启页面加载中过渡效果，默认返回true开启（某些页面若不需要支持，可重写该方法，返回false）
     *
     * @return
     */
    protected boolean isFragmentLoadingEnable() {
        return true;
    }

    /**
     * 是否开启TitleBar，默认返回false不开启（某些页面若需要支持，可重写该方法，返回true）
     *
     * @return
     */
    protected boolean isFragmentTitleBarEnable() {
        return true;
    }

    /**
     * 是否采用默认的onLoad执行时机，默认返回true开启（某些页面若不需要支持，可重写该方法，返回false）
     *
     * @return
     */
    protected boolean isExecuteOnLoadDataOnCreateViewEnable() {
        return true;
    }
}
