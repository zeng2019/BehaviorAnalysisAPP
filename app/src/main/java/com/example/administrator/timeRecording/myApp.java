package com.example.administrator.timeRecording;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.administrator.timeRecording.greendao.DaoMaster;
import com.example.administrator.timeRecording.greendao.DaoSession;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.readystatesoftware.chuck.ChuckInterceptor;

import com.sensoro.cloud.SensoroManager;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * 主要内容  ：全局配置
 * 创建人    : Wanzhuang
 * 日期时间  : 2018.8.2
 * 修改内容  ：增加了GreenDao配置，增加Beacon设备的配置（2018.9.18）
 */
public class myApp extends Application {

    private static myApp app; //用于异常退出后，重启App
    private static final String TAG= myApp.class.getSimpleName();
    //数据库操作全局对象
    SensoroManager sensoroManager;
    private DaoSession daoSession;
    private  DaoMaster daoMaster; //希望将数据库保存到sd卡时，注释掉，其它情况需要取消注释。
    public boolean checkinState = false; //该标志用于表明是否成功记录时间。
    @Override
    public void onCreate(){
        super.onCreate();
        app = this;
        // 程序崩溃时触发线程  以下用来捕获程序崩溃异常
        Thread.setDefaultUncaughtExceptionHandler(handler);
        //okttp
        initOkhttpClient();
        //bluetooth
        initSensoro();
        //greendao
        initGreenDao();
    }

    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            Toast.makeText(myApp.this, "程序运行异常，正在重启，请稍候！", Toast.LENGTH_SHORT).show();
//            restartAPP(); //程序异常时，重启APP
        }
    };

    private void restartAPP() {
        Intent intent = new Intent(this, LoginActivity.class);
        @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(
                app.getApplicationContext(),0,intent,Intent.FLAG_ACTIVITY_NEW_TASK);
        //退出程序
        AlarmManager mgr = (AlarmManager)app.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC,System.currentTimeMillis()+1000,restartIntent); //1秒后重启应用

        //结束进程之前，把程序注销或者退出代码放在此句之前
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /*
     *配置OkhttpClient
     */
    private void initOkhttpClient(){
        //配置Log
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("TAG");
        //设置log打印级别和打印颜色
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        //
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor( new ChuckInterceptor(this))
                .readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .writeTimeout(OkGo.DEFAULT_MILLISECONDS,TimeUnit.MILLISECONDS)
                .connectTimeout(OkGo.DEFAULT_MILLISECONDS,TimeUnit.MILLISECONDS)

                 //其他配置
                .build();

        //配置OkGo
        OkGo.getInstance().init(this)
                .setOkHttpClient(okHttpClient)
                .setRetryCount(3); //设置重新请求参数

    }

    /**
     * initSensoro
     */
    private void initSensoro(){
        //
        sensoroManager =SensoroManager.getInstance(getApplicationContext());
        //
        sensoroManager.setCloudServiceEnable(false);
        //
        sensoroManager.addBroadcastKey("01Y2GLh1yw3+6Aq0RsnOQ8xNvXTnDUTTLE937Yedd/DnlcV0ixCWo7JQ+VEWRSya80yea6u5aWgnW1ACjKNzFnig==");
    }

    /**
     * initGreenDao
     */

    private void initGreenDao(){
        //greenDao在升级数据库时会删除数据库，从重新建表。在使用时注意。以下代码实现在手机内存储数据库
        String db="info.db";
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this,db,null);
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();

        //以下代码用于增加，删除等操作数据表时使用。
        /**************** begin ********************/
        //MigrationHelper.DEBUG = true;  //if you want see the log info,default is false
//        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, "info.db",
//                null);
//        daoMaster = new DaoMaster(helper.getWritableDatabase());
//        daoSession =daoMaster.newSession();
        /****************** end *******************/
        //以下代码实现将数据库保存在sd卡上。
//        daoSession = GreenDaoHelper.getDaoSession(this);
//        daoSession.getCheckinInfoDao().deleteAll();  //清空所有数据
//        daoSession.getNodeInfoDao().deleteAll();   //清空所有数据
//        daoSession.getUserInfoDao().deleteAll();   //清空所有数据

    }
    public DaoSession getDaoSession(){
        return daoSession;
    }

    @GlideModule
    public final class MyappGildeModule extends AppGlideModule{}

    @Override
    public void onTerminate(){
        //
        if (sensoroManager != null) {
            sensoroManager.stopService();

        }
        super.onTerminate();
    }

}
