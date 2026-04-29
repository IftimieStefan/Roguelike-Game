package PaooGame.Managers;


import PaooGame.States.MainMenuState;
import PaooGame.States.State;

//TODO Mutat logica de play/mainmenu aici
public class StateManager {

    private static State currentState = null;

    public  static void SetState(State state){
        currentState=state;
    }

    public  static State GetState(){
        return currentState;
    }
}
