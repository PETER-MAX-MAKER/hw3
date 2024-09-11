package controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.LoginUI.UserDatabase;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminUI extends JFrame {

    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton viewTreasuresButton;
    private JButton clearTreasuresButton;
    private JTextArea treasuresDisplay;
    private JButton logoutButton;

    public AdminUI() {
        setTitle("管理員介面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Initialize table model and table
        tableModel = new DefaultTableModel(new Object[]{"用戶名", "密碼"}, 0);
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getSelectionModel().addListSelectionListener(e -> loadSelectedUserData());

        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Panel containing operation buttons and input fields
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(5, 1));

        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new GridLayout(4, 2));

        operationPanel.add(new JLabel("用戶名稱:"));
        usernameField = new JTextField();
        operationPanel.add(usernameField);

        operationPanel.add(new JLabel("密碼:"));
        passwordField = new JPasswordField();
        operationPanel.add(passwordField);

        addButton = new JButton("添加用戶");
        updateButton = new JButton("更新用戶");
        deleteButton = new JButton("刪除用戶");
        refreshButton = new JButton("刷新數據");

        operationPanel.add(addButton);
        operationPanel.add(updateButton);
        operationPanel.add(deleteButton);
        controlPanel.add(operationPanel);

        controlPanel.add(refreshButton);

        // Panel for viewing and clearing treasures
        JPanel treasurePanel = new JPanel();
        treasurePanel.setLayout(new BorderLayout());

        viewTreasuresButton = new JButton("查看寶物");
        clearTreasuresButton = new JButton("薩諾斯之手");
        treasuresDisplay = new JTextArea(5, 30);
        treasuresDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(treasuresDisplay);

        JPanel viewClearPanel = new JPanel();
        viewClearPanel.setLayout(new BorderLayout());
        viewClearPanel.add(viewTreasuresButton, BorderLayout.NORTH);
        viewClearPanel.add(clearTreasuresButton, BorderLayout.SOUTH);
        
        treasurePanel.add(viewClearPanel, BorderLayout.NORTH);
        treasurePanel.add(scrollPane, BorderLayout.CENTER);

        // Button to return to login page
        logoutButton = new JButton("返回登入頁面");

        controlPanel.add(logoutButton);

        panel.add(controlPanel, BorderLayout.SOUTH);
        panel.add(treasurePanel, BorderLayout.EAST);

        // Add action listeners
        addButton.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());
        refreshButton.addActionListener(e -> refreshData());
        viewTreasuresButton.addActionListener(e -> {
            String username = usernameField.getText();
            if (!username.isEmpty()) {
                displayTreasures(username);
            } else {
                JOptionPane.showMessageDialog(AdminUI.this, "請輸入用戶名稱", "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearTreasuresButton.addActionListener(e -> {
            String username = usernameField.getText();
            if (!username.isEmpty()) {
                clearTreasures(username);
            } else {
                JOptionPane.showMessageDialog(AdminUI.this, "請輸入用戶名稱", "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginUI().setVisible(true);
        });

        getContentPane().add(panel);
        refreshData(); // Initialize UI with data
    }

    private void loadSelectedUserData() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            String username = (String) tableModel.getValueAt(selectedRow, 0);
            String password = (String) tableModel.getValueAt(selectedRow, 1);
            usernameField.setText(username);
            passwordField.setText(password);
        }
    }

    // Add user with uniqueness check
    private void addUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (!username.isEmpty() && !password.isEmpty()) {
            if (isUsernameExists(username)) {
                JOptionPane.showMessageDialog(this, "此帳戶已存在！");
                return;
            }

            try (Connection conn = DriverManager.getConnection(UserDatabase.DB_URL, UserDatabase.DB_USER, UserDatabase.DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "用戶添加成功！");
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "添加用戶失敗！");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "添加用戶失敗！");
            }
        } else {
            JOptionPane.showMessageDialog(this, "請輸入用戶名稱和密碼！");
        }
    }

    // Check if username already exists
    private boolean isUsernameExists(String username) {
        try (Connection conn = DriverManager.getConnection(UserDatabase.DB_URL, UserDatabase.DB_USER, UserDatabase.DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update user details
    private void updateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (!username.isEmpty() && !password.isEmpty()) {
            try (Connection conn = DriverManager.getConnection(UserDatabase.DB_URL, UserDatabase.DB_USER, UserDatabase.DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET password = ? WHERE username = ?")) {
                pstmt.setString(1, password);
                pstmt.setString(2, username);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "用戶信息更新成功！");
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "用戶不存在！");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "更新用戶信息失敗！");
            }
        } else {
            JOptionPane.showMessageDialog(this, "請輸入用戶名稱和密碼！");
        }
    }

    // Delete user
    private void deleteUser() {
        String username = usernameField.getText();

        if (!username.isEmpty()) {
            try (Connection conn = DriverManager.getConnection(UserDatabase.DB_URL, UserDatabase.DB_USER, UserDatabase.DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE username = ?")) {
                pstmt.setString(1, username);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "用戶刪除成功！");
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "用戶不存在！");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "刪除用戶失敗！");
            }
        } else {
            JOptionPane.showMessageDialog(this, "請輸入用戶名稱！");
        }
    }

    // Refresh user data
    private void refreshData() {
        try (Connection conn = DriverManager.getConnection(UserDatabase.DB_URL, UserDatabase.DB_USER, UserDatabase.DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username, password FROM users")) {

            tableModel.setRowCount(0); // Clear existing data
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("username"),
                    rs.getString("password")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "刷新數據失敗！");
        }
    }

    // Display user's treasures
    private void displayTreasures(String username) {
        try (Connection conn = DriverManager.getConnection(UserDatabase.DB_URL, UserDatabase.DB_USER, UserDatabase.DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT reward FROM draw_results WHERE username = ?")) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("寶物: ").append(rs.getString("reward")).append("\n");
            }
            treasuresDisplay.setText(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "查詢寶物失敗！");
        }
    }

    // Clear user's treasures
    private void clearTreasures(String username) {
        try (Connection conn = DriverManager.getConnection(UserDatabase.DB_URL, UserDatabase.DB_USER, UserDatabase.DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM draw_results WHERE username = ?")) {
            pstmt.setString(1, username);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "寶物清空成功！");
                treasuresDisplay.setText(""); // Clear display area
            } else {
                JOptionPane.showMessageDialog(this, "用戶不存在或寶物已經清空！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "清空寶物失敗！");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminUI adminUI = new AdminUI();
            adminUI.setVisible(true);
        });
    }
}
