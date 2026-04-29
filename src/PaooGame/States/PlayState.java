package PaooGame.States;

import PaooGame.Core.GameCamera;
import PaooGame.Entities.Player.Player;
import PaooGame.Game;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Managers.LevelManager;

import java.awt.*;
import java.awt.image.BufferStrategy;

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
                        player.setLeftClick(false); // Oprește trasul
                    } else if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                        player.setRightClick(false); // Oprește pumnul
                    }
                }
            }
        });

        levelManager = new LevelManager(player,camera);
    }

    @Override
    public void Update(float time) {
        if(player !=null){
            player.Update();
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

            // fog of war sub hud peste mapa
            if(levelManager.isFogOfWarActive() && player != null) {
                //TODO FOW
            }
        }

        // Reseteaza transformarea ca sa desenam HUD-ul normal
        g2d.setTransform(old);

        if(player!=null) player.DrawHUD(g);
    }

}
