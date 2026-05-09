package PaooGame.States;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Managers.StateManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuState extends State
{
    private Rectangle   playButton;     //play
    private Rectangle   exitButton;     /*!< Zona butonului Exit */
    private Rectangle   optionsButton;
    private boolean     playPressed;    /*!< Flag - s-a apasat Play */
    private boolean     exitPressed;    /*!< Flag - s-a apasat Exit */
    private boolean     optionsPressed;
    private float       animTime;       /*!< Contor pentru animatie */

    public MainMenuState(GameWindow wnd)
    {
        super(wnd);
        playPressed     = false;
        exitPressed     = false;
        optionsPressed  = false;
        animTime        = 0;

        int cx = wnd.GetCanvas().getWidth() / 2; // mijloc pe latime

        playButton = new Rectangle(cx - 100, 300, 200, 100);
        optionsButton= new Rectangle( cx- 100,450,200,100);
        exitButton = new Rectangle(cx - 100, 600, 200, 100);

        wnd.GetCanvas().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(playButton.contains(e.getPoint()))
                {
                    playPressed = true;
                }
                if(optionsButton.contains(e.getPoint()))
                {
                    optionsPressed = true;
                }
                if(exitButton.contains(e.getPoint()))
                {
                    exitPressed = true;
                }
            }
        });
    }

    public void Update(float time)
    {
        this.animTime=time;
        if(exitPressed)
        {
            System.exit(0);
        }
        if(playPressed){
            CurataInput();
            StateManager.SetState(new SaveSelectionState(wnd));
        }
        if(optionsPressed){
            CurataInput();
            StateManager.SetState(new OptionsState(wnd));
        }
    }

    public void Draw(Graphics g)
    {
        g.setColor(new Color(15, 25, 45));
        int cx = wnd.GetCanvas().getWidth();
        g.fillRect(0, 0, cx, wnd.GetWndHeight());

        for(int i = 0; i < 40; i++)
        {
            int starX = (int)((Math.sin(i * 1.7f + animTime * 0.2f) + 1) * wnd.GetWndWidth() / 2);
            int starY = (int)((i * 37 + animTime * 30) % wnd.GetWndHeight());
            int alpha = 60 + (int)(Math.abs(Math.sin(i + animTime)) * 140);

            g.setColor(new Color(200, 220, 255, alpha));
            g.fillOval(starX, starY, 3, 3);
        }

        int titleY = 180;

        g.setFont(new Font("Arial", Font.BOLD, 60));
        g.setColor(new Color(0, 0, 0, 120));
        String title = "Dinu : Pinguinu";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (cx - fm.stringWidth(title)) / 2 + 4, titleY + 4);

        g.setColor(new Color(255, 215, 50));
        g.drawString(title, (cx - fm.stringWidth(title)) / 2, titleY);

        g.setFont(new Font("Arial", Font.ITALIC, 18));
        g.setColor(new Color(160, 180, 210));
        String sub = "Apasa Play pentru a incepe aventura...";
        fm = g.getFontMetrics();
        g.drawString(sub, (cx - fm.stringWidth(sub)) / 2, titleY + 50);

        DrawButton(g, playButton, "PLAY",  new Color(40, 160, 80));
        DrawButton(g, optionsButton, "OPTIONS", new Color(60, 239, 10, 72));
        DrawButton(g, exitButton, "EXIT",  new Color(180, 50, 50));
    }

    private void DrawButton(Graphics g, Rectangle btn, String text, Color color)
    {
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRoundRect(btn.x + 4, btn.y + 4, btn.width, btn.height, 20, 20);

        g.setColor(color);
        g.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 20, 20);

        g.setColor(new Color(255, 255, 255, 180));
        g.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 20, 20);

        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int textX = btn.x + (btn.width  - fm.stringWidth(text)) / 2;
        int textY = btn.y + (btn.height + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(text, textX, textY);
    }

    public boolean IsPlayPressed() { return playPressed; }
    public boolean IsOptionsPressed() { return optionsPressed; }

    public void Reset() {
        this.playPressed = false;
        this.optionsPressed = false;
    }
}