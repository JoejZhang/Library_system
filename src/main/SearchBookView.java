package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.activation.ActivationGroup_Stub;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by zjz on 2018/1/10.
 */
public class SearchBookView {
    public JPanel mJPanel;
    private JTable table1;
    private JLabel tv_welcome;
    private JTextField et_bookName;
    private JButton btn_search;
    private JLabel tv_result;

    private DefaultTableModel dtm;
    private String[] tableHeads = new String[]{"图书编号", "图书名",
            "图书总数", "可借数目"};

    private String info;
    public SearchBookView (){
        try {
            if(GlobalConn.sMode == 1){
                GlobalConn.sConnection = MySqlParam.getConnection("student_library","******","zjz_library");//连接学生账号的数据库用户
                GlobalConn.sStatement =GlobalConn.sConnection.createStatement();
                if( GlobalConn.sStatement != null){
                    String sql = "select * from zjz_Student where Sno = '"+GlobalConn.sAccount + "'";
                    GlobalConn.sRerult =  GlobalConn.sStatement.executeQuery(sql);


                    if( GlobalConn.sRerult.next()){
                        GlobalConn.sAccountName =  GlobalConn.sRerult.getString(2);
                        info =  GlobalConn.sRerult.getString(1)+ "    "+
                                GlobalConn.sRerult.getString(2)+"    "+
                                GlobalConn.sRerult.getString(3)+"    "+
                                GlobalConn.sRerult.getInt(4)+ "    "+
                                GlobalConn.sRerult.getString(5);
                    }

                }
            }
            else if(GlobalConn.sMode == 2){
                GlobalConn.sConnection = MySqlParam.getConnection("admin_library","******","zjz_library");//连接检查账号的数据库用户
                GlobalConn.sStatement =GlobalConn.sConnection.createStatement();
                if( GlobalConn.sStatement != null){
                    GlobalConn.sRerult =  GlobalConn.sStatement.executeQuery("select * from zjz_admin where Ano = '"+GlobalConn.sAccount + "'");

                    if(GlobalConn.sRerult.next()){
                        info =  GlobalConn.sRerult.getString(1)+ "    "+
                                GlobalConn.sRerult.getString(2)+"    "+
                                GlobalConn.sRerult.getString(3)+"    "+
                                GlobalConn.sRerult.getString(4);
                    }
                }
            }
            tv_welcome.setText(info);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dtm = (DefaultTableModel) table1.getModel();
        dtm.setColumnIdentifiers(tableHeads);
        table1.getTableHeader().setReorderingAllowed(false);
        table1.setModel(dtm);


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dtm.setRowCount(0);

                if(et_bookName.getText().equals("")){
                    try {
                        String sql = "select * from zjz_Book ";//查询所有图书
                        GlobalConn.sRerult =  GlobalConn.sStatement.executeQuery(sql);
                        while(GlobalConn.sRerult.next()){
                            String[] row = new String[]{
                                    GlobalConn.sRerult.getString(1),GlobalConn.sRerult.getString(2),String.valueOf(GlobalConn.sRerult.getInt(3)),
                                    String.valueOf(GlobalConn.sRerult.getInt(4))
                            };
                            dtm.addRow(row);
                        }

                        tv_result.setText("查询成功，共有"+dtm.getRowCount()+"条记录");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                }
                else{

                    try {
                        String sql = "select * from zjz_Book where Bname = '"+et_bookName.getText()+"'";//以书籍名为查询条件查询书籍
                        GlobalConn.sRerult =  GlobalConn.sStatement.executeQuery(sql);
                        while(GlobalConn.sRerult.next()){
                            String[] row = new String[]{
                                    GlobalConn.sRerult.getString(1),GlobalConn.sRerult.getString(2),String.valueOf(GlobalConn.sRerult.getInt(3)),
                                    String.valueOf(GlobalConn.sRerult.getInt(4))
                            };
                            dtm.addRow(row);
                        }

                        tv_result.setText("查询成功，共有"+dtm.getRowCount()+"条记录");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });
    }
}
