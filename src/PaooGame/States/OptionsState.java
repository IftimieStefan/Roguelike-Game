package PaooGame.States;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Managers.StateManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OptionsState extends State{
    private float animTime;

    private Rectangle backButton;
    private Rectangle devModeButton; // Doar vizual pentru exemplu

    private boolean backPressed = false;
    private boolean devModeOn = false;

    public OptionsState(GameWindow wnd) {
        super(wnd);
        this.animTime = 0;

        int cx = wnd.GetCanvas().getWidth() / 2;

        devModeButton = new Rectangle(cx - 150, 300, 300, 80);
        backButton = new Rectangle(cx - 150, 450, 300, 80);

        wnd.GetCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (backButton.contains(e.getPoint())) {
                    backPressed = true;
                }
                if (devModeButton.contains(e.getPoint())) {
                    devModeOn = !devModeOn;
                }
            }
        });
    }

    public void Update(float time) {
        this.animTime = time;

        if(backPressed){
            CurataInput();
            StateManager.SetState(new MainMenuState(wnd));
        }
    }

    public void Draw(Graphics g) {
        // Fundal la fel ca meniul principal
        g.setColor(new Color(15, 25, 45));
        int cx = wnd.GetCanvas().getWidth();
        g.fillRect(0, 0, cx, wnd.GetWndHeight());

        for (int i = 0; i < 40; i++) {
            int starX = (int) ((Math.sin(i * 1.7f + animTime * 0.2f) + 1) * wnd.GetWndWidth() / 2);
            int starY = (int) ((i * 37 + animTime * 30) % wnd.GetWndHeight());
            int alpha = 60 + (int) (Math.abs(Math.sin(i + animTime)) * 140);

            g.setColor(new Color(200, 220, 255, alpha));
            g.fillOval(starX, starY, 3, 3);
        }

        int titleY = 150;
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String title = "-- OPTIONS --";
        FontMetrics fm = g.getFontMetrics();
        
        g.setColor(new Color(0, 0, 0, 120));
        g.drawString(title, (cx - fm.stringWidth(title)) / 2 + 4, titleY + 4);
        g.setColor(new Color(255, 215, 50));
        g.drawString(title, (cx - fm.stringWidth(title)) / 2, titleY);

        DrawButton(g, backButton, "BACK", new Color(180, 50, 50));
    }

    private void DrawButton(Graphics g, Rectangle btn, String text, Color color) {
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRoundRect(btn.x + 4, btn.y + 4, btn.width, btn.height, 20, 20);

        g.setColor(color);
        g.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 20, 20);

        g.setColor(new Color(255, 255, 255, 180));
        g.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 20, 20);

        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int textX = btn.x + (btn.width - fm.stringWidth(text)) / 2;
        int textY = btn.y + (btn.height + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(text, textX, textY);
    }

    public boolean IsBackPressed() {
        return backPressed;
    }

    public void Reset() {
        backPressed = false;
    }
}
