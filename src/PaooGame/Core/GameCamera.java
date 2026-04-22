package PaooGame.Core;

import PaooGame.Entities.Player;

public class GameCamera {
    private static GameCamera instance;
    private float xOff,yOff;
    private int gameWidth, gameHeight;

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

    public void centerOnDinu(Player p){
        // pentru a centra mijlocul jucatorului pe ecran
        xOff = p.GetX() - gameWidth / 2f + 48;
        yOff = p.GetY() - gameHeight / 2f + 48;
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
