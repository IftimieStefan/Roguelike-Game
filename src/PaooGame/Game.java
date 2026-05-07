package PaooGame;

import PaooGame.Core.GameCamera;
import PaooGame.Entities.Enemies.EnemyFactory;
import PaooGame.Entities.NPCs.Bity;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Managers.LevelManager;
import PaooGame.Managers.StateManager;
import PaooGame.States.*;
import PaooGame.Entities.Player.Player;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;

// Inima jocului, tine tot frate.
// Rularea pe alt thread curat.
public class Game implements Runnable {
//    public enum GameState { MENU, SAVE_SELECTION, PLAYING, OPTIONS }

    private GameWindow wnd;
    private boolean runState;
    private Thread gameThread;
    private BufferStrategy bs;
    private Graphics g;
    private String title;

    private GameCamera camera;

    // Main character-ul
    private Player player;     // DINU - personajul principal

    // Extrage harta din txt.
    private PaooGame.Graphics.MapLoader currentMap;

    // Seful la scene si chestii
    private LevelManager levelManager;

    private float animTime = 0;
    private boolean mouseListenerAdded = false;

    public Game(String title, int width, int height) {
        wnd = new GameWindow(title, width, height);
        SetTitle(title);
        runState = false;
    }

    private void InitGame() {
        wnd.BuildGameWindow();
        Assets.Init();
        StateManager.SetState(new MainMenuState(wnd));
    }

    // Pune titlu la joc simplu.
    public void SetTitle(String title) {
        this.title = title;
    }

    public void run() {
        InitGame();
        long oldTime = System.nanoTime();
        long curentTime;

        final int framesPerSecond = 60;
        final double timeFrame = 1000000000 / framesPerSecond;

        while (runState == true) {
            curentTime = System.nanoTime();
            if ((curentTime - oldTime) > timeFrame) {
                Update();
                Draw();
                oldTime = curentTime;
            }
        }
    }

    public synchronized void StartGame() {
        if (runState == false) {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        } else {
            return;
        }
    }

    public synchronized void StopGame() {
        if (runState == true) {
            runState = false;
            try {
                gameThread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } else {
            return;
        }
    }

    private void Update()
    {
        animTime += 0.005;
        if(StateManager.GetState() != null) {
            StateManager.GetState().Update(animTime);
        }
    }

    private void Draw()
    {
        bs = wnd.GetCanvas().getBufferStrategy();
        if(bs == null)
        {
            try
            {
                wnd.GetCanvas().createBufferStrategy(3);
                return;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        g = bs.getDrawGraphics();
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        if(StateManager.GetState() != null) {
            StateManager.GetState().Draw(g);
        }

        bs.show();
        g.dispose();
    }

    private void ClearMouseListeners()
    {
        for (java.awt.event.MouseListener ml : wnd.GetCanvas().getMouseListeners())
        {
            wnd.GetCanvas().removeMouseListener(ml);
        }
        mouseListenerAdded=false;
    }

}