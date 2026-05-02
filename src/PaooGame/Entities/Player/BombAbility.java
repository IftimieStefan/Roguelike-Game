package PaooGame.Entities.Player;

import PaooGame.Entities.Projectiles.Bomb;
import PaooGame.Managers.LevelManager;

public class BombAbility extends Ability
{
    public BombAbility()
    {
        super("Bomb",2);
    }

    @Override
    public void use(Player p, LevelManager lm)
    {
        if (currentCharges>0)
        {
            int dX = 0, dY = 0;
            switch (p.getLastDir())
            {
                case 0: dY = -1; break;
                case 1: dY = 1;  break;
                case 2: dX = -1; break;
                case 3: dX = 1;  break;
            }

            p.getBombs().add(new Bomb(p.getX(), p.getY(), dX, dY));
            currentCharges--;
            lastUse = System.currentTimeMillis();        }
    }
}
