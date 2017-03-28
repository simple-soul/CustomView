package com.example.administrator.customview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by simple_soul on 2017/3/23.
 */

public abstract class BaseActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(initView());
        initData();
    }

    public abstract View initView();

    public abstract void initData();
}
