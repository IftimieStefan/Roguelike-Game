package PaooGame.Entities.Enemies;

import java.awt.*;
import java.awt.image.BufferedImage;

import PaooGame.Core.GameCamera;
import PaooGame.Entities.Entity;
import PaooGame.Entities.Player.Player;
import PaooGame.Graphics.Assets;
import PaooGame.Managers.EntityManager;

// Foca Mexic — inamic support care buff-ează celelalte foci cât timp e în viață.
// +25% viteză și +15% HP celorlalte foci.
public class Foca_Mexic extends Inamic {
    private Player player;
    private long lastAttackTime = 0;
    private BufferedImage sprite;
    private EntityManager entityManager; // Referință pentru a itera prin inamici

    public Foca_Mexic(float stX, float stY, Player player) {
        super(stX, stY, 30, 0.8f); // HP=30, Speed=0.8 (mai lent, e support)
        this.player = player;
        this.sprite = Assets.inamic_mehico;
    }

    // Se setează după ce e adăugat în EntityManager
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

    private boolean buffRemoved = false; // Flag ca sa stergem buff-ul o singura data

    @Override
    public void Update() {
        // Daca e mort, scoate buff-ul o singura data
        if (health <= 0 && !buffRemoved) {
            if (entityManager != null) {
                for (Inamic inamic : entityManager.getEnemies()) {
                    if (inamic != this) {
                        inamic.removeBuff();
                    }
                }
            }
            buffRemoved = true;
            return; // Nu mai face nimic daca e mort
        }

        // Aplică buff tuturor celorlalte foci
        if (entityManager != null) {
            for (Inamic inamic : entityManager.getEnemies()) {
                if (inamic != this && !(inamic instanceof Foca_Mexic)) {
                    inamic.applyBuff();
                }
            }
        }

        // Urmărește jucătorul (la fel ca Foca normală)
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

    @Override
    public boolean isActive() {
        return health > 0; // Fara side effects — logica de buff e in Update()
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

        // Indicator mov "+" pentru Foca Mexic (se distinge de celelalte)
        g.setColor(new Color(180, 0, 255));
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("+", (int)x - xOff + 50, (int)y - yOff - 3);
    }
}
