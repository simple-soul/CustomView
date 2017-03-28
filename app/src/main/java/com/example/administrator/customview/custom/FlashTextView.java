package com.example.administrator.customview.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/3/20.
 */

public class FlashTextView extends android.support.v7.widget.AppCompatTextView
{
    private int viewWidth;
    private TextPaint paint;
    private LinearGradient mLinearGradient;
    private Matrix matrix;
    private int mTranslate;

    public FlashTextView(Context context)
    {
        super(context);
    }

    public FlashTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FlashTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        if(viewWidth == 0)
        {
            viewWidth = getMeasuredWidth();
            if(viewWidth>0)
            {
                paint = getPaint();
                mLinearGradient = new LinearGradient(0, 0, viewWidth, 0, new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED}, null, Shader.TileMode.CLAMP);
                paint.setShader(mLinearGradient);
                matrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(matrix != null)
        {
            mTranslate += viewWidth/5;
            if(mTranslate>2*viewWidth)
            {
                mTranslate=-viewWidth;
            }
            matrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(matrix);
            postInvalidateDelayed(100);
        }
    }
}
