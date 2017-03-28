package com.example.administrator.customview.activity;

import android.content.pm.ActivityInfo;
import android.view.View;

import com.example.administrator.customview.R;

/**
 * Created by simple_soul on 2017/3/24.
 */

public class BezierActivity extends BaseActivity
{
    @Override
    public View initView()
    {
        View view = View.inflate(this, R.layout.activity_bezier, null);

        return view;
    }

    @Override
    public void initData()
    {

    }

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
}
