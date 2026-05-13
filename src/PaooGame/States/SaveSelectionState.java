package PaooGame.States;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Managers.Data.SaveData;
import PaooGame.Managers.DatabaseManager;
import PaooGame.Managers.StateManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SaveSelectionState extends State
{
    private Rectangle[] saveSlots;  // Un vector de 3 dreptunghiuri pentru sloturi
    private SaveData[] slotsData;// datele in baza de date
    private Rectangle[] deleteButtons;// butoane de delete
    private Rectangle   backButton;
    private int         selectedSlot = -1; // -1 inseamna nicio selectie
    private int slotToDelete = -1;
    private boolean     backPressed = false;
    private float animTime = 0;

    public SaveSelectionState(GameWindow wnd) {
        super(wnd);
        int cx = wnd.GetCanvas().getWidth() / 2;

        // Definim 3 butoane (sloturi) unul sub altul
        saveSlots = new Rectangle[3];
        slotsData = new SaveData[3];
        deleteButtons = new Rectangle[3];

        for(int i = 0; i < 3; i++) {
            saveSlots[i] = new Rectangle(cx - 150, 250 + (i * 110), 300, 80);
            deleteButtons[i] = new Rectangle(cx + 150, 250 + (i * 110), 80, 80);
            slotsData[i] = DatabaseManager.LoadProgress(i+1);
        }

        // Buton de inapoi in coltul stanga sus
        backButton = new Rectangle(20, 20, 100, 40);

        wnd.GetCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Verificam daca s-a apasat pe vreun slot
                for(int i = 0; i < 3; i++) {
                    if(saveSlots[i].contains(e.getPoint())) {
                        selectedSlot = i + 1; // Salvam slotul 1, 2 sau 3
                    }

                    if(deleteButtons[i].contains(e.getPoint())) {
                        slotToDelete=i+1;
                    }
                }
                // Verificam butonul back
                if(backButton.contains(e.getPoint())) {
                    backPressed = true;
                }
            }
        });
    }

    public void Update(float time)
    {
        this.animTime=time;

        if(backPressed){
            CurataInput();
            StateManager.SetState(new MainMenuState(wnd));
            backPressed=false;
        }

        if (slotToDelete != -1) {
            DatabaseManager.DeleteProgress(slotToDelete);
            slotsData[slotToDelete - 1] = null;
            slotToDelete = -1;
        }

        if(selectedSlot != -1 ){
            SaveData data=slotsData[selectedSlot-1];
            if(data!=null && data.getLevel()==3 && data.getRoom()==2)
            {
                selectedSlot=-1;
                return;
            }
            CurataInput();
            StateManager.SetState(new PlayState(wnd,selectedSlot));
            selectedSlot=-1;
        }
    }

    public void Draw(Graphics g) {
        // Fundal albastru inchis (ca la Main Menu)
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

        // Titlu pagina
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.setColor(Color.WHITE);
        String txt = "Selecteaza Salvarea";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(txt, (cx - fm.stringWidth(txt)) / 2, 150);

        // Desenam cele 3 sloturi
        for(int i = 0; i < 3; i++) {
            String label;
            Color btnColor;

            if(slotsData[i]==null)
            {
                label = "Slot " + (i+1) + " - New Game";
                btnColor = new Color(60, 100, 150);
            } else {
                if (slotsData[i].getLevel() == 3 && slotsData[i].getRoom() == 2) {
                    label = "Slot " + (i + 1) + " - Final Score: " + slotsData[i].getScore();
                    btnColor = new Color(100, 100, 100); // Gri pentru "dezactivat"
                } else {
                    label = "Slot " + (i + 1) + " - Lvl " + slotsData[i].getLevel() + " - Room " + slotsData[i].getRoom();
                    btnColor = new Color(40, 160, 80);
                }
                drawButton(g, deleteButtons[i], "X", new Color(180, 40, 40));
            }
            drawButton(g, saveSlots[i], label, btnColor);
        }

        // Desenam butonul Back
        drawButton(g, backButton, "BACK", Color.GRAY);
    }

    private void drawButton(Graphics g, Rectangle r, String text, Color color) {
        g.setColor(color);
        g.fillRoundRect(r.x, r.y, r.width, r.height, 15, 15);
        g.setColor(Color.WHITE);
        g.drawRoundRect(r.x, r.y, r.width, r.height, 15, 15);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g.getFontMetrics();
        int tx = r.x + (r.width - fm.stringWidth(text)) / 2;
        int ty = r.y + (r.height + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(text, tx, ty);
    }

    // Getteri pentru clasa Game
    public int GetSelectedSlot() { return selectedSlot; }
    public boolean IsBackPressed() { return backPressed; }

    public void Reset() {
        selectedSlot = -1;
        backPressed = false;
    }
}