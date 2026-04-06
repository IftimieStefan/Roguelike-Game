package PaooGame.Entities;

import PaooGame.Graphics.Assets;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

//ASTA_I DINU

public class Player
{

    public Player(float startX, float startY, Canvas canvas)
    {


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

    }

    public float GetX() { return 0; }
    public float GetY() { return 0; }
}
