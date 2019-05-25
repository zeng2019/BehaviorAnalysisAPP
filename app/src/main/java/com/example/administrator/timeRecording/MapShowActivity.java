package com.example.administrator.timeRecording;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

import static android.widget.Toast.makeText;
import static com.example.administrator.timeRecording.DBTimeOperator.queryMonthTimeInfo;
import static com.example.administrator.timeRecording.DBTimeOperator.queryTimeInfo;
import static com.example.administrator.timeRecording.DBTimeOperator.queryWeekTimeInfo;
import static com.example.administrator.timeRecording.DBTimeOperator.queryYearTimeInfo;

public class MapShowActivity extends BaseActivity {
    
    private timeShowSyncTask tsSyncTask = null;
    private List<Map<String,Object>> timeList = new ArrayList<>();
    String email;
    private BottomNavigationView bottomNavView;
    private Button weekTime;
    private Button monthTime;
    private Button yearTime;
    private Button allTime;

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
    private int choice; //用于判断哪个按钮被按下

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_show);

        weekTime =(Button)findViewById(R.id.weekTime);
        monthTime = (Button)findViewById(R.id.monthTime);
        yearTime = (Button)findViewById(R.id.yearTime);
        allTime = (Button)findViewById(R.id.allTime);

        mLineChart = (LineChart)findViewById(R.id.timeLineChart);
        //自定义的MarkerView对象
        allTimeShow.MyMarkerView mv = new allTimeShow.MyMarkerView(MapShowActivity.this, R.layout.custom_marker_view);
        mv.setChartView(mLineChart);
        mLineChart.setMarker(mv);



        weekTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开启异步任务，检索数据库，分析时间并展示
                choice = 1;
                showWeekTimeAnalysis(email);
            }
        });

        monthTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开启异步任务，检索数据库，分析并展示
                choice = 2;
                showMonthTimeAnalysis(email);
            }
        });

        yearTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //开启异步任务，检索数据库，分析并展示
                choice = 3;
                showYearTimeAnalysis(email);
            }
        });

        allTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent in = new Intent(MapShowActivity.this, allTimeShow.class);
