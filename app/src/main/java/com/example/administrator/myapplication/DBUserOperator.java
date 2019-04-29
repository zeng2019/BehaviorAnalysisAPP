package com.example.administrator.myapplication;

import android.util.Log;

import com.example.administrator.myapplication.Model.UserInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.administrator.myapplication.DbOperator.getConnection;

public class DBUserOperator {
    /**
     * 插入用户数据
     * @param
     * @return
     */
    public static boolean insertUserInfo(UserInfo user) {
        Connection conn =  getConnection();
        boolean insertUserDone = false;
        int i=0;
        String sql = "insert into userInfo (user_name,user_password,user_email) values(?,?,?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername()+"");
            pstmt.setString(2, user.getPassword()+"");
            pstmt.setString(3,user.getEmail()+"");
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            Log.d("用户数据库操作：","添加用户失败！");
            e.printStackTrace();
        }
        if (i>0){
            insertUserDone = true;
        }
        return insertUserDone;
    }

    public static int update(UserInfo user) {

        Connection conn = getConnection();
        int i = 0;
        String sql = "update user set user_password='" + user.getPassword() + "' where user_name='" + user.getUsername() + "'";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            i = pstmt.executeUpdate();
            System.out.println("resutl: " + i);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static boolean queryUserInfo(String name, String email) {
        Connection conn = getConnection();
        String sql = "select * from userInfo where user_name='" + name+ "'||" + "user_email='"+email+"'";
        PreparedStatement pstmt = null;
        boolean isExisted = true;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if(rs.wasNull()) {
                isExisted = false; //表示用户不存在，可以新加
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("用户数据库操作：","查询用户信息！");
        }

        try{
            if (conn != null)
                conn.close();
            if (pstmt != null)
                pstmt.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return isExisted;
    }

/*
    public static Integer getAll() {
        Connection conn = getConnection();
        String sql = "select * from userInfo";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();

            System.out.println("============================");
            while (rs.next()) {
                for (int i = 1; i <= col; i++) {
                    System.out.print(rs.getString(i) + "\t");
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        System.out.print("\t");
                    }
                }
                System.out.println("");
            }

            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }*/


/*    public static int delete(String name) {
        Connection conn = getConnection();
        int i = 0;
        String sql = "delete from user where iduser='" + name + "'";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            i = pstmt.executeUpdate();
            System.out.println("resutl: " + i);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }*/
}
