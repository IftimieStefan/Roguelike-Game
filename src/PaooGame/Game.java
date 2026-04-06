package PaooGame;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.States.*;
import PaooGame.Tiles.Tile;
import PaooGame.Entities.Player;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game implements Runnable
{
    public enum GameState { MENU, SAVE_SELECTION, PLAYING }

    private GameWindow      wnd;
    private boolean         runState;
    private Thread          gameThread;
    private BufferStrategy  bs;
    private Graphics        g;
    private String title;

    private GameState       gameState;
    private SaveSelectionState saveMenu;
    private MainMenuState   mainMenu;
    private Player          player;     //DINU

    private float animTime = 0;

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
        saveMenu = new SaveSelectionState(wnd);

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
        animTime += 0.005;

        if(gameState == GameState.MENU)
        {
            mainMenu.Update(animTime);

            /// daca da pe play go play
            if(mainMenu.IsPlayPressed())
            {
                ClearMouseListeners();
                gameState = GameState.SAVE_SELECTION;
                saveMenu = new SaveSelectionState(wnd);
                mainMenu.Reset();
            }
        }
            else if(gameState == GameState.SAVE_SELECTION)
            {
                saveMenu.Update(animTime);

                if(saveMenu.GetSelectedSlot()!=-1)
                {
                    int idSalvare = saveMenu.GetSelectedSlot();//ce salvare incarcam

                    ClearMouseListeners();
                    gameState = GameState.PLAYING;
                    wnd.GetCanvas().requestFocusInWindow();
                }

                if(saveMenu.IsBackPressed())
                {
                    ClearMouseListeners();
                    gameState=GameState.MENU;
                    mainMenu=new MainMenuState(wnd);
                    saveMenu.Reset();
                }
            }
            else if(gameState==GameState.PLAYING)
            {
                if (player != null) player.Update();
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
        else if(gameState == GameState.SAVE_SELECTION)
        {
            saveMenu.Draw(g);
        }
        else if(gameState == GameState.PLAYING)
        {
            if(player!=null) player.Draw(g);
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
    }

}