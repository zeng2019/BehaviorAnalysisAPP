package com.example.administrator.myapplication;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbOperator {
    private static Connection con;

    public DbOperator() {
        this.con = getConnection();
    }

    public static Connection getConnection() {

        String driver_class="com.mysql.jdbc.Driver";
        String ip="192.168.1.200";
        int port=3306;
        String dbName = "INFO";
        String url="jdbc:mysql://"+ip+":"+port+"/"+dbName;
        String user="zeng";
        String password="123456";

        try{
            Class.forName(driver_class);
            con = DriverManager.getConnection(url,user,password);
            if(con != null)
                Log.d("数据库连接：","数据库连接成功！");

        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("数据库连接：","找不到数据库驱动！");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("数据库连接：","失败！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("数据库连接：","未知错误！");
        }
        return con;
    }

}
