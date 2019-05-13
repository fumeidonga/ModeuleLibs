package com.rebeau.commons.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.views.loading.RBLoadStatusView;
import com.rebeau.views.loading.RBMainButton;
import com.rebeau.views.titlebar.RBBaseTitleBar;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * 项目基类Fragment，定义公共基础操作
 * 说明：
 * （未继承皮肤Fragment组件，当需要使用皮肤功能时，将Fragment->SkinBaseFragment，
 */

public abstract class BaseAppFragment extends Fragment {

    public static final String BASE_APP_FRAGMENT_ONE = "BASE_APP_FRAGMENT_ONE";
    public static final String BASE_APP_FRAGMENT_TWO = "BASE_APP_FRAGMENT_TWO";

    protected Activity mActivity;
    /**
     * 是否采用默认的onLoad()加载时间，默认true启用
     */
    private boolean isExecuteOnLoadDataOnCreateView;
    /**
     * 当前的fragment是否可见
     */
    protected boolean isCurrentFragmentVisible = false;
    /**
     * 是否创建了view
     */
    protected boolean isViewCreated = false;

    /**
     * 自定义页面加载状态Layout（加载中、加载完成、加载错误）
     */
    private RBLoadStatusView mLoadStatusLayout;
    /**
     * 自定义Titlebar
     */
    private RBBaseTitleBar mTitleBarView;
    /**
     * 自定义Content Layout
     */
    private LinearLayout mContentLayout;

    protected CompositeDisposable mCompositeDisposable;

    /*public static BaseAppFragment newInstance(String gender, String channelType) {
        Bundle args = new Bundle();
        args.putString(BASE_APP_FRAGMENT_ONE, gender);
        args.putString(BASE_APP_FRAGMENT_TWO, channelType);
        BaseAppFragment fragment = new BaseAppFragment();
        fragment.setArguments(args);
        return fragment;
    }*/

    /**
     * 页面需要显示的View（数据请求成功后or默认的静态View）
     *
     * @return
     */
    protected abstract View createSuccessView(@Nullable ViewGroup container);

    /**
     * 获取TitleBar名称
     * @return
     */
//    protected abstract String getTitleBarName();

    /**
     * 加载数据
     */
    protected abstract void onLoadData();

    @Override
    public void onAttach(Context context) {
        RBLogUtil.dt("onAttach");
        super.onAttach(context);
        mActivity= (Activity) context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        RBLogUtil.dt("setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
        isCurrentFragmentVisible = isVisibleToUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RBLogUtil.dt("onCreateView");
        // 1.去掉Layout，如果已有
        if (mLoadStatusLayout != null) {
            removeSelfFromParent(mLoadStatusLayout);
            mLoadStatusLayout = null;
        }
        if (mTitleBarView != null) {
            removeSelfFromParent(mTitleBarView);
            mTitleBarView = null;
        }
        if (mContentLayout != null) {
            removeSelfFromParent(mContentLayout);
            mContentLayout = null;
        }

        // 2.初始化页面加载状态View
        initLoadStatusView(container);

        // 3.初始化TitleBar
        initTitleBar();

        // 4.初始化Content Layout
        mContentLayout = createContentLayout();

        // 5.执行onLoadData
        isExecuteOnLoadDataOnCreateView = isExecuteOnLoadDataOnCreateViewEnable();
        if (isExecuteOnLoadDataOnCreateView) {
            onLoadData();
        }

        return mContentLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RBLogUtil.dt("onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RBLogUtil.dt();
        isViewCreated = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RBLogUtil.dt();
//        mActivity=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RBLogUtil.dt();
        unCPSubscribe();
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

    protected void initTitleBar() {
        if (isFragmentTitleBarEnable()) {
            mTitleBarView = createTitleBar();
            setTitleBtnListener();
            mTitleBarView.setTitleBarName(getTitleBarName());
        }
    }

    /**
     * 创建TitleBar，默认使用RBSubTitleBar（某些页面需要使用其他类型TitleBar，重写该方法）
     * com.rebeau.views.titlebar
     * @return
     */
    protected RBBaseTitleBar createTitleBar() {
        RBLogUtil.dt();
        /*if (this instanceof RecommendFragment) {
            return new RBSubPrimaryTitleBar(getActivity());
        } else {
            return new RBPrimaryTitleBar(getActivity());
        }*/
        return null;
    }

    /**
     * 设置监听TitleBar事件，默认只处理左侧退出（某些页面需要支持不同行为，重写该方法）
     */
    protected void setTitleBtnListener() {
        mTitleBarView.setOnClickListener(new RBBaseTitleBar.OnClickListener() {
            @Override
            public void onLeftClick(View v) {
//                CommonMethod.EventStatistic(getActivity(), "shelf_leftbar");

            }

            @Override
            public void onRightClick(View v, @RBBaseTitleBar.TitleBarRightType int type) {
//                CommonMethod.EventStatistic(getActivity(), "topbar_search");
            }
        });
    }

    /**
     * 获取TitleBar名称
     *
     * @return
     */
    protected String getTitleBarName() {
        return "";
    }

    /**
     * 移除Content Layout
     *
     * @param view
     */
    private void removeSelfFromParent(View view) {
        // 先找到父类，再通过父类移除孩子
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    /**
     * 创建Content Layout并初始化
     *
     * @return
     */
    private LinearLayout createContentLayout() {
        LinearLayout contentLayout = new LinearLayout(getActivity());
        contentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        // 如果显示TitleBar，则将其动态加入布局中
        if (isFragmentTitleBarEnable()) {
            contentLayout.addView(mTitleBarView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        // 创建状态布局Layout，并将其动态加入布局中
        contentLayout.addView(mLoadStatusLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return contentLayout;
    }

    /**
     * 初始化页面加载状态View
     */
    private void initLoadStatusView(@Nullable final ViewGroup container) {
        mLoadStatusLayout = new RBLoadStatusView(getActivity()) {
            @Override
            protected View createSuccessView() {
                return BaseAppFragment.this.createSuccessView(container);
            }
        };

        // 空值页面按钮事件处理
        RBMainButton emptyDataButton = mLoadStatusLayout.getEmptyDataView().getEmptyDataButton();
        setEmptyViewListener(emptyDataButton);

        initLoadingView();
    }

    protected void initLoadingView() {
        // 如果开启了加载中状态，初始化时采用loading状态布局，否则直接使用success状态下布局
        if (isFragmentLoadingEnable()) {
            notifyLoadStatus(RBLoadStatusView.LOADING);
        } else {
            notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
        }
    }

    public RBLoadStatusView getLoadStatusLayout() {
        return mLoadStatusLayout;
    }

    /**
     * 设置空值页监听
     * @param emptyDataButton 空值页按钮
     */
    protected void setEmptyViewListener(RBMainButton emptyDataButton) {
        emptyDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyLoadStatus(RBLoadStatusView.LOADING);
                BaseAppFragment.this.onLoadData();
            }
        });
    }

    @Nullable
    public RBBaseTitleBar getTitleBarView() {
        return mTitleBarView;
    }

    /**
     * 通知Load状态变化
     *
     * @param loadStatus
     */
    protected void notifyLoadStatus(@RBLoadStatusView.LoadStatus int loadStatus) {
        mLoadStatusLayout.notifyLoadStatus(loadStatus);
    }

    protected synchronized void addSubscription(Disposable disposable){
        if (disposable == null) {
            return;
        }
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    protected void unCPSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }
}
