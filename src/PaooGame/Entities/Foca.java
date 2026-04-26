package PaooGame.Entities;

import java.awt.*;
import java.awt.image.BufferedImage;

import PaooGame.Core.GameCamera;
import PaooGame.Graphics.Assets;

// Inamic basic, gen Foca.
public class Foca extends Inamic{
    private Player player; // Tinta inamicului.
    private long lastAttackTime = 0; // Timer spam consola.
    private BufferedImage sprite; // Poza inamicului.

    // Seteaza inceput, viata si poza.
    public Foca(float stX, float stY, Player player) {
        super(stX, stY, 20, 1.0f);
        this.player = player;
        this.sprite = Assets.inamic_samurai;
    }

    @Override
    // Urmareste jucatorul cand e langa.
    public void Update() {
        if (player != null) {
            float px = player.GetX();
            float py = player.GetY();

            float dx = px - this.x;
            float dy = py - this.y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance <= 40) { // Printeaza praduiala.
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastAttackTime > 1000) { // 1 pe secz
                    player.takeDamage(4);
                    lastAttackTime = currentTime;
                }
            } else if (distance > 0) {
                this.x += (dx / distance) * speed;
                this.y += (dy / distance) * speed;
            }
        }
    }

    @Override
    public void Draw(Graphics g) {
        GameCamera camera = PaooGame.Core.GameCamera.getInstance();
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;

            // Desenam poza de inamic
        g.drawImage(sprite, (int)x - xOff, (int)y - yOff, 48, 48, null);

        // Bara de viata deasupra.
        g.setColor(Color.BLACK);
        g.fillRect((int)x - xOff, (int)y - yOff - 10, 48, 5);
        g.setColor(Color.GREEN);
        int hpWidth = (int)((health / (float)maxHealth) * 48);
        if (hpWidth < 0) hpWidth = 0;
        g.fillRect((int)x - xOff, (int)y - yOff - 10, hpWidth, 5);
        g.setColor(Color.WHITE);
        g.drawRect((int)x - xOff, (int)y - yOff - 10, 48, 5);
    }
}
