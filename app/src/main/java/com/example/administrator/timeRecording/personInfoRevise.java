package com.example.administrator.timeRecording;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.timeRecording.Model.UserInfo;
import com.example.administrator.timeRecording.greendao.DaoSession;
import com.example.administrator.timeRecording.greendao.UserInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;

import static com.example.administrator.timeRecording.DBUserOperator.getUserInfo;
import static com.example.administrator.timeRecording.DBUserOperator.queryUserInfo;
import static com.example.administrator.timeRecording.DbOperator.getConnection;

public class personInfoRevise extends BaseActivity {
    private UserInfoDao userInfoDao;
    private Button btn_saveInfo;
    private TextView tv_name;
    private TextView tv_tel;
    private TextView tv_school;
    private String email;

    //用户信息查询异步任务
    private userInfoRequestTask mUserInfoRequest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        tv_name = (TextView)findViewById(R.id.username);
        tv_tel = (TextView)findViewById(R.id.telephone);
        tv_school = (TextView)findViewById(R.id.school);
        btn_saveInfo = (Button)findViewById(R.id.btn_infoSaved);
        Intent in = getIntent();
        email = in.getStringExtra("email"); //从Intent中取得登录用户的email信息，用于检索userInfo表，获取用户信息
        Log.d("personInfoRevise","当前登录用户："+email);
        //开启用户信息查询异步任务，查到结果后，显示在相应字段
        mUserInfoRequest = new userInfoRequestTask(email);
        mUserInfoRequest.execute((Void)null);

//        UserInfo user = queryUserByEmail(email);
//        tv_name.setText(user.getUsername());
//        tv_tel.setText(String.valueOf(user.getTelnumber()));
//        tv_school.setText(user.getSchool());

        btn_saveInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(personInfoRevise.this,"抱歉，还没有实现！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class userInfoRequestTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        UserInfo userInfo=null;

        userInfoRequestTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean isDone = false;

            userInfo = getUserInfo(mEmail,"");

            if (userInfo != null)
                isDone = true;

            return isDone;
        }

        @Override
        //线程结束后的ui处理
        protected void onPostExecute(final Boolean isDone) {
            mUserInfoRequest = null;

            if (isDone) {
                tv_name.setText(userInfo.getUsername());
                tv_tel.setText(String.valueOf(userInfo.getTelnumber()));
                tv_school.setText(userInfo.getSchool());
            } else {
                //密码错误，输入框获得焦点，并提示错误
                Toast.makeText(personInfoRevise.this, "出错了，查无此人！", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /*
     * 功能：查询用户信息
     * 输入：email
     * 输出：用户信息
     * */
    public UserInfo queryUserByEmail(String user_email)
    {
        DaoSession daoSession = ((myApp) getApplication()).getDaoSession();
        QueryBuilder<UserInfo> qb = daoSession.queryBuilder(UserInfo.class);
        QueryBuilder<UserInfo> checkInQueryBuilder = qb.where(UserInfoDao.Properties.Email.eq(user_email));
        List<UserInfo> users = checkInQueryBuilder.list(); //查出当前对应的数据
        UserInfo user = new UserInfo();
        if (users.isEmpty()) {
            Toast.makeText(personInfoRevise.this, "出错了，查无此人！", Toast.LENGTH_SHORT).show();
        } else
            user = users.get(0);
        return user;
    }



}
