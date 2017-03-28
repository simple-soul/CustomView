package com.example.administrator.customview.activity;

import android.util.Log;
import android.view.View;

import com.example.administrator.customview.R;
import com.example.administrator.customview.custom.TimeTableView;
import com.example.administrator.customview.domain.JsonTimeBean;
import com.example.administrator.customview.domain.TimeBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeTableActivity extends BaseActivity
{
    private TimeTableView timeTable;

    private List<TimeBean> times;

    public View initView()
    {
        View view = View.inflate(this, R.layout.activity_time_table, null);
        timeTable = (TimeTableView) view.findViewById(R.id.timetable);
        return view;
    }

    public void initData()
    {
        times = new ArrayList<>();
        TimeBean bean1 = new TimeBean(TimeBean.TimeType.CLASS, 2, 3, 5, "hqkvibgugircs");
        TimeBean bean2 = new TimeBean(TimeBean.TimeType.ACTIVITY, 5, 10, 11, "hqkvibgugircs");
        TimeBean bean3 = new TimeBean(TimeBean.TimeType.SLEEP, 7, 7, 8, "hqkvibgugircs");
        TimeBean bean4 = new TimeBean(TimeBean.TimeType.COMMUNITY, 1, 1, 3, "hqkvibgugircs");
        TimeBean bean5 = new TimeBean(TimeBean.TimeType.OTHER, 1, 4, 5, "hqkvibgugircs");
        times.add(bean1);
        times.add(bean2);
        times.add(bean3);
        times.add(bean4);
        times.add(bean5);

        timeTable.setData(times);

        //存数据
        Gson gson = new Gson();
        Map<String, List<TimeBean>> map = new HashMap<>();
        map.put("timeList", times);
        String json = gson.toJson(map);
        Log.i("main", json);

        //取数据
        JsonTimeBean.ListTimeBean bean = gson.fromJson(json, JsonTimeBean.ListTimeBean.class);
        Log.i("main", bean.timeList.get(1).getRemark()+" , "+bean.timeList.get(1).getType());
    }



}
