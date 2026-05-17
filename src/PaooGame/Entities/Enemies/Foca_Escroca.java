package PaooGame.Entities.Enemies;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import PaooGame.Core.GameCamera;
import PaooGame.Entities.Player.Player;
import PaooGame.Entities.Projectiles.EnemyBullet;
import PaooGame.Graphics.Assets;
import PaooGame.Managers.EntityManager;

public class Foca_Escroca extends Inamic {
    private final Player player;
    private BufferedImage sprite;
    private EntityManager entityManager;

    private int phase = 1;
    private long lastAttackTime = 0;
    private long attackCooldown = 8000; // 8 secunde
    private boolean minionsSpawned = false;

    // Proiectile
    private ArrayList<EnemyBullet> bullets = new ArrayList<>();
    private boolean isFiring=false;
    private int currentWave=0;
    private long lastWaveTime=0;
    private final long waveDelay=300;

    // Dash / Jump state
    private boolean isDashing = false;
    private float dashTargetX, dashTargetY;
    private float dashSpeed = 10.0f;
    private boolean isJumping = false;
    private float jumpTargetX, jumpTargetY;
    private float jumpSpeed = 10.0f;
    private float initialSpeed;
    private float jumpScale=1.0f;
    private float jumpProgress=0.f;
    private float jumpStartX, jumpStartY;

    private Random rand = new Random();

