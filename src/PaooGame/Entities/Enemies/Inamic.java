package PaooGame.Entities.Enemies;

import PaooGame.Entities.Damageable;
import PaooGame.Entities.Entity;

import java.awt.*;

// Sablon parinte pentru baietii rai.
public abstract class Inamic implements Entity, Damageable {
    // Constante comune tuturor inamicilor
    protected static final int SPRITE_SIZE = 48;       // Dimensiunea sprite-ului (48x48)
    protected static final float CONTACT_RANGE = 40f;  // Distanta de contact damage
    protected static final long ATTACK_COOLDOWN = 1000; // 1 secunda intre atacuri contact
    protected static final int HP_BAR_WIDTH = 48;      // Latimea barei de viata
    protected static final int HP_BAR_HEIGHT = 5;      // Inaltimea barei de viata
    protected static final int HP_BAR_OFFSET_Y = 10;   // Offset-ul vertical al barei

    protected float x,y; // Coordonate harta.
    protected int health; // HP curent zic.
    protected int maxHealth; // Viata lui full.
    protected float speed; // Cat de repede fuge.
    protected float baseSpeed; // Viteza originala (fara buff).
    protected int hitboxOffset=16, hitboxSize=40;
    protected boolean buffed = false; // Flag — e buff-at de Foca Mexic?

    // Valori de baza din fabrica.
    public Inamic(float stX,float stY,int stHealth,float stSpeed){
        this.x=stX;
        this.y=stY;
        this.health=stHealth;
        this.maxHealth=stHealth;
        this.speed=stSpeed;
        this.baseSpeed=stSpeed;
    }

    public abstract void Update();
    public abstract void Draw(Graphics g);

    @Override
    public boolean isActive() {
        return health > 0;  // Mort = scos 
    }

    public float getSpeed() {
        return speed;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle getHitbox()
    {
        return new Rectangle((int)x + hitboxOffset, (int)y+hitboxOffset,hitboxSize,hitboxSize);
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int catYa){
        health -= catYa;
    }

    // ── Buff system (Foca Mexic) ──

    public void applyBuff() {
        if (!buffed) {
            speed = baseSpeed * 1.25f;  // +25% viteză
            int hpBonus = (int)(maxHealth * 0.15f); // +15% HP
            maxHealth += hpBonus;
            health += hpBonus;
            buffed = true;
        }
    }

    public void removeBuff() {
        if (buffed) {
            speed = baseSpeed;
            int hpBonus = (int)((maxHealth / 1.15f) * 0.15f);
            maxHealth -= hpBonus;
            health = Math.min(health, maxHealth);
            buffed = false;
        }
    }

    public boolean isBuffed() { return buffed; }
}
