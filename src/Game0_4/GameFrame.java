package Game0_4;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

public class GameFrame extends Frame {
    Image bgImg = loadImage("../images/bg.jpg");
    Image planeImg = loadImage("../images/plane.jpg");
    int x = 275,y = 275;
    boolean left,right,up,down;
//    private Image offScreenImage = null;
//    public void update(Graphics g) {
//        if (offScreenImage == null) {
//            offScreenImage = this.createImage(600,600);
//            Graphics g0ff = offScreenImage.getGraphics();
//            paint(g0ff);
//            g.drawImage(offScreenImage,0,0,null);
//        }
//    }

    public static void main(String[] args) {
        //创建窗口
        GameFrame frame = new GameFrame();
        frame.InitalFrame();
    }


    public class paintThread extends Thread{
        public void run() {
            super.run();
            while (true) {
                repaint();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public void InitalFrame() {
        //设置窗口可见
        setVisible(true);

        setTitle("GameFrame0.4");
        setSize(600, 600);
        setLocation(500, 100);

        //关闭窗口功能
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        new paintThread().start();

        addKeyListener( new KeyMonitor());
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(bgImg, 0, 0, 600, 600, null);
        g.drawImage(planeImg, x ,y , 50, 50, null);
//        x += 2;
//        System.out.println("绘制中。。。");
        if(left)x-=2;
        if(right)x+=2;
        if(up)y-=2;
        if(down)y+=2;
    }

    public Image loadImage(String imagePath) {
        URL imageURL = getClass().getResource(imagePath);
        if (imageURL != null) {
            try {
                return ImageIO.read(imageURL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.err.println("Image not find: " + imagePath);
        return null;
    }
    public  class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: left = true; break;
                case KeyEvent.VK_RIGHT: right = true; break;
                case KeyEvent.VK_UP: up = true; break;
                case KeyEvent.VK_DOWN: down = true; break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: left = false; break;
                case KeyEvent.VK_RIGHT: right = false; break;
                case KeyEvent.VK_UP: up = false; break;
                case KeyEvent.VK_DOWN: down = false; break;
            }
        }
    }
    class KeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: System.out.println("1");break;
                case KeyEvent.VK_RIGHT: System.out.println("2");break;
                case KeyEvent.VK_UP: System.out.println("3");break;
                case KeyEvent.VK_DOWN: System.out.println("4");break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: System.out.println("1");break;
                case KeyEvent.VK_RIGHT: System.out.println("2");break;
                case KeyEvent.VK_UP: System.out.println("3");break;
                case KeyEvent.VK_DOWN: System.out.println("4");break;
            }
        }
    }

}