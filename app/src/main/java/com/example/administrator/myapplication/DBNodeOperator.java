package com.example.administrator.myapplication;

import android.util.Log;

import com.example.administrator.myapplication.Model.nodeInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.administrator.myapplication.DbOperator.getConnection;

public class DBNodeOperator {
    /**
     * 插入位置锚点数据
     * @param
     * @return
     */
    public static boolean insertNodeInfo(nodeInfo node) {
        Connection conn =  getConnection();
        boolean insertUserDone = false;
        int i=0;
        String sql = "insert into nodeInfo (nodeID,nodeSN,nodePosition,nodeName,nodeLongitude,nodeLatitude,nodeDescription) values(?,?,?,?,?,?,?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, node.getNodeID()+"");
            pstmt.setString(2, node.getNodeSN()+"");
            pstmt.setString(3,node.getPosition()+"");
            pstmt.setString(4,node.getNodeName()+"");
            pstmt.setString(5,node.getLongitude()+"");
            pstmt.setString(6,node.getLatitude()+"");
            pstmt.setString(7,node.getDescription()+"");
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            Log.d("位置锚点数据库操作：","添加新位置锚点成功！");
        } catch (SQLException e) {
            Log.d("位置锚点数据库操作：","添加新位置锚点失败！");
            e.printStackTrace();
        }
        if (i>0){
            insertUserDone = true;
        }
        return insertUserDone;
    }

    public static int update(nodeInfo node) {

        Connection conn = getConnection();
        int i = 0;
        String sql = "update nodeInfo set nodeName='" + node.getNodeName() + "' where nodeSN='" + node.getNodeSN() + "'";
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

    public static boolean queryNodeInfo(String nodeSN) {
        Connection conn = getConnection();
        String sql = "select * from nodeInfo where nodeSN='" +nodeSN+"'";
        PreparedStatement pstmt = null;
        boolean isExisted = true;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            Log.d("位置锚点数据库操作：","位置锚点信息查询执行完毕！");
            if(rs.next()==false) {
                isExisted = false; //表示用户不存在，可以新加
                Log.d("位置锚点数据库操作：","位置锚点不存在,可添加入库！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("位置锚点数据库操作：","查询位置锚点信息！");
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
}
