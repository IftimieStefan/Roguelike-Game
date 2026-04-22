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
    private float speed = 3.0f;
    private boolean up, down, left, right; // Starea tastelor
    private BufferedImage currentFrame;
    private int i=0;
    private int timer=0;
    private final int ANIMATION_SPEED=10;

    public Player(float startX, float startY, Canvas canvas)
    {
        this.x = startX;
        this.y = startY;
        currentFrame = Assets.playerDown[0];


        canvas.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                int key=e.getKeyCode();
                if(key == KeyEvent.VK_W) up = true;
                if(key == KeyEvent.VK_S) down = true;
                if(key == KeyEvent.VK_A) left = true;
                if(key == KeyEvent.VK_D) right = true;
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_W) up = false;
                if(key == KeyEvent.VK_S) down = false;
                if(key == KeyEvent.VK_A) left = false;
                if(key == KeyEvent.VK_D) right = false;
            }
        });

        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
    }

    public void Update()
    {
        boolean isMoving=false;
        if(up)
        {
            y -= speed;
            currentFrame = Assets.playerUp[i];
            isMoving=true;
        }
        if(down)
        {
            y += speed;
            currentFrame = Assets.playerDown[i];
            isMoving=true;
        }
        if(left)
        {
            x -= speed;
            currentFrame = Assets.playerLeft[i];
            isMoving=true;
        }
        if(right)
        {
            x += speed;
            currentFrame = Assets.playerRight[i];
            isMoving=true;
        }

        if(isMoving)
        {
            timer++;
            if(timer>ANIMATION_SPEED)
            {
                ++i;
                timer=0;
                if(i==3) i=0;
            }
        } else i=0;
    }

    public void Draw(Graphics g, PaooGame.Core.GameCamera camera)
    {
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;
        g.drawImage(currentFrame, (int)x - xOff, (int)y - yOff, null);
    }

    public float GetX() { return x; }
    public float GetY() { return y; }
}
