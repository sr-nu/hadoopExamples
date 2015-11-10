package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by srik on 11/10/2015.
 */
public class HiveTableCreateTest {
    public static void main(String args[]) throws SQLException {
        createTable();
    }
    private static void createTable() throws SQLException{
        String hiveHostName = "127.0.0.1"; //change this ip address if required
        String hiveForwardedPort = "10000"; //this is the port number, which is configured to forward to Sandbox's 10000 port

        try {
            String driverName = "org.apache.hive.jdbc.HiveDriver";
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        Connection con = DriverManager.getConnection("jdbc:hive2://" + hiveHostName + ":" + hiveForwardedPort + "/default", "", "");
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE pokes (foo INT, bar STRING)";
        System.out.println("Running: " + sql);
        boolean failed = stmt.execute(sql);
        System.out.println("Query execution complete: " + (failed != true ?"success":"failure"));
    }
}
