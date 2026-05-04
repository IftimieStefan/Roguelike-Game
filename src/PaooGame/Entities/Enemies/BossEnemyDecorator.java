package PaooGame.Entities.Enemies;

import PaooGame.Core.GameCamera;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class BossEnemyDecorator extends EnemyDecorator {

    public BossEnemyDecorator(Inamic wrappee) {
        super(wrappee);
        this.wrappee.maxHealth *= 3;
        this.wrappee.health = this.wrappee.maxHealth;
        this.wrappee.hitboxSize *= 2; // Modificam si cat de mare e hitboxul
        
        this.maxHealth = this.wrappee.maxHealth;
        this.health = this.wrappee.health;
    }

    @Override
    public void Draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        
        GameCamera camera = PaooGame.Core.GameCamera.getInstance();
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;

        // Centrul pentru o dimensiune de 48x48
        float cx = x - xOff + 24; 
        float cy = y - yOff + 24;

        // Il facem mai mare pe ecran
        g2d.translate(cx, cy);
        g2d.scale(1.5, 1.5);
        g2d.translate(-cx, -cy);

        super.Draw(g); // face apel super draw la clasa tata

        g2d.setTransform(old);
    }
}
