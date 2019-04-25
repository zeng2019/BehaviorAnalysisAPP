package com.example.administrator.myapplication;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.myapplication.BaseAcivity.BaseActivity;
import com.example.administrator.myapplication.greendao.CheckinInfoDao;
import com.example.administrator.myapplication.greendao.DaoSession;
import com.example.administrator.myapplication.greendao.UserInfoDao;

public class personInfoRevise extends BaseActivity {
    private UserInfoDao userInfoDao;
    private CheckinInfoDao checkinInfoDao;
    private Button btn_saveInfo;
    private TextView tv_name;
    private TextView tv_tel;
    private TextView tv_school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        init();

    }

    private void init(){
        btn_saveInfo = (Button)findViewById(R.id.btn_infoSaved);
        tv_name = (TextView)findViewById(R.id.username);
        tv_tel = (TextView)findViewById(R.id.telephone);
        tv_school = (TextView)findViewById(R.id.school);
        initGreendao();
    }

    private void initGreendao(){
        //get Dao
      DaoSession daoSession = ((myApp)getApplication()).getDaoSession();
      userInfoDao = daoSession.getUserInfoDao();
      checkinInfoDao = daoSession.getCheckinInfoDao();
    }

}
