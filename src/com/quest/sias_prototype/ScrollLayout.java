package com.quest.sias_prototype;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.quest.sias_prototype.ScrollLayout.OnScreenChanageCallBack;

public class ScrollLayout extends ViewGroup{

	private static final String TAG = "ScrollLayout";
	private Scroller mScroller;
	/**
	 * 数度检测
	 */
	private VelocityTracker mVelocityTracker;
	/**
	 * 当前页面号
	 */
	private int mCurScreen;
	private int mDefaultScreen = 0;
	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	private static final int SNAP_VELOCITY = 600;
	private int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop;
	private float mLastMotionX;
	/**
	 * 用于返回当前页号，和需要移动到的页号
	 */
	private OnScreenChanageCallBack occb;

	public ScrollLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScroller = new Scroller(context);
		// 默认显示第一屏
		mCurScreen = mDefaultScreen;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.e(TAG, "onLayout" + "--changed:" + changed+"----");
		// if (changed) {
		int childLeft = 0;
		final int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				final int childWidth = childView.getMeasuredWidth();
				childView.layout(childLeft, 0, childLeft + childWidth,
						childView.getMeasuredHeight());
				childLeft += childWidth;
				// 很明显 0 - 320 | 320 - 640 | 640 - 960 ...（假设屏幕宽320）
			}
		}
		// }
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.e(TAG, "onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		// final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		// if (widthMode != MeasureSpec.EXACTLY) {
		// throw new IllegalStateException(
		// "ScrollLayout only canmCurScreen run at EXACTLY mode!");
		// }
		//
		// final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		// if (heightMode != MeasureSpec.EXACTLY) {
		// throw new IllegalStateException(
		// "ScrollLayout only can run at EXACTLY mode!");
		// }

		// The children are given the same width and height as the scrollLayout
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		// Log.e(TAG, "moving to screen "+mCurScreen);
		// x坐标 y坐标
		// 移动到第几屏
		scrollTo(mCurScreen * width, 0);
	}

	/**
	 * According to the position of current layout scroll to the destination
	 * page. 判断滑动的位置 如果大于当前屏中间的位置 则换屏 否则 仍然是这个屏 getScrollX x方向的偏移量
	 */

	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		if (occb != null) {
			occb.glideNext(destScreen);
		}
	}

	public void snapToScreen(int whichScreen) {
		// get the valid layout page
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if (getScrollX() != (whichScreen * getWidth())) {

			final int delta = whichScreen * getWidth() - getScrollX();
			Log.e(TAG, "glibeSpeed:---------------------"+Math.abs(delta) * 2);
			// 开始滚动
			// x，y，x方向移动量，y方向移动量，滚动持续时间，负数往左滚
			int speed=Math.abs(delta)*2;//根据页面的距离来确定动画时间
			if(speed>960){
				speed=350;
			}
			mScroller.startScroll(getScrollX(), 0, delta, 0,
					speed);
			mCurScreen = whichScreen;
			if (occb != null) {
				occb.onScreenChanage(mCurScreen);
			}
			invalidate(); // Redraw the layout
		}
	}

	public void setOnScreenChangeCallBack(OnScreenChanageCallBack occb) {
		this.occb = occb;
	}

	public void setToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		mCurScreen = whichScreen;
		scrollTo(whichScreen * getWidth(), 0);
	}

	public int getCurScreen() {
		return mCurScreen;
	}

	public int getScreenCount() {
		return getChildCount();
	}

	// 只有当前LAYOUT中的某个CHILD导致SCROLL发生滚动，才会致使自己的COMPUTESCROLL被调用
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		Log.e(TAG, "computeScroll");
		// 如果返回true，表示动画还没有结束
		// 因为前面startScroll，所以只有在startScroll完成时 才会为false
		if (mScroller.computeScrollOffset()) {
			Log.e(TAG, mScroller.getCurrX() + "======" + mScroller.getCurrY());
			// 产生了动画效果 每次滚动一点
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onTouchEvent start");
		/*速度检测*/
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN://按下
			Log.e(TAG, "event down! " + mScroller.isFinished());
			// 如果屏幕的滚动动画没有结束 你就按下了 就结束动画
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE://移动
			int deltaX = (int) (mLastMotionX - x);
			//如果当前为第0页则不能右滑，deltaX<0代表向右滑
			if(mCurScreen==0&&deltaX<0){
				break;
			}
			//如果当前为第末页则不能左滑
			else if(mCurScreen==(getChildCount() - 1)&&deltaX>0){
				break;
			}
			Log.e(TAG, "event move!");
			// 取得偏移量
			Log.e(TAG, "detaX: " + deltaX);
			mLastMotionX = x;
			// x方向的偏移量 y方向的偏移量
			scrollBy(deltaX, 0);
			break;
		case MotionEvent.ACTION_UP://放开
			Log.e(TAG, "event : up");
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();
			Log.e(TAG, "velocityX:" + velocityX);
			//如果向右滑动速度大于默认速度，且大于0页
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				// Fling enough to move left
				Log.e(TAG, "snap left");
				if (occb != null) {
					//滑动到上一页
					occb.glideNext(mCurScreen - 1);
				}
			}
			//如果向左滑动速度大于默认速度，且小于末页，滑到下一页
			else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 1) {
				// Fling enough to move right
				Log.e(TAG, "snap right");
				if (occb != null) {
					occb.glideNext(mCurScreen + 1);
				}
			} else {
				//慢慢滑到大于1/2宽度则切换页号
				snapToDestination();
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}

		return true;
	}

	// 这个感觉没什么作用 不管true还是false 都是会执行onTouchEvent的 因为子view里面onTouchEvent返回false了
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);

		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			Log.e(TAG, "onInterceptTouchEvent move");
			final int xDiff = (int) Math.abs(mLastMotionX - x);
			if (xDiff > mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;

			}
			break;

		case MotionEvent.ACTION_DOWN:
			Log.e(TAG, "onInterceptTouchEvent down");
			mLastMotionX = x;
			Log.e(TAG, mScroller.isFinished() + "");
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			Log.e(TAG, "onInterceptTouchEvent up or cancel");
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		Log.e(TAG, mTouchState + "====" + TOUCH_STATE_REST);
		return mTouchState != TOUCH_STATE_REST;
	}

	public void mAddView(View view, int index) {
		addView(view, index);
	}

	public void mAddView(View view) {
		addView(view);
	}

	public void moveToView(int positon) {
		snapToScreen(positon);
	}

	public interface OnScreenChanageCallBack {
		void onScreenChanage(int page);
		void glideNext(int page);
	}
}
