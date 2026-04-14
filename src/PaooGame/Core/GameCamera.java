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

    public static void init(int gameWidth, int gameHeight) {
        if (instance == null) {
            instance = new GameCamera(gameWidth, gameHeight);
        }
    }

    public static GameCamera getInstance() {
        return instance;
    }

    public void centerOnDinu(Player p){
        xOff = p.GetX() - gameWidth / 2f;
        yOff = p.GetY() - gameHeight / 2f;
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
