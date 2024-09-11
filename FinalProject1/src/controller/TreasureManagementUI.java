package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;

public class TreasureManagementUI extends JFrame {

    private JTextArea textArea;
    private String username;

    public TreasureManagementUI(String username) {
        this.username = username;
        setTitle("寶物管理介面");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        refreshData();

        getContentPane().add(panel);
    }

    private void refreshData() {
        try (Connection conn = DriverManager.getConnection(LoginUI.UserDatabase.DB_URL, LoginUI.UserDatabase.DB_USER, LoginUI.UserDatabase.DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT reward, draw_time FROM draw_results WHERE username = ?")) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("獎勳: ").append(rs.getString("reward"))
                      .append(" 日期: ").append(rs.getTimestamp("draw_time"))
                      .append("\n");
                }
                textArea.setText(sb.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "刷新數據失敗！");
        }
    }
}
