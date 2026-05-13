package PaooGame.States;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Managers.StateManager;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WinState extends State {
    private Rectangle mainMenuButton;
    private Rectangle exitButton;

    // Flag-uri pentru Update
    private boolean menuPressed    = false;
    private boolean exitPressed    = false;

    private float animTime;
    private int score;

    public WinState(GameWindow wnd,int score) {
        super(wnd);
        this.score=score;

        int btnWidth = 220;
        int btnHeight = 60;

        // centru
        int canvasWidth = wnd.GetCanvas().getWidth();
        int cx = (canvasWidth > 0) ? canvasWidth / 2 : 400;

        mainMenuButton = new Rectangle(cx - btnWidth / 2, 380, btnWidth, btnHeight);
        exitButton     = new Rectangle(cx - btnWidth / 2, 460, btnWidth, btnHeight);

        wnd.GetCanvas().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (mainMenuButton.contains(e.getPoint())) menuPressed = true;
                if (exitButton.contains(e.getPoint()))     exitPressed = true;
            }
        });
    }


    @Override
    public void Update(float time)
    {
        this.animTime=time;

        if (menuPressed) {
            CurataInput();
            StateManager.SetState(new MainMenuState(wnd));
            menuPressed = false;
        }

        if (exitPressed) {
            System.exit(0);
        }
    }

    @Override
    public void Draw(Graphics g) {
        if (g != null) {
            int canvasWidth = wnd.GetCanvas().getWidth();
            int canvasHeight = wnd.GetCanvas().getHeight();

            g.setColor(new Color(15, 25, 45));
            g.fillRect(0, 0, canvasWidth, canvasHeight);

            for(int i = 0; i < 40; i++)
            {
                int starX = (int)((Math.sin(i * 1.7f + animTime * 0.2f) + 1) * canvasWidth / 2);
                int starY = (int)((i * 37 + animTime * 30) % canvasHeight);
                int alpha = 60 + (int)(Math.abs(Math.sin(i + animTime)) * 140);

                g.setColor(new Color(200, 220, 255, alpha));
                g.fillOval(starX, starY, 3, 3);
            }

            // Overlay negru
            g.setColor(new Color(10, 15, 30, 100));
            g.fillRect(0, 0, canvasWidth, canvasHeight);

            int titleY = 180;
            g.setFont(new Font("Arial", Font.BOLD, 70));
            String title = "CONGRATULATIONS, YOU WIN";
            FontMetrics fm = g.getFontMetrics();
            int titleX = (canvasWidth - fm.stringWidth(title)) / 2;

            // Umbra titlu
            g.setColor(new Color(0, 0, 0, 150));
            g.drawString(title, titleX + 5, titleY + 5);
            // Titlu
            g.setColor(new Color(255, 215, 50));
            g.drawString(title, titleX, titleY);

            //Scor
            String Scor="Final score: "+ score;
            g.setColor(new Color(255, 215, 50));
            g.drawString(Scor, titleX+250, titleY+75);
            // Butoane
            DrawButton(g, mainMenuButton, "MENU",    new Color(100, 100, 100));
            DrawButton(g, exitButton,     "EXIT",    new Color(180, 50, 50));
        }
    }

    private void DrawButton(Graphics g, Rectangle btn, String text, Color color) {
        g.setColor(new Color(0, 0, 0, 120));
        g.fillRoundRect(btn.x + 4, btn.y + 4, btn.width, btn.height, 20, 20);

        g.setColor(color);
        g.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 20, 20);

        g.setColor(new Color(255, 255, 255, 120));
        g.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 20, 20);

        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int textX = btn.x + (btn.width - fm.stringWidth(text)) / 2;
        int textY = btn.y + (btn.height + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(text, textX, textY);
    }

}