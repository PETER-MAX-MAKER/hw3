package controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.sql.*;
import java.util.HashMap;

public class LoginUI extends JFrame {

    private JTextField usernameField; // 使用者名稱輸入框
    private JPasswordField passwordField; // 密碼輸入框
    private JButton loginButton; // 登入按鈕
    private JButton registerButton; // 註冊按鈕
    private JButton adminButton; // 管理者登入按鈕
    private JLabel messageLabel; // 顯示訊息的標籤
    private JLabel timeLabel; // 時間顯示標籤
    private BufferedImage backgroundImage; // 背景圖片
    private SimpleDateFormat dateFormat; // 日期格式化

    public static class UserDatabase {

        public static final String DB_URL = "jdbc:mysql://localhost:3306/new_schema";
        public static final String DB_USER = "root";
        public static final String DB_PASSWORD = "1234";

        private static UserDatabase instance;

        private UserDatabase() {
            // 防止外部實例化
        }

        public static synchronized UserDatabase getInstance() {
            if (instance == null) {
                instance = new UserDatabase();
            }
            return instance;
        }

        public HashMap<String, String> getUserDatabase() {
            HashMap<String, String> userDatabase = new HashMap<>();
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT username, password FROM users")) {

                while (rs.next()) {
                    userDatabase.put(rs.getString("username"), rs.getString("password"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return userDatabase;
        }

        public boolean userExists(String username) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("SELECT 1 FROM users WHERE username = ?")) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public boolean addUser(String username, String password) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public LoginUI() {
        // 載入背景圖片
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/image/321.jpg")); // 圖片路徑
            if (backgroundImage == null) {
                throw new IOException("背景圖片未找到");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "無法載入背景圖片: " + e.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
        }

        // 設定 JFrame 基本參數
        setTitle("BlackMyth - Login"); // 設定視窗標題
        setSize(578, 517); // 設定視窗大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 設定關閉操作
        setLocationRelativeTo(null); // 將視窗置中

        // 設置日期格式
        dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

        // 建立登入面板，使用自訂背景
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 繪製背景圖片
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(null); // 設定面板佈局為空佈局

        // 使用者名稱標籤與輸入框
        JLabel userLabel = new JLabel("username:");
        userLabel.setFont(new Font("宋體", Font.BOLD, 18));
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userLabel.setForeground(new Color(255, 0, 255));
        userLabel.setBounds(10, 50, 140, 30);
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 50, 150, 30);
        panel.add(usernameField);

        // 密碼標籤與密碼框
        JLabel passwordLabel = new JLabel("password:");
        passwordLabel.setFont(new Font("宋體", Font.BOLD, 18));
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordLabel.setForeground(new Color(255, 0, 255));
        passwordLabel.setBounds(28, 100, 100, 30);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 100, 150, 30);
        panel.add(passwordField);

        // 登入按鈕
        loginButton = new JButton("login");
        loginButton.setBounds(30, 150, 100, 30);
        panel.add(loginButton);

        // 註冊按鈕
        registerButton = new JButton("register");
        registerButton.setBounds(140, 150, 100, 30);
        panel.add(registerButton);

        // 管理者登入按鈕
        adminButton = new JButton("adminlogin");
        adminButton.setBounds(250, 150, 130, 30);
        panel.add(adminButton);

        // 顯示訊息的標籤
        messageLabel = new JLabel("");
        messageLabel.setBounds(100, 250, 250, 30);
        messageLabel.setForeground(Color.RED);
        panel.add(messageLabel);

        // 添加顯示時間的標籤
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setBounds(10, 10, 370, 30);
        panel.add(timeLabel);

        // 設定定時器以更新時間顯示
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClock();
            }
        });
        timer.start();

        // 登入按鈕事件監聽器
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });

        // 註冊按鈕事件監聽器
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRegisterScreen();
            }
        });

        // 管理者登入按鈕事件監聽器
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAdminLoginDialog();
            }
        });

        // 將面板加入到 JFrame
        getContentPane().add(panel);
    }

    // 更新時鐘顯示
    private void updateClock() {
        Date now = new Date();
        String timeText = dateFormat.format(now);
        timeLabel.setText("Current time: " + timeText);
    }

    // 登入動作
    private void loginAction() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // 驗證帳號和密碼
        HashMap<String, String> userDatabase = UserDatabase.getInstance().getUserDatabase();
        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            messageLabel.setText("登入成功！");
            // 登入成功，顯示 LoginSuccess 介面
            LoginSuccess successFrame = new LoginSuccess(username);
            successFrame.setVisible(true);
            this.dispose(); // 關閉當前登入介面
        } else {
            messageLabel.setText("使用者名稱或密碼錯誤！");
        }
    }

    // 顯示註冊畫面
    private void showRegisterScreen() {
        this.setVisible(false); // 隱藏當前登入畫面
        RegisterUI registerUI = new RegisterUI();
        registerUI.setVisible(true);
    }

    // 顯示管理者登入對話框
    private void showAdminLoginDialog() {
        JPanel adminPanel = new JPanel(new GridLayout(2, 2));
        adminPanel.add(new JLabel("帳號:"));
        JTextField adminUsernameField = new JTextField();
        adminPanel.add(adminUsernameField);
        adminPanel.add(new JLabel("密碼:"));
        JPasswordField adminPasswordField = new JPasswordField();
        adminPanel.add(adminPasswordField);

        int result = JOptionPane.showConfirmDialog(this, adminPanel, "管理者登入", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String adminUsername = adminUsernameField.getText();
            String adminPassword = new String(adminPasswordField.getPassword());
            if ("admin123".equals(adminUsername) && "admin123".equals(adminPassword)) {
                JOptionPane.showMessageDialog(this, "管理者登入成功！");
                // 顯示 AdminUI
                AdminUI adminUI = new AdminUI();
                adminUI.setVisible(true);
                this.dispose(); // 關閉當前登入介面
            } else {
                JOptionPane.showMessageDialog(this, "管理者登入失敗！", "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginUI loginUI = new LoginUI();
            loginUI.setVisible(true);
        });
    }
}
