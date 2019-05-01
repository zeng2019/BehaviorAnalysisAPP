package com.example.administrator.timeRecording;

import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.timeRecording.BaseAcivity.BaseActivity;

public class aboutus extends BaseActivity {

    private TextView tv_aboutsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        tv_aboutsText = findViewById(R.id.aboutUsText);
        showInfo();
    }

    public void showInfo() {
        String aboutusInfo;
        aboutusInfo = "行为记录与分析APP旨在通过使用者主动记录个人时间，活动轨迹，" +
                "帮助他们分析个人日常时间开销，通过描绘出各项活动的时间支出，让使用者能够更加高效率地利用自己的时间，实现成功的人生。" +
                "我们是来自于河南科技大学的开发团队，我们将竭尽全力帮助你管理好你的时间！";
        tv_aboutsText.setText(aboutusInfo);
    }
}
