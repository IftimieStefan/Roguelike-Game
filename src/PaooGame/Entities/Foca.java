package PaooGame.Entities;

import java.awt.*;

public class Foca extends Inamic{
    private Player player;
    private long lastPrintTime = 0;

    public Foca(float stX, float stY, int stHealth, float stSpeed, Player player) {
        super(stX, stY, stHealth, stSpeed);
        this.player = player;
    }

    @Override
    public void Update() {
        if (player != null) {
            float px = player.GetX();
            float py = player.GetY();

            float dx = px - this.x;
            float dy = py - this.y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance <= 40) { //printeaza attack
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastPrintTime > 1000) { // 1 pe secz
                    System.out.println("attack");
                    lastPrintTime = currentTime;
                }
            } else if (distance > 0) {
                this.x += (dx / distance) * speed;
                this.y += (dy / distance) * speed;
            }
        }
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
