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
    private  List<PointF> pointFList;
    private int yData;
    private int xData;
    private Paint textPaint;
    private int TEXT_SIZE;
    private Paint linePaint;
    private Paint pointPaint;
    private Paint baseLinePaint;

    public LineChartView(Context context)
    {
        super(context);
    }

    public LineChartView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LineChartView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.LineChartView_setX:
                    x = a.getInt(attr, 100);
                    Log.i("main", "a.getInt(attr, 100)"+a.getInt(attr, 100));
                    break;
                case R.styleable.LineChartView_setY:
                    // 默认颜色设置为黑色
                    y = a.getInt(attr, 80);
                    break;
            }

        }
        a.recycle();

        linePaint = new Paint();
        //抗锯齿
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.argb(200, 102, 0, 255));

        pointPaint = new Paint();
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(Color.argb(200, 255, 0, 0));


        baseLinePaint = new Paint();
        baseLinePaint.setColor(Color.argb(200, 0, 0, 0));
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        winWidth = measureWidth(widthMeasureSpec);
        winHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(winWidth, winHeight);

        widthSize = winWidth/12;
        heightSize = winHeight/10;
        Log.i("main", "winWidth:"+winWidth+", winHeight:"+winHeight);

        TEXT_SIZE = winWidth/36;

        xData = x/10;
        yData = y/8;
        Log.i("main", "x="+x+", y="+y);

        textPaint = new Paint();
        textPaint.setColor(Color.argb(200, 95, 87, 84));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(TEXT_SIZE);
    }



    private int measureWidth(int measureSpec)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.EXACTLY)
        {
            result = specSize;
        }
        else
        {
            result = 300;
            if(specMode == MeasureSpec.AT_MOST)
            {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(MeasureSpec.EXACTLY == specMode)
        {
            result = specSize;
        }
        else
        {
            result = 200;
            if(specMode == MeasureSpec.AT_MOST)
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
        canvas.drawLine(widthSize, heightSize*9, widthSize*11+widthSize/2, heightSize*9, baseLinePaint);
        canvas.drawLine(widthSize, heightSize*9, widthSize, heightSize/2, baseLinePaint);
        //画箭头
        Path pathX = new Path();
        pathX.moveTo(widthSize*11+widthSize/2, heightSize*9);// 此点为多边形的起点
        pathX.lineTo(widthSize*11+widthSize/2-TEXT_SIZE, heightSize*9-TEXT_SIZE/3);
        pathX.lineTo(widthSize*11+widthSize/2-TEXT_SIZE, heightSize*9+TEXT_SIZE/3);
        pathX.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathX, baseLinePaint);

        Path pathY = new Path();
        pathY.moveTo(widthSize, heightSize/2);// 此点为多边形的起点
        pathY.lineTo(widthSize-TEXT_SIZE/3, heightSize/2+TEXT_SIZE);
        pathY.lineTo(widthSize+TEXT_SIZE/3, heightSize/2+TEXT_SIZE);
        pathY.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(pathY, baseLinePaint);
        //标注分段值
        //x轴
        for (int i = 2; i < 12; i++)
        {
            canvas.drawText(xData *(i-1)+"", widthSize*i, heightSize*9+heightSize/2, textPaint);
            canvas.drawLine(widthSize*i, heightSize*9, widthSize*i, heightSize*9-TEXT_SIZE/3, baseLinePaint);
        }
        //y轴
        for (int i = 2; i < 10; i++)
        {
            canvas.drawText(yData *(i-1)+"", widthSize/2, heightSize*(10-i), textPaint);
            canvas.drawLine(widthSize, heightSize*(10-i), widthSize+TEXT_SIZE/3, heightSize*(10-i), baseLinePaint);
        }
        //原点
        canvas.drawText("0", widthSize/2, heightSize*9+heightSize/2, textPaint);

        //画点与折线
        if(data != null)
        {
            pointFList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++)
            {
                PointF pointF = data.get(i);
                float xPer = pointF.x/x;
                float yPer = pointF.y/y;
                int xPos = (int)(xPer*10*widthSize+widthSize);
                int yPos = (int)((1-yPer)*8*heightSize+heightSize);
                pointFList.add(new PointF(xPos, yPos));
            }
            Gson gson = new Gson();
            Log.i("main", "pointFList="+gson.toJson(pointFList));
            Log.i("main", "data="+gson.toJson(data));
            for (int i = 0; i < pointFList.size(); i++)
            {
                PointF pointF = pointFList.get(i);
                if(i+1 < pointFList.size())
                {
                    PointF pointF2 = pointFList.get(i+1);
                    canvas.drawLine(pointF.x, pointF.y, pointF2.x, pointF2.y, linePaint);
                }
                canvas.drawCircle(pointF.x, pointF.y, TEXT_SIZE/6, pointPaint);
                canvas.drawText("("+data.get(i).x+", "+data.get(i).y+")", pointF.x, pointF.y-20, textPaint);
            }
        }

    }

    public void setData(List<PointF> data)
    {
        this.data = data;
        invalidate();
    }

    public void setData(int x, int y, List<PointF> data)
    {
        this.setData(data);
        this.x = x;
        this.y = y;
        invalidate();
    }
}
