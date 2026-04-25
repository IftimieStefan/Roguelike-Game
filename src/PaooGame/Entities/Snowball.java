package PaooGame.Entities;
import PaooGame.Core.GameCamera;
import java.awt.*;

public class Snowball
{
    private float x, y, speedX, speedY;
    private final int radius = 24;
    private boolean active=true;

    public Snowball(float x, float y, float speedX, float speedY)
    {
        this.x=x+24;
        this.y=y+24;
        this.speedX=speedX;
        this.speedY=speedY;
    }

    public void Update()
    {
        x=x+speedX;
        y=y+speedY;
    }

    public void Draw(Graphics g, GameCamera camera)
    {
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;
        g.setColor(Color.WHITE);
        g.fillOval((int)x - xOff, (int)y - yOff, radius, radius);
        g.setColor(Color.LIGHT_GRAY);
        g.drawOval((int)x - xOff, (int)y - yOff, radius, radius);
    }

    public boolean isActive()
    {
        return active;
    }
}
