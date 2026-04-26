package PaooGame.Entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import PaooGame.Graphics.Assets;

// Inamic basic, gen Foca.
public class Foca_Cowboy extends Inamic{
    private final Player player; // Tinta inamicului.
    private long lastPrintTime = 0; // Timer spam consola.
    private final BufferedImage sprite; // Poza inamicului.

    // Seteaza inceput, viata si poza.
    public Foca_Cowboy(float stX, float stY, Player player) {
        super(stX, stY, 40, 1.5f);
        this.player = player;
        this.sprite = Assets.inamic_cowboy; // Aici punem poza pe care vrei s-o foloseasca
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
                if (currentTime - lastPrintTime > 1000) { // 1 pe secz
                    System.out.println("attack");
                    lastPrintTime = currentTime;
                }
            } else if (distance > 0) {
                this.x += (dx / distance) * speed;
                this.y += (dy / distance) * speed;
            }
        }
    }

    @Override
    public void Draw(Graphics g) {
        PaooGame.Core.GameCamera camera = PaooGame.Core.GameCamera.getInstance();
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;

        // Randare imagine sprite
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
