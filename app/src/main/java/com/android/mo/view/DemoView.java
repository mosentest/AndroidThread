package com.android.mo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.nineoldandroids.view.ViewHelper;

import java.util.Random;

/**
 * Created by Administrator on 2016/6/2.
 */
public class DemoView extends View {

    private final static String TAG = DemoView.class.getSimpleName();

    private Context mContext;

    private Paint mPaint;
    private Random mRandom;
    private Handler mHandler;

    private int mLastX;
    private int mLastY;

    private Scroller mScroller;


    public DemoView(Context context) {
        super(context);
        init(context);
    }

    public DemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context) {
        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.YELLOW);
        mRandom = new Random();
        mHandler = new Handler(Looper.getMainLooper());
        mScroller = new Scroller(mContext);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        Log.e(TAG, "x:" + x + ",y:" + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "ACTION_UP");
                break;
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "ACTION_MOVE");
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                int translationX = (int) ViewHelper.getTranslationX(this) + deltaX;
                int translationY = (int) ViewHelper.getTranslationY(this) + deltaY;
                ViewHelper.setTranslationX(this, translationX);
                ViewHelper.setTranslationY(this, translationY);
                //smoothScrollTo(-deltaX, y);
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    public void smoothScrollTo(int destX, int destY) {
        mScroller.startScroll(getScrollX(), 0, destX, 0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                getWidthSize(100, widthMeasureSpec),
                getHeightSize(100, heightMeasureSpec));
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
    protected void onDraw(final Canvas canvas) {
        Log.e(TAG, "onDraw");
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        int radius = Math.min(width, height) / 2;
        canvas.drawCircle(paddingLeft + width / 2, paddingTop + height / 2, radius, mPaint);
        
        canvas.drawLine(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), 0, mPaint);


//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                scrollTo(0,60);
//                invalidate();
//            }
//        }, 1000);
    }
}
