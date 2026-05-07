package com.example.lost_and_found;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
    static Connection con;
    public  void setUpCon() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/lost_found_db" ,"root" , "abdulwasi098@@" );

    }
    public void closeCon() throws SQLException {
        con.close();
    }

    public static Connection getCon() {
        return con;
    }

    public static void setCon(Connection con) {
        dbConnection.con = con;
    }
}
