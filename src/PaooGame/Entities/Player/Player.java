package PaooGame.Entities.Player;
import java.util.ArrayList;

import PaooGame.Entities.Projectiles.Bomb;
import PaooGame.Entities.Projectiles.Snowball;
import PaooGame.Graphics.Assets;
import PaooGame.Managers.EntityManager;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

// ASTA_I DINU, taticu' jocului.
public class Player
{
    private Canvas canvas;

    private float x, y;

    // Viteza ba nene.
    private float speed = 3.0f;

    // Butoane apasate si faze.
    private boolean up, down, left, right, leftClick, rightClick, isReloading=false;

    // Poza lu Dinu din momentu' asta.
    private BufferedImage currentFrame;
    private int i=0;
    private int timer=0;
    private final int ANIMATION_SPEED=10;

    // detalii hitbox
    private int hitboxOffset=4;
    private int hitboxSize=40;

    //HP Dinu
    private int maxHP=5;
    private int currentHP=5;
    private long lastDamage=0;//pt animatie

    //cnt iframe
    private long iFrame=1000;
    private long lastiFrame=0;

    // Bulgarii zburatori ai lu' Dinu.
    private ArrayList<Snowball> snowballs = new ArrayList<>();
    private ArrayList<Bomb> bombs = new ArrayList<>();

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

    // Aatac bomba
    private int currentBomb = 2;
    private int maxBomb = 2;
    private long lastBomb;
    private final long bombReloadTime = 60000;

    //Atac mele
    private boolean isMeleeAttacking = false;
    private long meleeTimer = 0;
    private final long meleeDuration = 150;

    //Dash
    private int dashX=80,dashY=80;//distanta
    private long lastDash;
    private int dashReloadTime=2000;

