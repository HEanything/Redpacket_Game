package Game0_6;

import java.awt.*;
import java.util.Random;

public class RedPacket {
    private int x, y;
    private int value;

    public RedPacket() {
        x = new Random().nextInt(750);
        y = 0;
        value = new Random().nextInt(10) + 1;
    }

    public void move() {
        y += 5;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, 20, 20);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }
}