    public Foca_Escroca(float stX, float stY, Player player) {
        super(stX, stY, 100, 1.0f);
        this.player = player;
        this.initialSpeed = speed;
        // Folosim temporar un sprite existent daca nu avem foca_escroca
        this.sprite = Assets.foca_escroca;
        this.hitboxSize = 64; // Hitbox mai mare pentru boss
        this.hitboxOffset = 8;
    }

    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public void Update() {
        // Update proiectile
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).Update();
            float bx = bullets.get(i).getX();
            float by = bullets.get(i).getY();
            if (!bullets.get(i).isActive() || Math.abs(bx - x) > 1000 || Math.abs(by - y) > 1000) {
                bullets.remove(i);
                i--;
            }
        }

        // Faza 2 check
        if (health <= maxHealth / 2 && phase == 1) {
            phase = 2;
            attackCooldown = 6000; // 6 secunde
            PaooGame.Audio.AudioPlayer.playSound("res/audio/boss-phase2.wav");
            if (!minionsSpawned && entityManager != null) {
                spawnMinions();
                minionsSpawned = true;
            }
        }

        if (player == null) return;

        long currentTime = System.currentTimeMillis();

        if(isFiring)
        {
            if(currentTime-lastWaveTime>waveDelay)
            {
                handleBulletWave();
                currentWave++;
                lastWaveTime=currentTime;

                if(currentWave>=3)
                {
                    isFiring=false;
                }
            }
        }else if (isDashing) {
            handleDash();
        } else if (isJumping) {
            handleJump();
        } else {
            // Miscare normala de urmarire inceata
            float px = player.getX();
            float py = player.getY();
            float dx = px - this.x;
            float dy = py - this.y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance > CONTACT_RANGE) {
                float newX = this.x + (dx / distance) * speed;
                float newY = this.y + (dy / distance) * speed;

                if (!PaooGame.Managers.CollisionSystem.collidesWithWall(newX, this.y, hitboxOffset, hitboxSize, hitboxSize)) {
                    this.x = newX;
                }
                if (!PaooGame.Managers.CollisionSystem.collidesWithWall(this.x, newY, hitboxOffset, hitboxSize, hitboxSize)) {
                    this.y = newY;
                }
            } else {
                // Contact damage continuu (de la Inamic base, dar custom aici)
                if (currentTime - lastAttackTime > ATTACK_COOLDOWN) {
                    player.takeDamage();
                    lastAttackTime = currentTime;
                }
            }

            // Atac special
            if (currentTime - lastAttackTime > attackCooldown) {
                performRandomAttack(dx, dy, distance);
                lastAttackTime = currentTime;
            }
        }
    }

    private void performRandomAttack(float dx, float dy, float distance) {
        int attackType = rand.nextInt(3); // 0, 1, 2
        switch (attackType) {
            case 0: // Jump/AoE (Sare spre player)
                isJumping = true;
                jumpStartX = this.x;
                jumpStartY = this.y;
                jumpTargetX = player.getX();
                jumpTargetY = player.getY();
                jumpProgress=0;
                break;
            case 1: // Dash/Charge
                isDashing = true;
                PaooGame.Audio.AudioPlayer.playSound("res/audio/boss-slide.wav");
                if (distance > 0) {
                    dashTargetX = dx / distance;
                    dashTargetY = dy / distance;
                }
                break;
            case 2: // Projectile Barrage
                isFiring = true;
                currentWave = 0;
                lastWaveTime = 0;
                break;
        }
    }

    private void handleBulletWave(){
        PaooGame.Audio.AudioPlayer.playSound("res/audio/piuw.wav");
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(i * 45 + (currentWave*15));
            float dirX = (float) Math.cos(angle);
            float dirY = (float) Math.sin(angle);
            bullets.add(new EnemyBullet(x + hitboxSize / 2f, y + hitboxSize / 2f, dirX, dirY));
        }
    }

    private void handleDash() {
        float newX = this.x + dashTargetX * dashSpeed;
        float newY = this.y + dashTargetY * dashSpeed;

        boolean hitWall = false;
        if (!PaooGame.Managers.CollisionSystem.collidesWithWall(newX, this.y, hitboxOffset, hitboxSize, hitboxSize)) {
            this.x = newX;
        } else {
            hitWall = true;
        }
        if (!PaooGame.Managers.CollisionSystem.collidesWithWall(this.x, newY, hitboxOffset, hitboxSize, hitboxSize)) {
            this.y = newY;
        } else {
            hitWall = true;
        }

        if (hitWall) {
            isDashing = false; // Se opreste la primul obstacol
        }
    }

    private void handleJump() {
        float dx = jumpTargetX - this.x;
        float dy = jumpTargetY - this.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        jumpProgress=jumpProgress+0.015f;

        if (jumpProgress>=1.0f) {
            this.x = jumpTargetX;
            this.y = jumpTargetY;
            isJumping = false;
            jumpProgress=1.0f;
            jumpScale=1.0f;

            // AoE damage cand aterizeaza
            float distToPlayer = (float) Math.sqrt(Math.pow(player.getX() - this.x, 2) + Math.pow(player.getY() - this.y, 2));
            if (distToPlayer <= CONTACT_RANGE * 2.5f) {
                player.takeDamage();
            }
        } else {
            float smoothedProgress = jumpProgress * jumpProgress * (3 - 2 * jumpProgress);

            this.x = jumpStartX + (jumpTargetX - jumpStartX) * smoothedProgress;
            this.y = jumpStartY + (jumpTargetY - jumpStartY) * smoothedProgress;

            jumpScale = (float) (-4 * Math.pow(jumpProgress - 0.5, 2) + 1.5f);
            if (jumpScale < 1.0f) jumpScale = 1.0f;
        }
    }

    private void spawnMinions() {
        // 2 Samurai
        entityManager.addEntity(new Foca(x - 50, y - 50, player));
        entityManager.addEntity(new Foca(x + 50, y - 50, player));
        // 2 Cowboy
        entityManager.addEntity(new Foca_Cowboy(x - 50, y + 50, player));
        entityManager.addEntity(new Foca_Cowboy(x + 50, y + 50, player));
        // 2 Mexic
        entityManager.addEntity(new Foca_Mexic(x - 100, y, player));
        entityManager.addEntity(new Foca_Mexic(x + 100, y, player));
    }

    @Override
    public void Draw(Graphics g) {
        GameCamera camera = PaooGame.Core.GameCamera.getInstance();
        int xOff = (camera != null) ? (int) camera.getxOff() : 0;
        int yOff = (camera != null) ? (int) camera.getyOff() : 0;

        for (EnemyBullet b : bullets) {
            b.Draw(g, camera);
        }

        int currentSize = (int) (SPRITE_SIZE * 2 * jumpScale);

        // Desenam boss-ul mai mare (scale * 2 = 96x96)
        if (sprite != null) {
            g.drawImage(sprite, (int) x - xOff, (int) y - yOff, currentSize, currentSize, null);
        }

        if (isJumping) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillOval((int)x - xOff, (int)y - yOff + (SPRITE_SIZE * 2) - 10, SPRITE_SIZE * 2, 20);
        }

        // Bara de viata a boss-ului
        int barWidth = HP_BAR_WIDTH * 2;
        g.setColor(Color.BLACK);
        g.fillRect((int) x - xOff, (int) y - yOff - HP_BAR_OFFSET_Y - 5, barWidth, HP_BAR_HEIGHT + 5);
        
        // Culoarea se schimba in faza 2
        if (phase == 1) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.RED);
        }
        
        int hpWidth = (int) ((health / (float) maxHealth) * barWidth);
        if (hpWidth < 0) hpWidth = 0;
        g.fillRect((int) x - xOff, (int) y - yOff - HP_BAR_OFFSET_Y - 5, hpWidth, HP_BAR_HEIGHT + 5);
        g.setColor(Color.WHITE);
        g.drawRect((int) x - xOff, (int) y - yOff - HP_BAR_OFFSET_Y - 5, barWidth, HP_BAR_HEIGHT + 5);
        
        // Numele boss-ului
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Foca Escroca", (int) x - xOff, (int) y - yOff - HP_BAR_OFFSET_Y - 10);

        if (isBuffed()) {
            g.setColor(new Color(0, 200, 0));
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("+", (int) x - xOff + barWidth + 5, (int) y - yOff - 3);
        }
    }

    public ArrayList<EnemyBullet> getBullets() { return bullets; }
}