//                in.putExtra("email",email);
//                startActivity(in);
                choice = 4;
                showAllTimeAnalysis(email);
            }
        });

         bottomNavView = findViewById(R.id.nav_showInMap);
         bottomNavView.setSelectedItemId(R.id.nav_timeChart);
         bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 int id = item.getItemId();

                 if (id == R.id.nav_timeChart) { //时间页面，默认选中，不需要处理

                 } else { //地图显示页面
                     Intent in = new Intent(MapShowActivity.this, ShowinBDMap.class);
                     in.putExtra("email",email);
                     startActivity(in);
                 }
                 return true;
             }
         });

        Intent in = getIntent();
        email = in.getStringExtra("email"); //从Intent中取得登录用户的email
    }

    private void showYearTimeAnalysis(String email) {
        tsSyncTask = new timeShowSyncTask(email);
        tsSyncTask.execute((Void) null);
    }

    private void showMonthTimeAnalysis(String email) {
        tsSyncTask = new timeShowSyncTask(email);
        tsSyncTask.execute((Void) null);
    }

    private void showWeekTimeAnalysis(String email) {

        tsSyncTask = new timeShowSyncTask(email);
        tsSyncTask.execute((Void) null);
    }


    private void showAllTimeAnalysis(String email) {

        tsSyncTask = new timeShowSyncTask(email);
        tsSyncTask.execute((Void) null);

    }

    private  void allTimeStatistic(List<Map<String, Object>> timeList) {
        //设立每个区域的时间统计列表。统计每个区域的时间
        Log.d("时间记录信息：","分析！");
        List<Date> libraryTimeList = new ArrayList<>();
        List<Date> gongKeJiaoXueLouTimeList = new ArrayList<>();
        List<Date> jiaYuanTimeList = new ArrayList<>();

        for(int i = 0; i< this.timeList.size(); i++) {
            if (this.timeList.get(i).get("recNodeSN").toString().equals("0117C5976A3E")) //图书馆
                libraryTimeList.add((Date) this.timeList.get(i).get("recTime"));
            else if (this.timeList.get(i).get("recNodeSN").toString().equals("0117C597055B")) //工科教学楼
                gongKeJiaoXueLouTimeList.add((Date) this.timeList.get(i).get("recTime"));
            else if (this.timeList.get(i).get("recNodeSN").toString().equals("0117C5976771")) { //嘉园宿舍
                jiaYuanTimeList.add((Date) this.timeList.get(i).get("recTime"));
            }
            else
                Log.d("时间记录分析：","失败，存在未知时间记录！");
        }

        for(int i=libraryTimeList.size()-1;i>0;i--) { //图书馆时间
            // 获得两个时间的毫秒时间差异
            diff = libraryTimeList.get(i).getTime() - libraryTimeList.get(i-1).getTime();
            totalTimeLib = totalTimeLib + diff;
//                         Log.d("时间记录分析：","图书馆的总时间（秒）:"+totalTimeLib);

        }

        //统计教学楼时间
        for(int i=gongKeJiaoXueLouTimeList.size()-1;i>0;i--) {
            diff = gongKeJiaoXueLouTimeList.get(i).getTime() - gongKeJiaoXueLouTimeList.get(i-1).getTime();
            totalTimeGongKeJiaoXuelou = totalTimeGongKeJiaoXuelou + diff;
        }

        //统计宿舍时间
        for(int i=jiaYuanTimeList.size()-1;i>0;i--) {
            diff = jiaYuanTimeList.get(i).getTime() - jiaYuanTimeList.get(i-1).getTime();
            totalTimeJiaYuan = totalTimeJiaYuan + diff;
        }
    }


    //异步数据库查询代码
    public class timeShowSyncTask extends AsyncTask<Void, Void, Boolean> {

        String condition;

        public timeShowSyncTask(String cond) {
            super();
            condition = cond;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isDone = false;

            if(choice == 1) {
                timeList = queryWeekTimeInfo(condition);
            } else if (choice == 2) {
                timeList = queryMonthTimeInfo(condition);

            } else if (choice == 3) {
                timeList = queryYearTimeInfo(condition);
            } else {
                timeList = queryTimeInfo(condition);
            }

            isDone = true;

            return isDone;
        }

        @Override
        //线程结束后的ui处理
        protected void onPostExecute(final Boolean isDone) {
            tsSyncTask = null;

            if (isDone) {

                if(timeList.isEmpty()) {
                    Toast.makeText(MapShowActivity.this,"您还没有记录过时间信息！",Toast.LENGTH_SHORT).show();
                } else {
                    //创建描述信息
                    Description mDescription = new Description();
                    mDescription.setText("时间分布");
                    mDescription.setTextColor(Color.RED);
                    mDescription.setTextSize(20);
                    mLineChart.setDescription(mDescription);
                    //没有数据时显示的信息
                    mLineChart.setNoDataText("未能获取时间数据！");
                    mLineChart.setNoDataTextColor(Color.BLUE);

                    mLineChart.setDrawMarkers(true);
                    mLineChart.setDrawGridBackground(false); //绘制chart绘图区后面的背景矩形
                    mLineChart.setDrawBorders(false);//禁止绘制图标边框的先
                    //mLineChart.setBorderColor(); //设置 chart 边框线的颜色。
                    //mLineChart.setBorderWidth(); //设置 chart 边界线的宽度，单位 dp。
                    //mLineChart.setLogEnabled(true);//打印日志
                    //mLineChart.notifyDataSetChanged();//刷新数据

                    //设置x轴标签
                    final ArrayList<String> xValues = new ArrayList<>();
                    xValues.add("图书馆");
                    xValues.add("嘉园");
                    xValues.add("工科教学楼");
                    xValues.add("文科教学楼");
                    xValues.add("菁园");
                    xValues.add("乾园");
                    IAxisValueFormatter formatter = new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            return xValues.get((int)value);
                        }
                    };

                    XAxis xAxis = mLineChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter(formatter);

                    //根据查询结果统计各区域的时间
                    allTimeStatistic(timeList);
                    //将时间转化为小时为单位
                    totalTimeLib= totalTimeLib / nh;
                    totalTimeJiaYuan = totalTimeJiaYuan / nh;
                    totalTimeGongKeJiaoXuelou = totalTimeGongKeJiaoXuelou / nh;

                    //y轴数据(存放y轴数据的是一个Entry的ArrayList) 他是构建LineDataSet的参数之一
                    ArrayList<Entry> yValue = new ArrayList<>();
                    yValue.add(new Entry(0, totalTimeLib));
                    yValue.add(new Entry(1,totalTimeJiaYuan));
                    Random ra = new Random();
                    yValue.add(new Entry(2,ra.nextInt(800)+1));
                    yValue.add(new Entry(3, ra.nextInt(300)+1));
                    yValue.add(new Entry(4,ra.nextInt(100)+1));
                    yValue.add(new Entry(5,ra.nextInt(100)+1));


                    //构建一个LineDataSet 代表一组Y轴数据
                    LineDataSet dataSet = new LineDataSet(yValue, "时间（以小时为单位）");

                    dataSet.setColor(Color.BLACK);
                    dataSet.setCircleColor(Color.BLACK);
                    dataSet.setHighLightColor(Color.BLUE);
                    dataSet.setDrawValues(false);
                    dataSet.setLineWidth(1.75f); //设置线宽
                    dataSet.setCircleRadius(5f); //设置焦点圆心的大小
                    dataSet.setCircleHoleRadius(2.5f); //
                    dataSet.enableDashedHighlightLine(10f,5f,0f); //点击后的高亮线的显示样式
                    dataSet.setHighlightLineWidth(3f); //设置点击交点后显示高亮线宽
                    dataSet.setHighlightEnabled(true); //是否禁用点击高亮线
                    dataSet.setHighLightColor(Color.RED); //设置点击交点后显示交高亮线的颜色
                    dataSet.setValueTextSize(10f); //设置显示值的文字大小
                    dataSet.setDrawFilled(false);//设置禁用范围背景填充

                    LineData lineData = new LineData(dataSet);

                    mLineChart.setData(lineData);
                    mLineChart.invalidate(); //刷新，显示图形
                }
            } else {
                Toast.makeText(MapShowActivity.this,"时间记录检索失败！",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
