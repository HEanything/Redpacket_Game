package Game0_7;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private GameFrame gameFrame;

    public LoginFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setTitle("登录");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        panel.add(new JLabel("用户名:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        messageLabel = new JLabel("");
        panel.add(messageLabel);

        loginButton = new JButton("登录");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                if (validateLogin(username, new String(password))) {
                    messageLabel.setText("登录成功");
                    askToStartGame();
                } else {
                    messageLabel.setText("登录失败");
                }
            }
        });
        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    private boolean validateLogin(String username, String password) {
        // 这里可以添加实际的登录验证逻辑
        // 例如，从数据库中查询用户名和密码
        return "admin".equals(username) && "123456".equals(password);
    }

    private void askToStartGame() {
        int result = JOptionPane.showConfirmDialog(this, "是否开始游戏？", "确认", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            gameFrame.startGame();
            dispose();  // 关闭登录窗口
        } else {
            messageLabel.setText("已取消登录");
        }
    }
}

