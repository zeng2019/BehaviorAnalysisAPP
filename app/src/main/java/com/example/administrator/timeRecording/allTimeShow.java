package com.example.administrator.timeRecording;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class allTimeShow extends Activity {
    private MapShowActivity.timeShowSyncTask tsSyncTask = null;
    private List<Map<String,Object>> timeList = new ArrayList<>();

    long totalTimeLib = 0;
    long totalTimeGongKeJiaoXuelou = 0;
    long totalTimeJiaYuan = 0;
    long diff = 0;
    long nd = 1000 * 24 * 60 * 60;
    long nh = 1000 * 60 * 60;
    long nm = 1000 * 60;
    // 计算差多少天
    long day ;
    // 计算差多少小时
    long hour;
    //totalTimeLib 计算差多少分钟
    long min;

    //作图相关代码
    private LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_time_show);
        mLineChart = findViewById(R.id.allTimeLineChart);

        XAxis xAxis = mLineChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
      //  mLineChart.setDescription("时间");
        ArrayList<String> xValues = new ArrayList<>();
        for(int i=1; i<8; i++) {
            xValues.add("12/"+i);
        }

        //模拟一组y轴数据(存放y轴数据的是一个Entry的ArrayList) 他是构建LineDataSet的参数之一
        ArrayList<Entry> yValue = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            yValue.add(new Entry(i, i));
        }

        //构建一个LineDataSet 代表一组Y轴数据 （比如不同的彩票： 七星彩  双色球）
        LineDataSet dataSet = new LineDataSet(yValue, "双色球");

        dataSet.setColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        mLineChart.setData(lineData);
        mLineChart.invalidate();

        //开启异步任务，准备数据

    }


/*    //开启异步任务，检索数据库，分析并展示时间数据
    allTimeStatistic(timeList);
    //将时间转化为小时为单位
    totalTimeLib= totalTimeLib / nh;
    totalTimeJiaYuan = totalTimeJiaYuan / nh;
    totalTimeGongKeJiaoXuelou = totalTimeGongKeJiaoXuelou / nh;*/

}