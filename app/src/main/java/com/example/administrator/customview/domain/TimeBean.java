package com.example.administrator.customview.domain;

import android.graphics.Color;

/**
 * Created by Administrator on 2017/3/20.
 */

public class TimeBean
{
    private TimeType type;
    private int week;
    private int from;
    private int to;
    private String remark;

    public TimeBean(TimeType type, int week, int from, int to, String remark)
    {
        this.type = type;
        this.week = week;
        this.from = from;
        this.to = to;
        this.remark = remark;
    }

    public TimeType getType()
    {
        return type;
    }

    public void setType(TimeType type)
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

    public enum TimeType
    {
        CLASS("上课", Color.argb(127, 255, 0, 0)), ACTIVITY("体育活动", Color.argb(127, 255, 255, 0)),
        COMMUNITY("社团活动", Color.argb(127, 255, 153, 0)), SLEEP("睡觉", Color.argb(127, 0, 255, 0)),
        OTHER("其他", Color.argb(127, 0, 0, 0));

        private String type;
        private int color;

        TimeType(String type, int color)
        {
            this.type = type;
            this.color = color;
        }

        public String getType()
        {
            return type;
        }

        public int getColor()
        {
            return color;
        }
    }
}
