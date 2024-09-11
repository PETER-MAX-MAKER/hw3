package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PreLoginUI extends JFrame {

    private static final int MIN_FONT_SIZE = 18; // 最小字體大小
    private static final int MAX_FONT_SIZE = 36; // 最大字體大小
    private static final int FONT_STEP = 2; // 每次變化的字體大小
    private static final int TIMER_DELAY = 500; // 字體變化的間隔（毫秒）
    
  
    
    
    private static final int CLOCK_UPDATE_INTERVAL = 1000; // 時鐘更新間隔（毫秒）
    private static final int ONLINE_PLAYERS_UPDATE_INTERVAL = 1000; // 在線玩家數更新間隔（毫秒）

    private JLabel messageLabel;
    private JLabel clockLabel;
    private JLabel onlinePlayersLabel;
    private Timer timer;
    private Timer clockTimer;
    private Timer onlinePlayersTimer;
    private boolean increasing = true;
    private Random random = new Random();

    private int onlinePlayers = 20240913; // 初始在線玩家數

    public PreLoginUI() {
        setTitle("啟動畫面");
        setSize(800, 600); // 設定適合的大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 創建自定義面板來繪製背景圖像
        BackgroundPanel backgroundPanel = new BackgroundPanel("/image/32.gif");

        // 創建顯示提示文本的 JLabel
        messageLabel = new JLabel("Please press enter to play the game", SwingConstants.CENTER);
        messageLabel.setFont(new Font("微軟正黑體", Font.BOLD, MIN_FONT_SIZE));
        messageLabel.setForeground(Color.WHITE); // 設置文本顏色以便與背景顯示清晰

        // 創建顯示時鐘的 JLabel
        clockLabel = new JLabel("", SwingConstants.CENTER);
        clockLabel.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        clockLabel.setForeground(Color.WHITE);

        // 創建顯示在線玩家數的 JLabel
        onlinePlayersLabel = new JLabel("線上玩家: " + onlinePlayers, SwingConstants.CENTER);
        onlinePlayersLabel.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        onlinePlayersLabel.setForeground(Color.WHITE);

        // 將提示文本、時鐘和在線玩家數添加到背景面板上
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false); // 使底部面板透明
        bottomPanel.add(clockLabel, BorderLayout.WEST);
        bottomPanel.add(onlinePlayersLabel, BorderLayout.EAST);
        
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 創建定時器來調整字體大小
        timer = new Timer(TIMER_DELAY, e -> {
            // 根據當前字體大小變化放大或縮小
            Font currentFont = messageLabel.getFont();
            int newSize = currentFont.getSize() + (increasing ? FONT_STEP : -FONT_STEP);

            if (newSize >= MAX_FONT_SIZE) {
                newSize = MAX_FONT_SIZE;
                increasing = false;
            } else if (newSize <= MIN_FONT_SIZE) {
                newSize = MIN_FONT_SIZE;
                increasing = true;
            }

            messageLabel.setFont(new Font(currentFont.getFontName(), Font.BOLD, newSize));
        });
        timer.start();

        // 創建定時器來更新時鐘
        clockTimer = new Timer(CLOCK_UPDATE_INTERVAL, e -> {
            clockLabel.setText(getCurrentTime());
        });
        clockTimer.start();

        // 創建定時器來更新在線玩家數
        onlinePlayersTimer = new Timer(ONLINE_PLAYERS_UPDATE_INTERVAL, e -> {
            onlinePlayers += random.nextInt(401) - 200; // 隨機增加或減少1到200
            onlinePlayersLabel.setText("線上玩家: " + onlinePlayers);
        });
        onlinePlayersTimer.start();

        // 監聽 Enter 鍵事件
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // 當按下 Enter 鍵時，跳轉到 LoginUI
                    timer.stop(); // 停止字體動畫
                    clockTimer.stop(); // 停止時鐘更新
                    onlinePlayersTimer.stop(); // 停止在線玩家數更新
                    dispose(); // 關閉當前畫面
                    new LoginUI().setVisible(true); // 顯示 LoginUI
                }
            }
        });

        // 添加背景面板到框架
        getContentPane().add(backgroundPanel);
        setFocusable(true); // 確保畫面可以獲取焦點
    }

    // 獲取當前時間的字符串，包括年月日
    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter); // 格式化為 yyyy-MM-dd HH:mm:ss
    }

    // 自定義面板類用於繪製背景圖像
    private static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            // 加載背景圖像
            backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // 繪製背景圖像
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PreLoginUI preLoginUI = new PreLoginUI();
            preLoginUI.setVisible(true);
        });
    }
}
