package PaooGame.Entities.Player;

import PaooGame.Managers.EntityManager;
import PaooGame.Managers.LevelManager;

public abstract class Ability
{
    protected String name;
    protected int maxCharges,currentCharges;
    protected long lastUse;
    protected static long cooldown=30000;
    protected long duration=10000, activationTime;

    public Ability(String name, int maxCharges)
    {
        this.name=name;
        this.maxCharges=maxCharges;
        this.currentCharges=maxCharges;
        this.lastUse=System.currentTimeMillis();
    }

    public abstract void use(Player p, LevelManager lm);

    void Update()
    {
        if(currentCharges<maxCharges)
        {
            if (System.currentTimeMillis()-activationTime>cooldown)
            {
                if (System.currentTimeMillis() - lastUse > cooldown)
                {
                    ++currentCharges;
                    lastUse = System.currentTimeMillis();
                }
            }

        }
        else
        {
            lastUse = System.currentTimeMillis();
        }
    }

    public String getName(){return name;}
    public int getCharges(){return currentCharges;}
    public long getActivationTime() { return activationTime; }
    public long getDuration() { return duration; }
}
