package PaooGame.Entities;

import PaooGame.Graphics.Assets;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

//ASTA_I DINU

public class Player
{
    private float x, y;

    public Player(float startX, float startY, Canvas canvas)
    {
        this.x = startX;
        this.y = startY;


        canvas.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {

            }

            @Override
            public void keyReleased(KeyEvent e)
            {

            }
        });

        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
    }

    public void Update()
    {

    }

    public void Draw(Graphics g)
    {
        PaooGame.Core.GameCamera camera = PaooGame.Core.GameCamera.getInstance();
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;
        g.drawImage(Assets.playerRight, (int)x - xOff, (int)y - yOff, null);
    }

    public float GetX() { return x; }
    public float GetY() { return y; }
}
