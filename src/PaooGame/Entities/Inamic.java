package PaooGame.Entities;

import java.awt.*;

public abstract class Inamic {
    protected float x,y;
    protected int health;
    protected float speed;

    public Inamic(float stX,float stY,int stHealth,float stSpeed){
        this.x=stX;
        this.y=stY;
        this.health=stHealth;
        this.speed=stSpeed;
    }

    public abstract void Update();
    public abstract void Draw(Graphics g);

    public float getSpeed() {
        return speed;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int catYa){
        health -= catYa;
    }
}
