package PaooGame.Entities.Enemies;

// Decorator: dubleaza HP-ul inamicului
public class TankEnemyDecorator extends EnemyDecorator {

    public TankEnemyDecorator(Inamic wrappee) {
        super(wrappee);
        this.wrappee.maxHealth *= 2;
        this.wrappee.health = this.wrappee.maxHealth;

        this.maxHealth = this.wrappee.maxHealth;
        this.health = this.wrappee.health;
    }
}
