package PaooGame.Entities.Enemies;

import PaooGame.Entities.Player.Player;

public class EnemyFactory {

    public static Inamic createEnemy(String type, float x, float y, Player player) {
        switch (type.toUpperCase()) {
            case "FOCA":
                return new Foca(x, y, player);
            case "FOCA_COWBOY":
                return new Foca_Cowboy(x, y, player);
            case "FAST_FOCA":
                return new FastEnemyDecorator(new Foca(x, y, player));
            case "BOSS_COWBOY":
                return new BossEnemyDecorator(new Foca_Cowboy(x, y, player));
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }
}
