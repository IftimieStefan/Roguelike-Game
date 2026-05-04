package PaooGame.Entities.Player;

import PaooGame.Entities.Projectiles.Bomb;
import PaooGame.Managers.LevelManager;

public class FervorAbility extends Ability
{
    private boolean isActive=false;
    public FervorAbility()
    {
        super("Fervor",1);
    }

    @Override
    public void use(Player p, LevelManager lm) {
        if (currentCharges > 0) {
            activationTime = System.currentTimeMillis();
            currentCharges--;
            isActive = true;

            p.setAttackCooldown(250);
            p.setReloadTime(100);

            lastUse = System.currentTimeMillis();
        }
    }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; } // Metodă nouă
}