    public Player(Canvas canvas)
    {
        this.canvas=canvas;
//        this.x = startX;
//        this.y = startY;
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
                if(key == KeyEvent.VK_R) Reload();
                if(key == KeyEvent.VK_Q) throwBomb();
                if(key == KeyEvent.VK_SPACE) dash();
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
                if(key == KeyEvent.VK_E) ePressed = false;
            }
        });

    }

    public void setLeftClick(boolean state) { leftClick = state; }
    public void setRightClick(boolean state) { rightClick = state; }

    public Rectangle getHitbox()
    {
        return new Rectangle((int)x + hitboxOffset, (int)y+hitboxOffset,hitboxSize,hitboxSize);
    }

    public void Attack(int attackMode)
    {
        if(System.currentTimeMillis()-lastAttackTime<attackCooldown) return;

        if(attackMode==0)
        {
            if(amo==0 || isReloading) return;
            float sX=0,sY=0;
            switch (lastDir)
            {
                case 0:
                    sY = -7;
                    break;
                case 1:
                    sY = 7;
                    break;
                case 2:
                    sX = -7;
                    break;
                case 3:
                    sX = 7;
                    break;
            }
            snowballs.add(new Snowball(x,y,sX,sY));
            amo--;
            lastAttackTime=System.currentTimeMillis();
        }
        else if (attackMode==1)
        {
            isMeleeAttacking=true;
            meleeTimer=System.currentTimeMillis();
            lastAttackTime=System.currentTimeMillis();
        }
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

    public Rectangle getMeleeHitbox()
    {
        int range = 40; // Cât de departe ajunge lovitura
        if (!isMeleeAttacking) return new Rectangle(0, 0, 0, 0);

        switch (lastDir)
        {
            case 0: return new Rectangle((int)x + 8, (int)y - range, 48, range); // Sus
            case 1: return new Rectangle((int)x + 8, (int)y + 64, 48, range);    // Jos
            case 2: return new Rectangle((int)x - range, (int)y + 8, range, 48); // Stânga
            case 3: return new Rectangle((int)x + 64, (int)y + 8, range, 48);    // Dreapta
            default: return new Rectangle(0, 0, 0, 0);
        }
    }

    public boolean isMeleeAttacking()
    {
        return isMeleeAttacking;
    }

    public void stopMelee()
    {
        this.isMeleeAttacking=false;
        this.meleeTimer=0;
    }

    public void takeDamage()
    {
        if(System.currentTimeMillis()-lastiFrame>=iFrame)
        {
            currentHP--;
            lastiFrame=System.currentTimeMillis();
            lastDamage=System.currentTimeMillis();
        }
       // if(currentHP==0) gameOver();
    }

    private void dash()
    {
        if(System.currentTimeMillis()-lastDash<dashReloadTime) return;

        //invulnerabilitate pt feel
        lastiFrame=System.currentTimeMillis();

        switch(lastDir)
        {
            case 0: // Sus
                y=y-dashX;
                break;
            case 1: // Jos
                y=y+dashY;
                break;
            case 2: // Stânga
                x=x-dashX;
                break;
            case 3: // Dreapta
                x=x+dashX;
                break;
        }
        lastDash=System.currentTimeMillis();
    }

    public void Update(EntityManager em)
    {
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
            b.Update(em);
            if(!b.isActive()) {
                bombs.remove(k);
                k--;
            }
        }

        //selectare atac
        if(leftClick)
        {
            Attack(0);
        }
        else if(rightClick)
        {
            Attack(1);
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

        // Cooldown atac mele
        if(isMeleeAttacking)
        {
            if(System.currentTimeMillis() - meleeTimer > meleeDuration)
            {
                isMeleeAttacking = false;
            }
        }

    }

    public void Draw(Graphics g, PaooGame.Core.GameCamera camera)
    {
        //lume
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;

        // Desenează proiectilele
        for (Snowball s:snowballs) s.Draw(g,camera);
        for (Bomb b:bombs) b.Draw(g,camera);

        //animatie invulnerabilitate
        boolean drawPlayer = true;
        if (System.currentTimeMillis() - lastDamage < iFrame) {
            if ((System.currentTimeMillis() / 50) % 2 == 0) {
                drawPlayer = false;
            }
        }

        //player

        if(drawPlayer)
        {
            g.drawImage(currentFrame, (int) x - xOff, (int) y - yOff, null);
        }

        //atac melee
        drawMelee(g,xOff,yOff);
    }

    public void DrawHUD(Graphics g)
    {
        drawHP(g);
        drawSnowballCounter(g);
        drawBombCounter(g);
    }

    private void drawHP(Graphics g)
    {
        int r = 32;
        int gap = 38;
        int hudX = (maxHP * gap)  ;
        int hudY = 100;
        for (int h = 0; h < maxHP; h++) {
            int heartX = hudX + (h * gap) - 140;

            g.setColor(Color.BLACK);
            g.drawOval(heartX - 2, hudY - 42, r + 4, r + 4);

            if (h < currentHP) {
                g.setColor(Color.RED);
                g.fillOval(heartX, hudY - 40, r, r); // Inima are acum mărimea r (32px)
            }
        }
    }

    private void drawSnowballCounter(Graphics g) {
        int r = 32;
        int gap=38;
        int hudX = canvas.getWidth() - (maxHP * gap) - 40;
        int hudY = 50;


        g.setColor(Color.BLACK);
        g.fillOval(hudX - 1, hudY - 1, r + 4, r + 4);
        g.setColor(Color.WHITE);
        g.fillOval(hudX, hudY, r, r);
        g.setColor(new Color(200, 200, 200));
        g.drawOval(hudX, hudY, r, r);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(amo + "/" + maxAmo, hudX + r + 10, hudY + 30 );
    }

    private void drawBombCounter(Graphics g) {
        int r = 32;
        int gap = 38;
        int hudX = canvas.getWidth() - (maxHP * gap) - 40,hudY=50;
        int bombHudY = hudY + 45;

        g.setColor(Color.BLACK);
        g.fillOval(hudX - 1, bombHudY - 1, r + 4, r + 4);
        g.setColor(Color.DARK_GRAY);
        g.fillOval(hudX, bombHudY, r, r);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("x" + currentBomb, hudX + r + 10, bombHudY + 30 );
    }

    private void drawMelee(Graphics g, int xOff, int yOff)
    {
        if (isMeleeAttacking)
        {
            g.setColor(Color.RED);
            // Coordonate player
            int px = (int) x - xOff;
            int py = (int) y - yOff;

            // Dimensiunile atac
            int width = 0;
            int height = 0;
            int dx = 0; // offset player
            int dy = 0; // offset player

            switch (lastDir)
            {
                case 0: // Sus
                    width = 40; height = 20;
                    dx = 4; dy = -20;
                    break;
                case 1: // Jos
                    width = 40; height = 20;
                    dx = 4; dy = 48; // Jos
                    break;
                case 2: // Stânga
                    width = 20; height = 40;
                    dx = -20; dy = 4;
                    break;
                case 3: // Dreapta
                    width = 20; height = 40;
                    dx = 48; dy = 4;
                    break;
            }
            g.fillRect(px + dx, py + dy, width, height);
        }
    }

    public float GetX() { return x; }
    public float GetY() { return y; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }


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

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }
}
