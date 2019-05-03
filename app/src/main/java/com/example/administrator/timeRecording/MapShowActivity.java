package com.example.administrator.timeRecording;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.timeRecording.BaseAcivity.BaseActivity;
import com.example.administrator.timeRecording.Model.nodeInfo;
import com.mysql.jdbc.PreparedStatement;

import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.bCallBack;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.administrator.timeRecording.DBNodeOperator.insertNodeInfo;
import static com.example.administrator.timeRecording.DBNodeOperator.queryNodeInfo;
import static com.example.administrator.timeRecording.DBTimeOperator.queryTimeInfo;
import static com.example.administrator.timeRecording.DbOperator.getConnection;

public class MapShowActivity extends BaseActivity {
    
    private scut.carson_ho.searchview.SearchView searchView;
    private mapShowTQSyncTask msTQsyncTask = null;
    String email;
    private RecyclerView timeRecListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_show);

        timeRecListView = (RecyclerView) findViewById(R.id.recordList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        timeRecListView.setLayoutManager(layoutManager);

        Intent in = getIntent();
        email = in.getStringExtra("email"); //从Intent中取得登录用户的email信息，用于检索userInfo表，获取用户信息
        Log.d("MapShowActivity","当前登录用户邮箱："+email);

        searchView = (scut.carson_ho.searchview.SearchView) findViewById(R.id.search_view);
        
        searchView.setOnClickSearch(new ICallBack(){
            @Override
            public void SearchAciton(String condition) {
                makeText(MapShowActivity.this,"我收到了："+condition, LENGTH_SHORT).show();
                //根据条件，开启异步任务，检索数据库，找到符合条件的时间记录，并显示在recycleView
                condition = email; //为了测试，直接使用用户名检索
                msTQsyncTask = new mapShowTQSyncTask(condition);
                msTQsyncTask.execute((Void) null);
            }
        });
        
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });
    }


    public class mapShowTQSyncTask extends AsyncTask<Void, Void, Boolean> {

        String condition;
        private List<Map<String,Object>> list = new ArrayList<>();

        public mapShowTQSyncTask(String cond) {
            super();
            condition = cond;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isDone = false;

            //登陆mysql，根据条件检索timeInfo并根据返回的ResultSet构造用于recycleView显示的字符串
            list = queryTimeInfo(condition);
            if(!list.isEmpty())
                isDone = true;

                return isDone;
        }

        @Override
        //线程结束后的ui处理
        protected void onPostExecute(final Boolean isDone) {
            //           showProgress(false);//隐藏验证延时对话框
            msTQsyncTask = null;

            if (isDone) {
                Toast.makeText(MapShowActivity.this,"时间记录检索成功！",Toast.LENGTH_SHORT).show();
                //如果时间记录存在，则显示在recycleView界面，否则弹出无时间记录信息提示。
                if(list.isEmpty()) {
                    Toast.makeText(MapShowActivity.this,"您还没有记录过时间信息！",Toast.LENGTH_SHORT).show();
                } else{
                    for(int i=0; i<list.size();i++) {
                        Log.d("时间记录信息：",list.get(i).get("recTime").toString());
                    }
                    timeInfoAdapter adapter = new timeInfoAdapter(list);
                    timeRecListView.setAdapter(adapter);
                }

//                finish();
            } else {
                Toast.makeText(MapShowActivity.this,"时间记录检索失败！",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
