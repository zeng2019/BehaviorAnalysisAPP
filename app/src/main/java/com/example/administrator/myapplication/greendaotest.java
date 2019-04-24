package com.example.administrator.myapplication;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.myapplication.BaseAcivity.BaseActivity;
import com.example.administrator.myapplication.Model.CheckinInfo;
import com.example.administrator.myapplication.Model.UserInfo;
import com.example.administrator.myapplication.greendao.CheckinInfoDao;
import com.example.administrator.myapplication.greendao.DaoSession;
import com.example.administrator.myapplication.greendao.UserInfoDao;

import java.util.List;

public class greendaotest extends BaseActivity {
    private UserInfoDao userInfoDao;
    private CheckinInfoDao checkinInfoDao;
    private Button btn_scan;
    private Button btn_scan_checkin;
    private TextView tv_qb_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greendaotest);
        init();

    }

    private void init(){
        initView();
        initGreendao();
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<UserInfo> userInfoList=userInfoDao.queryBuilder().list();

                if(userInfoList!=null){
                    String searchAllInfo="";
                    for(int i = 0;i < userInfoList.size();i++){
                        UserInfo userInfo = userInfoList.get(i);
                        searchAllInfo += "id: "+userInfo.getId()+"UserName: "+userInfo.getUsername() +"Email: "+userInfo.getEmail()+"password: "+userInfo.getPassword()+"Tel: "+userInfo.getTelnumber()+"\n";
                    }
                    tv_qb_result.setText(searchAllInfo);
                }
            }
        });
        btn_scan_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CheckinInfo> checkinInfoList=checkinInfoDao.queryBuilder().list();
                if(checkinInfoList!=null){
                    String searchAllInfo="";
                    for(int i=0;i<checkinInfoList.size();i++){
                        CheckinInfo checkinInfo=checkinInfoList.get(i);
                        searchAllInfo+="ID:"+checkinInfo.getId()+"UserID:"+checkinInfo.getEmail()+"SN:"+checkinInfo.getIbeacn_sn()+"Status:"+checkinInfo.getStatus()+"Time:"+checkinInfo.getTime()+"\n";
                    }
                    tv_qb_result.setText(searchAllInfo);
                }
            }
        });
    }
    private void initView(){
       btn_scan=(Button)findViewById(R.id.btn_qb_user);
       btn_scan_checkin=(Button)findViewById(R.id.btn_qb_checkin);
       tv_qb_result=(TextView)findViewById(R.id.tv_QB_user);
    }
    private void initGreendao(){
        //get Dao
      DaoSession daoSession = ((myApp)getApplication()).getDaoSession();
      userInfoDao = daoSession.getUserInfoDao();
      checkinInfoDao = daoSession.getCheckinInfoDao();
    }

}
