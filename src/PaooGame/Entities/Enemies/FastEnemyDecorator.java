package PaooGame.Entities.Enemies;

public class FastEnemyDecorator extends EnemyDecorator {

    public FastEnemyDecorator(Inamic wrappee) {
        super(wrappee);
        this.wrappee.speed += 2.0f;
        // Modificam direct campul speed
        this.speed = this.wrappee.speed;
    }
}
