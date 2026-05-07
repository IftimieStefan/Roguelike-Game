package PaooGame.Entities.NPCs;

import PaooGame.Core.GameCamera;
import PaooGame.Entities.Entity;
import PaooGame.Entities.Interactable;
import PaooGame.Entities.Player.Player;

import java.awt.*;

public abstract class Npc implements Entity, Interactable {
    protected float x, y;
    protected String name;
    protected String dialogMessage;
    
    // Cand e true, va aparea mesajul pe ecran
    protected boolean showDialog = false;
    protected boolean hasBeenTalkedTo = false;

    public Npc(float x, float y, String name, String dialogMessage) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.dialogMessage = dialogMessage;
    }

    public abstract void Update();

    @Override
    public boolean isActive() { return true; }  // NPC-urile nu mor

    @Override
    public float getInteractRange() { return 120f; }  // Max distance

    @Override
    public boolean tryInteract(Player player) {
        // Calculeaza distance player - npc
        float dx = x - player.getX();
        float dy = y - player.getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist <= getInteractRange()) {
            toggleDialog();  
            return true;
        }
        return false;
    }

    public void Draw(Graphics g) {
        if (showDialog) {
            GameCamera camera = GameCamera.getInstance();
            int xOff = (camera != null) ? (int)camera.getxOff() : 0;
            int yOff = (camera != null) ? (int)camera.getyOff() : 0;

            // Cutie de dialog, stil "speech bubble" deasupra NPC-ului
            int dWidth = 400;
            int dHeight = 80;
            int dX = (int)x - xOff - (dWidth / 2) + 24; // centrat deasupra patratului de 48px
            int dY = (int)y - yOff - dHeight - 20; // deasupra NPC-ului

            // negru semi-transparent
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(dX, dY, dWidth, dHeight);

            // contur alb tip RPG
            g.setColor(Color.WHITE);
            g.drawRect(dX, dY, dWidth, dHeight);
            g.drawRect(dX + 2, dY + 2, dWidth - 4, dHeight - 4);

            // Numele sus
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.setColor(Color.YELLOW);
            g.drawString(name + ":", dX + 10, dY + 25);

            // textul mesajului
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.setColor(Color.WHITE);
            g.drawString(dialogMessage, dX + 10, dY + 50);
        }
    }

    public void toggleDialog() {
        showDialog = !showDialog;
        if(showDialog) {
            hasBeenTalkedTo = true;
        }
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public boolean isTalkedTo() { return hasBeenTalkedTo; }
}
