package PaooGame.Entities;
import java.util.ArrayList;
import PaooGame.Graphics.Assets;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

// ASTA_I DINU, taticu' jocului.
public class Player
{

    private float x, y;

    // Viteza ba nene.
    private float speed = 3.0f;

    // Butoane apasate si faze.
    private boolean up, down, left, right, isShooting=false, isReloading=false;

    // Poza lu Dinu din momentu' asta.
    private BufferedImage currentFrame;
    private int i=0;
    private int timer=0;
    private final int ANIMATION_SPEED=10;

    // Bulgarii zburatori ai lu' Dinu.
    private ArrayList<Snowball> snowballs = new ArrayList<>();
    private ArrayList<Bomb> bombs = new ArrayList<>();

    private int attackMode = 0; // Tipul de atac curent
    private long lastAttackTime; // Timpul de la ultimul atac pentru cooldown
    private int attackCooldown = 500; // Cooldown intre bulgari tras (in milisecunde)

    private int lastDir=0; // Ultima directie in care s-a miscat jucatorul pentru directia atacului (0=sus, 1=jos, 2=stanga, 3=dreapta)

    // Gestioneaza munitia
    private int maxAmo=10; // Maxim de bulgari
    private int amo=10; // Numarul curent de bulgari disponibili
    private long lastReload=0; // Ultimul reload time
    private final int reloadTime=200; // Timpul de asteptare intre reîncarcari

    private boolean ePressed = false; // A dat E sau?
    private boolean interact = false; // Parla con NPC-u?

    // Viata playerului
    private int maxHp = 100;
    private int hp = 100;
    private float displayHp = 100;

    // Aatac bomba
    private int currentBomb = 2;
    private int maxBomb = 2;
    private long lastBomb;
    private final long bombReloadTime = 60000;

