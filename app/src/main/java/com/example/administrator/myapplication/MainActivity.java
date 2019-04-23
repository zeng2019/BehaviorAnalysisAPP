package com.example.administrator.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.BaseAcivity.BaseActivity;
import com.example.administrator.myapplication.Model.CheckinInfo;
import com.example.administrator.myapplication.Model.nodeInfo;
import com.example.administrator.myapplication.UI.upImage;
import com.example.administrator.myapplication.greendao.CheckinInfoDao;
import com.example.administrator.myapplication.greendao.DaoSession;
import com.example.administrator.myapplication.utils.FileUtils;
import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 名称     ：MainActivity
 * 主要内容 ：主页
 * 创建人   ：
 * 创建时间 ：2018.6
 * 修改时间 ：2019-04
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Handler mHandler;
    //确认退出的标志值
    private static boolean isExit = false;
    //
    private TextView nav_header_username;
    private TextView nav_header_id;
    private ImageView nav_heder_img;
    private TextView tv_sn;
    private TextView tv_id;
    private TextView tv_sn_info;
    private TextView tv_id_info;
    private TextView tv_pos_des;
    private TextView tv_longitude;
    private TextView tv_latitude;
    private Button btn_checkin;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    myApp app;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BeaconManagerListener beaconManagerListener;
    private SensoroManager sensoroManager;
    private CopyOnWriteArrayList<Beacon> beacons;
    private SimpleDateFormat simpleDateFormat;
    private String timeMatchFormat;
    private Context mContext;
    private boolean scan_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        //初始化
        init();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*
     * 初始化活动
     */
    private void init() {
        initView(); //界面元素变量定义
        initNodeInfo(); //初始化nodeInfo数据库
        initCtrl();
        initHander();
        initUserInfo();
    }

    /*
     * 将节点信息写入本地数据库表nodeInfo，设置created_flag = true;
     */
    public void initNodeInfo() {

        Log.d("MainActivity", "in initNodeInfo");
        if (!((myApp) (getApplication())).created_flag) {
            //图书馆位置
            nodeInfo nodeinfo = new nodeInfo();
            nodeinfo.setNodeID("0x77BBCB73"); //Major + minor
            nodeinfo.setNodeSN("0117C5976A3E"); //SN
            nodeinfo.setNodeName("00001");
            nodeinfo.setPosition("图书馆");
            nodeinfo.setLatitude(34.61);
            nodeinfo.setLongitude(112.42);
            nodeinfo.setDescription("图书馆锚点位置");
            ((myApp) (getApplication())).getDaoSession().insert(nodeinfo);

            //工科教学楼
            nodeInfo nodeinfo2 = new nodeInfo();
            nodeinfo2.setNodeID("0x8E336A86"); //Major + minor
            nodeinfo2.setNodeSN("0117C597055B"); //SN
            nodeinfo2.setNodeName("00002");
            nodeinfo2.setPosition("工科教学楼");
            nodeinfo2.setLatitude(34.61);
            nodeinfo2.setLongitude(112.43);
            nodeinfo2.setDescription("工科教学楼锚点位置");
            ((myApp) (getApplication())).getDaoSession().insert(nodeinfo2);

            //宿舍楼

            ((myApp) (getApplication())).created_flag = true; //避免重复创建
        }
/*
        List<nodeInfo> nodeList = queryAll();
        for(int i=0; i<nodeList.size(); i++) {
            nodeList.get(i).getPosition();
            Log.d("MainActivity","已有位置锚点："+nodeList.get(i).getNodeSN());
        }
*/
    }


    private void initCtrl() {
        //测试蓝牙是否打开
        boolean status = isBlueEnable();
        if (status) {
            Toast.makeText(MainActivity.this, "蓝牙已打开", Toast.LENGTH_SHORT).show();
        }
        app = (myApp) getApplication();
        timeMatchFormat = "yyyy年MM月dd日 HH:mm:ss";
        //实例化 sdk
        sensoroManager = app.sensoroManager;
        beacons = new CopyOnWriteArrayList<>();
        initSensoroLister();
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        btn_checkin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            // has bugs
                            public void onClick(View view) {
                              /* if(scan_flag){

                                   startSensoroService(true);

                               }else{
                                   startSensoroService(false);
                        //           btn_checkin.setText("记录时间");
                               } */
                                startSensoroService(false);

                            }
                        });

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 初始化view
     */
    private void initView() {
        btn_checkin = (Button) findViewById(R.id.btn_checkin);
        tv_id = (TextView) findViewById(R.id.beacon_id_text);
        tv_sn = (TextView) findViewById(R.id.beacon_sn_text);
        tv_sn_info = (TextView) findViewById(R.id.beacon_sn_info);
        tv_id_info = (TextView) findViewById(R.id.beacon_id_info);
        tv_pos_des = (TextView) findViewById(R.id.posDescription);
        tv_longitude = (TextView) findViewById(R.id.longtitude);
        tv_latitude = (TextView) findViewById(R.id.latitude);
        //填充界面显示的数据
        nav_header_username = (TextView) findViewById(R.id.nav_header_username);
        nav_header_id = (TextView) findViewById(R.id.nav_header_id);
        nav_heder_img = (ImageView) findViewById(R.id.nav_header_img);
        //初始化工具栏
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 启动扫描服务
     */
    private void startSensoroService(final boolean enable) {
        sensoroManager.setBeaconManagerListener(beaconManagerListener);
        if (!enable) {
            //延时操作，计算扫描时长，初始设置为10秒，10秒后扫描功能停止。
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //       scan_flag = true;
                    //        btn_checkin.setText("记录时间");
                    //       Toast.makeText(MainActivity.this,"扫描位置锚点，请稍候!",Toast.LENGTH_SHORT).show();
                    sensoroManager.stopService();
                    Toast.makeText(MainActivity.this, "时间信息记录成功！", Toast.LENGTH_SHORT).show();
                }
            }, 10000);
            //
            //  scan_flag = false;
            //  Toast.makeText(MainActivity.this,"正在扫描记录位置，请稍候!",Toast.LENGTH_SHORT).show();
            //开启位置锚点扫描服务，开始扫描
            try {
                sensoroManager.startService();
                Toast.makeText(MainActivity.this, "启动位置锚点扫描服务！", Toast.LENGTH_SHORT).show();
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //sensoroManager.setForegroundScanPeriod(7000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Beacon Manager lister,use it to listen the appearence, disappearence and
     * updating of the beacons.
     */
    private void initSensoroLister() {
        //
        Toast.makeText(MainActivity.this, "初始化位置锚点监听器！", Toast.LENGTH_SHORT).show();
        //
        beaconManagerListener = new BeaconManagerListener() {
            @Override
            public void onNewBeacon(Beacon beacon) {
                //获得扫描的设备的sn码并通过toast显示
                final String sn = beacon.getSerialNumber();
                final String id = beacon.getMajor().toString() + beacon.getMinor().toString();
                final String mes_local = "发现位置锚点:" + sn;
                final String nodePos = "未知位置！";
                final double longitude = 0.0;
                final double latitude = 0.0;
                /*
                * 查询nodeInfo数据表，找到位置锚点相关信息
                * /
                DaoSession daoSession =  ((myApp)getApplication()).getDaoSession();
                QueryBuilder<nodeInfo> qb =daoSession.queryBuilder(nodeInfo.class);
                QueryBuilder<nodeInfo> nodeQueryBuilder = qb.where(nodeInfoDao.Properties.nodeSN.eq(sn));
                List<nodeInfo> nodeList = nodeQueryBuilder.list();
                if (!nodeList.isEmpty()) {
                    for(int i=0; i<nodeList.size(); i++) {
                        nodePos = nodeList.get(i).getPosition();
                        longitude = nodeList.get(i).getLongitude();
                        latitude = nodeList.get(i).getLatitude();
                        Log.d("MainActivity","查找到位置锚点信息！");
                    }
                }

                /*
                  写用户时间记录信息：位置锚点的 sn 和 id，时间等
                 */
                recordTimeInfo(sn, id, true);

                writeText(sn);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, mes_local, Toast.LENGTH_SHORT).show();
                        tv_sn_info.setText(sn);
                        tv_id_info.setText(id);
                        tv_pos_des.setText(nodePos);
                        tv_longitude.setText(Double.toString(longitude));
                        tv_latitude.setText(Double.toString(latitude));
                        sensoroManager.stopService();
                    }
                });
            }

            @Override
            public void onGoneBeacon(Beacon beacon) {
                final String sn = beacon.getSerialNumber();
                final String id = beacon.getMajor().toString() + beacon.getMinor().toString();
                recordTimeInfo(sn, id, false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(MainActivity.this, "超出节点感知范围" + sn, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onUpdateBeacon(final ArrayList<Beacon> arg0) {

            }
        };
    }

    //get key
    public String getKey(Beacon beacon) {
        if (beacon == null) {
            return null;
        }
        String key = beacon.getSerialNumber();
        return key;
    }

    /*
     * 数据库操作
     * 功能：写用户时间记录信息到数据库，对应的数据表为CheckinInfo
     * 输入：位置锚点信息（sn，id）
     * 输出：void
     *
     */
    private void recordTimeInfo(String sn, String id, boolean status) {
        //
        //   DaoSession daoSession = ((myApp)getApplication()).getDaoSession();
        CheckinInfoDao checkinInfoDao = ((myApp) getApplication()).getDaoSession().getCheckinInfoDao();
        String beacon_sn = sn;
        long beacon_id = Long.parseLong(id);
        long time = System.currentTimeMillis();
        long user_id = 1000;
        CheckinInfo checkinInfo = new CheckinInfo(null, user_id, beacon_sn, beacon_id, status, time);
        QueryBuilder<CheckinInfo> userQB = checkinInfoDao.queryBuilder();
        checkinInfoDao.insert(checkinInfo);
        Log.d("MainActivity", "Insert is successful");
    }

    //写一一个通知体
    /*
     *获取当前系统时间
     */
    private String writeTime() {
        //设置时间样式
        simpleDateFormat = new SimpleDateFormat(timeMatchFormat, Locale.CHINA);
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
        // lv_id.setText("Date获得当前日期"+time);

    }

    /**
     * 将扫描到的信息写到text文本中
     */

    private void writeText(String str) {

        FileUtils fileHelper1 = new FileUtils(mContext);
        String filename = "log.txt";
        String fileDetail = "sn:" + str + " " + writeTime() + "\n";
        try {
            fileHelper1.writeText(filename, fileDetail);
            Toast.makeText(mContext, "数据写入成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "数据写入失败", Toast.LENGTH_SHORT).show();
        }
        //return fileDetail;
    }

    /*
     *讲储存的文本读取出来
     */
    private String readText() {
        String detail = "";
        FileUtils fileHelper2 = new FileUtils(mContext);
        try {
            String filename = "1.txt";
            detail = fileHelper2.readText(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    /*
     *判断蓝牙是否打开
     */
    private boolean isBlueEnable() {
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        boolean status = bluetoothAdapter.isEnabled();
        if (!status) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(intent);
                }
            }).setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setTitle(R.string.ask_bt_open);
            builder.show();
        }

        return status;
    }

    /**
     * 创建
     * 首次登录后的加载用户信息,将用户信息保存在数据库中,以备下次登录时直接从本地读取。
     * 先判断数据库中是否存在，存在就是用数据库中信息。
     * 否则调用网络通信获取信息
     * 设置一个标志值标识是否识新用户
     */
    private void initUserInfo() {

    }


    private void initHander() {
        //主线程处理视图，isExit默认为false，就是点击第一次时，弹出"再按一次退出程序"
        //点击第二次时关闭应用
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                isExit = false;
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }

    /*
     * 点击两次退出程序
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    // 利用handler延迟发送更改状态信息
                    Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            //参数用作状态码；根据惯例，非 0 的状态码表示异常终止。
            System.exit(0);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 待修改
     * 右上的选项
     * 目前还不知道用于什么用途
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 待修改
     * 将里面内容的命名规范化,把前面xml和activity中的命名匹配
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, greendaotest.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this, upImage.class));//修改
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_quit) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));//从主界面退到登陆界面
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
     * 以下为数据库操作
     * */
    public List queryAll()
    {
        DaoSession daoSession = ((myApp) getApplication()).getDaoSession();
        String nodePos ="";
        double longitude = 0.0;
        double latitude = 0.0;
        List<nodeInfo> nodeList = daoSession.loadAll(nodeInfo.class);

        for(int i=0;i<nodeList.size();i++)
        {
            nodePos = nodeList.get(i).getPosition();
            longitude = nodeList.get(i).getLongitude();
            latitude = nodeList.get(i).getLatitude();
            Log.d("MainActivity", "in queryAll():查找所有位置锚点信息！");
        }

        return nodeList;
}
}
