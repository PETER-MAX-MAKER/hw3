package controller;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LoginSuccess extends JFrame {

    private BufferedImage backgroundImage; // 背景圖片
    private JTextArea drawResultArea; // 顯示抽獎結果的區域
    private JButton drawButton; // 十連抽按鈕
    private JButton multiDrawButton; // 多次十連抽按鈕
    private JButton backButton; // 返回按鈕
    private String username; // 當前登入的用戶名
    private JLabel gifLabel; // 顯示GIF動畫的標籤
    private JTextArea intermediateResultArea; // 中間顯示抽獎結果的區域
    private JLabel timeLabel; // 時間顯示標籤
    private SimpleDateFormat dateFormat; // 日期格式化

    // MySQL 連線資訊
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_schema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public LoginSuccess(String username) {
        this.username = username; // 設定當前登入用戶

        setTitle("黑悟空 - 遊戲畫面");
        setSize(600, 508); // 設定視窗大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 設定關閉操作
        setLocationRelativeTo(null); // 將視窗置中

        // 設定日期格式
        dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

        // 載入遊戲背景圖片
        try {
            URL imageUrl = getClass().getResource("/image/12345.jpg"); // 圖片路徑
            if (imageUrl == null) {
                throw new IOException("遊戲背景圖片未找到");
            }
            backgroundImage = ImageIO.read(imageUrl); // 讀取圖片
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "無法載入遊戲背景圖片: " + e.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
        }

        // 創建遊戲面板並設定背景圖片
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 繪製背景圖片
                }
            }
        };
        panel.setLayout(new BorderLayout());

        // 創建遊戲介面的標籤
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("歡迎來到黑悟空的世界，" + username + "！");
        welcomeLabel.setFont(new Font("微軟正黑體", Font.BOLD, 24)); // 設定字體
        welcomeLabel.setForeground(new Color(128, 0, 64)); // 設定文字顏色為黃色
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        // 創建時鐘顯示標籤
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("微軟正黑體", Font.PLAIN, 14)); // 設定字體
        timeLabel.setForeground(new Color(0, 0, 139)); // 設定文字顏色為深藍色 (RGB 值)
        headerPanel.add(timeLabel, BorderLayout.EAST); // 時間顯示在標籤的右側

        panel.add(headerPanel, BorderLayout.NORTH);

        // 顯示抽獎結果區域
        drawResultArea = new JTextArea();
        drawResultArea.setEditable(false); // 結果區不可編輯
        drawResultArea.setFont(new Font("微軟正黑體", Font.PLAIN, 16)); // 設定字體
        drawResultArea.setForeground(Color.YELLOW); // 設定文字顏色為黃色
        drawResultArea.setBackground(Color.BLACK); // 設定背景顏色
        panel.add(new JScrollPane(drawResultArea), BorderLayout.EAST);

        // 中間顯示區域的面板
        JPanel centerPanel = new JPanel(new BorderLayout());

        // 顯示GIF動畫的區域
        gifLabel = new JLabel();
        URL gifUrl = getClass().getResource("/image/prpr.gif"); // GIF 路徑
        if (gifUrl != null) {
            gifLabel.setIcon(new ImageIcon(gifUrl));
            gifLabel.setPreferredSize(new Dimension(200, 100)); // 設定 GIF 的顯示大小
            gifLabel.setVisible(false); // 預設隱藏
        } else {
            System.out.println("GIF檔案未找到");
        }
        centerPanel.add(gifLabel, BorderLayout.NORTH); // GIF動畫顯示在北部

        // 中間顯示抽獎結果的區域
        intermediateResultArea = new JTextArea();
        intermediateResultArea.setEditable(false); // 結果區不可編輯
        intermediateResultArea.setFont(new Font("微軟正黑體", Font.PLAIN, 16)); // 設定字體
        intermediateResultArea.setForeground(Color.YELLOW); // 設定文字顏色為黃色
        intermediateResultArea.setBackground(Color.BLACK); // 設定背景顏色
        intermediateResultArea.setVisible(false); // 預設隱藏
        centerPanel.add(new JScrollPane(intermediateResultArea), BorderLayout.CENTER); // 抽獎結果顯示在中部

        panel.add(centerPanel, BorderLayout.CENTER); // 把中間面板添加到主面板中

        // 創建十連抽按鈕
        drawButton = new JButton("十連抽");
        drawButton.setFont(new Font("微軟正黑體", Font.BOLD, 18)); // 設定字體
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 顯示GIF動畫並啟動新線程執行十連抽
                gifLabel.setVisible(true);
                intermediateResultArea.setVisible(true); // 顯示中間結果區域
                new Thread(LoginSuccess.this::performTenDraws).start();
            }
        });

        // 創建多次十連抽按鈕
        multiDrawButton = new JButton("多次十連抽直到抽中四個相同的");
        multiDrawButton.setFont(new Font("微軟正黑體", Font.BOLD, 18)); // 設定字體
        multiDrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 顯示GIF動畫並啟動新線程執行多次十連抽
                gifLabel.setVisible(true);
                intermediateResultArea.setVisible(true); // 顯示中間結果區域
                new Thread(LoginSuccess.this::performMultipleTenDraws).start();
            }
        });

        // 創建返回按鈕
        backButton = new JButton("返回");
        backButton.setFont(new Font("微軟正黑體", Font.BOLD, 18)); // 設定字體
        backButton.addActionListener(e -> {
            // 返回到登入頁面
            SwingUtilities.invokeLater(() -> {
                LoginUI loginFrame = new LoginUI();
                loginFrame.setVisible(true);
                dispose(); // 關閉當前視窗
            });
        });

        JPanel buttonPanel = new JPanel(); // 按鈕面板
        buttonPanel.add(drawButton);
        buttonPanel.add(multiDrawButton);
        buttonPanel.add(backButton); // 添加返回按鈕
        panel.add(buttonPanel, BorderLayout.SOUTH); // 添加按鈕面板到主面板的南部

        // 設定定時器以更新時間顯示
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClock();
            }
        });
        timer.start(); // 開始定時器

        // 將面板加入到窗口中
        getContentPane().add(panel);
    }

    // 更新時鐘顯示
    private void updateClock() {
        String timeText = dateFormat.format(new java.util.Date());
        timeLabel.setText("當前時間: " + timeText);
    }

    // 執行十連抽
    private void performTenDraws() {
        // 模擬十連抽
        String[] rewards = {"金幣", "鑽石", "角色碎片", "高級武器", "稀有道具", "技能書", "高級坐騎"};
        ArrayList<String> result = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            // 隨機選擇一個獎品
            String reward = rewards[rand.nextInt(rewards.length)];
            result.add("抽中: " + reward);
            
            try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // 儲存到資料庫
            saveDrawResultToDatabase(reward);

            // 更新中間結果區域
            String intermediateResult = "第" + (i + 1) + "次抽中: " + reward;
            SwingUtilities.invokeLater(() -> {
                intermediateResultArea.append(intermediateResult + "\n");
            });
        }

        // 更新結果區
        SwingUtilities.invokeLater(() -> {
            drawResultArea.setText(""); // 清空之前的結果
            for (String res : result) {
                drawResultArea.append(res + "\n"); // 顯示抽獎結果
            }
        });
        
        // 隱藏GIF動畫
        SwingUtilities.invokeLater(() -> gifLabel.setVisible(false));
    }

    // 執行多次十連抽直到抽到四個相同的獎品
    private void performMultipleTenDraws() {
        boolean foundFourSame = false;

        while (!foundFourSame) {
            // 模擬十連抽
            String[] rewards = {"金幣", "鑽石", "角色碎片", "高級武器", "稀有道具", "技能書", "高級坐騎"};
            ArrayList<String> result = new ArrayList<>();
            HashMap<String, Integer> rewardCount = new HashMap<>();
            Random rand = new Random();

            for (int i = 0; i < 10; i++) {
                // 隨機選擇一個獎品
                String reward = rewards[rand.nextInt(rewards.length)];
                result.add("抽中: " + reward);

                // 更新計數
                rewardCount.put(reward, rewardCount.getOrDefault(reward, 0) + 1);

                // 儲存到資料庫
                saveDrawResultToDatabase(reward);

                // 更新中間結果區域
                String intermediateResult = "第" + (i + 1) + "次抽中: " + reward;
                SwingUtilities.invokeLater(() -> {
                    intermediateResultArea.append(intermediateResult + "\n");
                });
            }

            // 檢查是否有四個一樣的獎品
            foundFourSame = rewardCount.values().stream().anyMatch(count -> count >= 4);

            // 如果找到了，結束循環
            if (foundFourSame) {
                int response = JOptionPane.showConfirmDialog(this, "恭喜！你抽中四個相同的獎品！是否要玩香腸老虎機？", "獎勳通知", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    // 顯示賭香腸遊戲
                    SwingUtilities.invokeLater(() -> {
                        BetSausageGame gameFrame = new BetSausageGame();
                        gameFrame.setVisible(true);
                    });
                    SwingUtilities.invokeLater(this::dispose); // 關閉當前窗口
                }
                // 如果選擇 NO，則什麼都不做
            }

            // 更新結果區
            SwingUtilities.invokeLater(() -> {
                drawResultArea.setText(""); // 清空之前的結果
                for (String res : result) {
                    drawResultArea.append(res + "\n"); // 顯示抽獎結果
                }
            });

            // 模擬稍微的延遲，以便用戶看到每次抽獎的結果
            try {
                Thread.sleep(1000); // 1秒延遲
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 隱藏GIF動畫
        SwingUtilities.invokeLater(() -> gifLabel.setVisible(false));
    }

    // 儲存抽獎結果到資料庫
    private void saveDrawResultToDatabase(String reward) {
        String query = "INSERT INTO draw_results (username, reward, draw_time) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, reward);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // 當前時間
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("成功插入資料: 用戶名 = " + username + ", 獎品 = " + reward);
            } else {
                System.out.println("未插入資料: 用戶名 = " + username + ", 獎品 = " + reward);
            }
        } catch (SQLException e) {
            System.err.println("資料庫操作失敗: " + e.getMessage());
            e.printStackTrace(); // 印出堆疊跟蹤以幫助調試
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginSuccess successFrame = new LoginSuccess("goku");
            successFrame.setVisible(true); // 顯示登入成功的遊戲視窗
        });
    }
}
