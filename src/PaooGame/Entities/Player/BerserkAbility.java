package PaooGame.Entities.Player;

import PaooGame.Entities.Projectiles.Bomb;
import PaooGame.Managers.LevelManager;

public class BerserkAbility extends Ability
{
    private boolean isActive=false;
    public BerserkAbility()
    {
        super("Fervor",1);
    }

    @Override
    public void use(Player p, LevelManager lm) {
        if (currentCharges > 0) {
            activationTime = System.currentTimeMillis();
            currentCharges--;
            isActive = true;

            p.setMeleeOnly(true);
            p.setMeleeDMG(20);
            p.setSpeed(6.0f);

            lastUse = System.currentTimeMillis();
        }
    }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; } // Metodă nouă
}
