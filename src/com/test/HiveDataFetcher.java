package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jfree.data.category.DefaultCategoryDataset;

public class HiveDataFetcher {

    private String hiveHostName = "127.0.0.1"; //change this ip address if required
    private String hiveForwardedPort = "10000"; //this is the port number, which is configured to forward to Sandbox's 10000 port

    public DefaultCategoryDataset getDataFromHive() throws SQLException{
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ResultSet rs  = runQueryAndGetResult();
        System.out.println("Populating dataset for chart...");
        while(rs.next()){
            dataset.setValue(rs.getDouble("salary_difference"), "Salary Change", rs.getString("job_category"));
        }
        System.out.println("Dataset population complete!");
        return dataset;
    }

    private ResultSet runQueryAndGetResult() throws SQLException{
        try {
            String driverName = "org.apache.hive.jdbc.HiveDriver";
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        Connection con = DriverManager.getConnection("jdbc:hive2://"+hiveHostName + ":" +hiveForwardedPort + "/default", "", "");
        Statement stmt = con.createStatement();
        String sql = "SELECT s07.description AS job_category, s07.salary , s08.salary ,  (s08.salary - s07.salary) AS salary_difference FROM  sample_07 s07 JOIN sample_08 s08 ON ( s07.code = s08.code) WHERE s07.salary < s08.salary SORT BY s08.salary-s07.salary DESC LIMIT 5";
        System.out.println("Running: " + sql);
        ResultSet res = stmt.executeQuery(sql);
        System.out.println("Query execution complete");
        return res;
    }
}