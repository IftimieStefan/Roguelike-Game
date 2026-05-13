package PaooGame.States;

import PaooGame.Core.GameCamera;
import PaooGame.Entities.Player.Player;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Managers.LevelManager;
import PaooGame.Managers.StateManager;
import PaooGame.Managers.DatabaseManager;
import PaooGame.Managers.Data.SaveData;

import java.awt.*;
import java.awt.event.*;

public class PlayState extends State {

    Player player;
    LevelManager levelManager;
    GameCamera camera;

    private boolean playPause=false;
    private MouseListener mouseListener;
    private KeyListener keyListener;
    private int slot;
    private boolean roomSaved=false;
    private int score = 10000;
    private long lastSec=System.currentTimeMillis();


    public PlayState(GameWindow wnd,int slot) {
        super(wnd);
        this.slot=slot;
        player= new Player(wnd.GetCanvas());

        camera = GameCamera.getInstance(wnd.GetWndWidth(), wnd.GetWndHeight());
        camera.centerOnDinu(player);

        InitInput();

        ActivareInput();

        SaveData data = DatabaseManager.LoadProgress(slot);
        levelManager = new LevelManager(player,camera);

        if(data!=null)
        {
            levelManager.setLevelAndRoom(data.getLevel(), data.getRoom());
            player.setX(data.getX());
            player.setY(data.getY());
            player.setCurrentHP(data.getHP());
            setCurrentScore(data.getScore());
        }
    }

    @Override
    public void Update(float time) {
        if(player !=null && levelManager != null){
            player.Update(levelManager);
        }

        if(player.getCurrentHP()==0) {
            CurataInput();
            StateManager.SetState(new GameOverState(wnd,slot));
            return;
        }

        if(System.currentTimeMillis()-lastSec>=1000)
        {
            if(score > 0) modifyScore(-10);
            lastSec=System.currentTimeMillis();
        }

        if(playPause)
        {
            CurataInput();
            StateManager.SetState(new PauseState(wnd,this));
            playPause=false;
            return;
        }

        if(levelManager.getEntityManager().getEnemies().isEmpty())
        {
            if(!roomSaved) {
                DatabaseManager.SaveProgress(slot, player.getCurrentHP(), player.getX(), player.getY(), levelManager.getCurrentLevel(), levelManager.getCurrentRoom(), score);
                roomSaved=true;
                if (levelManager.getCurrentRoom() == 2 && levelManager.getCurrentLevel() == 3) {
                    CurataInput();
                    StateManager.SetState(new WinState(wnd,score));
                    return;
                }
            }
        } else {
            roomSaved=false;
        }

        if(camera !=null){
            camera.centerOnDinu(player);
        }

        if(levelManager !=null){
            levelManager.Update();
        }


    }

    @Override
    public void Draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        java.awt.geom.AffineTransform old = g2d.getTransform();

        if (camera != null) {
            float zoom = camera.getZoom();
            g2d.scale(zoom, zoom);
        }

        if(levelManager != null) {
            if(levelManager.getCurrentMap() != null) levelManager.getCurrentMap().Draw(g, camera);
            if(player != null) player.Draw(g, camera);
            if(levelManager.getEntityManager() != null) levelManager.getEntityManager().Draw(g);
        }

        // Reseteaza transformarea
        g2d.setTransform(old);

        // Fog of war bazat pe tile-uri (pe toate nivelurile)
        if (levelManager != null && levelManager.getFogOfWar() != null) {
            levelManager.getFogOfWar().Draw(g2d, camera, wnd.GetWndWidth(), wnd.GetWndHeight());
        }

        if(player!=null) player.DrawHUD(g);
    }

    private void InitInput() {
        // Configurăm Mouse Adapter-ul
        mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (player != null) {
                    if (e.getButton() == MouseEvent.BUTTON1) player.setLeftClick(true);
                    else if (e.getButton() == MouseEvent.BUTTON3) player.setRightClick(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (player != null) {
                    if (e.getButton() == MouseEvent.BUTTON1) player.setLeftClick(false);
                    else if (e.getButton() == MouseEvent.BUTTON3) player.setRightClick(false);
                }
            }
        };

        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) playPause = true;
                if (e.getKeyCode() == KeyEvent.VK_N) {
                    if (levelManager != null) {
                        levelManager.skipToNextRoom();
                    }
                }
            }
        };
    }

    public void ActivareInput() {
        // Punem înapoi inputurile
        wnd.GetCanvas().addMouseListener(this.mouseListener);
        wnd.GetCanvas().addKeyListener(this.keyListener);

        // Punem inapoi controalele din player
        if (player != null) {
            wnd.GetCanvas().addKeyListener(player.getKeyControls());

            // Resetăm stările ca sa nu ramana blocat
            player.setLeftClick(false);
            player.setRightClick(false);
        }

        // Forțăm focusul
        wnd.GetCanvas().setFocusable(true);
        wnd.GetCanvas().requestFocusInWindow();
    }

    public void setCurrentScore(int score){this.score=score;}
    public int getScore(){return score;}
    public void modifyScore(int val)
    {
        score = score + val;
        if(score<0) score=0;
    }
}
