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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.BaseAcivity.BaseActivity;
import com.example.administrator.myapplication.Model.CheckinInfo;
import com.example.administrator.myapplication.Model.nodeInfo;
import com.example.administrator.myapplication.UI.upImage;
import com.example.administrator.myapplication.greendao.CheckinInfoDao;
import com.example.administrator.myapplication.greendao.DaoSession;
import com.example.administrator.myapplication.greendao.nodeInfoDao;
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
    private TextView tv_time;
    private ListView tv_pos_list;
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
    private String email; //用于保存从login活动中传递过来的登录用户email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        Intent in = getIntent();
        email = in.getStringExtra("email"); //从Intent中取得登录用户的email信息，用于检索userInfo表，获取用户信息
        Log.d("MainActivity","当前登录用户邮箱："+email);

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
//        initUserInfo();
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
            nodeinfo.setDescription("图书馆锚点");
            //查找是否已存在相同记录，不存在插入数据库
            ((myApp) (getApplication())).getDaoSession().insert(nodeinfo);

            //工科教学楼
            nodeInfo nodeinfo2 = new nodeInfo();
            nodeinfo2.setNodeID("0x8E336A86"); //Major + minor
            nodeinfo2.setNodeSN("0117C597055B"); //SN
            nodeinfo2.setNodeName("00002");
            nodeinfo2.setPosition("工科教学楼");
            nodeinfo2.setLatitude(34.61);
            nodeinfo2.setLongitude(112.43);
            nodeinfo2.setDescription("工科教学楼锚点");
            ((myApp) (getApplication())).getDaoSession().insert(nodeinfo2);

            //宿舍楼
            nodeInfo nodeinfo3 = new nodeInfo();
            nodeinfo3.setNodeID("0xCE5CF997"); //Major + minor
            nodeinfo3.setNodeSN("0117C5976771"); //SN
            nodeinfo3.setNodeName("00002");
            nodeinfo3.setPosition("宿舍楼");
            nodeinfo3.setLatitude(34.61);
            nodeinfo3.setLongitude(112.43);
            nodeinfo3.setDescription("宿舍楼锚点");
             ((myApp) (getApplication())).getDaoSession().insert(nodeinfo3);

            ((myApp) (getApplication())).created_flag = true; //避免重复创建
        }

        List<nodeInfo> nodeList = queryAll();
        for(int i=0; i<nodeList.size(); i++) {
            Log.d("MainActivity","已有位置锚点："+nodeList.get(i).getNodeSN()+": "+ nodeList.get(i).getPosition());
        }

    }


    private void initCtrl() {
        //测试蓝牙是否打开
        boolean status = isBlueEnable();
        if (!status) {
            Toast.makeText(MainActivity.this, "请打开蓝牙信号！", Toast.LENGTH_SHORT).show();
        }
        app = (myApp) getApplication();
        timeMatchFormat = "yyyy-MM-dd HH:mm:ss";
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
                               ((myApp)getApplication()).checkinState = false; //设置为false，表示还没有找到位置锚点。
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
        tv_time = (TextView) findViewById(R.id.checkTime);
        tv_pos_list = (ListView) findViewById(R.id.positionList);
        //填充界面显示的数据
        nav_header_username = (TextView) findViewById(R.id.nav_header_username);
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
            //延时操作，计算扫描时长，初始设置为30秒，30秒后扫描功能停止。
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (! ((myApp)getApplication()).checkinState ) //时间记录失败
                        Toast.makeText(MainActivity.this,"未能发现位置锚点，时间记录失败，请重试！",Toast.LENGTH_SHORT).show();
                    sensoroManager.stopService();
                }
            }, 10000);


            //开启位置锚点扫描服务，开始扫描
            try {
                sensoroManager.startService();
                Toast.makeText(MainActivity.this, "开始扫描位置锚点！", Toast.LENGTH_SHORT).show();
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
//        Toast.makeText(MainActivity.this, "初始化位置锚点监听器！", Toast.LENGTH_SHORT).show();
        //
 //       ((myApp)getApplication()).checkinState = false; //设置为false，表示还没有找到位置锚点。
        beaconManagerListener = new BeaconManagerListener() {
            @Override
            public void onNewBeacon(Beacon beacon) {
                //获得扫描的设备的sn码并通过toast显示
                final String sn = beacon.getSerialNumber();
                final String id = beacon.getMajor().toString() + beacon.getMinor().toString();
                final String mes_local = "发现位置锚点:" + sn;

                //查询nodeInfo数据表，找到位置锚点相关信息
                if (!sn.isEmpty()) {
                    Log.d("MainActivity", "获取到位置锚点:"+sn);
                    //写用户（基于email检索）时间记录信息：位置锚点的 sn 和 id，时间等
                    recordTimeInfo(sn, id, true);

//                writeText(sn);
                    //以下用于在view显示位置信息
                    nodeInfo node = new nodeInfo();
                    node = queryBySN(sn);
                    final String nodePos = node.getPosition();
                    final String longitude = node.getLongitude().toString();
                    final String latitude = node.getLatitude().toString();
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat stime = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
                    final String recTime = stime.format(date);
                    //处理最近5条时间记录
                    List<CheckinInfo> checkInList = queryCheckInfoByEmail(email);
                    int numberOfRec = checkInList.size();
                    if (numberOfRec > 5)
                        numberOfRec = checkInList.size() - 5;
                    else
                        numberOfRec = 0;
                    int j=1;
                    final String[] checkInfo = new String[6];
                    checkInfo[0] = "位置" + "             "+"时间";
                    for(int i=checkInList.size()-1;i>numberOfRec-1;i--) {
                        checkInfo[j] = checkInList.get(i).getPosition() + "     " + stime.format(checkInList.get(i).getTime());
                        j = j + 1;
                    }

                    if (j <6) {
                        checkInfo[j] = " ";
                        j = j + 1;
                    }
                    for (int i=0;i<6;i++)
                        Log.d("MainActivity","最近记录："+checkInfo[i]);
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.listview_item,checkInfo);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(MainActivity.this, mes_local, Toast.LENGTH_SHORT).show();
                            tv_sn_info.setText(sn);
                            tv_id_info.setText(id);
                            tv_pos_des.setText(nodePos);
                            tv_longitude.setText(longitude);
                            tv_latitude.setText(latitude);
                            tv_time.setText(recTime);
                            tv_pos_list.setAdapter(adapter);
                            sensoroManager.stopService();
                        }
                    });

                    ((myApp)getApplication()).checkinState = true; //设置为true，表示已经找到位置锚点。
                }

                }


            @Override
            public void onGoneBeacon(Beacon beacon) {
                final String sn = beacon.getSerialNumber();
                final String id = beacon.getMajor().toString() + beacon.getMinor().toString();
            //    recordTimeInfo(sn, id, false);
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
//    private void initUserInfo() {
//
//    }


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

        //noinspection SimplifiableIfStatement. 右边的配置选项，暂时不做处理！！
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 功能：处理时间记录界面的左上角导航栏的项目
     * 输入：无
     * 输出：各种界面操作响应动作，及活动的调用
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) { //个人信息修改
            Intent in = new Intent(MainActivity.this, personInfoRevise.class);
            in.putExtra("email",email);
            startActivity(in);
        } else if (id == R.id.nav_gallery) { //上传头像
            startActivity(new Intent(MainActivity.this, upImage.class));//修改
        } /*else if (id == R.id.nav_slideshow) { //关于我们

        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } */else if (id == R.id.nav_share) { //分享给好友

        } /*else if (id == R.id.nav_send) {

        } */else if (id == R.id.nav_quit) { //退出程序
            startActivity(new Intent(MainActivity.this, LoginActivity.class));//从主界面退到登陆界面
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
     * ****************** 数据库操作部分 ****************************
     * */

    /*
    * 功能：查询nodeInfo表，获得所有位置锚点信息
    * 返回值：位置锚点信息的列表
    * */
    public List queryAll()
    {
        Log.d("MainActivity", "in queryAll():查找所有位置锚点信息！");
        DaoSession daoSession = ((myApp) getApplication()).getDaoSession();
        List<nodeInfo> nodeList = daoSession.loadAll(nodeInfo.class);

/*        for(int i=0;i<nodeList.size();i++)
        {
            nodePos = nodeList.get(i).getPosition();
            longitude = nodeList.get(i).getLongitude();
            latitude = nodeList.get(i).getLatitude();
        }*/

        return nodeList;
    }

    /*
    * 功能：查询指定位置锚点信息
    * 输入：指定位置锚点的 SN
    * 输出：符合条件的位置锚点对象
    * */
    public nodeInfo queryBySN(String sn)
    {
        nodeInfo node = new nodeInfo();
        DaoSession daoSession = ((myApp) getApplication()).getDaoSession();
        QueryBuilder<nodeInfo> qb = daoSession.queryBuilder(nodeInfo.class);
        QueryBuilder<nodeInfo> studentQueryBuilder = qb.where(nodeInfoDao.Properties.NodeSN.eq(sn));
        List<nodeInfo> nodeList = studentQueryBuilder.list(); //查出当前对应的数据
        if (!nodeList.isEmpty()) {
            node = nodeList.get(0);
        }
        Log.d("MainActivity","in queryBySN(),find node:"+ node.getPosition());
        return node;
    }

    public boolean nodeIsExisted(String sn)
    {
        DaoSession daoSession = ((myApp) getApplication()).getDaoSession();
        QueryBuilder<nodeInfo> qb = daoSession.queryBuilder(nodeInfo.class);
        QueryBuilder<nodeInfo> studentQueryBuilder = qb.where(nodeInfoDao.Properties.NodeSN.eq(sn));
        List<nodeInfo> nodeList = studentQueryBuilder.list(); //查出当前对应的数据
        if (!nodeList.isEmpty()) {
            return true;
        }
        return false;
    }


    /*
     *
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
        Date date = new Date(time);
        SimpleDateFormat stime = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        Log.d("MainActivity","格式化后的时间："+ stime.format(date));

        //根据SN，查询nodeInfo，获取位置信息
        nodeInfo node = queryBySN(beacon_sn);
        String position = node.getPosition();

        CheckinInfo checkinInfo = new CheckinInfo(null, email, beacon_sn, beacon_id, status, position, date);
//     //   QueryBuilder<CheckinInfo> userQB = checkinInfoDao.queryBuilder();
        checkinInfoDao.insert(checkinInfo);
        //将用户的时间信息读取出来，确认写入成功
        Log.d("MainActivity", "in recordTimeInfo():写入用户时间信息成功！");
        Toast.makeText(MainActivity.this, "时间信息记录成功！", Toast.LENGTH_SHORT).show();
        List<CheckinInfo> checkList = queryCheckInfoByEmail(email);
        if (!checkList.isEmpty()) {
            for (int i=0; i<checkList.size(); i++) {
                Log.d("MainActivity","in recordTimeInfo(),用户时间记录:"+ "位置："+checkList.get(i).getPosition()+"，时间："+checkList.get(i).getTime());
            }
        } else
            Log.d("MainActivity","in recordTimeInfo(): 用户没有时间记录信息！");
    }

    /*
    * 功能：查询用户所有时间记录信息
    * 输入：email
    * 输出：用户的时间记录信息列表
    * */
    public List queryCheckInfoByEmail(String user_email)
    {
        DaoSession daoSession = ((myApp) getApplication()).getDaoSession();
        QueryBuilder<CheckinInfo> qb = daoSession.queryBuilder(CheckinInfo.class);
        QueryBuilder<CheckinInfo> checkInQueryBuilder = qb.where(CheckinInfoDao.Properties.Email.eq(user_email));
        List<CheckinInfo> checkList = checkInQueryBuilder.list(); //查出当前对应的数据
        return checkList;
    }

}
