package com.rebeau.views.loading;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 加载状态的Layout，
 *
 * 包含加载中、加载成功View、加载错误
 *
 */

public abstract class RBLoadStatusView extends FrameLayout {

    /** 未加载，初始状态 */
    public static final int UNLOAD = 0;
    /** 加载中，Loading View显示 */
    public static final int LOADING = 1;
    /** 加载成功，Success View显示 */
    public static final int LOAD_SUCCESS = 2;
    /** 加载出错，默认状态暂无数据样式提示 */
    public static final int LOAD_ERROR_DEFAULT = 3;
    /** 加载出错，网络状态提示 */
    public static final int LOAD_ERROR_NO_NETWORK = 4;
    /** 加载出错，规范样式一提示 */
    public static final int LOAD_ERROR_OTHER_ONE = 5;
    /** 加载出错，规范样式二提示 */
//    public static final int LOAD_ERROR_OTHER_TWO = 6;

    /** 加载中View */
    private RBInnerLoadingView mLoadingView;
    /** 提示View */
    private RBMainEmptyDataView mEmptyDataView;
    /** 成功View */
    private View mSuccessView;

    private boolean isLoadAdd;

    @IntDef({UNLOAD, LOADING, LOAD_SUCCESS, LOAD_ERROR_DEFAULT, LOAD_ERROR_NO_NETWORK, LOAD_ERROR_OTHER_ONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadStatus {}

    protected abstract View createSuccessView();

    public RBLoadStatusView(@NonNull Context context) {
        this(context, null);
    }

    public RBLoadStatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RBLoadStatusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChildView(context);
        initLoadStatus();
    }

    private void initChildView(Context context) {
        initLoadingView(context);
        initErrorView(context);
        initSuccessView();
    }

    private void initLoadingView(Context context) {
        if (mLoadingView == null) {
            mLoadingView = new RBInnerLoadingView(context);
        }
        if (mLoadingView.getParent() == null) {
            addView(mLoadingView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            isLoadAdd = true;
        }
    }

    private void initErrorView(Context context) {
        mEmptyDataView = new RBMainEmptyDataView(context);
        addView(mEmptyDataView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void initSuccessView() {
        mSuccessView = createSuccessView();
        if (mSuccessView != null) {
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            if (mSuccessView.getParent() == null) {
                addView(mSuccessView, layoutParams);
            } else {
                ((ViewGroup) mSuccessView.getParent()).removeView(mSuccessView);
                addView(mSuccessView, layoutParams);
            }
        }
    }

    private void initLoadStatus() {
        notifyLoadStatus(UNLOAD);
    }

    /**
     * 根据状态设置当前View的显示
     * @param loadStatus
     */
    public void notifyLoadStatus(@LoadStatus int loadStatus) {
        switch (loadStatus) {
            case UNLOAD:
                mLoadingView.setVisibility(View.GONE);
                mLoadingView.controlAnimation(false);
                isLoadAdd = false;
                mEmptyDataView.setVisibility(View.GONE);
                if (mSuccessView != null) {
                    mSuccessView.setVisibility(View.GONE);
                }
                break;
            case LOADING:
                mLoadingView.setVisibility(View.VISIBLE);
                mLoadingView.controlAnimation(true);
                mEmptyDataView.setVisibility(View.GONE);
                if (mSuccessView != null) {
                    mSuccessView.setVisibility(View.GONE);
                }
                break;
            case LOAD_SUCCESS:
                mLoadingView.setVisibility(View.GONE);
                mLoadingView.controlAnimation(false);
                isLoadAdd = false;
                mEmptyDataView.setVisibility(View.GONE);
                if (mSuccessView != null) {
                    mSuccessView.setVisibility(View.VISIBLE);
                }
                break;
            case LOAD_ERROR_DEFAULT:
                mLoadingView.setVisibility(View.GONE);
                mLoadingView.controlAnimation(false);
                isLoadAdd = false;
                mEmptyDataView.setShowStyle(RBMainEmptyDataView.EMPTY_DATA_VIEW_STYLE_DEFAULT);
                mEmptyDataView.setVisibility(View.VISIBLE);
                if (mSuccessView != null) {
                    mSuccessView.setVisibility(View.GONE);
                }
                break;
            case LOAD_ERROR_NO_NETWORK:
                mLoadingView.setVisibility(View.GONE);
                mLoadingView.controlAnimation(false);
                isLoadAdd = false;
                mEmptyDataView.setShowStyle(RBMainEmptyDataView.EMPTY_DATA_VIEW_STYLE_NO_NETWORK);
                mEmptyDataView.setVisibility(View.VISIBLE);
                if (mSuccessView != null) {
                    mSuccessView.setVisibility(View.GONE);
                }
                break;
            case LOAD_ERROR_OTHER_ONE:
                mLoadingView.setVisibility(View.GONE);
                mLoadingView.controlAnimation(false);
                isLoadAdd = false;
                mEmptyDataView.setShowStyle(RBMainEmptyDataView.EMPTY_DATA_VIEW_STYLE_OTHER_ONE);
                mEmptyDataView.setVisibility(View.VISIBLE);
                if (mSuccessView != null) {
                    mSuccessView.setVisibility(View.GONE);
                }
                break;
//            case LOAD_ERROR_OTHER_TWO:
//                mLoadingView.setVisibility(View.GONE);
//                mEmptyDataView.setShowStyle(RBMainEmptyDataView.EMPTY_DATA_VIEW_STYLE_OTHER_TWO);
//                mEmptyDataView.setVisibility(View.VISIBLE);
//                if (mSuccessView != null) {
//                    mSuccessView.setVisibility(View.GONE);
//                }
//                break;
            default:
                break;
        }
    }

    public RBMainEmptyDataView getEmptyDataView() {
        return mEmptyDataView;
    }

}
