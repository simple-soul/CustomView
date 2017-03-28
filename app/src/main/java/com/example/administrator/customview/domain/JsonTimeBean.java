package com.example.administrator.customview.domain;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */

public class JsonTimeBean
{
    private String type;
    private int week;
    private int from;
    private int to;
    private String remark;

    public JsonTimeBean(String type, int week, int from, int to, String remark)
    {
        this.type = type;
        this.week = week;
        this.from = from;
        this.to = to;
        this.remark = remark;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public int getWeek()
    {
        return week;
    }

    public void setWeek(int week)
    {
        this.week = week;
    }

    public int getFrom()
    {
        return from;
    }

    public void setFrom(int from)
    {
        this.from = from;
    }

    public int getTo()
    {
        return to;
    }

    public void setTo(int to)
    {
        this.to = to;
    }

    public static class ListTimeBean
    {
        public List<JsonTimeBean> timeList;
    }
}
