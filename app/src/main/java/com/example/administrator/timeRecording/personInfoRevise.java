package com.example.administrator.timeRecording;


import android.content.Intent;
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

import java.util.List;

public class personInfoRevise extends BaseActivity {
    private UserInfoDao userInfoDao;
    private Button btn_saveInfo;
    private TextView tv_name;
    private TextView tv_tel;
    private TextView tv_school;
    private String email;

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
        UserInfo user = queryUserByEmail(email);
        tv_name.setText(user.getUsername());
        tv_tel.setText(String.valueOf(user.getTelnumber()));
        tv_school.setText(user.getSchool());

        btn_saveInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(personInfoRevise.this,"抱歉，还没有实现！",Toast.LENGTH_SHORT).show();
            }
        });
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
