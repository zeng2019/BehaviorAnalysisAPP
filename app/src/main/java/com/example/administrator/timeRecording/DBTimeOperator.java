package com.example.administrator.timeRecording;

import android.util.Log;

import com.example.administrator.timeRecording.Model.CheckinInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.administrator.timeRecording.DbOperator.getConnection;

public class DBTimeOperator {

    /**
     * 插入时间记录
     * @param
     * @return
     */
    public static boolean insertTimeInfo(CheckinInfo time) {
        Connection conn =  getConnection();
        boolean insertTimeDone = false;
        int i=0;
        String sql = "insert into timeInfo (recEmail,recNodeSN,recTime) values(?,?,?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, time.getEmail()+"");
            pstmt.setString(2, time.getIbeacn_sn()+"");
            //处理时间格式问题（java格式到mysql时间格式）
            Timestamp timestamp = new Timestamp(time.getTime().getTime());
            pstmt.setTimestamp(3, timestamp);
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            Log.d("时间记录数据库操作：","添加新时间记录成功！");
        } catch (SQLException e) {
            Log.d("时间记录数据库操作：","添加新时间记录失败！");
            e.printStackTrace();
        }
        if (i>0){
            insertTimeDone = true;
        }
        return insertTimeDone;
    }

    public static int update(CheckinInfo time) {

        Connection conn = getConnection();
        int i = 0;
        String sql = "update timeInfo set recTime='" + time.getTime() + "' where recEmail='" + time.getEmail() + "'";
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

    public static List<Map<String, Object>> queryTimeInfo(String email) {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Connection conn = getConnection();
        String sql = "select * from timeInfo where recEmail='" + email + "'";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            Log.d("时间记录数据库操作：", "时间记录检索完毕！");
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("时间记录数据库操作：", "时间记录检索出错！");
        }


        try {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            Log.d("时间分析操作：", "查询时间记录成功！");
            while (rs.next()) //获得时间记录列表，构造字符串数组
            {
                Map<String,Object> rowData = new HashMap<String, Object>();
                for(int i=1;i<=columnCount;i++) {
                    rowData.put(md.getColumnName(i),rs.getObject(i));
                }
                list.add(rowData);

            }
        } catch (SQLException e) {
            Log.d("时间分析操作:","查询时间记录信息出错！");
            e.printStackTrace();
        }


        try {
            if (conn != null)
                conn.close();
            if (pstmt != null)
                pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
