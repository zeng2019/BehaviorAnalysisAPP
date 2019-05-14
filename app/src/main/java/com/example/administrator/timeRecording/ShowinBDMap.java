package com.example.administrator.timeRecording;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.example.administrator.timeRecording.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.administrator.timeRecording.DBTimeOperator.queryTimeInfo;

public class ShowinBDMap extends BaseActivity {

    public LocationClient mLocationClient;
    private MapView mapView;
    private TextView positionText;
    private BaiduMap baiduMap;
    private boolean isFirstLocate  = true;
    LatLng llcu = null; //所处位置坐标

    //统计用户时间
    private timeStatisticalSyncTask timeStatSyncTask = null;
    private String email;
    private List<Map<String,Object>> timeList = new ArrayList<>();

    //定义位图变量
    private Marker mMarkerTsg;
    private Marker mMarkerGkl;
    private Marker mMarkerJiaYuan;
    private Marker mMarkerJinYuan;
    private Marker mMarkerXXDaMen;
    private Marker mMarkerXXLongXiangJie;
    private InfoWindow  mInfoWindow;
    BitmapDescriptor bitmap = null;
    BitmapDescriptor bdground = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //百度地图相关处理
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_showin_bdmap);
        mapView = (MapView)findViewById(R.id.bdmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        positionText = (TextView)findViewById(R.id.coordination);
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        bdground = BitmapDescriptorFactory.fromResource(R.drawable.ground_overlay);

        requestLocation();

        //获得指定用户的时间记录，统计在各处花费的时间
        Intent in = getIntent();
        email = in.getStringExtra("email"); //从Intent中取得登录用户的email
        totalTimeStatistic(email);
        for(int i=0; i<timeList.size();i++) {
            Log.d("时间记录信息：",timeList.get(i).get("recTime").toString());
        }

        //初始化覆盖物
        initOverlay();

        //为每个标记设置监听器
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.popup);
                InfoWindow.OnInfoWindowClickListener listener = null;

                if(marker == mMarkerTsg) {
                    button.setText("图书馆");
                    button.setTextColor(Color.BLACK);
                    button.setWidth(300);

                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick() {
                            baiduMap.hideInfoWindow();
                        }
                    };

                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                    baiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerJiaYuan) {
                    button.setText("嘉 园");
                    button.setTextColor(Color.BLACK);
                    button.setWidth(300);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            baiduMap.hideInfoWindow();
                        }
                    });

                    LatLng ll  = marker.getPosition();
                    mInfoWindow = new InfoWindow(button,ll,-47);
                    baiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerJinYuan) {
                    button.setText("菁 园");
                    button.setTextColor(Color.BLACK);
                    button.setWidth(300);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            baiduMap.hideInfoWindow();
                        }
                    });

                    LatLng ll  = marker.getPosition();
                    mInfoWindow = new InfoWindow(button,ll,-47);
                    baiduMap.showInfoWindow(mInfoWindow);
                }  else if (marker == mMarkerXXDaMen) {
                    button.setText("学校正门出口：");
                    button.setTextColor(Color.BLACK);
                    button.setWidth(300);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            baiduMap.hideInfoWindow();
                        }
                    });

                    LatLng ll  = marker.getPosition();
                    mInfoWindow = new InfoWindow(button,ll,-47);
                    baiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerXXLongXiangJie) {
                    button.setText("龙祥街出口：");
                    button.setTextColor(Color.BLACK);
                    button.setWidth(300);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            baiduMap.hideInfoWindow();
                        }
                    });

                    LatLng ll  = marker.getPosition();
                    mInfoWindow = new InfoWindow(button,ll,-47);
                    baiduMap.showInfoWindow(mInfoWindow);
                }  else if (marker == mMarkerGkl) {
                    button.setText("工科教学楼");
                    button.setTextColor(Color.BLACK);
                    button.setWidth(300);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            baiduMap.hideInfoWindow();
                        }
                    });

                    LatLng ll  = marker.getPosition();
                    mInfoWindow = new InfoWindow(button,ll,-47);
                    baiduMap.showInfoWindow(mInfoWindow);
                }

                return true;
            }
        });
    }

    private void totalTimeStatistic(String email) {

        //根据用户名，将用户在该区域活动的时间记录提取出来
        timeStatSyncTask = new timeStatisticalSyncTask(this.email);
        timeStatSyncTask.execute((Void) null);

        for(int i=0; i<timeList.size();i++) {
            Log.d("时间记录信息：",timeList.get(i).get("recTime").toString());
        }

    }

    public class timeStatisticalSyncTask extends AsyncTask<Void, Void, Boolean> {
        String condition;
   //     private List<Map<String,Object>> timeList = new ArrayList<>();

        public timeStatisticalSyncTask(String cond) {
            super();
            condition = cond;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isDone = false;

            //登陆mysql，根据条件检索timeInfo并根据返回的ResultSet构造用于recycleView显示的字符串
            timeList = queryTimeInfo(condition);
            isDone = true;

            return isDone;
        }

        @Override
        //线程结束后的ui处理
        protected void onPostExecute(final Boolean isDone) {
            timeStatSyncTask = null;

            if (isDone) {
//                Toast.makeText(MapShowActivity.this,"时间记录检索成功！",Toast.LENGTH_SHORT).show();
                if(timeList.isEmpty()) {
                    Toast.makeText(ShowinBDMap.this,"您还没有记录过时间信息！",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ShowinBDMap.this,"时间记录检索失败！",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initOverlay() {
        LatLng llTsg = new LatLng(34.614424,112.427532);
        LatLng llJiayuan = new LatLng(34.610458,112.423256);
        LatLng llGkl = new LatLng(34.610102,112.433138);
        LatLng llJinYuan = new LatLng(34.603387,112.426436);
        LatLng llXXDamen = new LatLng(34.615698,112.428035);
        LatLng llXXLongXiangJie = new LatLng(34.61295,112.423679);

        MarkerOptions ooTsg = new MarkerOptions().position(llTsg).icon(bitmap).zIndex(1).draggable(true);
        ooTsg.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerTsg = (Marker)(baiduMap.addOverlay(ooTsg));

        MarkerOptions ooJiayuan = new MarkerOptions().position(llJiayuan).icon(bitmap).zIndex(2).draggable(false);
        ooJiayuan.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerJiaYuan = (Marker)(baiduMap.addOverlay(ooJiayuan));

        MarkerOptions ooGkl = new MarkerOptions().position(llGkl).icon(bitmap).zIndex(3).draggable(false);
        ooGkl.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerGkl = (Marker)(baiduMap.addOverlay(ooGkl));

        MarkerOptions ooJinYuan = new MarkerOptions().position(llJinYuan).icon(bitmap).zIndex(4).draggable(false);
        ooJinYuan.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerJinYuan = (Marker)(baiduMap.addOverlay(ooJinYuan));

        MarkerOptions ooXXDaMen = new MarkerOptions().position(llXXDamen).icon(bitmap).zIndex(5).draggable(false);
        ooXXDaMen.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerXXDaMen = (Marker)(baiduMap.addOverlay(ooXXDaMen));

        MarkerOptions ooXXLongXJie = new MarkerOptions().position(llXXLongXiangJie).icon(bitmap).zIndex(6).draggable(false);
        ooXXLongXJie.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerXXLongXiangJie = (Marker)(baiduMap.addOverlay(ooXXLongXJie));

        //add ground overlay
        LatLng southwest = new LatLng(34.598172,112.415495);
        LatLng northeast = new LatLng(34.618587,112.441806);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast).include(southwest).build();

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds)
                .image(bdground)
                .transparency(0.9f);
        baiduMap.addOverlay(ooGround);

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(bounds.getCenter());
        baiduMap.setMapStatus(u);

        baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(
                        ShowinBDMap .this,
                        "拖拽结束，新位置：" + marker.getPosition().latitude + ", " + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        });

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

    //第一次使用地图时，将我的位置显示出来
    private void navigateTo(BDLocation location) {
        if(isFirstLocate) {
            llcu = new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(llcu);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(18f);
            isFirstLocate = false;
        }

        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    //    @Override
    protected void OnResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
        mLocationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        bitmap.recycle();
        bdground.recycle();
    }

    @Override
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
        @Override
        public void onReceiveLocation(final BDLocation location) {

            if(location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
            }
        }
        //        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
