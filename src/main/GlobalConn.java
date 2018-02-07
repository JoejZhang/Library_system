package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by zjz on 2018/1/10.
 */
public class GlobalConn {
    public static Connection sConnection = null;
    public static Statement sStatement = null;
    public static ResultSet sRerult = null;
    public static int sMode = 0;//0未登录，1学生登录，2管理员登录
    public static String sAccount = "";
    public static String sAccountName = "";
   // public static Connection
}
