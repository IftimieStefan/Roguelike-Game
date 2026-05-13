package PaooGame.Entities;

import PaooGame.Core.GameCamera;
import PaooGame.Entities.Enemies.EnemyDeathObs;
import PaooGame.Entities.Player.Player;
import PaooGame.Graphics.Assets;

import java.awt.*;

/**
 * Implementeaza Observer pattern — se deschide cand toti inamicii mor.
 * Player-ul trebuie sa apese E langa usa deschisa pentru a trece mai departe.
 */
public class Door implements Entity, Interactable, EnemyDeathObs {
    private float x, y;
    private boolean isOpen;       // Se deschide cand mor toti inamicii (Observer)
    private boolean isActivated;  // Player-ul a interactionat cu ea
    private int nextRoom;
    private boolean orientation;

    // Callback pentru tranzitia la urmatorul nivel — evitam dependenta de LevelManager
    private Runnable onActivate;

    private static final int SIZE = 48;
    private static final float INTERACT_DISTANCE = 80f;

    public Door(float x, float y, int nextRoom, boolean orientation) {
        this.x = x;
        this.y = y;
        this.nextRoom=nextRoom;
        this.orientation=orientation;
        this.isOpen = false;
        this.isActivated = false;
    }

    /**
     * arrow function
     */
    public void setOnActivate(Runnable callback) {
        this.onActivate = callback;
    }

    @Override
    public void onDeadEnemies() {
        isOpen = true;
    }

    @Override
    public void Update() { }  

    @Override
    public boolean isActive() { return !isActivated; }  // Dispare dupa

    @Override
    public float getInteractRange() { return INTERACT_DISTANCE; }

    public int getNextRoom(){ return nextRoom; }

    /**
     * Returneaza true daca interactiunea a fost consumata (usa deschisa + player aproape).
     */
    public boolean tryInteract(Player player) {
        if (!isOpen || isActivated) return false;

        float dx = x - player.getX();
        float dy = y - player.getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist <= INTERACT_DISTANCE) {
            isActivated = true;
            if (onActivate != null) {
                onActivate.run();
            }
            return true;
        }
        return false;
    }

    public void Draw(Graphics g) {
        GameCamera camera = GameCamera.getInstance();
        int xOff = (camera != null) ? (int) camera.getxOff() : 0;
        int yOff = (camera != null) ? (int) camera.getyOff() : 0;

        int drawX = (int) x - xOff;
        int drawY = (int) y - yOff;

        if (isOpen) {
            // Usa deschisa
            if(orientation) {
                // dam asset de usa verticala
                g.drawImage(Assets.usavertical, drawX, drawY, SIZE, SIZE, null);
            }
            else {
                // dam asset de usa orizontal
                g.drawImage(Assets.usaorizontal, drawX, drawY, SIZE, SIZE, null);
            }

        } else {
            // Usa inchisa
            if(orientation) {
                // dam asset de perete vertical
                g.drawImage(Assets.peretevertical, drawX, drawY, SIZE, SIZE, null);
            }
            else {
                // dam asset de perete orizontal
                g.drawImage(Assets.usaorizontal, drawX, drawY, SIZE, SIZE, null);
            }
            g.drawImage(Assets.pereteorizontal,drawX,drawY,SIZE,SIZE,null);
        }

        // Text indicator
        if (isOpen && !isActivated) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString("[E] Pentru a trece la camera urmator.", drawX + 14, drawY - 5);
        }
    }

    public boolean isOpen() { return isOpen; }
    public boolean isActivated() { return isActivated; }
    public float getX() { return x; }
    public float getY() { return y; }
}
