package PaooGame.Core;

import PaooGame.Entities.Player.Player;

public class GameCamera {
    private static GameCamera instance;
    private float xOff,yOff;
    private int gameWidth, gameHeight;
    private float zoom = 1.7f; // Nivel standard de zoom

    // Dimensiunile hartii 
    private int mapWidthPx;
    private int mapHeightPx;

    private GameCamera (int gameWidth, int gameHeight){
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        xOff = 0;
        yOff = 0;
    }
    public static GameCamera getInstance() {
        if (instance == null) {
            instance = new GameCamera(1920, 1080);
        }
        return instance;
    }

    public static GameCamera getInstance(int gameWidth, int gameHeight) {
        if (instance == null) {
            instance = new GameCamera(gameWidth, gameHeight);
        }
        return instance;
    }

    // Seteaza limitele hartii (apelat din LevelManager la incarcarea unui nivel)
    public void setMapBounds(int mapWidthPx, int mapHeightPx) {
        this.mapWidthPx = mapWidthPx;
        this.mapHeightPx = mapHeightPx;
    }

    public void centerOnDinu(Player p){
        // pentru a centra mijlocul jucatorului pe ecran, respectand marirea zoom-ului
        xOff = p.getX() - (gameWidth / zoom) / 2f + 48;
        yOff = p.getY() - (gameHeight / zoom) / 2f + 48 ;

        // Limitam camera sa nu depaseasca marginile hartii
        if (mapWidthPx > 0 && mapHeightPx > 0) {
            float viewW = gameWidth / zoom;
            float viewH = gameHeight / zoom;

            // Nu permitem offset negativ (marginea stanga/sus)
            if (xOff < 0) xOff = 0;
            if (yOff < 0) yOff = 0;

            // Nu permitem sa vedem dincolo de marginea dreapta/jos
            float maxX = mapWidthPx - viewW;
            float maxY = mapHeightPx - viewH;

            if (maxX > 0 && xOff > maxX) xOff = maxX;
            if (maxY > 0 && yOff > maxY) yOff = maxY;
        }
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void move(float x,float y){
        xOff+=x;yOff+=y;
    }

    public float getxOff() {
        return xOff;
    }

    public float getyOff() {
        return yOff;
    }

    public void setxOff(float x){
        xOff=x;
    }
    public void setyOff(float y){
        yOff=y;
    }
}
