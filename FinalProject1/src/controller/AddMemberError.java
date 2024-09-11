package controller;

import javax.swing.*;
import java.awt.*;

public class AddMemberError extends JFrame {

    public AddMemberError() {
        setTitle("註冊失敗");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel messageLabel = new JLabel("該使用者名稱已被使用，請選擇其他名稱。");
        messageLabel.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        getContentPane().add(messageLabel);
    }
}

