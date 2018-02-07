package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zjz on 2018/1/9.
 */
public class MySqlParam {

    public static void DBManager() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection(String userName, String psw, String dbName) {
        String  url ="jdbc:mysql://localhost:3306/"+dbName;
        Connection conn =null;
        try {
            conn= DriverManager.getConnection(url, userName,psw);
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}

