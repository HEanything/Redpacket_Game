package Game0_7;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// 红包类，包含红包的位置、速度、金额和图像
interface RedPacketType {
    Image getRedPacketImage();
    double calculateAmount(double scale);
}

class RedPacketType1 implements RedPacketType {
    private static final Image RED_POCKET_IMG = loadImage("../images/redpocket.jpg");

    @Override
    public Image getRedPacketImage() {
        return RED_POCKET_IMG;
    }

    @Override
    public double calculateAmount(double scale) {
        return 10*scale;  // 红包金额为10
    }
    public static Image loadImage(String imagePath) {
        URL imageURL = RedPacketType1.class.getResource(imagePath);
        if (imageURL != null) {
            try {
                return ImageIO.read(imageURL);
            } catch (IOException e) {
                throw new RuntimeException("Error loading image: " + imagePath, e);
            }
        }
        System.err.println("Image not found: " + imagePath);
        return null;
    }
}

class RedPacketType2 implements RedPacketType {
    private static final Image RED_POCKET_IMG = loadImage("../images/redpocket2.png");

    @Override
    public Image getRedPacketImage() {
        return RED_POCKET_IMG;
    }

    @Override
    public double calculateAmount(double scale) {
        return 20*scale;  // 红包金额为20
    }
    public static Image loadImage(String imagePath) {
        URL imageURL = RedPacketType2.class.getResource(imagePath);
        if (imageURL != null) {
            try {
                return ImageIO.read(imageURL);
            } catch (IOException e) {
                throw new RuntimeException("Error loading image: " + imagePath, e);
            }
        }
        System.err.println("Image not found: " + imagePath);
        return null;
    }
}
class RedPacket {
    private int x, y;
    private double direction;
    private int width, height;
    private RedPacketType redPacketType;  // 使用桥接模式来实现图像和金额的不同实现

    // 构造方法：传入红包类型和其他基本信息
    public RedPacket(int x, int y, double direction, RedPacketType redPacketType) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.redPacketType = redPacketType;

