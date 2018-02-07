package main;

import javax.swing.*;

/**
 * Created by zjz on 2018/1/9.
 */
public class LoginView {
    public JPanel mJPanel;
    public static JFrame frame;
    public static JFrame frame1;

    public LoginView( )   {
        init();
    }

    private void init(){
        //选项卡
        JTabbedPane tabbedPane = new JTabbedPane();

        SearchBookView searchView = new SearchBookView();
        tabbedPane.addTab("查询图书", searchView.mJPanel);
        SearchBSView searchBSView = new SearchBSView();
        tabbedPane.addTab("借书还书", searchBSView.mJPanel);

        tabbedPane.setSelectedIndex(0);

        // 将选项卡添加到panl中
        mJPanel.add(tabbedPane);

    }

}
