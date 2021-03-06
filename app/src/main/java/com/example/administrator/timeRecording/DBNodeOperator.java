package com.example.administrator.timeRecording;

import android.util.Log;

import com.example.administrator.timeRecording.Model.nodeInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.administrator.timeRecording.DbOperator.getConnection;

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

    public static nodeInfo queryNodeInfo(String nodeSN) {

        nodeInfo node = new nodeInfo();

        Connection conn = getConnection();
        String sql = "select * from nodeInfo where nodeSN='" +nodeSN+"'";
        PreparedStatement pstmt = null;
        ResultSet rs=null;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            //构造一个node，并返回它
            while(rs.next()) {
                node.setNodeID(rs.getString("nodeID"));
                node.setNodeSN(rs.getString("nodeSN"));
                node.setNodeName(rs.getString("nodeName"));
                node.setLongitude(rs.getDouble("nodeLongitude"));
                node.setLatitude(rs.getDouble("nodeLatitude"));
                node.setPosition(rs.getString("nodePosition"));
                node.setDescription(rs.getString("nodeDescription"));
                Log.d("位置锚点数据库操作：","位置锚点信息查询：SN："+rs.getString("nodeSN"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("位置锚点数据库操作：","查询位置锚点信息出错！");
        }

        try{
            if (conn != null)
                conn.close();
            if (pstmt != null)
                pstmt.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return node;
    }
}
