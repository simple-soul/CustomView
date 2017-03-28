package com.example.administrator.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.customview.activity.BezierActivity;
import com.example.administrator.customview.activity.LineChartActivity;
import com.example.administrator.customview.activity.TimeTableActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView image;
    private Button btn1, btn2, btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData()
    {

    }

    private void initView()
    {
        image = (ImageView) findViewById(R.id.image);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = null;
        switch (v.getId())
        {
            case R.id.btn1:
                intent = new Intent(this, TimeTableActivity.class);
                startActivity(intent);
                break;

            case R.id.btn2:
                intent = new Intent(this, LineChartActivity.class);
                startActivity(intent);
                break;

            case R.id.btn3:
                intent = new Intent(this, BezierActivity.class);
                startActivity(intent);
                break;
        }
    }
}
