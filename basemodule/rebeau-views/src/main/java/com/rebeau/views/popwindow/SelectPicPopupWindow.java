/**
 * 文件名 SelectPicPopupWindw.jaba
 * 程序名： 选择图片的popupWindow
 * @author 
 */

package com.rebeau.views.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rebeau.views.R;

/**
 *  mMenuWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
 * 	mMenuWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
 */
public class SelectPicPopupWindow extends PopupWindow {

	private Button btn_cancel;
	private TextView btn_take_photo, btn_pick_photo;
	private View mMenuView;
	private View trans_view;
	private Context mContext;

	public SelectPicPopupWindow(Activity context, OnClickListener itemsOnClick) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.control_pic_popupwindow, null);
		btn_take_photo = (TextView) mMenuView.findViewById(R.id.btn_myquestion_first);
		btn_pick_photo = (TextView) mMenuView.findViewById(R.id.btn_myquestion_second);
		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		trans_view = (View) mMenuView.findViewById(R.id.trans_view);
		// 取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});

		// 设置按钮监听
		btn_pick_photo.setOnClickListener(itemsOnClick);
		btn_take_photo.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.dv_alpha_change);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x6a000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}
	@Override
	public void dismiss() {
		// 在pop消失之前，给咱们加的view设置背景渐变出场动画（Android3.0以上的开发环境，这里建议使用属性动画，那样很柔和，视觉效果更棒！）
		trans_view.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.rb_alpha_ont_to_dot5));
		trans_view.setVisibility(View.GONE);
		super.dismiss();

	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		trans_view.setVisibility(View.VISIBLE);
		trans_view.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.rb_alpha_dot7_to_dot9));
		super.showAtLocation(parent, gravity, x, y);
	}

}
