package controller;

import javax.swing.*;

import java.awt.*;

public class LoginError extends JFrame {

    public LoginError() {
        setTitle("登入失敗");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel messageLabel = new JLabel("帳號或密碼錯誤，請重新嘗試。");
        messageLabel.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        getContentPane().add(messageLabel);
    }
}
