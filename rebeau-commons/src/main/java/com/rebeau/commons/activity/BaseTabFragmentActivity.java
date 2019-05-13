package com.rebeau.commons.activity;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;
import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.base.utils.RBScreenUtil;
import com.rebeau.commons.R;
import com.rebeau.commons.R2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: david
 * date: 2019/5/9
 * description: ${参考下面的开源库做} .
 * @see <a href="https://github.com/H07000223/FlycoTabLayout">App 各种 Tab实现方法</a>
 * 通过fragment的hide show方式实现
 */
public abstract class BaseTabFragmentActivity extends BaseFragmentActivity {

    public static final int HOME_PAGE_NONE = -1;
    public static final int HOME_PAGE_ONE = 0;
    public static final int HOME_PAGE_TWO = 1;
    public static final int HOME_PAGE_THREE = 2;
    public static final int HOME_PAGE_FOUR = 3;

    /**
     * 当前显示的页面标记
     */
    private static int CURRENT_SHOW_INDEX = HOME_PAGE_TWO;

    private static int SWITCH_TAB_INDEX = HOME_PAGE_NONE;

    public List<Fragment> mFragments;
    public Fragment mCurrentFragment;

    @BindView(R2.id.base_tab_fragment_navigation_bar)
    CommonTabLayout mCommonTabLayout;

    /*@BindView(R2.id.base_tab_fragment_container)
    FrameLayout mFrameLayout;*/

    /**
     * 底部tab的标题
     */
    public String[] mTitles;
    /**
     * 底部tab 未选中的图标
     */
    public int[] mIconUnselectIds;
    /**
     *底部tab 选中的图标
     */
    public int[] mIconSelectIds;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RBLogUtil.dt();
        switchDefaultTabIndex();
    }

    public void switchDefaultTabIndex(){
        switchTab(HOME_PAGE_ONE);
    }

    @Override
    protected View createSuccessView() {
        RBLogUtil.dt();
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.base_tab_fragment_layout, null);
        ButterKnife.bind(this, view);
        initTabUI();
        initTab();
        initTabListener();

        return view;
    }

    @Override
    protected void onLoadData() {
        RBLogUtil.dt();

    }

    /**
     * 初始化tab的标题、图片
     */
    public abstract void initTabUI();

    /**
     *
     */
    public abstract void initFragments();

    public void initTabListener(){
        RBLogUtil.dt();
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                RBLogUtil.dt();
                switchTab(position);
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
    /**
     * 切换Tab
     */
    public void switchTab(@HomePage int position) {
        RBLogUtil.dt(position);
        //如果传入的Position是HOME_PAGE_NONE 即 -1 则必须要给他一个默认值，现默认为书架界面
        if (HOME_PAGE_NONE == position) {
            position = HOME_PAGE_ONE;
        }
        if (mCurrentFragment != null && CURRENT_SHOW_INDEX == position) {
            return;
        }
        CURRENT_SHOW_INDEX = position;

        switchFragment(getFragmentByPos(position), position);
        mCommonTabLayout.setCurrentTab(position);
    }

    private Fragment getFragmentByPos(@HomePage int pos) {
        if (mFragments == null || mFragments.size() <= 0) {
            initFragments();
        }
        if (mFragments != null && mFragments.size() > pos) {
            Fragment fragment = mFragments.get(pos);
            if (fragment != null) {
                return fragment;
            } else {
                //防止fragments没有被回收，但是内部包含页面的实例对象被回收了。
                initFragments();
                Fragment newFrg = mFragments.get(pos);
                if (newFrg != null) {
                    return newFrg;
                }
            }
        }
        //错误场景下，返回一个空白页。
        return new Fragment();
    }
    
    /**
     * 设置tab的数据
     */
    private void initTab(){
        RBLogUtil.dt();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mCommonTabLayout.setTabData(mTabEntities);
    }
    
    /**
     * 切换Tab
     */
    private void switchFragment(Fragment to, int position) {
        RBLogUtil.et(position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mCurrentFragment != to) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (!to.isAdded()) {
                if (mCurrentFragment != null) {
                    transaction.hide(mCurrentFragment).add(R.id.base_tab_fragment_container, to, position + "").commitAllowingStateLoss();
                } else {
                    transaction.add(R.id.base_tab_fragment_container, to, position + "").commitAllowingStateLoss();
                }
            } else {
                if (mCurrentFragment == null) {
                    transaction.show(to).commitAllowingStateLoss();
                } else {
                    transaction.hide(mCurrentFragment).show(to).commitAllowingStateLoss();
                }
            }
            mCurrentFragment = to;
        }
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
            UnreadMsgUtils.setSize(rtv_2_2, RBScreenUtil.dpToPx(this, 7.5f));
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
     * 是否开启页面加载中过渡效果，
     * 默认返回true开启
     * （某些页面若不需要支持，可重写该方法，返回false）
     *
     * @return
     */
    protected boolean isActivityLoadingEnable() {
        return false;
    }

    /**
     * 是否开启TitleBar，默认返回true开启（某些页面若不需要支持，可重写该方法，返回false）
     *
     * @return
     */
    protected boolean isActivityTitleBarEnable() {
        return false;
    }

    /**
     * 是否开启滑动返回，默认返回true开启
     *
     * 不需要滑动返回的页面return  FALSE
     *
     * @return
     */
    protected boolean isSlidingPaneBackEnable() {
        return false;
    }
    /**
     * 不需要后台到前台展示广告
     * @return
     */
    protected boolean isShowAdLoading(){
        return false;
    }

    /**
     * 是否采用默认的onLoad执行时机，默认返回true开启（某些页面若不需要支持，可重写该方法，返回false）
     *
     * @return
     */
    protected boolean isExecuteOnLoadDataOnCreateViewEnable() {
        return true;
    }

    /**
     * 是否限制打开的页面的数量 特殊页面 其他页面不要覆写
     */
    public boolean isStintActivity() {
        return false;
    }


    @IntDef({HOME_PAGE_NONE, HOME_PAGE_ONE, HOME_PAGE_TWO, HOME_PAGE_THREE, HOME_PAGE_FOUR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HomePage {
    }
}
