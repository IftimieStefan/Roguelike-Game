package PaooGame.Entities.Player;
import java.util.ArrayList;

import PaooGame.Managers.StateManager;
import PaooGame.States.PlayState;
import PaooGame.Entities.Projectiles.Bomb;
import PaooGame.Entities.Projectiles.Snowball;
import PaooGame.Graphics.Assets;
import PaooGame.Graphics.MapLoader;
import PaooGame.Managers.CollisionSystem;
import PaooGame.Managers.LevelManager;
import PaooGame.Tiles.Tile;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferedImage;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

// ASTA_I DINU, taticu' jocului.
public class Player
{
    private Canvas canvas;

    //coordonatele
    private float x, y;

    //directiile
    private int dirX,dirY;

    // Viteza ba nene.
    private float speed = 4.0f;
    private boolean isMoving=false;

    // Butoane apasate si faze.
    private final KeyListener keyControls;
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
    private long lastAttackTime; // Timpul de la ultimul atac pentru cooldown
    private int attackCooldown = 500; // Cooldown intre bulgari tras (in milisecunde)
    private int rangeDMG=5;

    //abilitatile lui Dinu
    private ArrayList<Ability> abilities = new ArrayList<>();
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private int abilityIndex =0;
    private boolean abilityPressed=false;


    private int lastDir=0; // Ultima directie in care s-a miscat jucatorul pentru directia atacului (0=sus, 1=jos, 2=stanga, 3=dreapta)

    // Gestioneaza munitia
    private int maxAmo=10; // Maxim de bulgari
    private int amo=10; // Numarul curent de bulgari disponibili
    private long lastReload=0; // Ultimul reload time
    private int reloadTime=200; // Timpul de asteptare intre reîncarcari

    private boolean ePressed = false; // A dat E sau?
    private boolean interact = false; // Parla con NPC-u?

    //Atac mele
    private boolean isMeleeAttacking = false;
    private long meleeTimer = 0;
    private final long meleeDuration = 150;
    private boolean meleeOnly=false;
    private int meleeDMG=10;

    //Dash
    private boolean isDashing=false;
    private int dashX,dashY;
    private float dashDistance=80;//distanta;
    private float dashSpeed=0.15f;
    private float dashProgress=0;
    private long lastDash;
    private int dashReloadTime=2000;

    public Player(Canvas canvas)
    {
        this.canvas=canvas;
//        this.x = startX;
//        this.y = startY;
        currentFrame = Assets.playerDown[0];

        //ii dam abilitati lui Dinu
        abilities.add(new BombAbility());
        abilities.add(new FervorAbility());
        abilities.add(new BerserkAbility());

        keyControls=new KeyAdapter()
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
                if(key == KeyEvent.VK_Q) abilityPressed=true;
                if(key == KeyEvent.VK_SPACE) dash();
                if(key == KeyEvent.VK_E && !ePressed) {
                    interact = true;
                    ePressed = true;
                }
                if (key >= KeyEvent.VK_1 && key <= KeyEvent.VK_3)
                {
                    int targetIndex = key - KeyEvent.VK_1;

                    if (targetIndex < abilities.size())
                    {
                        abilityIndex = targetIndex;
                    }
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
                if(key == KeyEvent.VK_Q) abilityPressed=false;
                if(key == KeyEvent.VK_E) ePressed = false;
            }
        };

        canvas.addKeyListener(keyControls);

        canvas.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();

                if (!abilities.isEmpty()) {
                    if (rotation > 0) {
                        abilityIndex = (abilityIndex + 1) % abilities.size();
                    } else {
                        abilityIndex = (abilityIndex - 1 + abilities.size()) % abilities.size();
                    }
                }
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

        int sX=0,sY=0;
        switch (lastDir)
        {
            case 0: sY=-1; break; // Sus
            case 1: sY =1;  break; // Jos
            case 2: sX = -1; break; // Stânga
            case 3: sX = 1;  break; // Dreapta
        }
        if(attackMode==0)
        {
            if(amo==0 || isReloading) return;
            PaooGame.Audio.AudioPlayer.playSound("res/audio/throw.wav");
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

    private void useAbilty(LevelManager lm)
    {
        if(!abilities.isEmpty())
        {
            abilities.get(abilityIndex).use(this,lm);
        }
        if(StateManager.GetState() instanceof PlayState) {
            ((PlayState) StateManager.GetState()).modifyScore(-300);
        }
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
            PaooGame.Audio.AudioPlayer.playSound("res/audio/oof.wav");
            currentHP--;
            lastiFrame=System.currentTimeMillis();
            lastDamage=System.currentTimeMillis();
            if(StateManager.GetState() instanceof PlayState)
            {
                ((PlayState) StateManager.GetState()).modifyScore(-500);
            }
        }
    }

    private void dash()
    {

        if(System.currentTimeMillis()-lastDash<dashReloadTime || dirX==0 && dirY==0) return;

        //invulnerabilitate pt feel
        isDashing=true;
        dashX=dirX;
        dashY=dirY;
        PaooGame.Audio.AudioPlayer.playSound("res/audio/dash.wav");
        lastiFrame=System.currentTimeMillis();
        lastDash=System.currentTimeMillis();
    }

