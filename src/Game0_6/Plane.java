package Game0_6;

import java.awt.*;

public class Plane {
    private int x, y;

    public Plane() {
        x = 400;
        y = 500;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 50, 50);
    }

    public boolean collidesWith(RedPacket packet) {
        return x < packet.getX() + 20 && x + 50 > packet.getX() &&
               y < packet.getY() + 20 && y + 50 > packet.getY();
    }
}

