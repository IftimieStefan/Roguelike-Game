package PaooGame.Core;

import PaooGame.Entities.Player;

public class GameCamera {
    private float xOff,yOff;
    private int gameWidth, gameHeight;

    public GameCamera (int gameWidth, int gameHeight){
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        xOff = 0;
        yOff = 0;
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
