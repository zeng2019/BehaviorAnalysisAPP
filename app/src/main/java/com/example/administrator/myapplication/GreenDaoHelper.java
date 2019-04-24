package com.example.administrator.myapplication;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.myapplication.greendao.DaoMaster;
import com.example.administrator.myapplication.greendao.DaoSession;

import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class GreenDaoHelper extends Application {
    private GreenDaoHelper Instance;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    public GreenDaoHelper getInstance() {
        if (Instance == null) {
            Instance = this;
        }
        return Instance;
    }

    /**
     * 获取DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {

        if (daoMaster == null) {

            try{
                ContextWrapper wrapper = new ContextWrapper(context) {
                    /**
                     * 获得数据库路径，如果不存在，则创建对象对象
                     *
                     * @param name
                     */
                    @Override
                    public File getDatabasePath(String name) {
                        // 判断是否存在sd卡
                        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
                        if (!sdExist) {// 如果不存在,
                            Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");
                            return null;
                        } else {// 如果存在
                            // 获取sd卡路径
                            String dbDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                            dbDir += "/Android";// 数据库所在目录
                            String dbPath = dbDir + "/" + name;// 数据库路径
                            // 判断目录是否存在，不存在则创建该目录
                            File dirFile = new File(dbDir);
                            if (!dirFile.exists())
                                dirFile.mkdirs();

                            // 数据库文件是否创建成功
                            boolean isFileCreateSuccess = false;
                            // 判断文件是否存在，不存在则创建该文件
                            File dbFile = new File(dbPath);
                            if (!dbFile.exists()) {
                                try {
                                    isFileCreateSuccess = dbFile.createNewFile();// 创建文件
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else
                                isFileCreateSuccess = true;
                            Log.d("SD卡管理：", "创建数据库文件！");
                            // 返回数据库文件对象
                            if (isFileCreateSuccess)
                                return dbFile;
                            else
                                return super.getDatabasePath(name);

                        }
                    }

                    /**
                     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
                     *
                     * @param name
                     * @param mode
                     * @param factory
                     */
                    @Override
                    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
                        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
                    }

                    /**
                     * Android 4.0会调用此方法获取数据库。
                     *
                     * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String,
                     *      int,
                     *      android.database.sqlite.SQLiteDatabase.CursorFactory,
                     *      android.database.DatabaseErrorHandler)
                     * @param name
                     * @param mode
                     * @param factory
                     * @param errorHandler
                     */
                    @Override
                    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
                        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
                    }
                };
                DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(wrapper,"info.db",null);
                daoMaster = new DaoMaster(helper.getWritableDatabase()); //获取未加密的数据库
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return daoMaster;
    }

    /**
     * 获取DaoSession对象
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {

        if (daoSession == null) {
            if (daoMaster == null) {
                getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }

        return daoSession;
    }

}
