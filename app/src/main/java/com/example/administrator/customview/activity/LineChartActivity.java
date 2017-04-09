package com.example.administrator.customview.activity;

import android.graphics.PointF;
import android.view.View;

import com.example.administrator.customview.R;
import com.example.administrator.customview.custom.LineChartView;

import java.util.ArrayList;
import java.util.List;

public class LineChartActivity extends BaseActivity
{
    private LineChartView lineChartView;

    @Override
    public View initView()
    {
        View view = View.inflate(this, R.layout.activity_line_chart, null);

        lineChartView = (LineChartView) view.findViewById(R.id.lineChartView);

        return view;
    }

    @Override
    public void initData()
    {
        List<PointF> data = new ArrayList<>();
        PointF pointF1 = new PointF(10, 67);
        PointF pointF2 = new PointF(20, 80);
        PointF pointF3 = new PointF(30, 75);
        PointF pointF4 = new PointF(37, 97);
        PointF pointF5 = new PointF(42, 50);
        PointF pointF6 = new PointF(55, 110);
        PointF pointF7 = new PointF(60, 43);
        PointF pointF8 = new PointF(70, 20);
        PointF pointF9 = new PointF(80, 36);

        data.add(pointF1);
        data.add(pointF2);
        data.add(pointF3);
        data.add(pointF4);
        data.add(pointF5);
        data.add(pointF6);
        data.add(pointF7);
        data.add(pointF8);
        data.add(pointF9);

        lineChartView.setData(data);
    }
}
