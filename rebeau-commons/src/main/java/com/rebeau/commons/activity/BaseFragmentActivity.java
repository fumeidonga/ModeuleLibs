package com.rebeau.commons.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.rebeau.base.cache.DataManager;
import com.rebeau.base.config.Constants;
import com.rebeau.base.thread.DVThreadManager;
import com.rebeau.base.utils.RBAppManager;
import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.base.utils.RBStatusBarUtil;
import com.rebeau.commons.lifecycle.RBActivityLifecycleCallbacks;
import com.rebeau.views.loading.RBLoadStatusView;
import com.rebeau.views.loading.RBMainButton;
import com.rebeau.views.night.RBNightShadowHelper;
import com.rebeau.views.slidingview.SlidingPaneLayout;
import com.rebeau.views.titlebar.RBBaseTitleBar;
import com.rebeau.views.titlebar.RBSubPrimaryTitleBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/3/28.
 * 配置的主题，不能使用Theme.AppCompat
 * @style/Custom.SwipeBack.Transparent.Theme
 */

public abstract class BaseFragmentActivity extends FragmentActivity implements SlidingPaneLayout.SlidingPaneListener{

    /**
     * 是否启用物理返回键监听
     */
    private boolean isKeycodeBackDownEnable = true;

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

    /**
     * 自定义侧滑返回View
     * activity use theme  @style/Custom.SwipeBack.Transparent.Theme
     */
    protected SlidingPaneLayout mSlidingPaneLayout;

    /** 夜间模式辅助类 */
    public RBNightShadowHelper mNightShadowHelper;

    /**
     * rxjava
     */
    protected CompositeDisposable compositeSubscription;


    /**
     * 页面需要显示的View（数据请求成功后or默认的静态View）
     *
     * @return
     */
    protected abstract View createSuccessView();

    /**
     * 获取TitleBar名称
     *
     * @return
     */
    protected abstract String getTitleBarName();

    /**
     * 加载数据
     */
    protected abstract void onLoadData();

    /**
     * 是否需要执行load
     *
     * @return
     */
    protected boolean isNeedLoadCreateView() {
        return true;
    }

    /**
     * 该Activity实例，命名为context是因为大部分方法都只需要context，写成context使用更方便
     * @warn 不能在子类中创建
     */
    protected BaseFragmentActivity mActivity = null;
    /**
     * 该Activity的界面，即contentView
     * @warn 不能在子类中创建
     */
    protected View view = null;
    /**
     * 布局解释器
     * @warn 不能在子类中创建
     */
    protected LayoutInflater inflater = null;

    /**
     * Fragment管理器
     * @warn 不能在子类中创建
     */
    protected FragmentManager fragmentManager = null;

    private boolean isAlive = false;
    private boolean isRunning = false;
    /**
     * 线程名列表
     */
    protected List<String> threadNameList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 1.去掉默认的Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        initBaseData();

        // 2.初始化Status View
        initSubStatusBar();

        if (isNeedLoadCreateView()) {

            // 3.初始化页面加载状态View
            initLoadStatusView();

            // 4.初始化TitleBar
            initTitleBar();

            // 5.初始化Content Layout
            mContentLayout = createContentLayout();

            // 6.设置布局
            setContentView(mContentLayout);

            // 7.初始化滑动返回状态
            initSlidingPaneBack();
        } else {
            View view = createSuccessView();
            setContentView(view);
        }

        // 9.初始化夜间模式Layout
        initNightShadow();

        // 10.初始化数据
        initData();

        // 11.执行onLoadData
        if (isExecuteOnLoadDataOnCreateViewEnable()) {
            onLoadData();
        }

