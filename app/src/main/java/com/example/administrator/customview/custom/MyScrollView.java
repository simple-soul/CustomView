package com.example.administrator.customview.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by simple_soul on 2017/3/27.
 */

public class MyScrollView extends ViewGroup
{
    private Scroller mScroller;
    private int winWidth;
    private int winHeight;
    private int mLastY;
    private VelocityTracker tracker;
    private int mMaxVelocity;
    private int yVelocity;
    private int mMinVelocity;

    public MyScrollView(Context context)
    {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mScroller = new Scroller(context);

        //获取屏幕宽高
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        winWidth = dm.widthPixels;
        winHeight = dm.heightPixels;
        Log.i("main", "winHeight"+winHeight);

        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    //支持margin
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量子控件
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //拿到父布局给的宽高和模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //计算出来的宽高保存在这里面
        int width = 0;
        int height = 0;

        Log.i("main", "ChildCount=" + getChildCount());
        for (int i = 0; i < getChildCount(); i++)
        {
            //拿到每一个子view和它们的参数
            View childView = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            Log.i("main", "childWidth=" + childWidth + ", childHeight=" + childHeight);
            //宽为最大的子view的宽加margin
            width = Math.max(childWidth + params.leftMargin + params.rightMargin, width);
            //高为子view高之和
            height += childHeight + params.topMargin + params.bottomMargin;
        }
        //如果不是EXACTLY也就是没有给固定值，也就是wrap_content,就用我们计算出来的值
        if (widthMode != MeasureSpec.EXACTLY)
        {
            widthSize = width;
        }
        if (heightMode != MeasureSpec.EXACTLY)
        {
            heightSize = height;
        }
        //保存
        setMeasuredDimension(widthSize, heightSize);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int left = 0, top = 0, right = 0, bottom = 0;

        for (int i = 0; i < getChildCount(); i++)
        {
            //拿到每一个子view和它们的参数
            View childView = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            left = params.leftMargin;
            top = bottom + params.topMargin;
            right = left + childWidth + params.rightMargin;
            bottom = top + childHeight + params.bottomMargin;

            //保存
            childView.layout(left, top, right, bottom);
        }
        Log.i("main", "getHeight()-winHeight=" + (getHeight() - winHeight));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int y = (int) event.getY();

        acquireVelocityTracker(event);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished())
                {
                    mScroller.abortAnimation();
                }
                Log.i("main", "getScrollY()=" + getScrollY());
                int dy = mLastY - y;
                if (getScrollY() < -200 && dy < 0)
                {
                    dy = 0;
                }
                if (getScrollY() > getHeight() - winHeight + 200 && dy > 0)
                {
                    dy = 0;
                }
                scrollBy(0, dy);
                mLastY = y;
                break;

            case MotionEvent.ACTION_UP:

                tracker.computeCurrentVelocity(300, mMaxVelocity);
                yVelocity = (int) tracker.getYVelocity();

                if (Math.abs(yVelocity) > mMinVelocity)
                {
                    mScroller.fling(0, getScrollY(), 0, -yVelocity, 0, 0, 0,
                            getHeight() - winHeight);
                }

                //回弹动画
                if (getScrollY() < 0)
                {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                }
                if (getScrollY() > getHeight() - winHeight)
                {
                    mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() - getHeight() + winHeight));
                }
                releaseVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;
        }
        invalidate();
        return true;
    }

    //向VelocityTracker添加MotionEvent
    private void acquireVelocityTracker(final MotionEvent event) {
        if(null == tracker) {
            tracker = VelocityTracker.obtain();
        }
        tracker.addMovement(event);
    }

     //释放VelocityTracker
    private void releaseVelocityTracker() {
        if(null != tracker) {
            tracker.clear();
            tracker.recycle();
            tracker = null;
        }
    }

    @Override
    public void computeScroll()
    {
        super.computeScroll();
        if (mScroller.computeScrollOffset())
        {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }
}
