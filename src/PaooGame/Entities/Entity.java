package PaooGame.Entities;

import java.awt.Graphics;

// Contract comun — orice entitate din joc implementează asta
public interface Entity {
    void Update();           // Logica per frame
    void Draw(Graphics g);   // Desenare pe ecran
    float getX();            // Poziția X pe hartă
    float getY();            // Poziția Y pe hartă
    boolean isActive();      // false = EntityManager o scoate din listă
}
