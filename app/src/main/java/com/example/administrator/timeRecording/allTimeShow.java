package com.example.administrator.timeRecording;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

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

        //开启异步任务，准备数据


        mLineChart = (LineChart) findViewById(R.id.allTimeLineChart);
        //在chart上的右下角加描述
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setPinchZoom(true);
//        mLineChart.setDescription(mAllTime.getDescription());
//      //设置Y轴上的单位
//        mLineChart.setUnit("小时");
        //设置透明度
//      mLineChart.setAlpha(0.8f);
        //设置网格底下的那条线的颜色
//      mLineChart.setBorderColor(Color.rgb(213, 216, 214));
//      mLineChart.setBorderColor(Color.rgb(0, 0, 0));
//      mLineChart.setBackgroundColor(Color.rgb(255, 255, 255));
        mLineChart.setGridBackgroundColor(Color.rgb(255, 255, 255));

        //设置Y轴前后倒置
//        mLineChart.setInvertYAxisEnabled(false);
//        //设置高亮显示
//        mLineChart.setHighlightEnabled(true);
        //设置是否可以触摸，如为false，则不能拖动，缩放等
        mLineChart.setTouchEnabled(true);
        //设置是否可以拖拽，缩放
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        //设置是否能扩大扩小
        mLineChart.setPinchZoom(true);
        // 设置背景颜色
//         mLineChart.setBackgroundColor(Color.GRAY);
        //设置点击chart图对应的数据弹出标注
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        // define an offset to change the original position of the marker
        // (optional)
//        mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());
//        mv.setMinimumHeight(80);
//        // set the marker to the chart
//        mLineChart.setMarkerView(mv);
//        // enable/disable highlight indicators (the lines that indicate the
//        // highlighted Entry)
//        mChart.setHighlightIndicatorEnabled(false);
        //设置字体格式，如正楷
        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
//        mLineChart.setDescriptionTypeface(tf);

        LimitLine ll1 = new LimitLine(95f, "警戒值 95%");
        ll1.setLineWidth(2f);
//        ll1.setLineColor(Color.rgb(0,0,0));
//        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        ll1.setTextSize(15f);
        ll1.setTypeface(tf);

        XAxis xl = mLineChart.getXAxis();
//      xl.setAvoidFirstLastClipping(true);
//      xl.setAdjustXLabels(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的数据在底部显示
        xl.setTypeface(tf); // 设置字体
        xl.setTextSize(10f); // 设置字体大小
//        xl.setSpaceBetweenLabels(0); // 设置数据之间的间距'

        YAxis yl = mLineChart.getAxisLeft();
        yl.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//      yl.setAxisMaxValue(220f);
        yl.addLimitLine(ll1);
        yl.setTypeface(tf); // 设置字体
        yl.setTextSize(10f); // s设置字体大小
        yl.setTypeface(tf);
        yl.setAxisMinValue(90f);
        yl.setStartAtZero(false);
//      yl.setLabelCount(5); // 设置Y轴最多显示的数据个数

        YAxis y2 = mLineChart.getAxisRight();
        y2.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y2.setTypeface(tf); // 设置字体
        y2.setTextSize(10f); // s设置字体大小
        y2.setTypeface(tf);
        y2.setAxisMinValue(90f);
        y2.setStartAtZero(false);
        getData();
        new Thread(mRunnable).start();
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(15*1000);//每隔15s刷新一次，可以看到动态图
                    mHandler.sendMessage(mHandler.obtainMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


/*    //开启异步任务，检索数据库，分析并展示时间数据
    allTimeStatistic(timeList);
    //将时间转化为小时为单位
    totalTimeLib= totalTimeLib / nh;
    totalTimeJiaYuan = totalTimeJiaYuan / nh;
    totalTimeGongKeJiaoXuelou = totalTimeGongKeJiaoXuelou / nh;*/

}
