package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class RegisterUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JButton registerButton;
    private BufferedImage backgroundImage;

    public RegisterUI() {
        // Load background image
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/image/mo.jpg")); // Ensure the image is in src/image folder
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unable to load background image!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Set frame properties
        setTitle("User Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Use GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Background image label
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setLayout(new GridBagLayout());
        getContentPane().add(backgroundLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        usernameField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 15));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(passwordField, gbc);

        registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(registerButton, gbc);

        JButton returnButton = new JButton("Return");
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(returnButton, gbc);

        messageLabel = new JLabel("");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        panel.add(messageLabel, gbc);

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (!username.isEmpty() && !password.isEmpty()) {
                    if (LoginUI.UserDatabase.getInstance().userExists(username)) {
                        messageLabel.setText("Username already exists!");
                    } else {
                        if (LoginUI.UserDatabase.getInstance().addUser(username, password)) {
                            messageLabel.setText("Registration successful!");
                        } else {
                            messageLabel.setText("Registration failed!");
                        }
                    }
                } else {
                    messageLabel.setText("Please enter all fields!");
                }
            }
        });

        // Return button action
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginUI loginUI = new LoginUI(); // Create login interface
                loginUI.setVisible(true); // Show login page
                dispose(); // Close current registration window
            }
        });

        backgroundLabel.add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegisterUI registerUI = new RegisterUI();
                registerUI.setVisible(true);
            }
        });
    }
}