    public Player(float startX, float startY, Canvas canvas)
    {
        this.x = startX;
        this.y = startY;
        currentFrame = Assets.playerDown[0];
        lastBomb = System.currentTimeMillis();

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
                if(key == KeyEvent.VK_SPACE) isShooting=true;
                if(key == KeyEvent.VK_R) Reload();
                if(key == KeyEvent.VK_Q) throwBomb();
                if(key == KeyEvent.VK_E && !ePressed) {
                    interact = true;
                    ePressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_W) up = false;
                if(key == KeyEvent.VK_S) down = false;
                if(key == KeyEvent.VK_A) left = false;
                if(key == KeyEvent.VK_D) right = false;
                if(key==KeyEvent.VK_SPACE) isShooting=false;
                if(key == KeyEvent.VK_E) ePressed = false;
            }
        });

        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
    }

    private void Attack()
    {
        if(System.currentTimeMillis()-lastAttackTime<attackCooldown) return;
        if(amo==0 || isReloading) return;

        float sX=0,sY=0;

        switch (lastDir)
        {
            case 0: sY = -7; break;
            case 1: sY = 7;  break;
            case 2: sX = -7; break;
            case 3: sX = 7;  break;
        }

        switch (attackMode)
        {
            case 0:
                snowballs.add(new Snowball(x,y,sX,sY));
                --amo;
                break;
            case 1:
                break;
            case 2:
                break;
        }

        lastAttackTime=System.currentTimeMillis();
    }

    public void takeDamage(int catYa){
        this.hp -=catYa;
    }

    private void Reload()
    {
        if(!isReloading && amo<maxAmo)
        {
            isReloading=true;
            lastReload=System.currentTimeMillis();
        }
    }

    private void throwBomb()
    {
        if(currentBomb <= 0) return;

        float dirX = 0, dirY = 0;
        switch (lastDir)
        {
            case 0: dirY = -1; break;
            case 1: dirY = 1;  break;
            case 2: dirX = -1; break;
            case 3: dirX = 1;  break;
        }

        bombs.add(new Bomb(x, y, dirX, dirY));
        currentBomb--;
    }

    public void Update()
    {
        // Tranzitie lina (smooth) pentru bara de viata
        if (displayHp > hp) {
            displayHp -= 0.5f; // Cat de repede sa scada "animatia" barei
            if (displayHp < hp) displayHp = hp;
        } else if (displayHp < hp) {
            displayHp += 0.5f; // Animatie pentru heal
            if (displayHp > hp) displayHp = hp;
        }

        boolean isMoving=false;
        if(up)
        {
            y -= speed;
            currentFrame = Assets.playerUp[i];
            isMoving=true;
            lastDir=0;
        }
        if(down)
        {
            y += speed;
            currentFrame = Assets.playerDown[i];
            isMoving=true;
            lastDir=1;
        }
        if(left)
        {
            x -= speed;
            currentFrame = Assets.playerLeft[i];
            isMoving=true;
            lastDir=2;
        }
        if(right)
        {
            x += speed;
            currentFrame = Assets.playerRight[i];
            isMoving=true;
            lastDir=3;
        }
        if(isShooting && !isReloading)
        {
            Attack();
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

        // Update proiectile
        for(int k=0; k<snowballs.size();k++)
        {
            Snowball s=snowballs.get(k);
            s.Update();
            if(!s.isActive()) {
                snowballs.remove(k);
                k--;
            }
        }

        // Update bombe
        for(int k=0; k<bombs.size();k++)
        {
            Bomb b=bombs.get(k);
            b.Update();
            if(!b.isActive()) {
                bombs.remove(k);
                k--;
            }
        }

        // Reload muniție
        if(isReloading)
        {
            if (System.currentTimeMillis() - lastReload >= reloadTime)
            {
                amo++;
                lastReload = System.currentTimeMillis();

                if (amo >= maxAmo)
                {
                    isReloading = false;
                }
            }
        }
        // Regenerare bombe
        if(currentBomb < maxBomb)
        {
            if(System.currentTimeMillis() - lastBomb >= bombReloadTime)
            {
                currentBomb++;
                lastBomb = System.currentTimeMillis();
            }
        } else {
            lastBomb = System.currentTimeMillis();
        }
    }


    public void Draw(Graphics g, PaooGame.Core.GameCamera camera)
    {
        //lume
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;

        //  proiectilele
        for (Snowball s:snowballs) s.Draw(g,camera);
        for (Bomb b:bombs) b.Draw(g,camera);

        //player
        g.drawImage(currentFrame, (int)x - xOff, (int)y - yOff, null);

        //hud
        drawSnowballCounter(g);
        drawBombCounter(g);

    }

    private void drawSnowballCounter(Graphics g) {
        int r = 32;
        int hudX = 1080 - 32, hudY = 32;

        g.setColor(Color.BLACK);
        g.fillOval(hudX - 1, hudY - 1, r + 4, r + 4);
        g.setColor(Color.WHITE);
        g.fillOval(hudX, hudY, r, r);
        g.setColor(new Color(200, 200, 200));
        g.drawOval(hudX, hudY, r, r);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(amo + "/" + maxAmo, hudX + r + 10, hudY + 24);
    }

    private void drawBombCounter(Graphics g) {
        int r = 32;
        int hudX = 1080 - 32, hudY = 32;
        int bombHudY = hudY + 45;

        g.setColor(Color.BLACK);
        g.fillOval(hudX - 1, bombHudY - 1, r + 4, r + 4);
        g.setColor(Color.DARK_GRAY);
        g.fillOval(hudX, bombHudY, r, r);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("x" + currentBomb, hudX + r + 10, bombHudY + 24);
    }

    public float GetX() { return x; }
    public float GetY() { return y; }

    public boolean consumeInteract() {
        if (interact) {
            interact = false;
            return true;
        }
        return false;
    }

    public ArrayList<Snowball> getSnowballs() {
        return snowballs;
    }
}
