package Game0_5;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.stream.IntStream;

public class GameFrame extends Frame {
    Image bgImg = loadImage("../images/bg.jpg");
    Image planeImg = loadImage("../images/plane.jpg");
    int x = 275,y = 275;
    boolean left,right,up,down;

    int x_shell=300,y_shell=300;
    double degree = Math.random()*Math.PI*2;

    int[] x_array =new int[50];
    int[] y_array =new int[50];
    double[] degree_array =new double[50];


    public static void main(String[] args) {
        //创建窗口
        Game0_5.GameFrame frame = new Game0_5.GameFrame();
        frame.InitalFrame();
    }


    class paintThread extends Thread{
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
    private Image offScreenImage = null;
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(600,600);
            Graphics g0ff = offScreenImage.getGraphics();
            paint(g0ff);
            g.drawImage(offScreenImage,0,0,null);
        }
    }
    public void InitalFrame() {
        //设置窗口可见
        setVisible(true);

        setTitle("GameFrame0.5");
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

        new Game0_5.GameFrame.paintThread().start();

        addKeyListener( new Game0_5.GameFrame.KeyMonitor());

        for (int i = 0; i < 50; i++) {
            x_array[i]=100;
            y_array[i]=100;
            degree_array[i]=Math.random()*Math.PI*2;
        }
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

        Color c=g.getColor();
        g.setColor(Color.blue);
        g.fillOval(x_shell,y_shell,10,10);

        x_shell += 5*Math.cos(degree);
        y_shell += 5*Math.sin(degree);
        g.setColor(c);

        Color c2=g.getColor();
        g.setColor(Color.red);
        IntStream.range(0,50).forEach(i->{
            g.fillOval(x_array[i],y_array[i],10,10);
            x_array[i]+=7*Math.cos(degree_array[i]);
            y_array[i]+=7*Math.sin(degree_array[i]);

            if(y_array[i]>600-10||y_array[i]<10)
            {
                degree_array[i]=-degree_array[i];
            }
            if (x_array[i]>600-10||x_array[i]<10)
            {
                degree_array[i]=Math.PI-degree_array[i];
            }
        });
        g.setColor(c2);
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
    class KeyMonitor extends KeyAdapter {
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
