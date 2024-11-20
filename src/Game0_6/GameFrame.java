package Game0_6;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameFrame extends JFrame {
    private boolean gameStarted = false;
    private List<RedPacket> redPackets = new ArrayList<>();
    private Plane plane;
    private Timer timer;
    private int score = 0;
    private int money = 0;

    public GameFrame() {
        setTitle("红包大战");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        plane = new Plane();

        timer = new Timer(100, e -> update());
        timer.start();

        init();
    }

    private void init() {
        // 初始化游戏窗口，但不显示
        setVisible(false);
    }

    private void update() {
        if (gameStarted) {
            // 更新游戏状态
            for (RedPacket packet : redPackets) {
                packet.move();
                if (plane.collidesWith(packet)) {
                    score++;
                    money += packet.getValue();
                    redPackets.remove(packet);
                }
            }

            // 生成新的红包
            if (new Random().nextInt(100) < 5) {
                redPackets.add(new RedPacket());
            }

            // 重绘游戏界面
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (gameStarted) {
            // 绘制游戏元素
            plane.draw(g);
            for (RedPacket packet : redPackets) {
                packet.draw(g);
            }

            // 绘制分数和金钱
            g.drawString("Score: " + score, 10, 20);
            g.drawString("Money: " + money, 10, 40);
        }
    }

    public void startGame() {
        gameStarted = true;
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
