package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DumplingMiniGame extends JFrame {

    private int dumplingsMade = 0;
    private JLabel dumplingLabel;
    private JLabel timeLabel;
    private Timer gameTimer;
    private static final int GAME_DURATION = 15000; // 遊戲時長（毫秒）

    public DumplingMiniGame() {
        setTitle("餃子製作迷你遊戲");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 創建顯示餃子數量的標籤
        dumplingLabel = new JLabel("製作餃子: 0", SwingConstants.CENTER);
        dumplingLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        panel.add(dumplingLabel, BorderLayout.NORTH);

        // 創建顯示剩餘時間的標籤
        timeLabel = new JLabel("時間剩餘: 15秒", SwingConstants.CENTER);
        timeLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        panel.add(timeLabel, BorderLayout.SOUTH);

        JButton makeDumplingButton = new JButton("包餃子");
        makeDumplingButton.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        makeDumplingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeDumpling();
            }
        });
        panel.add(makeDumplingButton, BorderLayout.CENTER);

        getContentPane().add(panel);

        // 設定計時器來結束遊戲
        gameTimer = new Timer(GAME_DURATION, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameOver();
            }
        });
        gameTimer.setRepeats(false);
        gameTimer.start();

        // 設定倒數計時器
        Timer countdownTimer = new Timer(1000, new ActionListener() {
            private int timeRemaining = GAME_DURATION / 1000;

            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timeLabel.setText("時間剩餘: " + timeRemaining + "秒");
                if (timeRemaining <= 0) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        countdownTimer.start();
    }

    private void makeDumpling() {
        dumplingsMade++;
        dumplingLabel.setText("製作餃子: " + dumplingsMade);
    }

    private void gameOver() {
        JOptionPane.showMessageDialog(this, "遊戲結束！您包了 " + dumplingsMade + " 顆餃子！", "結果", JOptionPane.INFORMATION_MESSAGE);
        dispose(); // 關閉餃子製作迷你遊戲視窗
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DumplingMiniGame gameFrame = new DumplingMiniGame();
            gameFrame.setVisible(true); // 顯示餃子製作迷你遊戲視窗
        });
    }
}
