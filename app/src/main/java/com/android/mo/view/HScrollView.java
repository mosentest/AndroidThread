package com.android.mo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/6/3.
 */
public class HScrollView extends ViewGroup {

    private static final String TAG = HScrollView.class.getSimpleName();

    private Scroller mScroller;
    private int mLastX;
    private int mLastY;
    private int mLastInterceptX;
    private int mLastInterceptY;

    private VelocityTracker velocityTracker;

    public HScrollView(Context context) {
        super(context);
        init(context);
    }

    public HScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context) {
        mScroller = new Scroller(getContext());
        velocityTracker = VelocityTracker.obtain();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        Log.e(TAG, "onInterceptTouchEvent");
        int action = ev.getAction();
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isIntercept = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastInterceptX = (int) ev.getX();
                mLastInterceptY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltX = mLastInterceptX - x;
                int deltY = mLastInterceptY - y;
                if (Math.abs(deltY) > Math.abs(deltX)) {
                    isIntercept = false;
                } else {
                    isIntercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                isIntercept = false;
                break;
            default:
                break;
        }
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        Log.e(TAG, "x:" + x);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastX = (int) event.getX();
                Log.e(TAG, "mLastMotionX:" + mLastX);
                break;
            case MotionEvent.ACTION_MOVE:
                int delt = mLastX - x;
                if (isCanMove(delt)) {
                    mLastX = x;
                    mScroller.startScroll(mScroller.getFinalX(), 0, delt, 0);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    private boolean isCanMove(int delat) {
        /*if(getScrollX()<0 && delat<0){
            return false;
        }*/
        if (getScrollX() >= (getChildCount() - 1) * getWidth() && delat > 0) {
            return false;
        }
        return true;
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(
                getWidthSize(300, widthMeasureSpec),
                getHeightSize(300, heightMeasureSpec));
    }

    public int getWidthSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST://WRAP_CONTENT
                result = Math.min(size, specSize);
                break;
            case MeasureSpec.UNSPECIFIED://MATCH_PARENT
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    public int getHeightSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST://WRAP_CONTENT
                result = Math.min(size, specSize);
                break;
            case MeasureSpec.UNSPECIFIED://MATCH_PARENT
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int cCount = getChildCount();//子view
        int cWidth = 0;
        int cHeight = 0;
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            childView.layout(left, cHeight * i, cWidth, cHeight * (i + 1));
        }
    }
}
