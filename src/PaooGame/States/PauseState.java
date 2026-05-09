package PaooGame.States;

import PaooGame.Entities.Player.Player;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Managers.StateManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PauseState extends State
{
    private Rectangle resumeButton;
    private Rectangle mainMenuButton;

    private boolean resumePressed;
    private boolean mainMenuPressed;

    private MouseListener mouseListener;
    private PlayState playState;

    public PauseState(GameWindow wnd, PlayState playState) {
        super(wnd);

        this.playState=playState;

        int btnWidth = 220;
        int btnHeight = 60;

        // centru
        int canvasWidth = wnd.GetCanvas().getWidth();
        int cx = (canvasWidth > 0) ? canvasWidth / 2 : 400;

        resumeButton = new Rectangle(cx - btnWidth / 2, 300, btnWidth, btnHeight);
        mainMenuButton = new Rectangle(cx - btnWidth / 2, 380, btnWidth, btnHeight);

        mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (resumeButton.contains(e.getPoint())) resumePressed = true;
                if (mainMenuButton.contains(e.getPoint())) mainMenuPressed = true;
            }
        };

        wnd.GetCanvas().addMouseListener(mouseListener);
    }

    @Override
    public void Update(float time)
    {
        if (resumePressed) {
            CurataInput();
            playState.ActivareInput();
            StateManager.SetState(playState);
            resumePressed = false;
        }

        if (mainMenuPressed) {
            CurataInput();
            StateManager.SetState(new MainMenuState(wnd));
            mainMenuPressed = false;
        }
    }

    @Override
    public void Draw(Graphics g) {
        if (g != null) {
            int canvasWidth = wnd.GetCanvas().getWidth();
            int canvasHeight = wnd.GetCanvas().getHeight();

            // Overlay
            g.setColor(new Color(15, 25, 45, 180));
            g.fillRect(0, 0, canvasWidth, canvasHeight);

            // Titlu
            g.setFont(new Font("Arial", Font.BOLD, 70));
            String title = "PAUSED";
            FontMetrics fm = g.getFontMetrics();
            int titleX = (canvasWidth - fm.stringWidth(title)) / 2;

            g.setColor(new Color(0, 0, 0, 150));
            g.drawString(title, titleX + 5, 185);
            g.setColor(Color.WHITE);
            g.drawString(title, titleX, 180);

            // Butoane
            DrawButton(g, resumeButton, "RESUME", new Color(40, 160, 80));
            DrawButton(g, mainMenuButton, "MENU",   new Color(100, 100, 100));
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

