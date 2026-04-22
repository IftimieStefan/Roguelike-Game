package PaooGame.Entities;

import PaooGame.Core.GameCamera;
import java.awt.*;

public class Bity extends Npc {

    public Bity(float x, float y) {
        super(x, y, "Bity", "nmk");
    }

    @Override
    public void Update() {
        //sta pi loc
    }

    @Override
    public void Draw(Graphics g) {
        GameCamera camera = GameCamera.getInstance();
        int xOff = (camera != null) ? (int) camera.getxOff() : 0;
        int yOff = (camera != null) ? (int) camera.getyOff() : 0;

        // patrat NPC verde
        g.setColor(Color.GREEN);
        g.fillRect((int) x - xOff, (int) y - yOff, 48, 48);

        // contur negru
        g.setColor(Color.BLACK);
        g.drawRect((int) x - xOff, (int) y - yOff, 48, 48);

        // numele deasupra
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        
        int textX = (int) x - xOff + (48 - fm.stringWidth(name)) / 2;
        int textY = (int) y - yOff - 5;

        g.drawString(name, textX, textY);

        super.Draw(g);
    }
}
