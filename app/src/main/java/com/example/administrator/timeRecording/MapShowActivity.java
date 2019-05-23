package com.example.administrator.timeRecording;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

import static android.widget.Toast.makeText;
import static com.example.administrator.timeRecording.DBTimeOperator.queryTimeInfo;

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

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_show);

//        toolbar = findViewById(R.id.toolbar);
//        this.setSupportActionBar(toolbar);

        weekTime =(Button)findViewById(R.id.weekTime);
        monthTime = (Button)findViewById(R.id.monthTime);
        yearTime = (Button)findViewById(R.id.yearTime);
        allTime = (Button)findViewById(R.id.allTime);

        weekTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开启异步任务，检索数据库，分析时间并展示

            }
        });

        monthTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开启异步任务，检索数据库，分析并展示

            }
        });

        yearTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //开启异步任务，检索数据库，分析并展示

            }
        });

        allTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MapShowActivity.this, allTimeShow.class);
                in.putExtra("email",email);
                startActivity(in);
            }
        });

         bottomNavView = findViewById(R.id.nav_showInMap);
         bottomNavView.setSelectedItemId(R.id.nav_timeChart);
         bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 int id = item.getItemId();

                 if (id == R.id.nav_timeChart) { //时间图形页面，默认选中，不需要处理

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

            timeList = queryTimeInfo(condition);
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
                } else{

                }
            } else {
                Toast.makeText(MapShowActivity.this,"时间记录检索失败！",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
