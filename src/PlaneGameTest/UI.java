package PlaneGameTest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

public class UI extends Frame {
    public static void main(String[] args) {
        UI frame = new UI();
        frame.setVisible(true);
        frame.setTitle("Plane Game");
        frame.setSize(800, 600);
        frame.setLocation(500,600);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
    }
}
