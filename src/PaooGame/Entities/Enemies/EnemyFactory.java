package PaooGame.Entities.Enemies;

import PaooGame.Entities.Player.Player;

public class EnemyFactory {

    // Creeaza inamicul de baza dupa tip
    public static Inamic createEnemy(String type, float x, float y, Player player) {
        switch (type.toUpperCase()) {
            case "FOCA":
                return new Foca(x, y, player);
            case "FOCA_COWBOY":
                return new Foca_Cowboy(x, y, player);
            case "FOCA_MEXIC":
                return new Foca_Mexic(x, y, player);
            case "FOCA_ESCROCA":
                return new Foca_Escroca(x, y, player);
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }

    // Aplica decoratori dintr-un string "FAST,BOSS" separat cu virgula
    public static Inamic applyDecorators(Inamic base, String decorators) {
        if (decorators == null || decorators.isEmpty()) return base;

        Inamic result = base;
        for (String dec : decorators.split(",")) {
            switch (dec.trim().toUpperCase()) {
                case "FAST":
                    result = new FastEnemyDecorator(result);
                    break;
                case "BOSS":
                    result = new BossEnemyDecorator(result);
                    break;
                case "TANK":
                    result = new TankEnemyDecorator(result);
                    break;
                default:
                    System.out.println("Unknown decorator: " + dec);
            }
        }
        return result;
    }
}
