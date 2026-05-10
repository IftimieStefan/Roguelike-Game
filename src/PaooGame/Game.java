package PaooGame;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Managers.DatabaseManager;
import PaooGame.Managers.StateManager;
import PaooGame.States.*;

import java.awt.*;
import java.awt.image.BufferStrategy;

// Inima jocului, tine tot frate.
// Rularea pe alt thread curat.
public class Game implements Runnable {

    private GameWindow wnd;
    private boolean runState;
    private Thread gameThread;
    private BufferStrategy bs;
    private Graphics g;

    private float animTime = 0;

    public Game(String title, int width, int height) {
        wnd = new GameWindow(title, width, height);
        runState = false;
    }

    private void InitGame() {
        wnd.BuildGameWindow();
        DatabaseManager.Initialize();
        Assets.Init();
        StateManager.SetState(new MainMenuState(wnd));
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
}