package PaooGame;

import PaooGame.Core.GameCamera;
import PaooGame.Entities.Enemies.EnemyFactory;
import PaooGame.Entities.NPCs.Bity;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Managers.EntityManager;
import PaooGame.Managers.LevelManager;
import PaooGame.Graphics.*;
import PaooGame.States.*;
import PaooGame.Entities.Player.Player;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;

// Inima jocului, tine tot frate.
// Rularea pe alt thread curat.
public class Game implements Runnable
{
    public enum GameState { MENU, SAVE_SELECTION, PLAYING, OPTIONS }

    private PaooGame.GameWindow.GameWindow      wnd;
    private boolean         runState;
    private Thread          gameThread;
    private BufferStrategy  bs;
    private Graphics        g;
    private String title;

    private GameCamera camera;

    // Fazele jocului zic, meniurile.
    private GameState       gameState;
    private SaveSelectionState saveMenu;
    private MainMenuState   mainMenu;
    private OptionsState    optionsMenu;
    
    // Main character-ul
    private Player          player;     // DINU - personajul principal

    // Extrage harta din txt.
    private MapLoader currentMap;
    
    // Seful la scene si chestii
    private LevelManager levelManager;

    private float animTime = 0;
    private boolean mouseListenerAdded = false;

    public Game(String title, int width, int height)
    {
        wnd      = new GameWindow(title, width, height);
        SetTitle(title);
        runState = false;
    }

    private void InitGame()
    {
        wnd.BuildGameWindow();
        Assets.Init();

        gameState = GameState.MENU;
        mainMenu  = new MainMenuState(wnd);
        saveMenu = new SaveSelectionState(wnd);
        optionsMenu = new OptionsState(wnd);

    }

    // Pune titlu la joc simplu.
    public void SetTitle(String title){
        this.title=title;
    }

    public void run()
    {
        InitGame();
        long oldTime = System.nanoTime();
        long currentTime;

        final int    framesPerSecond = 60;
        final double timeFrame       = 1000000000 / framesPerSecond;

        while(runState == true)
        {
            currentTime = System.nanoTime();
            if((currentTime - oldTime) > timeFrame)
            {
                Update();
                Draw();
                oldTime = currentTime;
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

        if(gameState == GameState.MENU)//meniu prinipal
        {
            mainMenu.Update(animTime);

            if(mainMenu.IsPlayPressed())
            {
                ClearMouseListeners();
                gameState = GameState.SAVE_SELECTION;//trecem in starea de seletie a salavrilor
                saveMenu = new SaveSelectionState(wnd);
                mainMenu.Reset();
            }
            if(mainMenu.IsOptionsPressed())
            {
                ClearMouseListeners();
                gameState = GameState.OPTIONS;
                optionsMenu = new OptionsState(wnd);
                mainMenu.Reset();
            }
        }
        else if (gameState == GameState.OPTIONS)
        {
            optionsMenu.Update(animTime);
            if (optionsMenu.IsBackPressed())
            {
                ClearMouseListeners();
                gameState = GameState.MENU;
                mainMenu = new MainMenuState(wnd);
                optionsMenu.Reset();
            }
        }
        else if(gameState == GameState.SAVE_SELECTION)
            {
                saveMenu.Update(animTime);

                if(saveMenu.GetSelectedSlot()!=-1)
                {
                    ClearMouseListeners();

                    currentMap = new MapLoader("res/harti/Level1/room1.csv");//incarcam nivelul 1
                    player = new Player(100, 100, wnd.GetCanvas());

                    if(!mouseListenerAdded)
                    {
                        wnd.GetCanvas().addMouseListener(new java.awt.event.MouseAdapter()
                        {
                            @Override
                            public void mousePressed(java.awt.event.MouseEvent e)
                            {
                                if(gameState == GameState.PLAYING && player != null)
                                {
                                    if(e.getButton() == java.awt.event.MouseEvent.BUTTON1)
                                    {
                                        player.setLeftClick(true); // Trage cu bulgări
                                    }
                                    else if(e.getButton() == java.awt.event.MouseEvent.BUTTON3)
                                    {
                                        player.setRightClick(true); // Dă cu pumnul (Melee)
                                    }
                                }
                            }

                            @Override
                            public void mouseReleased(java.awt.event.MouseEvent e)
                            {
                                if(gameState == GameState.PLAYING && player != null)
                                {
                                    if(e.getButton() == java.awt.event.MouseEvent.BUTTON1)
                                    {
                                        player.setLeftClick(false);
                                    }
                                    else if(e.getButton() == java.awt.event.MouseEvent.BUTTON3)
                                    {
                                        player.setRightClick(false);
                                    }
                                }
                            }
                        });
                    }
                    mouseListenerAdded=true;
                    int idSalvare = saveMenu.GetSelectedSlot();//ce salvare incarcam

                    camera = GameCamera.getInstance(wnd.GetWndWidth(), wnd.GetWndHeight());
                    camera.centerOnDinu(player);

                    levelManager = new LevelManager(player, camera);

                    gameState = GameState.PLAYING;//trecem in starea de joc
                    wnd.GetCanvas().requestFocusInWindow();
                }

                if(saveMenu.IsBackPressed())//ne intoarcem la meniul prinipal
                {
                    ClearMouseListeners();
                    gameState=GameState.MENU;
                    mainMenu=new MainMenuState(wnd);
                    saveMenu.Reset();
                }
            }
            else if(gameState==GameState.PLAYING)
            {
                if (player != null) player.Update(levelManager);
                if (levelManager != null) {
                    levelManager.Update();
                }
                if (camera != null && player != null) camera.centerOnDinu(player);
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
        else if(gameState == GameState.OPTIONS)
        {
            optionsMenu.Draw(g);
        }
        else if(gameState == GameState.PLAYING)
        {
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

                // fog of war sub hud peste mapa
                if(levelManager.isFogOfWarActive() && player != null) {
                    //TODO FOW
                }
            }

            // Reseteaza transformarea ca sa desenam HUD-ul normal
            g2d.setTransform(old);

            if(player!=null) player.DrawHUD(g);
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