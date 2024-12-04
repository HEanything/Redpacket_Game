package Game0_8;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private GameFrame gameFrame;

    // 构造方法，接收 GameFrame 作为参数
    public LoginFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;

        // 设置窗口属性
        setTitle("登录");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 窗口居中显示

        // 创建主面板，使用 GridLayout 布局
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        // 添加用户名标签和输入框
        panel.add(new JLabel("用户名:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        // 添加密码标签和密码框
        panel.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // 添加消息标签，用于显示登录结果信息
        messageLabel = new JLabel("");
        panel.add(messageLabel);

        // 创建登录按钮，并添加事件监听器
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

        // 将面板添加到窗口
        add(panel);
        setVisible(true); // 显示窗口
    }

    // 简单的登录验证方法，实际应用中需要与数据库或其他数据源进行验证
    private boolean validateLogin(String username, String password) {
        // 这里使用硬编码的用户名和密码进行验证
        return "admin".equals(username) && "123456".equals(password);
    }

    // 显示确认对话框，询问用户是否开始游戏
    private void askToStartGame() {
        int result = JOptionPane.showConfirmDialog(this, "是否开始游戏？", "确认", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            gameFrame.startGame(); // 启动游戏
            dispose();  // 关闭登录窗口
        } else {
            messageLabel.setText("已取消登录");
        }
    }
}
