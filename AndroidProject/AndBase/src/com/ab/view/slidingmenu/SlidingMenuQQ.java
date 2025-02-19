package com.ab.view.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import com.ab.R;
import com.ab.util.AbAppUtil;
import com.nineoldandroids.view.ViewHelper;

public class SlidingMenuQQ extends HorizontalScrollView {
	public static boolean isOpen;
	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;
	/**
	 * dp
	 */
	private int mMenuRightPadding;
	/**
	 * 菜单的宽度
	 */
	private int mMenuWidth;
	private int mHalfMenuWidth;
	private boolean once;

	private float up = 0;
	private float down = 0;

	private ViewGroup mMenu;
	private ViewGroup mContent;

	public SlidingMenuQQ(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingMenuQQ(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = AbAppUtil.getDisplayMetrics(context).widthPixels;

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			if (attr == R.styleable.SlidingMenu_rightPadding) {
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));// 默认为10DP
			}
		}
		a.recycle();
	}

	public SlidingMenuQQ(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 显示的设置一个宽度
		 */
		if (!once) {
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) wrapper.getChildAt(0);
			mContent = (ViewGroup) wrapper.getChildAt(1);

			mMenuWidth = mScreenWidth - mMenuRightPadding;
			mHalfMenuWidth = mMenuWidth / 2;
			mMenu.getLayoutParams().width = mMenuWidth;
			mContent.getLayoutParams().width = mScreenWidth;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// 将菜单隐藏
			this.scrollTo(mMenuWidth, 0);
			once = true;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		int action = ev.getAction();
//		switch (action) {
//		// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
//		case MotionEvent.ACTION_UP:
//			int scrollX = getScrollX();
//			Log.v("ss", "mHalfMenuWidth"+ mHalfMenuWidth +"scrollX"+scrollX + "");
//			if (scrollX > mHalfMenuWidth - mHalfMenuWidth / 4) {
//				this.smoothScrollTo(mMenuWidth, 0);
//				isOpen = false;
//			} else {
//				this.smoothScrollTo(0, 0);
//				isOpen = true;
//			}
//			return true;
//		}
//		return super.onTouchEvent(ev);
		
//		int action = ev.getAction();
		if (action == MotionEvent.ACTION_MOVE) {
			if (down == 0) {
				down = ev.getX();
			}
		} else if (action == MotionEvent.ACTION_UP) {
			up = ev.getX();
			int move = (int) (down - up);
			if (move > 80) {
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			} else if (move < -80) {
				this.smoothScrollTo(0, 0);
				isOpen = true;
			} else {
//				
//				if (isOpen) {
//					if (up > mMenuWidth) {
//						this.smoothScrollTo(mMenuWidth, 0);
//					}
//				} else {
					this.smoothScrollTo(mMenuWidth, 0);
					isOpen = false;
//				}
			}
			up = 0;
			down = 0;
			return true;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 打开菜单
	 */
	public void openMenu() {
		if (isOpen)
			return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	/**
	 * 关闭菜单
	 */
	public void closeMenu() {
		if (isOpen) {
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
		}
	}

	/**
	 * 切换菜单状态
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / mMenuWidth;
		float leftScale = 1 - 0.3f * scale;
		float rightScale = 0.8f + scale * 0.2f;

		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);

		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);

	}

}
