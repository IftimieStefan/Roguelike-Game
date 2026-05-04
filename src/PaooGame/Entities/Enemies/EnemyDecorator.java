package PaooGame.Entities.Enemies;

import java.awt.*;

public abstract class EnemyDecorator extends Inamic {
    protected Inamic wrappee;

    public EnemyDecorator(Inamic wrappee) {
        super(wrappee.getX(), wrappee.getY(), wrappee.getHealth(), wrappee.getSpeed());
        this.wrappee = wrappee;
    }

    @Override
    public void Update() {
        wrappee.Update();
        // Sincronizam coordonatele cu wrappee pentru calcule de hitboxes/etc.
        this.x = wrappee.getX();
        this.y = wrappee.getY();
        this.health = wrappee.getHealth();
    }

    @Override
    public void Draw(Graphics g) {
        wrappee.Draw(g);
    }

    @Override
    public Rectangle getHitbox() {
        return wrappee.getHitbox();
    }

    @Override
    public void takeDamage(int damage) {
        wrappee.takeDamage(damage);
        this.health = wrappee.getHealth();
    }
}
