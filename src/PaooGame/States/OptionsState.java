package PaooGame.States;

import PaooGame.Audio.AudioPlayer;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Managers.DatabaseManager;
import PaooGame.Managers.StateManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class OptionsState extends State{
    private float animTime;

    private Rectangle backButton;
    private Rectangle devModeButton; // Doar vizual pentru exemplu

    private boolean backPressed = false;
    private boolean devModeOn = false;

    // ---- Bara de volum ----
    private final int barX;
    private final int barY = 320;
    private final int barW = 300;
    private final int barH = 20;
    private boolean dragging = false;

    public OptionsState(GameWindow wnd) {
        super(wnd);
        this.animTime = 0;

        int cx = wnd.GetCanvas().getWidth() / 2;
        barX = cx - barW / 2;

        devModeButton = new Rectangle(cx - 150, 400, 300, 80);
        backButton = new Rectangle(cx - 150, 530, 300, 80);

        wnd.GetCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Verifica daca s-a apasat pe bara de volum (zona mai larga pe Y)
                if (e.getX() >= barX && e.getX() <= barX + barW
                        && e.getY() >= barY - 10 && e.getY() <= barY + barH + 10) {
                    dragging = true;
                    updateVolume(e.getX());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }

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

        wnd.GetCanvas().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    updateVolume(e.getX());
                }
            }
        });
    }

    /** Calculeaza volumul pe baza pozitiei X a mouse-ului pe bara. */
    private void updateVolume(int mouseX) {
        float ratio = (float)(mouseX - barX) / barW;
        ratio = Math.max(0f, Math.min(1f, ratio));
        AudioPlayer.setVolume(ratio);
    }

    public void Update(float time) {
        this.animTime = time;

        if(backPressed){
            // Salvam volumul in baza de date
            DatabaseManager.SaveSetting("volume", String.valueOf(AudioPlayer.getVolume()));
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

        // ---- Desenam bara de volum ----
        drawVolumeBar(g);

        DrawButton(g, backButton, "BACK", new Color(180, 50, 50));
    }

    private void drawVolumeBar(Graphics g) {
        float vol = AudioPlayer.getVolume();
        int filledW = (int)(barW * vol);
        int percent = Math.round(vol * 100);

        // Label
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.setColor(new Color(220, 220, 220));
        String label = "VOLUME: " + percent + "%";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(label, barX + (barW - fm.stringWidth(label)) / 2, barY - 15);

        // Fundal bara (gri inchis)
        g.setColor(new Color(50, 50, 70));
        g.fillRoundRect(barX, barY, barW, barH, 10, 10);

        // Bara umpluta (gradient simplu)
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(barX, barY, new Color(80, 180, 255),
                barX + filledW, barY, new Color(50, 120, 220)));
        g2.fillRoundRect(barX, barY, filledW, barH, 10, 10);

        // Contur
        g.setColor(new Color(255, 255, 255, 80));
        g.drawRoundRect(barX, barY, barW, barH, 10, 10);

        // Indicator (cerculet pe pozitia curenta)
        int knobX = barX + filledW;
        int knobR = 12;
        g.setColor(Color.WHITE);
        g.fillOval(knobX - knobR, barY + barH / 2 - knobR, knobR * 2, knobR * 2);
        g.setColor(new Color(50, 120, 220));
        g.fillOval(knobX - knobR + 3, barY + barH / 2 - knobR + 3, (knobR - 3) * 2, (knobR - 3) * 2);
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
