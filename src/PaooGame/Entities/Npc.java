package PaooGame.Entities;

import PaooGame.Core.GameCamera;
import java.awt.*;

public abstract class Npc {
    protected float x, y;
    protected String name;
    protected String dialogMessage;
    
    // Cand e true, va aparea mesajul pe ecran
    protected boolean showDialog = false;

    public Npc(float x, float y, String name, String dialogMessage) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.dialogMessage = dialogMessage;
    }

    public abstract void Update();

    public void Draw(Graphics g) {
        if (showDialog) {
            GameCamera camera = GameCamera.getInstance();
            int xOff = (camera != null) ? (int)camera.getxOff() : 0;
            int yOff = (camera != null) ? (int)camera.getyOff() : 0;

            //TODO dialog or deasupra carac or defualt dreapta jos
        }
    }

    public void toggleDialog() {
        showDialog = !showDialog;
    }

    public float getX() { return x; }
    public float getY() { return y; }
}
