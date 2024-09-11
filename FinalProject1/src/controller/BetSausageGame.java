package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.Random;

public class BetSausageGame extends JFrame {

    private JLabel[] slotLabels; // 用於顯示老虎機圖像的標籤
    private BufferedImage[] slotImages; // 存儲老虎機圖像
    private Timer spinTimer; // 設定計時器來處理動畫
    private Random random; // 隨機數生成器
    private static final int SPIN_DURATION = 2000; // 自定義旋轉持續時間（毫秒）

    public BetSausageGame() {
        setTitle("餃子老虎機");
        setSize(600, 400); // 設定視窗大小
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 設定關閉操作
        setLocationRelativeTo(null); // 將視窗置中

        random = new Random(); // 初始化隨機數生成器

        // 設定圖像
        loadSlotImages();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel infoLabel = new JLabel("按下「拉霸」來開始遊戲！", SwingConstants.CENTER);
        infoLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        panel.add(infoLabel, BorderLayout.NORTH);

        // 創建顯示老虎機圖像的標籤
        JPanel slotsPanel = new JPanel();
        slotsPanel.setLayout(new GridLayout(1, 3));
        slotLabels = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            slotLabels[i] = new JLabel(new ImageIcon(slotImages[0]));
            slotsPanel.add(slotLabels[i]);
        }
        panel.add(slotsPanel, BorderLayout.CENTER);

        // 創建旋轉按鈕
        JButton spinButton = new JButton("拉霸");
        spinButton.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSpin();
            }
        });
        panel.add(spinButton, BorderLayout.SOUTH);

        // 創建強制獲勝按鈕
        JButton forceWinButton = new JButton("強制三個一樣");
        forceWinButton.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        forceWinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forceWin();
            }
        });
        panel.add(forceWinButton, BorderLayout.NORTH);

        getContentPane().add(panel);
    }

    // 加載老虎機圖像
    private void loadSlotImages() {
        String[] imagePaths = {"image/small.jpg", "image/middle.jpg", "image/big.jpg"};
        slotImages = new BufferedImage[imagePaths.length];
        for (int i = 0; i < imagePaths.length; i++) {
            try {
                URL imgURL = getClass().getClassLoader().getResource(imagePaths[i]);
                if (imgURL != null) {
                    slotImages[i] = ImageIO.read(imgURL);
                } else {
                    throw new IOException("圖片未找到: " + imagePaths[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "無法載入老虎機圖像: " + e.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 開始旋轉動畫
    private void startSpin() {
        spinTimer = new Timer(100, new ActionListener() {
            private int spinsLeft = SPIN_DURATION / 100;

            @Override
            public void actionPerformed(ActionEvent e) {
                for (JLabel slotLabel : slotLabels) {
                    slotLabel.setIcon(new ImageIcon(slotImages[random.nextInt(slotImages.length)]));
                }
                if (--spinsLeft <= 0) {
                    spinTimer.stop();
                    checkWinCondition();
                }
            }
        });
        spinTimer.start();
    }

    // 強制三個一樣
    private void forceWin() {
        BufferedImage forcedImage = slotImages[random.nextInt(slotImages.length)];
        for (JLabel slotLabel : slotLabels) {
            slotLabel.setIcon(new ImageIcon(forcedImage));
        }
        checkWinCondition(); // 直接檢查是否贏
    }

    // 檢查是否獲勝
    private void checkWinCondition() {
        BufferedImage[] displayedImages = new BufferedImage[3];
        for (int i = 0; i < slotLabels.length; i++) {
            displayedImages[i] = (BufferedImage) ((ImageIcon) slotLabels[i].getIcon()).getImage();
        }

        // 計算每種圖像的數量
        int[] counts = new int[slotImages.length];
        for (BufferedImage image : displayedImages) {
            for (int i = 0; i < slotImages.length; i++) {
                if (image.equals(slotImages[i])) {
                    counts[i]++;
                }
            }
        }

        boolean allMatch = false;

        // 檢查三個相同的情況
        for (int count : counts) {
            if (count == 3) {
                allMatch = true;
                break;
            }
        }

        if (allMatch) {
            // 直接啟動餃子製作迷你遊戲
            SwingUtilities.invokeLater(() -> {
                DumplingMiniGame miniGame = new DumplingMiniGame();
                miniGame.setVisible(true);
            });
            // 關閉當前窗口
            SwingUtilities.invokeLater(this::dispose);
        } else {
            JOptionPane.showMessageDialog(this, "台子很硬喔。", "結果", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BetSausageGame gameFrame = new BetSausageGame();
            gameFrame.setVisible(true); // 顯示餃子老虎機遊戲視窗
        });
    }
}
