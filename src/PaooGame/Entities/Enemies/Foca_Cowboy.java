package PaooGame.Entities.Enemies;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import PaooGame.Core.GameCamera;
import PaooGame.Entities.Player.Player;
import PaooGame.Entities.Projectiles.EnemyBullet;
import PaooGame.Graphics.Assets;

// Inamic basic, gen Foca.
public class Foca_Cowboy extends Inamic {
    private final Player player;
    private long lastPrintTime = 0;
    private final BufferedImage sprite;

    // Proiectile
    private ArrayList<EnemyBullet> bullets = new ArrayList<>();
    private long lastShootTime = 0;
    private static final long SHOOT_COOLDOWN = 2000;  // 2 secunde intre trageri
    private static final float SHOOT_RANGE = 250;     // Distanta la care incepe sa traga
    private static final float CHASE_RANGE = 500;     // Distanta la care urmareste

    public Foca_Cowboy(float stX, float stY, Player player) {
        super(stX, stY, 30, 1.5f);
        this.player = player;
        this.sprite = Assets.inamic_cowboy;
    }

    @Override
    public void Update() {
        // Update proiectile
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).Update();
            // Scoatem proiectilele inactive sau prea departate
            float bx = bullets.get(i).getX();
            float by = bullets.get(i).getY();
            if (!bullets.get(i).isActive() ||
                Math.abs(bx - x) > 500 || Math.abs(by - y) > 500) {
                bullets.remove(i);
                i--;
            }
        }

        if (player != null) {
            float px = player.getX();
            float py = player.getY();

            float dx = px - this.x;
            float dy = py - this.y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance <= CONTACT_RANGE) {
                // Contact damage
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastPrintTime > ATTACK_COOLDOWN) {
                    player.takeDamage();
                    lastPrintTime = currentTime;
                }
            } else if (distance <= SHOOT_RANGE) {
                // In raza de tragere — trage proiectile
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShootTime > SHOOT_COOLDOWN) {
                    shoot(dx / distance, dy / distance);
                    lastShootTime = currentTime;
                }
            } else if (distance <= CHASE_RANGE && distance > 0) {
                // Urmareste playerul
                float newX = this.x + (dx / distance) * speed;
                float newY = this.y + (dy / distance) * speed;
                
                if (!PaooGame.Managers.CollisionSystem.collidesWithWall(newX, this.y, hitboxOffset, hitboxSize, hitboxSize)) {
                    this.x = newX;
                }
                if (!PaooGame.Managers.CollisionSystem.collidesWithWall(this.x, newY, hitboxOffset, hitboxSize, hitboxSize)) {
                    this.y = newY;
                }
            }
        }
    }

    // Trage un proiectil negru spre player
    private void shoot(float dirX, float dirY) {
        bullets.add(new EnemyBullet(x, y, dirX, dirY));
    }

    @Override
    public void Draw(Graphics g) {
        GameCamera camera = PaooGame.Core.GameCamera.getInstance();
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;

        // Desenam proiectilele
        for (EnemyBullet b : bullets) {
            b.Draw(g, camera);
        }

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

    // Getter pentru bullets — folosit de CollisionSystem
    public ArrayList<EnemyBullet> getBullets() { return bullets; }
}
