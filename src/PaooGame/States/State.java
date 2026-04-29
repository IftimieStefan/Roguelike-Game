package PaooGame.States;

import PaooGame.GameWindow.GameWindow;

import java.awt.*;
import java.awt.event.MouseListener;

public abstract class State {
    protected GameWindow wnd;

    public State(GameWindow wnd){
        this.wnd=wnd;
    }

//    public abstract void Update();
    public abstract void Update(float time);
    public abstract  void Draw(Graphics g);

    protected void CurataMouseListeners() {
        for (MouseListener ml : wnd.GetCanvas().getMouseListeners()) {
            wnd.GetCanvas().removeMouseListener(ml);
        }
    }
}
