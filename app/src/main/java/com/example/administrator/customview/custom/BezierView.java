package com.example.administrator.customview.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by simple_soul on 2017/3/24.
 */

public class BezierView extends View
{
    private int winWidth;
    private int winHeight;
    private Paint mainLinePaint;
    private Paint GuideLinePaint;
    private int color;
    private int radius;
    private List<PointSet> setList;
    private PointF mainPoint;
    private PointF guidePoint1;
    private PointF guidePoint2;
    private Bitmap mainBitmap;
    private Bitmap bitmap;
    //已存入点数
    private int num = 0;
    private Random random = new Random();

    public BezierView(Context context)
    {
        super(context);
    }

    public BezierView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        winWidth = measureWidth(widthMeasureSpec);
        winHeight = measureHeight(heightMeasureSpec);

        radius = winWidth / 180;

        mainBitmap = Bitmap.createBitmap(winWidth, winHeight, Bitmap.Config.ARGB_8888);
        bitmap = Bitmap.createBitmap(winWidth, winHeight, Bitmap.Config.ARGB_8888);

        setMeasuredDimension(winWidth, winHeight);
    }

    private int measureHeight(int heightMeasureSpec)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY)
        {
            result = specSize;
        }
        else
        {
            result = 300;
            if (specMode == MeasureSpec.AT_MOST)
            {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY)
        {
            result = specSize;
        }
        else
        {
            result = 300;
            if (specMode == MeasureSpec.AT_MOST)
            {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                initPoint(event);
                break;

            case MotionEvent.ACTION_MOVE:
                drawGuideLine(event);
                break;

            case MotionEvent.ACTION_UP:
                //历史点集为空就创建一个
                if (setList == null)
                {
                    setList = new ArrayList<>();
                }
                //存储历史点集
                PointSet pointSet = new PointSet(mainPoint, guidePoint1, guidePoint2);
                setList.add(pointSet);
                num++;
                //存储最终画面
                mainBitmap = bitmap;
                break;
        }
        return true;
    }

    //画出主点，初始化画笔
    private void initPoint(MotionEvent event)
    {
        mainPoint = new PointF(event.getX(), event.getY());
        color = Color.rgb(random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255));
        //初始化主线画笔
        mainLinePaint = new Paint();
        mainLinePaint.setColor(color);
        mainLinePaint.setAntiAlias(true);
        mainLinePaint.setStrokeWidth(10);
        mainLinePaint.setStyle(Paint.Style.FILL);
        //画出主点
        Path mainPath = new Path();
        mainPath.addRect(mainPoint.x - radius,
                mainPoint.y - radius,
                mainPoint.x + radius,
                mainPoint.y + radius,
                Path.Direction.CW);
        Canvas canvas = new Canvas(mainBitmap);
        canvas.drawPath(mainPath, mainLinePaint);
        bitmap = Bitmap.createBitmap(mainBitmap);
        //初始化辅助线画笔
        GuideLinePaint = new Paint();
        GuideLinePaint.setColor(color);
        GuideLinePaint.setAntiAlias(true);
        GuideLinePaint.setStrokeWidth(3);
        GuideLinePaint.setStyle(Paint.Style.FILL);
        invalidate();
    }

    //画辅助线、主线
    private void drawGuideLine(MotionEvent event)
    {
        //从上个最终画面重绘
        Path guidePath = new Path();
        bitmap = Bitmap.createBitmap(mainBitmap);
        Canvas canvas = new Canvas(bitmap);
        //当前手指按下的为第一个辅助点
        guidePoint1 = new PointF(event.getX(), event.getY());
        //第二个辅助点为关于主点的对称点
        guidePoint2 = new PointF(mainPoint.x - (guidePoint1.x - mainPoint.x),
                mainPoint.y - (guidePoint1.y - mainPoint.y));
        //画出辅助点
        guidePath.addCircle(guidePoint1.x, guidePoint1.y, radius, Path.Direction.CW);
        guidePath.addCircle(guidePoint2.x, guidePoint2.y, radius, Path.Direction.CW);
        canvas.drawPath(guidePath, GuideLinePaint);
        //画出辅助线
        canvas.drawLine(guidePoint1.x, guidePoint1.y,guidePoint2.x, guidePoint2.y, GuideLinePaint);
        //画出主线
        if (num != 0)
        {
            Path mainPath = new Path();
            PointSet pointSet = setList.get(num - 1);
            mainPath.moveTo(pointSet.mainPoint.x, pointSet.mainPoint.y);
            mainPath.cubicTo(pointSet.guidePoint1.x,
                    pointSet.guidePoint1.y,
                    guidePoint2.x,
                    guidePoint2.y,
                    mainPoint.x,
                    mainPoint.y);
            mainLinePaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(mainPath, mainLinePaint);
        }
        invalidate();
    }


    public class PointSet
    {
        public PointF mainPoint;
        public PointF guidePoint1;
        public PointF guidePoint2;

        public PointSet(PointF mainPoint, PointF guidePoint1, PointF guidePoint2)
        {
            this.mainPoint = mainPoint;
            this.guidePoint1 = guidePoint1;
            this.guidePoint2 = guidePoint2;
        }
    }
}
