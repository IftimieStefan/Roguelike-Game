package PaooGame.Entities;

import PaooGame.Core.GameCamera;
import java.awt.*;
import PaooGame.Managers.EntityManager;

public class Bomb
{
    //pt animatie
    private float startX, startY, targetX, targetY, currentX, currentY;
    private float progress = 0;
    private float speed = 0.02f; // Viteza zborului
    private int maxHeight = 60;  // Înălțimea saltului
    private int r = 24;
    private boolean active = true;

    //pt explozie
    private boolean isExploding = false;
    private long explosionTimer = 0;
    private final int explosionDuration = 200;
    private int explosionRadius=100;

    public Bomb(float x, float y, float dirX, float dirY)
    {

        this.startX = x + 16;
        this.startY = y + 16;

        this.targetX = startX + dirX * 200;
        this.targetY = startY + dirY * 200;
    }

    public void Update(PaooGame.Managers.EntityManager em)
    {
        if (progress < 1.0f) {
            progress += speed;
            currentX = startX + (targetX - startX) * progress;
            currentY = startY + (targetY - startY) * progress;
        } else if (!isExploding){
            isExploding=true;
            explosionTimer=System.currentTimeMillis();
            if(em!=null) {
                em.processExplosion(getExplosionHitbox());
            }
        }

        if(isExploding)
        {
            if(System.currentTimeMillis()-explosionTimer>explosionDuration)
            {
                active=false;
            }
        }

    }

    public void Draw(Graphics g, GameCamera camera) {
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;

        float jumpY = 4 * maxHeight * progress * (1 - progress);

        // umbra
        g.setColor(new Color(0, 0, 0, 80));
        g.fillOval((int)currentX - xOff + 4, (int)currentY - yOff + r - 5, r - 8, 8);

        // bomba
        g.setColor(Color.BLACK);
        g.fillOval((int)currentX - xOff, (int)currentY - yOff - (int)jumpY, r, r);

        // Detaliu
        g.setColor(Color.WHITE);
        g.fillOval((int)currentX - xOff + 6, (int)currentY - yOff - (int)jumpY + 6, 5, 5);
    }

    public boolean isActive() { return active; }

    public Rectangle getExplosionHitbox()
    {
        if (!isExploding) return new Rectangle(0, 0, 0, 0);
        return new Rectangle((int)targetX - explosionRadius/2, (int)targetY - explosionRadius/2, explosionRadius, explosionRadius);
    }

    public boolean isExploding() { return isExploding; }
}