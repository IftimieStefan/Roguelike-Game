package PaooGame.Entities.Projectiles;

import PaooGame.Core.GameCamera;
import java.awt.*;

// Proiectil negru tras de inamici
public class EnemyBullet {
    private float x, y, speedX, speedY;
    private final int size = 10;
    private boolean active = true;
    private int damage = 10;

    public EnemyBullet(float x, float y, float speedX, float speedY) {
        this.x = x + 24;
        this.y = y + 24;
        this.speedX = speedX * 4;
        this.speedY = speedY * 4;
    }

    public void Update() {
        x += speedX;
        y += speedY;
        if (PaooGame.Managers.CollisionSystem.collidesWithWall(x, y, 0, size, size)) {
            active = false;
        }
    }

    public void Draw(Graphics g, GameCamera camera) {
        int xOff = (camera != null) ? (int) camera.getxOff() : 0;
        int yOff = (camera != null) ? (int) camera.getyOff() : 0;
        g.setColor(Color.BLACK);
        g.fillOval((int) x - xOff, (int) y - yOff, size, size);
        g.setColor(Color.DARK_GRAY);
        g.drawOval((int) x - xOff, (int) y - yOff, size, size);
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public int getDamage() { return damage; }
    public float getX() { return x; }
    public float getY() { return y; }
    public Rectangle getHitbox() {
        return new Rectangle((int) x, (int) y, size, size);
    }
}
