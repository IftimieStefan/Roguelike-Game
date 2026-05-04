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
public class Door implements EnemyDeathObs {
    private float x, y;
    private boolean isOpen;       // Se deschide cand mor toti inamicii (Observer)
    private boolean isActivated;  // Player-ul a interactionat cu ea

    // Callback pentru tranzitia la urmatorul nivel — evitam dependenta de LevelManager
    private Runnable onActivate;

    private static final int SIZE = 48;
    private static final float INTERACT_DISTANCE = 80f;

    public Door(float x, float y) {
        this.x = x;
        this.y = y;
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

    /**
     * Returneaza true daca interactiunea a fost consumata (usa deschisa + player aproape).
     */
    public boolean tryInteract(Player player) {
        if (!isOpen || isActivated) return false;

        float dx = x - player.GetX();
        float dy = y - player.GetY();
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
            // Usa deschisa — verde, player-ul poate trece
            g.setColor(new Color(0, 200, 0, 180));
        } else {
            // Usa inchisa — rosu, inamici inca vii
            g.setColor(new Color(200, 0, 0, 180));
        }
        g.fillRect(drawX, drawY, SIZE, SIZE);

        // Contur pentru vizibilitate
        g.setColor(Color.BLACK);
        g.drawRect(drawX, drawY, SIZE, SIZE);

        // Text indicator
        if (isOpen && !isActivated) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString("[E] Pentru a trece la nivelul urmator.", drawX + 14, drawY - 5);
        }
    }

    public boolean isOpen() { return isOpen; }
    public boolean isActivated() { return isActivated; }
    public float getX() { return x; }
    public float getY() { return y; }
}
