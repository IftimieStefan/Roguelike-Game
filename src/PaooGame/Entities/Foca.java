package PaooGame.Entities;

import java.awt.*;

public class Foca extends Inamic{
    public Foca(float stX, float stY, int stHealth, float stSpeed) {
        super(stX, stY, 10, 2);
    }

    @Override
    public void Update() {

    }

    @Override
    public void Draw(Graphics g) {
        PaooGame.Core.GameCamera camera = PaooGame.Core.GameCamera.getInstance();
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;

        // patrat enamic
        g.setColor(Color.RED);
        g.fillRect((int)x - xOff, (int)y - yOff, 48, 48);
    }
}