    public void Update(LevelManager lm)
    {
        if(!isDashing)
        {
        isMoving=false;
        if(up)
        {
            dirY=-1;
            currentFrame = Assets.playerUp[i];
            isMoving=true;
            lastDir=0;
        }
        else if(down)
        {
            dirY=1;
            currentFrame = Assets.playerDown[i];
            isMoving=true;
            lastDir=1;
        }
        else
        {
            dirY=0;
        }
        if(left)
        {
            dirX=-1;
            currentFrame = Assets.playerLeft[i];
            isMoving=true;
            lastDir=2;
        }
        else if(right)
        {
            dirX=+1;
            currentFrame = Assets.playerRight[i];
            isMoving=true;
            lastDir=3;
        }
        else
        {
            dirX=0;
        }
        if(isMoving && lm != null && lm.getCurrentMap() != null)
        {
            float newX = x + dirX * speed;
            float newY = y + dirY * speed;

            // Verificam coliziunea pe axa X separat
            if (dirX != 0 && !CollisionSystem.collidesWithWall(newX, y, hitboxOffset, hitboxSize, hitboxSize)) {
                x = newX;
            }
            // Verificam coliziunea pe axa Y separat
            if (dirY != 0 && !CollisionSystem.collidesWithWall(x, newY, hitboxOffset, hitboxSize, hitboxSize)) {
                y = newY;
            }
        }
        else if(isMoving)
        {
            x = x + dirX * speed;
            y = y + dirY * speed;
        }
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

        //Update abilitati
        if(abilityPressed)
        {
            useAbilty(lm);
            abilityPressed=false;
        }

        if(!abilities.isEmpty())
        {
            abilities.get(abilityIndex).Update();

            for(Ability a:abilities)
            {
                if (a instanceof FervorAbility)
                {
                    FervorAbility fervor = (FervorAbility) a;
                    if (fervor.isActive() && System.currentTimeMillis() - fervor.getActivationTime() > fervor.getDuration()) {

                        this.attackCooldown = 500;
                        this.reloadTime = 200;
                        fervor.setActive(false);
                    }
                }
                else if (a instanceof BerserkAbility) {
                    BerserkAbility berserk = (BerserkAbility) a;
                    if (berserk.isActive() && System.currentTimeMillis() - berserk.getActivationTime() > berserk.getDuration()) {
                        this.meleeOnly = false;
                        this.meleeDMG = 10;
                        this.speed = 4.0f;
                        berserk.setActive(false);
                    }
                }
            }
        }

        // Update bombe
        for(int k=0; k<bombs.size();k++)
        {
            Bomb b=bombs.get(k);
            b.Update(lm.getEntityManager());
            if(!b.isActive()) {
                bombs.remove(k);
                k--;
            }
        }

        //selectare atac
        if(meleeOnly || rightClick)
        {
            Attack(1);
        }
        else if(leftClick)
        {
            Attack(0);
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

        // Cooldown atac mele
        if(isMeleeAttacking)
        {
            if(System.currentTimeMillis() - meleeTimer > meleeDuration)
            {
                isMeleeAttacking = false;
            }
        }

        if(isDashing)
        {
            lastiFrame = System.currentTimeMillis();
            int centerX = (int)(x + hitboxOffset + hitboxSize / 2) / Tile.TILE_WIDTH;
            int centerY = (int)(y + hitboxOffset + hitboxSize / 2) / Tile.TILE_HEIGHT;
            int currentTileId = lm.getCurrentMap().getTileId(centerX, centerY);
            boolean onIce = (currentTileId == 22);

            if(dashProgress<1 || onIce) {
                if (!onIce) {
                    dashProgress = dashProgress + dashSpeed;
                }

                float dashMoveX = dashX * dashDistance * dashSpeed;
                float dashMoveY = dashY * dashDistance * dashSpeed;

                // Coliziune si in dash
                if (lm != null && lm.getCurrentMap() != null) {
                    if (!CollisionSystem.collidesWithWall(x + dashMoveX, y, hitboxOffset, hitboxSize, hitboxSize)) {
                        x = x + dashMoveX;
                    } else {
                        isDashing = false;
                        dashProgress = 0;
                    }
                    if (!CollisionSystem.collidesWithWall(x, y + dashMoveY, hitboxOffset, hitboxSize, hitboxSize))
                    {
                        y = y + dashMoveY;
                    } else { 
                        isDashing = false;
                        dashProgress = 0;
                    }
                } else {
                    x = x + dashMoveX;
                    y = y + dashMoveY;
                }
            }else {
                dashProgress = 0;
                isDashing=false;
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

        //desenare scor
        if (StateManager.GetState() instanceof PlayState) {
            int currentScor = ((PlayState) StateManager.GetState()).getScore();
            g.setColor(new Color(200, 200, 200));
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + currentScor, 50, 120);
        }

        drawSnowballCounter(g);

        if(!abilities.isEmpty())
        {
            Ability activeAbility = abilities.get(abilityIndex);

            if (activeAbility instanceof BombAbility)
            {
                drawBombHUD(g,activeAbility);
            }
            else if (activeAbility instanceof FervorAbility)
            {
                drawFervorHUD(g, (FervorAbility) activeAbility);
            }
            else if (activeAbility instanceof BerserkAbility)
            {
                drawBerserkHUD(g, (BerserkAbility) activeAbility);
            }
        }
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

    private void drawBombHUD(Graphics g, Ability bomb) {
        int r = 32;
        int gap = 38;
        int hudX = canvas.getWidth() - (maxHP * gap) - 40;
        int hudY = 95;

        // Desenăm cercul de fundal
        g.setColor(Color.BLACK);
        g.fillOval(hudX - 1, hudY - 1, r + 4, r + 4);
        g.setColor(Color.DARK_GRAY);
        g.fillOval(hudX, hudY, r, r);

        // Afișăm numărul de bombe
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("x" + bomb.getCharges(), hudX + r + 10, hudY + 25);
    }

    private void drawFervorHUD(Graphics g, FervorAbility fervor) {
        int barWidth = 100;
        int barHeight = 20;
        int gap = 38;
        int hudX = canvas.getWidth() - (maxHP * gap) - 40;
        int hudY = 110;

        // fundalul barei
        g.setColor(Color.BLACK);
        g.fillRect(hudX, hudY, barWidth, barHeight);

        float progress = 1.0f;
        Color barColor = Color.GRAY;

        if (fervor.isActive()) {
            long timeActive = System.currentTimeMillis() - fervor.getActivationTime();
            progress = 1.0f - (float) timeActive / fervor.getDuration();
            barColor=Color.ORANGE;
        }
        else if (fervor.getCharges() > 0)
        {
            progress = 1.0f;
            barColor = Color.GREEN;
        }
        else
        {
            long timeSinceLastUse = System.currentTimeMillis() - fervor.getActivationTime();
            progress = (float) timeSinceLastUse / 30000; // 30000 e cooldown-ul tău
            barColor = Color.DARK_GRAY;
        }


        progress = Math.min(1.0f, Math.max(0f, progress));
        g.setColor(barColor);
        g.fillRect(hudX + 2, hudY + 2, (int) ((barWidth - 4) * progress), barHeight - 4);

        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.BOLD, 12));
        String status = fervor.isActive() ? "ACTIVE" : (fervor.getCharges() > 0 ? "FERVOR READY" : "RECHARGING...");
        g.drawString(status, hudX, hudY - 5);
    }

    private void drawBerserkHUD(Graphics g, BerserkAbility berserk) {
        int barWidth = 100;
        int barHeight = 20;
        int gap = 38;
        int hudX = canvas.getWidth() - (maxHP * gap) - 40;
        int hudY = 110;

        g.setColor(Color.BLACK);
        g.fillRect(hudX, hudY, barWidth, barHeight);

        float progress = 1.0f;
        Color barColor = Color.GRAY;

        if (berserk.isActive()) {
            long timeActive = System.currentTimeMillis() - berserk.getActivationTime();
            progress = 1.0f - (float) timeActive / berserk.getDuration();
            barColor=Color.RED;
        }
        else if (berserk.getCharges() > 0)
        {
            progress = 1.0f;
            barColor = Color.GREEN;
        }
        else
        {
            long timeSinceLastUse = System.currentTimeMillis() - berserk.getActivationTime();
            progress = (float) timeSinceLastUse / 30000; // 30000 e cooldown-ul tău
            barColor = Color.DARK_GRAY;
        }


        progress = Math.min(1.0f, Math.max(0f, progress));
        g.setColor(barColor);
        g.fillRect(hudX + 2, hudY + 2, (int) ((barWidth - 4) * progress), barHeight - 4);

        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.BOLD, 12));
        String text = berserk.isActive() ? "ACTIVE" : (berserk.getCharges() > 0 ? "BERSERK READY" : "RECHARGING...");
        g.drawString(text, hudX, hudY - 5);
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

    public float getX() { return x; }
    public float getY() { return y; }
    public int getLastDir(){return lastDir;}
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

    public void setAttackCooldown(int attackCooldown){
        this.attackCooldown=attackCooldown;
    }

    public void setReloadTime(int reloadTime) {
        this.reloadTime=reloadTime;
    }

    public void setMeleeOnly(boolean state){
        this.meleeOnly=state;
    }

    public void setMeleeDMG(int meleeDMG){
        this.meleeDMG=meleeDMG;
    }
    public int getMeleeDMG(){
        return this.meleeDMG;
    }

    public int getRangeDMG(){
        return this.rangeDMG;
    }

    public int getCurrentHP(){return this.currentHP;}
    public void setCurrentHP(int HP){this.currentHP=HP;}

    public void setSpeed(float speed){
        this.speed=speed;
    }

    public KeyListener getKeyControls() {
        return keyControls;
    }

}
