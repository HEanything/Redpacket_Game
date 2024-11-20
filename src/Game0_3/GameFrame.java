package Game0_3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

public class GameFrame extends Frame {
    Image bgImg = loadImage("../images/bg.jpg");
    Image planeImg = loadImage("../images/plane.jpg");
    public static void main(String[] args) {
        //创建窗口
        GameFrame frame = new GameFrame();
        frame.InitalFrame();
    }

    public void InitalFrame() {
        //设置窗口可见
        setVisible(true);

        setTitle("GameFrame0.3");
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
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(bgImg, 0, 0, 600, 600, null);
        g.drawImage(planeImg, 275, 275, 50, 50, null);
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

}
