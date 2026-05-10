package PaooGame.Entities.Enemies;

public class FastEnemyDecorator extends EnemyDecorator {

    public FastEnemyDecorator(Inamic wrappee) {
        super(wrappee);
        this.wrappee.speed += 2.0f;
        this.wrappee.baseSpeed = this.wrappee.speed; // Sincronizam baseSpeed cu viteza decorata
        // Modificam direct campul speed
        this.speed = this.wrappee.speed;
        this.baseSpeed = this.wrappee.baseSpeed;
    }
}
