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
	 * ���ȼ��
	 */
	private VelocityTracker mVelocityTracker;
	/**
	 * ��ǰҳ���
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
	 * ���ڷ��ص�ǰҳ�ţ�����Ҫ�ƶ�����ҳ��
	 */
	private OnScreenChanageCallBack occb;

	public ScrollLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScroller = new Scroller(context);
		// Ĭ����ʾ��һ��
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
				// ������ 0 - 320 | 320 - 640 | 640 - 960 ...��������Ļ��320��
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
		// x���� y����
		// �ƶ����ڼ���
		scrollTo(mCurScreen * width, 0);
	}

	/**
	 * According to the position of current layout scroll to the destination
	 * page. �жϻ�����λ�� ������ڵ�ǰ���м��λ�� ���� ���� ��Ȼ������� getScrollX x�����ƫ����
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
			// ��ʼ����
			// x��y��x�����ƶ�����y�����ƶ�������������ʱ�䣬���������
			int speed=Math.abs(delta)*2;//����ҳ��ľ�����ȷ������ʱ��
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

	// ֻ�е�ǰLAYOUT�е�ĳ��CHILD����SCROLL�����������Ż���ʹ�Լ���COMPUTESCROLL������
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		Log.e(TAG, "computeScroll");
		// �������true����ʾ������û�н���
		// ��Ϊǰ��startScroll������ֻ����startScroll���ʱ �Ż�Ϊfalse
		if (mScroller.computeScrollOffset()) {
			Log.e(TAG, mScroller.getCurrX() + "======" + mScroller.getCurrY());
			// �����˶���Ч�� ÿ�ι���һ��
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onTouchEvent start");
		/*�ٶȼ��*/
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN://����
			Log.e(TAG, "event down! " + mScroller.isFinished());
			// �����Ļ�Ĺ�������û�н��� ��Ͱ����� �ͽ�������
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE://�ƶ�
			int deltaX = (int) (mLastMotionX - x);
			//�����ǰΪ��0ҳ�����һ���deltaX<0�������һ�
			if(mCurScreen==0&&deltaX<0){
				break;
			}
			//�����ǰΪ��ĩҳ������
			else if(mCurScreen==(getChildCount() - 1)&&deltaX>0){
				break;
			}
			Log.e(TAG, "event move!");
			// ȡ��ƫ����
			Log.e(TAG, "detaX: " + deltaX);
			mLastMotionX = x;
			// x�����ƫ���� y�����ƫ����
			scrollBy(deltaX, 0);
			break;
		case MotionEvent.ACTION_UP://�ſ�
			Log.e(TAG, "event : up");
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();
			Log.e(TAG, "velocityX:" + velocityX);
			//������һ����ٶȴ���Ĭ���ٶȣ��Ҵ���0ҳ
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				// Fling enough to move left
				Log.e(TAG, "snap left");
				if (occb != null) {
					//��������һҳ
					occb.glideNext(mCurScreen - 1);
				}
			}
			//������󻬶��ٶȴ���Ĭ���ٶȣ���С��ĩҳ��������һҳ
			else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 1) {
				// Fling enough to move right
				Log.e(TAG, "snap right");
				if (occb != null) {
					occb.glideNext(mCurScreen + 1);
				}
			} else {
				//������������1/2������л�ҳ��
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

	// ����о�ûʲô���� ����true����false ���ǻ�ִ��onTouchEvent�� ��Ϊ��view����onTouchEvent����false��
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
