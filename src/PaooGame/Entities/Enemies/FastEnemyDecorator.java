package PaooGame.Entities.Enemies;

public class FastEnemyDecorator extends EnemyDecorator {

    public FastEnemyDecorator(Inamic wrappee) {
        super(wrappee);
        // Modificam direct campul protejat speed al inamicului impachetat
        this.wrappee.speed += 2.0f; 
        this.speed = this.wrappee.speed;
    }
}
