package com.romide.main.ide.view;

import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {

	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;

	private int mScreenWidth;
	private int mMenuRightPadding = 50; // dp

	private int mMenuWidth;

	private boolean once = false;

	
	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);

		mScreenWidth = outMetrics.widthPixels;

		mMenuRightPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
						.getDisplayMetrics());

	}

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (!once) {
			mWapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWapper.getChildAt(0);
			mContent = (ViewGroup) mWapper.getChildAt(1);

			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPadding;
			mContent.getLayoutParams().width = mScreenWidth;

			once = true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		if (changed) {
			close();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			int x = getScrollX(); // 隐藏部分宽度

			// 滑动的太小，隐藏
			if (x >= mMenuWidth / 2) {
				close();
			}
			// 滑动大了，显示
			else {
				open();
			}

			return true;
		}

		return super.onTouchEvent(ev);
	}
	
	
	public void open(){
		this.smoothScrollTo(0, 0);
	}
	
	public void close(){
		this.smoothScrollTo(mMenuWidth, 0);
	}
	
	
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		
		float scale = l * 1.0f / mMenuWidth;
		float contentScale = 0.7f + 0.3f * scale;
		float menuScale = 1.0f - scale * 0.3f;
		float menuAlpha = 0.6f + 0.4f * (1-scale);
		
		
		ViewHelper.setTranslationX(mMenu, mMenuWidth*scale*0.7f);
		
		
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight()/2);
		ViewHelper.setScaleX(mContent, contentScale);
		ViewHelper.setScaleY(mContent, contentScale);
		
		
		ViewHelper.setScaleX(mMenu, menuScale);
		ViewHelper.setScaleY(mMenu, menuScale);
		ViewHelper.setAlpha(mMenu, menuAlpha);
	}
	
	

}





















