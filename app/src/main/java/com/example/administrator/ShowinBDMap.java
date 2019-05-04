package com.example.administrator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.drm.DrmInfoEvent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.timeRecording.BaseAcivity.BaseActivity;
import com.example.administrator.timeRecording.MainActivity;
import com.example.administrator.timeRecording.R;
import com.mysql.jdbc.MiniAdmin;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class ShowinBDMap extends BaseActivity {

    public LocationClient mLocationClient;
    private TextView positionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //百度地图相关处理
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_showin_bdmap);
        positionText = (TextView)findViewById(R.id.coordination);
        //构造权限list，检测是否定位所要求权限得到满足，不满足，将该权限加入list
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(ShowinBDMap.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(ShowinBDMap.this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(ShowinBDMap.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(ShowinBDMap.this,permissions,1);
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() { //设置连续定位的时间间隔，并启用连续定位
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); //设置定位模式，GPS+网络共用的高精度定位方式
        option.setIsNeedAddress(true); //获取位置相关的信息，比如省、市，区等。
        mLocationClient.setLocOption(option);
    }

    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0) {
                    for(int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this,"所请求为程序运行必须权限，请同意！",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this,"发生未知错误！", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                default:
        }
    }

    public class MyLocationListener implements BDLocationListener {
        public void onReceiveLocation(final BDLocation location) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    {
                        StringBuilder currentPosition = new StringBuilder();
                        currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
                        currentPosition.append("经线：").append(location.getLongitude()).append("\n");
                        currentPosition.append("定位方式：");
                        if(location.getLocType() == BDLocation.TypeGpsLocation) {
                            currentPosition.append("GPS");
                        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                            currentPosition.append("网络");
                        }
                        positionText.setText(currentPosition);
                    }
                }
            });
        }

        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
