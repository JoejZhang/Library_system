package main;

import javax.crypto.SealedObject;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.SplittableRandom;

/**
 * Created by zjz on 2018/1/10.
 */
public class SearchBSView {
    public JPanel mJPanel;
    private JTextField et_snoOrName;
    private JLabel tv_SnoOrName;
    private JButton btn_search;
    private JTable table;
    private JLabel tv_lend;
    private JTextField et_lendBno;
    private JButton btn_lend;
    private JTextField et_lendSno;
    private JPanel mPanelLend;
    private JTextField et_backSno;
    private JTextField et_backBno;
    private JButton btn_back;
    private JPanel mPanelBack;
    private JPanel mJPanel1;
    private JLabel tv_result;
    private JTextField et_lend_count;
    private JTextField et_back_count;

    private DefaultTableModel dtm;
    private String[] tableHeadsStu = new String[]{"姓名", "图书名",
            "管理员名", "借书时间","还书时间","借书数量"};
    private String[] tableHeadsAdmin = new String[]{"学生编号", "学生姓名","图书编号","图书名","管理员编号 ","借书时间","还书时间","借书数量"};

    public SearchBSView(){
        if(GlobalConn.sMode == 1){//学生
            mJPanel1.setVisible(false);
            mPanelBack.setVisible(false);
            mPanelLend.setVisible(false);

            dtm = (DefaultTableModel) table.getModel();
            dtm.setColumnIdentifiers(tableHeadsStu);
            table.getTableHeader().setReorderingAllowed(false);
            table.setModel(dtm);


            if( GlobalConn.sStatement != null){
                String sql = "select * from view_studentbookname where Sname = '"+GlobalConn.sAccountName + "'";
                try {
                    GlobalConn.sRerult =  GlobalConn.sStatement.executeQuery(sql);
                    while( GlobalConn.sRerult.next()){
                        String[] row = new String[]{
                                GlobalConn.sRerult.getString(1) ,GlobalConn.sRerult.getString(2),GlobalConn.sRerult.getString(3),
                                GlobalConn.sRerult.getString(4),GlobalConn.sRerult.getString(5),GlobalConn.sRerult.getInt(6)+""
                        };
                        dtm.addRow(row);
                    }
                    tv_result.setText("查询成功，共有"+dtm.getRowCount()+"条记录");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
        else{
            dtm = (DefaultTableModel) table.getModel();
            dtm.setColumnIdentifiers(tableHeadsAdmin);
            table.getTableHeader().setReorderingAllowed(false);
            table.setModel(dtm);
        }

        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dtm.setRowCount(0);

                if(!et_snoOrName.getText().equals("")){
                    //查询某个人的借书记录，通过连接zjz_Student , zjz_Book , zjz_BS ，三个表
                    String sql = "select  zjz_Student.Sno ,zjz_Student.Sname , zjz_Book.Bno , zjz_Book.Bname , Ano ,BSlendtime , BSbacktime , BScount " +
                            "from zjz_Student , zjz_Book , zjz_BS  " +
                            "where zjz_Student.Sno = zjz_BS.Sno and zjz_Book.Bno = zjz_BS.Bno and (zjz_Student.Sno = '"+et_snoOrName.getText() + "' or zjz_Student.Sname = '"+ et_snoOrName.getText() +"')";
                    try {
                        GlobalConn.sRerult =  GlobalConn.sStatement.executeQuery(sql);
                        while( GlobalConn.sRerult.next()){
                            String[] row = new String[]{
                                    GlobalConn.sRerult.getString(1) ,GlobalConn.sRerult.getString(2),GlobalConn.sRerult.getString(3),
                                    GlobalConn.sRerult.getString(4) ,GlobalConn.sRerult.getString(5),GlobalConn.sRerult.getString(6),
                                    GlobalConn.sRerult.getString(7),GlobalConn.sRerult.getInt(8)+""
                            };
                            dtm.addRow(row);
                        }
                        tv_result.setText("查询成功，共有"+dtm.getRowCount()+"条记录");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                else{
                    //查询所有人的借书记录，通过连接zjz_Student , zjz_Book , zjz_BS ，三个表
                    String sql = "select  zjz_Student.Sno ,zjz_Student.Sname , zjz_Book.Bno , zjz_Book.Bname , Ano ,BSlendtime , BSbacktime , BScount " +
                            "from zjz_Student , zjz_Book , zjz_BS  " +
                            "where zjz_Student.Sno = zjz_BS.Sno and zjz_Book.Bno = zjz_BS.Bno ";
                    try {
                        GlobalConn.sRerult =  GlobalConn.sStatement.executeQuery(sql);
                        while( GlobalConn.sRerult.next()){
                            String[] row = new String[]{
                                    GlobalConn.sRerult.getString(1) ,GlobalConn.sRerult.getString(2),GlobalConn.sRerult.getString(3),
                                    GlobalConn.sRerult.getString(4) ,GlobalConn.sRerult.getString(5),GlobalConn.sRerult.getString(6),
                                    GlobalConn.sRerult.getString(7),GlobalConn.sRerult.getInt(8)+""
                            };
                            dtm.addRow(row);
                        }
                        tv_result.setText("查询成功，共有"+dtm.getRowCount()+"条记录");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        btn_lend.addActionListener(new ActionListener() {//借书
            @Override
            public void actionPerformed(ActionEvent e) {


                try {
                    String sql = "select BlendCount from zjz_book where Bno = '"+et_lendBno.getText()+ "'";//查询要借的书的数量
                    int num = 0;
                    GlobalConn.sRerult =  GlobalConn.sStatement.executeQuery(sql);

                    if  (GlobalConn.sRerult.next()){
                      num= GlobalConn.sRerult.getInt(1);
                    }

                    if(num < Integer.parseInt(et_lend_count.getText())){
                        tv_result.setText("借书失败，书本数量不够");
                    }
                    else{
                        Calendar now = Calendar.getInstance();//生成现在的时间和还书时间
                        int year =  now.get(Calendar.YEAR);
                        int month = now.get(Calendar.MONTH)+1;
                        int day = now.get(Calendar.DAY_OF_MONTH);
                        String sYear =  String.format("%04d", year);

                        String sM = String.format("%02d",month);
                        System.out.print(sM+" "+month);
                        String sD = String.format("%02d",day);
                        String nowDate = sYear+sM+sD;
                        if(month == 12){
                            sM = "01";
                            sYear  =  String.format("%04d", year+1);
                        }
                        String endDate = sYear+sM+sD;
                        //插入借书记录
                        String sql2 = "insert into zjz_BS values ('"+et_lendSno.getText()+"','"+et_lendBno.getText()+"','"+GlobalConn.sAccount+"','"+nowDate+"','"+endDate+"',"+et_lend_count.getText()+")";
                        int k =  GlobalConn.sStatement.executeUpdate(sql2);
                        if(k>0){
                            //更新书本数量
                            String sql1 = "update zjz_Book set Blendcount  = "+String.valueOf(num-Integer.parseInt(et_lend_count.getText()) +" where Bno = '"+et_lendBno.getText()+"'");
                            int q =  GlobalConn.sStatement.executeUpdate(sql1);

                            if(q>0){
                                tv_result.setText("借书成功");
                            }
                            else{
                                tv_result.setText("借书失败");
                            }
                        }
                        else{
                            tv_result.setText("借书失败");
                        }

                    }
                } catch (Exception ex) {
                    tv_result.setText("借书失败");
                    ex.printStackTrace();
                }

            }
        });

        btn_back.addActionListener(new ActionListener() {//还书
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    //查询要还的书的借书数量
                    String sql = "select BSCount from zjz_BS where Bno = '"+et_backBno.getText()+ "' and Sno = '"+et_backSno.getText()+"'";
                    int num = 0;
                    GlobalConn.sRerult =  GlobalConn.sStatement.executeQuery(sql);

                    if  (!GlobalConn.sRerult.next()){
                       tv_result.setText("无借书记录");
                    }
                    else{
                        num = GlobalConn.sRerult.getInt(1);
                        if(Integer.parseInt(et_back_count.getText())>num){
                            tv_result.setText("还书失败,数量过多");
                        }else if(Integer.parseInt(et_back_count.getText()) == num){
                            //删除该借书记录
                            String sql1 = "delete from zjz_BS where Bno = '"+et_backBno.getText()+ "' and Sno = '"+et_backSno.getText()+"'";//删除还书记录
                            int k =  GlobalConn.sStatement.executeUpdate(sql1);
                            if(k>0){
                                //查询现有书的数目
                                String sql3 = "select BlendCount from zjz_book where Bno = '"+et_backBno.getText()+ "'";//现有书本数目
                                int num9 = 0;
                                GlobalConn.sRerult =  GlobalConn.sStatement.executeQuery(sql3);

                                if  (GlobalConn.sRerult.next()){
                                    num9= GlobalConn.sRerult.getInt(1);
                                    System.out.print(num9);
                                }
                                //更新书本表的相应记录
                                String sql2 = "update zjz_book set  BlendCount ="+String.valueOf(num9+Integer.parseInt(et_back_count.getText()))+"  where Bno = '"+et_backBno.getText()+ "'";
                                int i =  GlobalConn.sStatement.executeUpdate(sql2);
                                if(i>0){
                                    tv_result.setText("还书成功");
                                }else{
                                    tv_result.setText("还书失败");
                                }
                            }
                        }else{
                            //更新借书记录
                            String sql1 = "update  zjz_BS set BScount = "+String.valueOf(num-Integer.parseInt(et_back_count.getText()))+" where Bno = '"+et_backBno.getText()+ "' and Sno = '"+et_backSno.getText()+"'";//删除还书记录
                            int k =  GlobalConn.sStatement.executeUpdate(sql1);
                            if(k>0){
                                //查看现有书本的数目
                                String sql3 = "select BlendCount from zjz_book where Bno = '"+et_backBno.getText()+ "'";//现有书本数目
                                int num9 = 0;
                                GlobalConn.sRerult =  GlobalConn.sStatement.executeQuery(sql3);

                                if  (GlobalConn.sRerult.next()){
                                    num9= GlobalConn.sRerult.getInt(1);
                                }
                                //更新相应的书本信息
                                String sql2 = "update zjz_book set  BlendCount ="+String.valueOf(num9+Integer.parseInt(et_back_count.getText()))+"  where Bno = '"+et_backBno.getText()+ "'";
                                int i =  GlobalConn.sStatement.executeUpdate(sql2);
                                if(i>0){
                                    tv_result.setText("还书成功");
                                }else{
                                    tv_result.setText("还书失败");
                                }
                            }
                        }
                    }

                } catch (SQLException e1) {
                    tv_result.setText("还书失败");
                    e1.printStackTrace();
                }


            }
        });
    }

}
