package com.example.administrator.timeRecording;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.administrator.timeRecording.R;
import com.example.administrator.timeRecording.utils.AppManager;

/**
 * 名称     ：BaseActivity
 * 主要内容  ：作为活动模板使用
 * 创建人   ：wanzhuang
 * 创建时间 ：2018.8.2
 */
public class BaseActivity extends AppCompatActivity {
   // public AppManager appManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //将当前活动进栈

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseactivity);
        AppManager.getAppManager().addActivity(this);

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //将当前活动退栈
        AppManager.getAppManager().finishActivity(this);
    }


}
