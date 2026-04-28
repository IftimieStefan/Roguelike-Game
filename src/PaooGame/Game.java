package PaooGame;

import PaooGame.Core.GameCamera;
import PaooGame.Entities.Enemies.EnemyFactory;
import PaooGame.Entities.NPCs.Bity;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.States.*;
import PaooGame.Entities.Player.Player;

import java.awt.*;
import java.awt.image.BufferStrategy;

// Inima jocului, tine tot frate.
// Rularea pe alt thread curat.
public class Game implements Runnable
{
    public enum GameState { MENU, SAVE_SELECTION, PLAYING, OPTIONS }

    private GameWindow      wnd;
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
    private PaooGame.MapLoader currentMap;
    
    // Seful la inamici si chestii.
    private PaooGame.Managers.EntityManager entityManager;

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

                    currentMap = new PaooGame.MapLoader("res/harti/Level1/room1.txt");//incarcam nivelul 1
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


                    entityManager = new PaooGame.Managers.EntityManager();
                    
                    entityManager.addEnemy(EnemyFactory.createEnemy("BOSS_COWBOY", 300, 300, player));
                    entityManager.addEnemy(EnemyFactory.createEnemy("FAST_FOCA", 300, 3500, player));
                    entityManager.addEnemy(EnemyFactory.createEnemy("FOCA_COWBOY", 300, 320, player));
                    
                    entityManager.addNpc(new Bity(200, 200));

                    camera = GameCamera.getInstance(wnd.GetWndWidth(), wnd.GetWndHeight());
                    camera.centerOnDinu(player);


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
                if (currentMap !=null) currentMap.Update();
                if (player != null) player.Update();
                if (entityManager != null) {
                    entityManager.Update();
                    entityManager.checkInteractions(player);
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
            if(currentMap!=null) currentMap.Draw(g, camera);
            if(player!=null) player.Draw(g, camera);
            if(entityManager!=null) entityManager.Draw(g);
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