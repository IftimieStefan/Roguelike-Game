package PaooGame.States;

import PaooGame.Core.GameCamera;
import PaooGame.Entities.Player.Player;
import PaooGame.Game;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Managers.EntityManager;
import PaooGame.Managers.LevelManager;

import java.awt.*;

public class PlayState extends State {

    Player player;
    LevelManager levelManager;
    GameCamera camera;



    public PlayState(GameWindow wnd) {
        super(wnd);

        player= new Player(wnd.GetCanvas());

        camera = GameCamera.getInstance(wnd.GetWndWidth(), wnd.GetWndHeight());
        camera.centerOnDinu(player);

        wnd.GetCanvas().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (player != null) {
                    if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                        player.setLeftClick(true); // Stanga - bulgari
                    } else if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                        player.setRightClick(true); // Dreapta - melee
                    }
                }
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (player != null) {
                    if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                        player.setLeftClick(false);
                    } else if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                        player.setRightClick(false);
                    }
                }
            }
        });

        levelManager = new LevelManager(player,camera);
    }

    @Override
    public void Update(float time) {
        if(player !=null && levelManager != null){
            player.Update(levelManager);
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
    
}
