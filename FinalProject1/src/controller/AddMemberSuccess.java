package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMemberSuccess extends JFrame {

    public AddMemberSuccess() {
        setTitle("帳號建立成功");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JLabel successLabel = new JLabel("帳號建立成功！", SwingConstants.CENTER);
        successLabel.setFont(new Font("宋体", Font.BOLD, 16)); // 設定字體
        panel.add(successLabel);

        JButton backToLoginButton = new JButton("返回登入頁面");
        backToLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginUI().setVisible(true); // 顯示登入頁面
                dispose(); // 關閉成功提示視窗
            }
        });
        panel.add(backToLoginButton);

        getContentPane().add(panel);
    }
}
