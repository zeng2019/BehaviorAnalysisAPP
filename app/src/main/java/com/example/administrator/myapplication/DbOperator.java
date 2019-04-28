package com.example.administrator.myapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbOperator {
    private static Connection con;

    public DbOperator() {
        this.con = getConnection();
    }

    private static Connection getConnection() {

        String driver_class="com.mysql.jdbc.Driver";
        String ip="192.168.0.110";
        int port=3306;
        String dbName = "INFO";
        String url="jdbc:mysql://"+ip+":"+port+"/"+dbName;
        String user="zeng";
        String password="123456";

        try{
            Class.forName(driver_class);
            con = DriverManager.getConnection(url,user,password);

        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return con;
    }

    /**
     * 插入数据
     * @param student
     * @return
     */
    public static int insert(User student) {
        Connection conn =  getConnection();
        int i = 0;
        String sql = "insert into user (iduser,user_count) values(?,?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, student.getId()+"");
            pstmt.setString(2, student.getCout_us()+"");
            //    pstmt.setString(3, student.getAge());
            i = pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;



    }




    public static int update(User student) {
        Connection conn = getConnection();
        int i = 0;
        String sql = "update user set user_count='" + student.getCout_us() + "' where iduser='" + student.getId() + "'";
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


    public static Integer getAll() {
        Connection conn = getConnection();
        String sql = "select * from user";
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
    }


    public static int delete(String name) {
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
    }

}
