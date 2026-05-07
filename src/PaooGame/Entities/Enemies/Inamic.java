package PaooGame.Entities.Enemies;

import PaooGame.Entities.Damageable;
import PaooGame.Entities.Entity;

import java.awt.*;

// Sablon parinte pentru baietii rai.
public abstract class Inamic implements Entity, Damageable {
    protected float x,y; // Coordonate harta.
    protected int health; // HP curent zic.
    protected int maxHealth; // Viata lui full.
    protected float speed; // Cat de repede fuge.
    protected int hitboxOffset=16, hitboxSize=40;

    // Valori de baza din fabrica.
    public Inamic(float stX,float stY,int stHealth,float stSpeed){
        this.x=stX;
        this.y=stY;
        this.health=stHealth;
        this.maxHealth=stHealth;
        this.speed=stSpeed;
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
}
