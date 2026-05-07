package PaooGame.Entities;

import java.awt.Rectangle;

public interface Damageable {
    void takeDamage(int amount);  // Scade HP
    int getHealth();              // HP curent
    Rectangle getHitbox();       // Dreptunghi pentru coliziuni
}
