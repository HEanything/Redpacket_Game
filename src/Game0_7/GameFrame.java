package Game0_7;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import javax.imageio.ImageIO;
import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameFrame extends Frame {
    private Image bgImg;  // 背景图像
    private Image planeImg;  // 飞机图像
    private Image redPocketImg;  // 红包图像
    private int x = 275, y = 275;  // 飞机的初始坐标
    private boolean left, right, up, down;  // 飞机运动方向标志

    private List<RedPacket> redPackets;  // 存储所有红包
    private int redPacketCount = 0;  // 抢到的红包数量
    private int totalAmount = 0;  // 抢到的总金额
    private long startTime;  // 游戏开始时间
    private long endTime = 20000;  // 游戏持续时间（毫秒）
    private boolean gameOver = false;  // 游戏是否结束
    private boolean gameStarted = false;  // 游戏是否已经开始

    // 游戏入口
    public static void main(String[] args) {
        GameFrame gameFrame = new GameFrame();
        gameFrame.init();
    }

    // 构造方法，初始化资源和集合
    public GameFrame() {
        this.bgImg = loadImage("../images/bg.jpg");  // 加载背景图像
        this.planeImg = loadImage("../images/nets.jpg");  // 加载网图像
        this.redPocketImg = loadImage("../images/redpocket2.png");  // 加载红包图像
        this.redPackets = new ArrayList<>();  // 初始化红包集合
        this.startTime = System.currentTimeMillis();  // 记录游戏开始时间
    }

    // 初始化游戏窗口和游戏对象
    public void init() {
        // 设置窗口属性
        setTitle("抢红包游戏");
        setSize(600, 600);
        setLocation(500, 100);
        setVisible(false);  // 初始时不显示游戏窗口

        // 窗口关闭时退出程序
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // 初始化红包
        initializeRedPackets();

        // 添加键盘监听器，监听按键事件
        addKeyListener(new KeyMonitor());

        // 启动登录窗口
        new LoginFrame(this);
    }

    // 开始游戏的方法
    public void startGame() {
        gameStarted = true;
        setVisible(true);
        new Thread(new GameLoop()).start();
    }

    // 初始化红包的集合，默认50个
    private void initializeRedPackets() {
        for (int i = 0; i < 50; i++) {
            redPackets.add(new RedPacket(200, 400, Math.random() * Math.PI * 2, (int) (Math.random() * 100) + 1));
        }
    }

    // 加载图像资源的方法
    private Image loadImage(String imagePath) {
        URL imageURL = getClass().getResource(imagePath);
        if (imageURL != null) {
            try {
                return ImageIO.read(imageURL);  // 返回加载的图像
            } catch (IOException e) {
                throw new RuntimeException("Error loading image: " + imagePath, e);
            }
        }
        System.err.println("Image not found: " + imagePath);  // 如果图像未找到，打印错误信息
        return null;
    }

    // 绘制游戏界面的方法
    @Override
    public void update(Graphics g) {
        if (!gameStarted) return;  // 如果游戏没有开始，不绘制界面

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        Graphics gOffScreen = bs.getDrawGraphics();
        gOffScreen.clearRect(0, 0, getWidth(), getHeight());

        // 绘制背景和飞机
        if (bgImg != null) {
            gOffScreen.drawImage(bgImg, 0, 0, getWidth(), getHeight(), null);
        }
        if (planeImg != null && !gameOver) {
            gOffScreen.drawImage(planeImg, x, y, 50, 50, null);
        }

        // 根据方向标志更新飞机位置，并进行边界判断
        if (!gameOver) {
            if (left && x > 0) x -= 5;
            if (right && x < getWidth() - 50) x += 5;
            if (up && y > 0) y -= 5;
            if (down && y < getHeight() - 50) y += 5;
        }

        // 绘制并更新红包
        if (!gameOver) {
            Iterator<RedPacket> iterator = redPackets.iterator();
            while (iterator.hasNext()) {
                RedPacket packet = iterator.next();
                if (redPocketImg != null) {
                    gOffScreen.drawImage(redPocketImg, packet.getX(), packet.getY(), 30, 45, null);  // 绘制红包
                }
                packet.update(getWidth(), getHeight());  // 更新红包的位置

                // 碰撞检测
                if (isCollision(x, y, packet.getX(), packet.getY())) {
                    redPacketCount++;
                    totalAmount += packet.getAmount();
                    iterator.remove();  // 移除被抢到的红包
                }
            }
        }

        // 显示抢到的红包数量和金额
        gOffScreen.setColor(Color.red);
        gOffScreen.drawString("抢到红包: " + redPacketCount, 10, 20);
        gOffScreen.drawString("总金额: " + totalAmount, 10, 40);

        // 计算剩余时间并显示
        long remainingTime = endTime - (System.currentTimeMillis() - startTime);
        if (remainingTime > 0) {
            gOffScreen.drawString("剩余时间: " + (remainingTime / 1000) + " 秒", 10, 60);
        } else {
            gameOver = true;
            Font largeFont = new Font("Dialog", Font.BOLD, 40);  // 创建大号字体
            gOffScreen.setFont(largeFont);

            // 计算文本的宽度和高度
            FontMetrics fm = gOffScreen.getFontMetrics();
            String gameOverText = "游戏结束";
            String redPacketCountText = "抢到红包: " + redPacketCount;
            String totalAmountText = "总金额: " + totalAmount;
            String restartText = "按 R 重新开始";

            int gameOverWidth = fm.stringWidth(gameOverText);
            int redPacketCountWidth = fm.stringWidth(redPacketCountText);
            int totalAmountWidth = fm.stringWidth(totalAmountText);
            int restartWidth = fm.stringWidth(restartText);

            // 计算文本的起始位置
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            // 绘制文本
            gOffScreen.drawString(gameOverText, centerX - gameOverWidth / 2, centerY - 60);
            gOffScreen.drawString(redPacketCountText, centerX - redPacketCountWidth / 2, centerY - 20);
            gOffScreen.drawString(totalAmountText, centerX - totalAmountWidth / 2, centerY + 20);
            gOffScreen.drawString(restartText, centerX - restartWidth / 2, centerY + 60);
        }

        // 完成绘制的图像
        bs.show();
        gOffScreen.dispose();
    }

    // 碰撞检测方法
    private boolean isCollision(int x1, int y1, int x2, int y2) {
        int distance = (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        return distance < 30;  // 判断两个圆心之间的距离是否小于30
    }

    // 游戏主循环，控制游戏的更新和渲染
    private class GameLoop implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (!gameOver) {
                    repaint();  // 每帧都重绘游戏界面
                } else {
                    try {
                        Thread.sleep(100);  // 游戏结束时降低刷新频率
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(30);  // 让线程休眠30毫秒，控制帧率
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 红包类，表示一个红包
    private class RedPacket {
        private int x, y;  // 红包的位置
        private double degree;  // 红包的运动角度
        private int amount;  // 红包的金额

        // 构造方法，设置红包的初始位置、运动角度和金额
        public RedPacket(int startX, int startY, double degree, int amount) {
            this.x = startX;
            this.y = startY;
            this.degree = degree;
            this.amount = amount;
        }

        // 更新红包位置的方法
        public void update(int width, int height) {
            this.x += 7 * Math.cos(degree);  // 更新x坐标
            this.y += 7 * Math.sin(degree);  // 更新y坐标

            // 如果红包碰到边界，则反弹
            if (y > height - 10 || y < 10) {
                degree = -degree;  // 反弹后改变角度
            }
            if (x > width - 10 || x < 10) {
                degree = Math.PI - degree;  // 反弹后改变角度
            }

            // 确保红包不卡在边界处
            if (x < 10) x = 10;
            if (x > width - 10) x = width - 10;
            if (y < 10) y = 10;
            if (y > height - 10) y = height - 10;
        }

        // 获取红包的x坐标
        public int getX() {
            return x;
        }

        // 获取红包的y坐标
        public int getY() {
            return y;
        }

        // 获取红包的金额
        public int getAmount() {
            return amount;
        }
    }

    // 键盘监听器，处理按键事件
    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // 根据按键控制飞机的运动
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: left = true; break;
                case KeyEvent.VK_RIGHT: right = true; break;
                case KeyEvent.VK_UP: up = true; break;
                case KeyEvent.VK_DOWN: down = true; break;
                case KeyEvent.VK_R:
                    if (gameOver) {
                        restartGame();
                    }
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // 释放按键时停止飞机运动
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: left = false; break;
                case KeyEvent.VK_RIGHT: right = false; break;
                case KeyEvent.VK_UP: up = false; break;
                case KeyEvent.VK_DOWN: down = false; break;
            }
        }
    }

    // 重新开始游戏的方法
    private void restartGame() {
        gameOver = false;
        redPacketCount = 0;
        totalAmount = 0;
        x = 275;
        y = 275;
        redPackets.clear();
        initializeRedPackets();
        startTime = System.currentTimeMillis();
    }
}
