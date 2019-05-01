package com.example.administrator.timeRecording;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.timeRecording.greendao.CheckinInfoDao;
import com.example.administrator.timeRecording.greendao.DaoMaster;
//import com.example.administrator.myapplication.greendao.DaoMaster.OpenHelper;
import com.example.administrator.timeRecording.greendao.UserInfoDao;
import com.example.administrator.timeRecording.greendao.nodeInfoDao;
import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }
            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        },CheckinInfoDao.class, nodeInfoDao.class, UserInfoDao.class);
    }
}
