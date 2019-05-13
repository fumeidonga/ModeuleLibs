package com.rebeau.views.loading;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rebeau.views.R;
import com.rebeau.views.R2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 页面无内容时空值提醒View（包括默认无数据提醒、无网络提醒、并可设置下架提醒等）
 *
 */

public class RBMainEmptyDataView extends RBBaseEmptyDataView {

    /** 暂无数据样式 */
    public final static int EMPTY_DATA_VIEW_STYLE_DEFAULT = 0;
    /** 无网络样式 */
    public final static int EMPTY_DATA_VIEW_STYLE_NO_NETWORK = 1;
    /** 其他自由设置样式：自由设置文案、按钮等 */
    public final static int EMPTY_DATA_VIEW_STYLE_OTHER_ONE = 2;
//    public final static int EMPTY_DATA_VIEW_STYLE_OTHER_TWO = 3;

    /**
     * 提示图片
     */
    @BindView(R2.id.iv_empty_data_style_image)
    ImageView mIVImage;
    /**
     * 提示1
     */
    @BindView(R2.id.tv_empty_data_style_first_text)
    TextView mTVFirstText;
    /**
     * 提示2
     */
    @BindView(R2.id.tv_empty_data_style_tips)
    TextView mTVTips;
    /**
     * 提示按钮
     */
    @BindView(R2.id.bt_empty_data_style_default)
    RBMainButton mBTButton;

    private @DrawableRes
    int DEFAULT_NO_DATA_IMAGE;
    private String DEFAULT_NO_DATA_TEXT;
    private @DrawableRes
    int NO_NETWORK_IMAGE;
    private String NO_NETWORK_TEXT;
    private String NO_NETWORK_BUTTON;
    private @DrawableRes
    int OTHER_ONE_IMAGE;
    //    private @DrawableRes int OTHER_TWO_IMAGE;

    @IntDef({EMPTY_DATA_VIEW_STYLE_DEFAULT, EMPTY_DATA_VIEW_STYLE_NO_NETWORK, EMPTY_DATA_VIEW_STYLE_OTHER_ONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EmptyViewStyle {

    }

    public RBMainEmptyDataView(Context context) {
        super(context);
    }

    public RBMainEmptyDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RBMainEmptyDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onInit(Context context) {
        View styleView = LayoutInflater.from(context).inflate(R.layout.rb_ui_empty_view_style, this);
        ButterKnife.bind(this, styleView);

        DEFAULT_NO_DATA_IMAGE = R.drawable.rb_empty_remind_ic_no_data;
        DEFAULT_NO_DATA_TEXT = getContext().getString(R.string.rb_ui_empty_remind_no_data);
        NO_NETWORK_IMAGE = R.drawable.rb_empty_remind_ic_network_error;
        NO_NETWORK_TEXT = getContext().getString(R.string.rb_ui_empty_remind_network_error);
        NO_NETWORK_BUTTON = getContext().getString(R.string.rb_ui_empty_remind_try_again);
        OTHER_ONE_IMAGE = R.drawable.rb_empty_remind_ic_no_bookmark;
//        OTHER_TWO_IMAGE = R.drawable.page_none_comment;

        // 默认
        setShowStyle(EMPTY_DATA_VIEW_STYLE_DEFAULT);
        setGravity(Gravity.CENTER);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setEmptyDataText(String text) {
        mTVFirstText.setText(text);
    }

    private void setEmptyDataImage(int resId) {
        mIVImage.setBackgroundResource(resId);
    }

    public void setEmptyDataTipsText(String text) {
        mTVTips.setVisibility(View.VISIBLE);
        mTVTips.setText(text);
    }

    public void setEmptyDataButton(String text) {
        mBTButton.setVisibility(View.VISIBLE);
        mBTButton.setText(text);
    }

    public RBMainButton getEmptyDataButton() {
        return mBTButton;
    }

    public void setShowStyle(@EmptyViewStyle int showStyle) {
        switch (showStyle) {
            case EMPTY_DATA_VIEW_STYLE_DEFAULT:
                setEmptyDataImage(DEFAULT_NO_DATA_IMAGE);
                setEmptyDataText(DEFAULT_NO_DATA_TEXT);
                mTVTips.setVisibility(View.GONE);
                mBTButton.setVisibility(View.GONE);
                break;
            case EMPTY_DATA_VIEW_STYLE_NO_NETWORK:
                setEmptyDataImage(NO_NETWORK_IMAGE);
                setEmptyDataText(NO_NETWORK_TEXT);
                mTVTips.setVisibility(View.GONE);
                setEmptyDataButton(NO_NETWORK_BUTTON);
                break;
            case EMPTY_DATA_VIEW_STYLE_OTHER_ONE:
                setEmptyDataImage(OTHER_ONE_IMAGE);
                break;
//            case EMPTY_DATA_VIEW_STYLE_OTHER_TWO:
//                setEmptyDataImage(OTHER_TWO_IMAGE);
//                break;
            default:
                break;
        }
    }
    
}