        // 随机生成一个放大倍数
        double scale = 1 + Math.random() * 1;
        this.width = (int) (30 * scale);
        this.height = (int) (45 * scale);
    }

    public double getAmount() {
        double scale = (width/30.0);
        return redPacketType.calculateAmount(scale);  // 根据红包类型计算金额
    }

    public Image getRedPocketImg() {
        return redPacketType.getRedPacketImage();  // 根据红包类型获取图像
    }

    public void update(int width, int height) {
        x += Math.cos(direction) * 2;
        y += Math.sin(direction) * 2;

        if (x < 0 || x > width || y < 0 || y > height) {
            x = (int) (Math.random() * width);
            y = (int) (Math.random() * height);
            direction = Math.random() * Math.PI * 2;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}


public class GameFrame extends Frame {
    private Image bgImg;
    private Image planeImg;
    private Image redPocketImg1;
    private Image redPocketImg2;
    private int x = 275, y = 275;
    private boolean left, right, up, down;
    private List<RedPacket> redPackets;
    private int redPacketCount = 0;
    private int totalAmount = 0;
    private long startTime;
    private long endTime = 20000;
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private boolean gameReset = false;  // 新增标志，表示游戏是否被重置
    private GameLoop gameLoop;  // 游戏循环对象
    private Thread gameLoopThread;  // 游戏循环线程

    public static void main(String[] args) {
        GameFrame gameFrame = new GameFrame();
        gameFrame.init();
    }

    public GameFrame() {
        this.bgImg = loadImage("../images/bg.jpg");
        this.planeImg = loadImage("../images/nets.jpg");
        this.redPocketImg1 = loadImage("../images/redpocket.jpg");
        this.redPocketImg2 = loadImage("../images/redpocket2.png");
        this.redPackets = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
    }

    public void init() {
        setTitle("抢红包游戏");
        setSize(1200, 800);
        setLocation(300, 100);
        setVisible(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        initializeRedPackets();

        addKeyListener(new KeyMonitor());

        new LoginFrame(this);
    }

    public void startGame() {
        gameStarted = true;
        gameReset = false;  // 确保没有重置过
        setVisible(true);
        startGameLoop();  // 启动游戏循环
    }

    // 开始游戏循环
    private void startGameLoop() {
        if (gameLoopThread != null && gameLoopThread.isAlive()) {
            gameLoopThread.interrupt();  // 停止当前线程
        }

        gameLoop = new GameLoop();
        gameLoopThread = new Thread(gameLoop);
        gameLoopThread.start();
    }

    public void initializeRedPackets() {
        for (int i = 0; i < 25; i++) {
            RedPacketType redPacketType = Math.random() < 0.5 ? new RedPacketType1() : new RedPacketType2();  // 随机选择红包类型

            redPackets.add(new RedPacket(
                    (int) (Math.random() * getWidth()),  // 随机生成红包的初始位置
                    (int) (Math.random() * getHeight()),
                    Math.random() * Math.PI * 2,  // 随机生成红包的运动角度
                    redPacketType  // 传入红包类型
            ));
        }
    }




    private Image loadImage(String imagePath) {
        URL imageURL = getClass().getResource(imagePath);
        if (imageURL != null) {
            try {
                return ImageIO.read(imageURL);
            } catch (IOException e) {
                throw new RuntimeException("Error loading image: " + imagePath, e);
            }
        }
        System.err.println("Image not found: " + imagePath);
        return null;
    }

    @Override
    public void update(Graphics g) {
        if (!gameStarted) return;

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        Graphics gOffScreen = bs.getDrawGraphics();
        gOffScreen.clearRect(0, 0, getWidth(), getHeight());

        if (bgImg != null) {
            gOffScreen.drawImage(bgImg, 0, 0, getWidth(), getHeight(), null);
        }
        if (planeImg != null && !gameOver) {
            gOffScreen.drawImage(planeImg, x, y, 50, 50, null);
        }

        if (!gameOver) {
            if (left && x > 0) x -= 5;
            if (right && x < getWidth() - 50) x += 5;
            if (up && y > 0) y -= 5;
            if (down && y < getHeight() - 50) y += 5;
        }

        if (!gameOver) {
            Iterator<RedPacket> iterator = redPackets.iterator();
            while (iterator.hasNext()) {
                RedPacket packet = iterator.next();
                gOffScreen.drawImage(packet.getRedPocketImg(), packet.getX(), packet.getY(), packet.getWidth(), packet.getHeight(), null);
                packet.update(getWidth(), getHeight());

                if (isCollision(x, y, packet.getX(), packet.getY())) {
                    redPacketCount++;
                    totalAmount += packet.getAmount();
                    iterator.remove();
                }
            }
        }

        gOffScreen.setColor(Color.red);
        gOffScreen.drawString("抢到红包: " + redPacketCount, 10, 20);
        gOffScreen.drawString("总金额: " + totalAmount, 10, 40);

        long remainingTime = endTime - (System.currentTimeMillis() - startTime);
        if (remainingTime > 0) {
            gOffScreen.drawString("剩余时间: " + (remainingTime / 1000) + " 秒", 10, 60);
        } else {
            gameOver = true;
            Font largeFont = new Font("Dialog", Font.BOLD, 40);
            gOffScreen.setFont(largeFont);

            FontMetrics fm = gOffScreen.getFontMetrics();
            String gameOverText = "游戏结束";
            String redPacketCountText = "抢到红包: " + redPacketCount;
            String totalAmountText = "总金额: " + totalAmount;
            String restartText = "按 R 重新开始";

            int gameOverWidth = fm.stringWidth(gameOverText);
            int redPacketCountWidth = fm.stringWidth(redPacketCountText);
            int totalAmountWidth = fm.stringWidth(totalAmountText);
            int restartWidth = fm.stringWidth(restartText);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            gOffScreen.drawString(gameOverText, centerX - gameOverWidth / 2, centerY - 60);
            gOffScreen.drawString(redPacketCountText, centerX - redPacketCountWidth / 2, centerY - 20);
            gOffScreen.drawString(totalAmountText, centerX - totalAmountWidth / 2, centerY + 20);
            gOffScreen.drawString(restartText, centerX - restartWidth / 2, centerY + 60);
        }

        bs.show();
        gOffScreen.dispose();
    }

    private boolean isCollision(int x1, int y1, int x2, int y2) {
        int distance = (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        return distance < 30;
    }

    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_LEFT) left = true;
            if (keyCode == KeyEvent.VK_RIGHT) right = true;
            if (keyCode == KeyEvent.VK_UP) up = true;
            if (keyCode == KeyEvent.VK_DOWN) down = true;
            if (keyCode == KeyEvent.VK_R && gameOver) {
                resetGame();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_LEFT) left = false;
            if (keyCode == KeyEvent.VK_RIGHT) right = false;
            if (keyCode == KeyEvent.VK_UP) up = false;
            if (keyCode == KeyEvent.VK_DOWN) down = false;
        }
    }

    private class GameLoop implements Runnable {
        @Override
        public void run() {
            while (!gameOver) {
                repaint();
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void resetGame() {
        redPacketCount = 0;
        totalAmount = 0;
        redPackets.clear();
        initializeRedPackets();
        gameOver = false;
        startTime = System.currentTimeMillis();
        repaint();
        gameReset = true;  // 标记游戏已被重置
        startGameLoop();  // 重启游戏循环
    }
}


