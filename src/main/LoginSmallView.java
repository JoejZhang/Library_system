package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjz on 2018/1/9.
 */
public class LoginSmallView {
    private static   JFrame frame = null;
    public JPanel mJPanel;
    public JTextField et_account;
    private JTextField et_password;
    private JButton btn_signin_student;
    private JButton btn_add;
    private JButton btn_signin_admin;
    private JLabel tv_result;

    //private static MySqlParam mySqlParam = null;
    private static  Connection connection = null;//账号用户连接
    private static  Statement statement =null ;
    private static  ResultSet resultSet = null;

    private Map<String ,String> mStudentAccountMap = null;//学生账号密码
    private Map<String ,String> mAdminAccountMap = null;//管理员账号密码
    private ArrayList<String> mSnoList = null;//学生学号

    public static void main(String[] arg){

        MySqlParam.DBManager();//加载驱动器

        frame = new JFrame();
        frame.setContentPane(new LoginSmallView().mJPanel);
        frame.setTitle("登录图书管理系统");
        frame.setLocationRelativeTo(null);
        frame .setSize(400, 200);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - 200)/2;
        int y = (d.height- 100)/2;
        frame.setLocation(x, y);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeFrame();
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }
        });

    }
    private static void closeFrame()
    {
        int result = JOptionPane.showConfirmDialog(null, "是否要退出？", "退出确认", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION)
        { //关闭资源
            try {
            if(resultSet !=null)resultSet.close();
        } catch (Exception e) {}

            try {
                if(statement !=null)statement.close();
            } catch (Exception e) {}

            try {
                if( connection!=null)connection.close();
            } catch (Exception e) {}
            frame.dispose();
        }

    }

    LoginSmallView(){

        try {
            connection = MySqlParam.getConnection("account_library","xxxxxx","zjz_library");//连接检查账号的数据库用户account_library
            statement =connection.createStatement();

            if(statement != null){
                resultSet = statement.executeQuery("select * from zjz_Student_login");//查询学生登录信息
                mStudentAccountMap = new HashMap<String ,String>();
            }
            while(resultSet.next()){
                mStudentAccountMap.put(resultSet.getString(1),resultSet.getString(2));
            }
            if(statement != null){
                resultSet = statement.executeQuery("select * from zjz_Admin_login");//查询管理员登录信息
                mAdminAccountMap = new HashMap<String ,String>();
            }
            while(resultSet.next()){
                mAdminAccountMap.put(resultSet.getString(1),resultSet.getString(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        Iterator<Map.Entry<String,String>> it = mStudentAccountMap.entrySet().iterator();
//        while (it.hasNext()){
//            Map.Entry<String,String> map = it.next();
//        }

        btn_signin_student.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (mStudentAccountMap.containsKey(et_account.getText())){
                    if(et_password.getText().equals(mStudentAccountMap.get(et_account.getText()))){//账号密码匹配
                        tv_result.setText("登录成功");

                        try {//回收资源
                            if(resultSet !=null)resultSet.close();
                        } catch (Exception ex) {}

                        try {
                            if(statement !=null)statement.close();
                        } catch (Exception ex) {}

                        try {
                            if( connection!=null)connection.close();
                        } catch (Exception ex) {}
                        frame.dispose();

                        GlobalConn.sMode = 1;//学生登录
                        GlobalConn.sAccount = et_account.getText();

                        openView();
                    }
                    else{
                        tv_result.setText("密码错误！");
                    }
                }
                else{
                    tv_result.setText("不存在该账号");
                }
            }
        });


        btn_signin_admin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mAdminAccountMap.containsKey(et_account.getText())){
                    if(et_password.getText().equals(mAdminAccountMap.get(et_account.getText()))){//账号密码匹配
                        tv_result.setText("登录成功");

                        try {
                            if(resultSet !=null)resultSet.close();
                        } catch (Exception ex) {}

                        try {
                            if(statement !=null)statement.close();
                        } catch (Exception ex) {}

                        try {
                            if( connection!=null)connection.close();
                        } catch (Exception ex) {}
                        frame.dispose();

                        GlobalConn.sMode = 2;//管理员登录
                        GlobalConn.sAccount = et_account.getText();

                        openView();

                    }
                    else{
                        tv_result.setText("密码错误！");
                    }
                }
                else{
                    tv_result.setText("不存在该账号");
                }
            }
        });

        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mStudentAccountMap.containsKey(et_account.getText())){
                    tv_result.setText("此账号已存在");
                }
                else{
                    if(et_password.getText().equals("")){
                        tv_result.setText("密码不能为空");
                    }
                    else{
                        if(mSnoList == null){
                            try {
                                if(statement != null){
                                    resultSet = statement.executeQuery("select * from zjz_Student");//查询学生表
                                    mSnoList = new ArrayList<>();
                                }
                                while(resultSet.next()){
                                    mSnoList.add(resultSet.getString(1));
                                }
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }
                        else{
                            boolean flag =false;
                            for (int i = 0; i < mSnoList.size(); i++) {//遍历学生表，查看是否有该学号
                                if(mSnoList.get(i).equals(et_account.getText())){
                                    flag = true ;
                                    break;
                                }
                            }
                            if(flag){
                                int k = 0;
                                try {
                                    k = statement.executeUpdate("insert into zjz_Student_login values('"+et_account.getText()+"','"+et_password.getText()+"')");//插入学生登录信息表
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                                if(k>0){
                                    tv_result.setText("注册账号成功");

                                    try {//刷新学生登录信息表
                                        if(statement != null){
                                            resultSet = statement.executeQuery("select * from zjz_Student_login");
                                            mStudentAccountMap = new HashMap<String ,String>();
                                        }
                                        while(resultSet.next()){
                                            mStudentAccountMap.put(resultSet.getString(1),resultSet.getString(2));
                                        }
                                        while(resultSet.next()){
                                            mAdminAccountMap.put(resultSet.getString(1),resultSet.getString(2));
                                        }
                                    } catch (SQLException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                else {
                                    tv_result.setText("注册账号失败");
                                }
                            }
                            else{
                                tv_result.setText("不存在该学号");
                            }
                        }
                    }
                }
            }
        });


    }
    private void openView(){
        JFrame frame1 = new JFrame();
        frame1.setContentPane(new LoginView().mJPanel);
        frame1.setTitle("图书管理系统");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame1.setSize(d.width, d.height);
        // frame.setAlwaysOnTop(true);
        frame1.setVisible(true);
        frame1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame1.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeLoginView(frame1);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }
        });
    }

    private void closeLoginView(JFrame jFrame){
        int result = JOptionPane.showConfirmDialog(null, "是否要退出？", "退出确认", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION)
        { //关闭资源
            try {
                if(GlobalConn.sRerult !=null)resultSet.close();
            } catch (Exception e) {}

            try {
                if(GlobalConn.sStatement !=null)statement.close();
            } catch (Exception e) {}

            try {
                if( GlobalConn.sConnection!=null)connection.close();
            } catch (Exception e) {}
            jFrame.dispose();
        }
    }
}
