package PaooGame;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.States.MainMenuState;
import PaooGame.Tiles.Tile;
import PaooGame.Entities.Player;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game implements Runnable
{
    public enum GameState { MENU, PLAYING }

    private GameWindow      wnd;

    private boolean         runState;
    private Thread          gameThread;
    private BufferStrategy  bs;
    private Graphics        g;
    private String title;

    private GameState       gameState;
    private MainMenuState   mainMenu;
    private Player          player;     //DINU

    public Game(String title, int width, int height)
    {
        wnd      = new GameWindow(title, width, height);
        SetTitle(title);

        runState = false;
    }

    private void InitGame()
    {
        wnd = new GameWindow(this.title, wnd.GetWndWidth(), wnd.GetWndHeight());
        wnd.BuildGameWindow();
//        Assets.Init();


        gameState = GameState.MENU;
        mainMenu  = new MainMenuState(wnd);


    }

    public void SetTitle(String title){
        this.title=title;
    }

    public void run()
    {
        InitGame();
        long oldTime = System.nanoTime();
        long curentTime;

        final int    framesPerSecond = 60;
        final double timeFrame       = 1000000000 / framesPerSecond;

        while(runState == true)
        {
            curentTime = System.nanoTime();
            if((curentTime - oldTime) > timeFrame)
            {
                Update();
                Draw();
                oldTime = curentTime;
            }
        }
    }

    public synchronized void StartGame()
    {
        if(runState == false)
        {
            runState   = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
        else
        {
            return;
        }
    }

    public synchronized void StopGame()
    {
        if(runState == true)
        {
            runState = false;
            try
            {
                gameThread.join();
            }
            catch(InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            return;
        }
    }

    private void Update()
    {
        if(gameState == GameState.MENU)
        {
            mainMenu.Update();

            /// daca da pe play go play
            if(mainMenu.IsPlayPressed())
            {
                gameState = GameState.PLAYING;
                wnd.GetCanvas().requestFocusInWindow();
            }
        }
        else if(gameState == GameState.PLAYING)
        {
            player.Update(); // updatam pe dinu
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

        if(gameState == GameState.MENU)
        {
            mainMenu.Draw(g);
        }
        else if(gameState == GameState.PLAYING)
        {
//            Play state  :
//                    -personaj
//                    -mapa/mape

            player.Draw(g);
        }

        bs.show();
        g.dispose();
    }
}