package com.example.administrator.customview.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.administrator.customview.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simple_soul on 2017/3/22.
 */

public class LineChartView extends View
{
    private int x;
    private int y;
    private int winWidth;
    private int winHeight;
    private int widthSize;
    private int heightSize;
    private List<PointF> data;
    private List<PointF> pointFList;
    private int yData;
    private int xData;
    private Paint textPaint;
    private int TEXT_SIZE;
    private Paint linePaint;
    private Paint pointPaint;

    private Paint baseLinePaint;
    private int intervalX;
    private int intervalY;
    private String unitX = "";
    private String unitY = "";

    public LineChartView(Context context)
    {
        super(context);
    }

    public LineChartView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        //获取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LineChartView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.LineChartView_setX:
                    x = a.getInt(attr, 100);
                    Log.i("main", "a.getInt(attr, 100)" + a.getInt(attr, 100));
                    break;
                case R.styleable.LineChartView_setY:
                    // 默认颜色设置为黑色
                    y = a.getInt(attr, 80);
                    break;
                case R.styleable.LineChartView_intervalX:
                    intervalX = a.getInt(attr, 10);
                    break;
                case R.styleable.LineChartView_intervalY:
                    intervalY = a.getInt(attr, 8);
                    break;
                case R.styleable.LineChartView_unitX:
                    unitX = a.getString(attr);
                    break;
                case R.styleable.LineChartView_unitY:
                    unitY = a.getString(attr);
                    break;
            }

        }
        a.recycle();

        //初始化画笔
        linePaint = new Paint();
        //抗锯齿
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.argb(200, 102, 0, 255));
        linePaint.setStrokeWidth(2);

        pointPaint = new Paint();
        //实心
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(Color.argb(200, 255, 0, 0));

        baseLinePaint = new Paint();
        baseLinePaint.setColor(Color.argb(200, 0, 0, 0));
        baseLinePaint.setStrokeWidth(2);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算出view的宽高并保存
        winWidth = measureWidth(widthMeasureSpec);
        winHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(winWidth, winHeight);

        //把宽分成intervalX份，高分成intervalY份，上下左右各留一份空白，其余是表格
        widthSize = winWidth / (intervalX + 2);
        heightSize = winHeight / (intervalY + 2);
        Log.i("main", "winWidth:" + winWidth + ", winHeight:" + winHeight);

        //字体为宽度的36分之一，自我感觉正好
        TEXT_SIZE = winWidth / 36;

        //一格的数值大小
        xData = x / intervalX;
        yData = y / intervalY;
        Log.i("main", "x=" + x + ", y=" + y);

        //初始化笔
        textPaint = new Paint();
        textPaint.setColor(Color.argb(200, 95, 87, 84));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(TEXT_SIZE);
    }

    //计算view的宽度
    private int measureWidth(int measureSpec)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
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

    //计算view的高度
    private int measureHeight(int measureSpec)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (MeasureSpec.EXACTLY == specMode)
        {
            result = specSize;
        }
        else
        {
            result = 200;
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
        //画两条基准线
        canvas.drawLine(widthSize, heightSize * (intervalY + 1), widthSize * (intervalX + 1) +
                widthSize / 2, heightSize * (intervalY + 1), baseLinePaint);
        canvas.drawLine(widthSize, heightSize * (intervalY + 1), widthSize, heightSize / 2,
                baseLinePaint);
        //画箭头
        Path pathX = new Path();
        pathX.moveTo(widthSize * (intervalX + 1) + widthSize / 2, heightSize * (intervalY + 1));
        // 此点为多边形的起点
        pathX.lineTo(widthSize * (intervalX + 1) + widthSize / 2 - TEXT_SIZE, heightSize *
                (intervalY + 1) - TEXT_SIZE / 3);
        pathX.lineTo(widthSize * (intervalX + 1) + widthSize / 2 - TEXT_SIZE, heightSize *
                (intervalY + 1) + TEXT_SIZE / 3);
        pathX.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathX, baseLinePaint);

        Path pathY = new Path();
        pathY.moveTo(widthSize, heightSize / 2);// 此点为多边形的起点
        pathY.lineTo(widthSize - TEXT_SIZE / 3, heightSize / 2 + TEXT_SIZE);
        pathY.lineTo(widthSize + TEXT_SIZE / 3, heightSize / 2 + TEXT_SIZE);
        pathY.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathY, baseLinePaint);
        //标注分段值
        //x轴
        for (int i = 2; i < intervalX + 2; i++)
        {
            canvas.drawText(xData * (i - 1) + "", widthSize * i, heightSize * (intervalY + 1) +
                    heightSize / 2, textPaint);
            //间隔线
            canvas.drawLine(widthSize * i, heightSize * (intervalY + 1), widthSize * i,
                    heightSize * (intervalY + 1) - TEXT_SIZE / 3, baseLinePaint);
        }
        //y轴
        for (int i = 2; i < intervalY + 2; i++)
        {
            canvas.drawText(yData * (i - 1) + "", widthSize / 2, heightSize * (intervalY + 2 - i)
                    , textPaint);
            //间隔线
            canvas.drawLine(widthSize, heightSize * (intervalY + 2 - i), widthSize + TEXT_SIZE /
                    3, heightSize * (intervalY + 2 - i), baseLinePaint);
        }
        //原点
        canvas.drawText("0", widthSize / 2, heightSize * (intervalY + 1) + heightSize / 2,
                textPaint);
        //单位
        canvas.drawText(unitX, widthSize * (intervalX + 1) + widthSize / 2, heightSize *
                (intervalY + 1) + heightSize / 2, textPaint);
        canvas.drawText(unitY, widthSize / 2, heightSize / 2, textPaint);

        //画点与折线
        if (data != null)
        {
            pointFList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++)
            {
                PointF pointF = data.get(i);
                float xPer = pointF.x / x;
                float yPer = pointF.y / y;
                int xPos = (int) (xPer * intervalX * widthSize + widthSize);
                int yPos = (int) ((1 - yPer) * intervalY * heightSize + heightSize);
                pointFList.add(new PointF(xPos, yPos));
            }
            Gson gson = new Gson();
            Log.i("main", "pointFList=" + gson.toJson(pointFList));
            Log.i("main", "data=" + gson.toJson(data));
            for (int i = 0; i < pointFList.size(); i++)
            {
                PointF pointF = pointFList.get(i);
                if (i + 1 < pointFList.size())
                {
                    PointF pointF2 = pointFList.get(i + 1);
                    canvas.drawLine(pointF.x, pointF.y, pointF2.x, pointF2.y, linePaint);
                }
                canvas.drawCircle(pointF.x, pointF.y, TEXT_SIZE / 4, pointPaint);
                canvas.drawText("(" + data.get(i).x + ", " + data.get(i).y + ")", pointF.x,
                        pointF.y - 20, textPaint);
            }
        }
    }

    public void setData(List<PointF> data)
    {
        this.data = data;
        invalidate();
    }

    public void setXY(int x, int y)
    {
        this.x = x;
        this.y = y;
        invalidate();
    }

    public void setIntervalXY(int intervalX, int intervalY)
    {
        this.intervalX = intervalX;
        this.intervalY = intervalY;
        invalidate();
    }

    public void setUnitX(String unitX, String unitY)
    {
        this.unitX = unitX;
        this.unitY = unitY;
        invalidate();
    }

}