        // 10.添加Activity到堆栈
        RBAppManager.getAppManager().addActivity(this, isStintActivity());


    }

    @Override
    protected void onStart() {
        super.onStart();
//        没有网络，没必要展示广告
//        if(!NetworkUtil.isNetWorkOnLine(this)){
//            return;
//        }
        // 判断是否从后台恢复, 且时间间隔符合要求, 显示广告页面
        boolean isFromBackToFront = RBActivityLifecycleCallbacks.sAppState == RBActivityLifecycleCallbacks.STATE_BACK_TO_FRONT;
        if (isShowAdLoading() && isFromBackToFront && !DataManager.getInstance().isSplashTimeOut()) {
            RBLogUtil.e("后台到前台，");
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        isAlive = false;
        isRunning = false;
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        RBAppManager.getAppManager().finishActivity(this);

        DVThreadManager.getThreadManager().destroyThread(threadNameList);
        if (view != null) {
            try {
                view.destroyDrawingCache();
            } catch (Exception e) {
                RBLogUtil.dt( "onDestroy  try { view.destroyDrawingCache();" +
                        " >> } catch (Exception e) {\n" + e.getMessage());
            }
        }

        inflater = null;
        view = null;
        fragmentManager = null;
        threadNameList = null;
        mActivity = null;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                setExitSwichLayout();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 滑动返回动画完成后的回调
     */
    @Override
    public void onSlidingPaneClosed() {

    }

    /**
     * 滑动返回open
     */
    @Override
    public void onSlidingPaneOpen() {

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public final boolean isAlive() {
        return isAlive && mActivity != null;// & ! isFinishing();导致finish，onDestroy内runUiThread不可用
    }

    public final boolean isRunning() {
        return isRunning & isAlive();
    }

    /**
     * 一些不需要后台到前台展示广告的页面可复写此方法
     * @return
     */
    protected boolean isShowAdLoading(){
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

    /**
     * 是否限制打开的页面的数量 特殊页面 其他页面不要覆写
     */
    public boolean isStintActivity() {
        return false;
    }

    /**
     * 是否开启页面加载中过渡效果，
     * 默认返回true开启
     * （某些页面若不需要支持，可重写该方法，返回false）
     *
     * @return
     */
    protected boolean isActivityLoadingEnable() {
        return true;
    }

    /**
     * 初始化页面加载状态View
     */
    private void initLoadStatusView() {
        RBLogUtil.dt();
        mLoadStatusLayout = new RBLoadStatusView(this) {
            @Override
            protected View createSuccessView() {
                return mActivity.createSuccessView();
            }
        };

        // 空值页面按钮事件处理
        setEmptyViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyLoadStatus(RBLoadStatusView.LOADING);
                mActivity.onLoadData();
            }
        });

        // 如果开启了加载中状态，初始化时采用loading状态布局，否则直接使用success状态下布局
        if (isActivityLoadingEnable()) {
            notifyLoadStatus(RBLoadStatusView.LOADING);
        } else {
            notifyLoadStatus(RBLoadStatusView.LOAD_SUCCESS);
        }
    }

    /**
     * 设置空值页监听
     */
    protected void setEmptyViewListener(View.OnClickListener onClickListener) {
        RBMainButton emptyDataButton = mLoadStatusLayout.getEmptyDataView().getEmptyDataButton();
        emptyDataButton.setOnClickListener(onClickListener);
    }

    /**
     * 通知Load状态变化
     *
     * @param loadStatus
     */
    protected void notifyLoadStatus(@RBLoadStatusView.LoadStatus int loadStatus) {
        mLoadStatusLayout.notifyLoadStatus(loadStatus);
    }

    /**
     * 是否开启TitleBar，默认返回true开启（某些页面若不需要支持，可重写该方法，返回false）
     *
     * @return
     */
    protected boolean isActivityTitleBarEnable() {
        return true;
    }

    protected void initTitleBar() {
        RBLogUtil.dt();
        if (isActivityTitleBarEnable()) {
            mTitleBarView = createTitleBar();
            setTitleBtnListener();
            mTitleBarView.setTitleBarName(getTitleBarName());
        }
    }

    /**
     * 创建TitleBar，默认使用rbSubTitleBar（某些页面需要使用其他类型TitleBar，重写该方法）
     *
     * @return
     */
    protected RBBaseTitleBar createTitleBar() {
//        return new rbSubTitleBar(this);
        return new RBSubPrimaryTitleBar(this);
    }

    /**
     * 设置监听TitleBar事件，默认只处理左侧退出（某些页面需要支持不同行为，重写该方法）
     */
    protected void setTitleBtnListener() {
        mTitleBarView.setOnClickListener(new RBBaseTitleBar.OnClickListener() {
            @Override
            public void onLeftClick(View v) {
                setExitSwichLayout();
            }

            @Override
            public void onRightClick(View v, @RBBaseTitleBar.TitleBarRightType int type) {

            }
        });
    }

    /**
     * 创建Content Layout并初始化
     *
     * @return
     */
    private LinearLayout createContentLayout() {
        LinearLayout contentLayout = new LinearLayout(this);
        //设置统一的背景色
        //contentLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        // 如果显示TitleBar，则将其动态加入布局中
        if (isActivityTitleBarEnable()) {
            contentLayout.addView(mTitleBarView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        // 创建状态布局Layout，并将其动态加入布局中
        contentLayout.addView(mLoadStatusLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return contentLayout;
    }

    /**
     * 是否开启滑动返回，默认返回true开启
     *
     * 不需要滑动返回的页面return  FALSE
     *
     * @return
     */
    protected boolean isSlidingPaneBackEnable() {
        return true;
    }

    /**
     * 初始化滑动返回
     */
    private void initSlidingPaneBack() {
        mSlidingPaneLayout = new SlidingPaneLayout(this);
        if (isSlidingPaneBackEnable()) {
            mSlidingPaneLayout.bindActivity(this);
            mSlidingPaneLayout.setSlidingPaneListener(this);
            mSlidingPaneLayout.setCloseSlidingPane(false);
        } else {
            mSlidingPaneLayout.setCloseSlidingPane(true);
        }
    }

    public void setExitSwichLayout() {
        finish();
        //this.overridePendingTransition(0, R.anim.slide_out_right);
    }

    /**
     * 初始化页面状态栏
     *
     * @param
     */
    protected void initSubStatusBar() {
        RBLogUtil.dt();
        RBStatusBarUtil.initSubStatusBar(this);

    }

    /**
     * 初始化夜间模式Layout
     */
    protected void initNightShadow() {
        mNightShadowHelper = RBNightShadowHelper.create(this, getNightModel());
    }


    /**
     * 初始化页面数据（如getIntent()数据）
     */
    protected void initData() {

    }

    public RBLoadStatusView getLoadStatusLayout() {
        return mLoadStatusLayout;
    }

    /**
     * @return
     */
    protected int getNightModel(){
        return DataManager.getInstance().getInt(Constants.ACTIVITY_NIGHT_MODEL);
    }

    protected void initBaseData() {

        isAlive = true;
        mActivity = (BaseFragmentActivity) getActivity();
        fragmentManager = getSupportFragmentManager();

        inflater = getLayoutInflater();

        threadNameList = new ArrayList<String>();

    }

    /**
     * 是否关闭侧滑返回
     * @param closeSlidingPane true关闭，否则开启
     */
    public void setCloseSlidingPane(boolean closeSlidingPane) {
        if(mSlidingPaneLayout != null){
            mSlidingPaneLayout.setCloseSlidingPane(closeSlidingPane);
        }
    }

    public void addSubscription(Disposable subscription) {
        if (subscription == null) {
            return;
        }
        if (compositeSubscription != null) {
            compositeSubscription.add(subscription);
        } else {
            compositeSubscription = new CompositeDisposable();
            compositeSubscription.add(subscription);

        }
    }

    protected void unCPSubscribe() {
        if (compositeSubscription != null) {
            compositeSubscription.clear();
        }
    }
    /**
     * return this
     * @return
     */
    protected Activity getActivity() {
        return this;
    }


    /**
     * USE ThreadPoolExecuter
     * @param runnable
     */
    public void runThreadByThreadPool(Runnable runnable) {
        DVThreadManager.getThreadManager().execute(runnable);
    }

    /**
     * use handlerThread
     * @param name
     * @param runnable
     */
    public final Handler runThreadByThreadHandler(String name, Runnable runnable) {

        if (isAlive() == false) {
            RBLogUtil.dt( "runThread  isAlive() == false >> return null;");
            return null;
        }
        Handler handler = DVThreadManager.getThreadManager().runThread(name, runnable);
        if (handler == null) {
            RBLogUtil.dt( "runThread handler == null >> return null;");
            return null;
        }

        if (threadNameList.contains(name) == false) {
            threadNameList.add(name);
        }
        return handler;
    }
}
