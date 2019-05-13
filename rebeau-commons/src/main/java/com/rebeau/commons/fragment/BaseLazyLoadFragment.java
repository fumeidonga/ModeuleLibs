package com.rebeau.commons.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.views.loading.RBLoadStatusView;

/**
 *
 *     在BaseAppFragment基础上加入懒加载，在ViewPager嵌入Fragment时使用此BaseLazyLoadFragment。
 *
 *     1.关闭BaseAPPFragment里在onCreateView时加载数据
 *
 *     2.利用判断View是否加载完毕isViewCreated参数
 *     以及setUserVisibleHint参数来判断当前Fragment是否可见
 *
 *      1.setUserVisibileHint() 只在FragmentPagerAdapter中才会被调用，所以此BaseLazyLoadFragment只能用在ViewPager之中。
 *      2.在加载数据后，如下次切换不需要再次加载数据，haveLazyData方法返回是否已有加载数据。
 */
public abstract class BaseLazyLoadFragment extends BaseAppFragment {

    /**
     * 是否在加载或已加载过数据
     */
    protected boolean isLazyLoad = false;

    /**
     * 是否拥有数据，在加载完毕时务必返回true
     * 否则在ViewPager切换时，会再次触发onLoadData方法
     * @return
     *       true
     *              页面也有数据，在切换时不需要重写执行onLoadData方法
     *
     *       false
     *              页面无数据，或在切换时需要再次执行onLoadData方法
     */
    protected abstract boolean haveLazyData();

    /**
     * 视图是否已经对用户可见
     * 在Fragment切换时，来实现数据重新加载的功能
     * 其传入值isVisibleToUser也是返回值getUserVisibleHint()
     * true
     *      Fragment可见
     * false
     *      Fragment不可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        RBLogUtil.dt();
        /**
         * 切换时让没有加载过数据的Fragment加载数据
         */
        isCanLoadData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RBLogUtil.dt();
        /**
         * 初始化的时候去加载数据
         */
        isCanLoadData();
    }

    /**
     * lazy模式下，不自动加载数据
     * @return
     */
    @Override
    protected boolean isExecuteOnLoadDataOnCreateViewEnable() {
        return false;
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isViewCreated) {
            return;
        }
        if (getUserVisibleHint()) {
            if(!haveLazyData()) {
                notifyLoadStatus(RBLoadStatusView.LOADING);
                onLoadData();
                isLazyLoad = true;
            } else {
                notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
            }
        } else {
            if (isLazyLoad) {
                stopLoad();
            }
            notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
        }
    }

    @Override
    protected void initLoadingView() {
        RBLogUtil.dt();
        notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
    }

    /**
     * 当视图已经对用户不可见并且加载过数据，
     * 如果需要在切换到其他页面时停止加载数据，
     * 可以重写此方法
     */
    protected void stopLoad() { }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RBLogUtil.dt();
        isLazyLoad = false;
    }
}
