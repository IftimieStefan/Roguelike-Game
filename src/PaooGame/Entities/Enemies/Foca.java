package PaooGame.Entities.Enemies;

import java.awt.*;
import java.awt.image.BufferedImage;

import PaooGame.Core.GameCamera;
import PaooGame.Entities.Player.Player;
import PaooGame.Graphics.Assets;

// Inamic basic, gen Foca.
public class Foca extends Inamic {
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
            float px = player.getX();
            float py = player.getY();

            float dx = px - this.x;
            float dy = py - this.y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance <= CONTACT_RANGE) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastAttackTime > ATTACK_COOLDOWN) {
                    player.takeDamage();
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
        g.drawImage(sprite, (int)x - xOff, (int)y - yOff, SPRITE_SIZE, SPRITE_SIZE, null);

        // Bara de viata deasupra.
        g.setColor(Color.BLACK);
        g.fillRect((int)x - xOff, (int)y - yOff - HP_BAR_OFFSET_Y, HP_BAR_WIDTH, HP_BAR_HEIGHT);
        g.setColor(Color.GREEN);
        int hpWidth = (int)((health / (float)maxHealth) * HP_BAR_WIDTH);
        if (hpWidth < 0) hpWidth = 0;
        g.fillRect((int)x - xOff, (int)y - yOff - HP_BAR_OFFSET_Y, hpWidth, HP_BAR_HEIGHT);
        g.setColor(Color.WHITE);
        g.drawRect((int)x - xOff, (int)y - yOff - HP_BAR_OFFSET_Y, HP_BAR_WIDTH, HP_BAR_HEIGHT);

        // Indicator buff Foca Mexic
        if (isBuffed()) {
            g.setColor(new Color(0, 200, 0));
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("+", (int)x - xOff + 50, (int)y - yOff - 3);
        }
    }
}
