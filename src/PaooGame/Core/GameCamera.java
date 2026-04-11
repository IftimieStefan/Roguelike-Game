package PaooGame.Core;


public class GameCamera {
    float xOff,yOff;

    GameCamera (){
    }

    //TODO
    public void centerOnDinu(){

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
