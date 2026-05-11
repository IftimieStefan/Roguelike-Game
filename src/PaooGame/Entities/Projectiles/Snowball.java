package PaooGame.Entities.Projectiles;
import PaooGame.Core.GameCamera;
import java.awt.*;

public class Snowball
{
    private float x, y, speedX, speedY;
    private final int r = 24;
    private boolean active=true;

    public Snowball(float x, float y, float speedX, float speedY)
    {
        this.x=x+24;
        this.y=y+24;
        this.speedX=speedX*7;
        this.speedY=speedY*7;
    }

    public void Update()
    {
        x=x+speedX;
        y=y+speedY;
        if (PaooGame.Managers.CollisionSystem.collidesWithWall(x, y, 0, r, r)) {
            active = false;
        }
    }

    public void Draw(Graphics g, GameCamera camera)
    {
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;
        g.setColor(Color.WHITE);
        g.fillOval((int)x - xOff, (int)y - yOff, r, r);
        g.setColor(Color.LIGHT_GRAY);
        g.drawOval((int)x - xOff, (int)y - yOff, r, r);
    }

    public boolean isActive()
    {
        return active;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public void setActive(boolean active) { this.active = active; }
    public Rectangle getHitbox()
    {
        return new Rectangle((int)x, (int)y,24,24);
    }
}
