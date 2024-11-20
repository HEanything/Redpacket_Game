package Game0_6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginFrame() {
        setTitle("登录");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("密码:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("登录");
        messageLabel = new JLabel("");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(messageLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                if (validateLogin(username, new String(password))) {
                    messageLabel.setText("登录成功");
                    dispose(); // 关闭登录窗口
                    // 调用外部方法来处理游戏启动逻辑
                    GameStarter.startGame();
                } else {
                    messageLabel.setText("登录失败");
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    private boolean validateLogin(String username, String password) {
        return "admin".equals(username) && "123456".equals(password);
    }
}
