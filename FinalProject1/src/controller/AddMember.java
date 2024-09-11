package controller;

import javax.swing.*;


import controller.LoginUI.UserDatabase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMember extends JFrame {

    private JTextField newUserField;
    private JPasswordField newPasswordField;

    public AddMember() {
        setTitle("註冊新帳號");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(3, 2));

        JLabel newUserLabel = new JLabel("新使用者名稱:");
        newUserField = new JTextField();
        JLabel newPasswordLabel = new JLabel("新密碼:");
        newPasswordField = new JPasswordField();
        JButton createAccountButton = new JButton("建立帳號");

        registerPanel.add(newUserLabel);
        registerPanel.add(newUserField);
        registerPanel.add(newPasswordLabel);
        registerPanel.add(newPasswordField);
        registerPanel.add(new JLabel()); // 空白佔位
        registerPanel.add(createAccountButton);

        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newUsername = newUserField.getText();
                String newPassword = new String(newPasswordField.getPassword());

                // 獲取用戶資料庫實例
                if (UserDatabase.getInstance().getUserDatabase().containsKey(newUsername)) {
                    new AddMemberError().setVisible(true);
                } else {
                    UserDatabase.getInstance().getUserDatabase().put(newUsername, newPassword);
                    new AddMemberSuccess().setVisible(true);
                }
                dispose();
            }
        });

        getContentPane().add(registerPanel);
    }
}
