package PaooGame.Managers;

import PaooGame.Entities.Entity;
import PaooGame.Entities.Interactable;
import PaooGame.Entities.Damageable;
import PaooGame.Entities.Enemies.Inamic;
import PaooGame.Entities.Enemies.Foca_Cowboy;
import PaooGame.Entities.NPCs.Npc;
import PaooGame.Entities.Player.Player;
import PaooGame.Entities.Projectiles.Snowball;
import PaooGame.Entities.Projectiles.EnemyBullet;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Toata logica de coliziuni/interacțiuni
public class CollisionSystem {

    // Verifica daca a apasat playerul E
    public static void handleInteractions(Player player, EntityManager em, LevelManager lm) {
        if (player == null || !player.consumeInteract()) return;
        for (Entity e : em.getEntities()) {
            if (e instanceof Interactable) {
                Interactable it = (Interactable) e;
                if (it.tryInteract(player)) {
                    // Daca e NPC si suntem pe Level 1, spawneaza inamicii
                    if (e instanceof Npc && lm != null && lm.isDelayedSpawn()) {
                        em.liftFog();
                        lm.spawnDelayedEntities();
                    }
                    return;   
                }
            }
        }
    }

    // Coliziuni snowball
    public static void handleProjectiles(Player player, List<Inamic> enemies) {
        ArrayList<Snowball> snowballs = player.getSnowballs();
        if (snowballs == null) return;
        for (Inamic inamic : enemies) {
            for (Snowball s : snowballs) {
                if (s.isActive() && inamic.getHitbox().intersects(s.getHitbox())) {
                    inamic.takeDamage(player.getRangeDMG());
                    s.setActive(false);
                    break;
                }
            }
        }
    }

    // Coliziuni melee
    public static void handleMelee(Player player, List<Inamic> enemies) {
        if (!player.isMeleeAttacking()) return;
        for (Inamic inamic : enemies) {
            if (player.getMeleeHitbox().intersects(inamic.getHitbox())) {
                inamic.takeDamage(player.getMeleeDMG());
                player.stopMelee();
                break;
            }
        }
    }

    // Coliziuni contact inamic
    public static void handleContactDamage(Player player, List<Inamic> enemies) {
        for (Inamic inamic : enemies) {
            if (player.getHitbox().intersects(inamic.getHitbox())) {
                player.takeDamage();
            }
        }
    }

    // Explozie bomba  inamici
    public static void processExplosion(Rectangle exp, List<Inamic> enemies) {
        for (Inamic inamic : enemies) {
            if (exp.intersects(inamic.getHitbox())) {
                inamic.takeDamage(20);
            }
        }
    }

    // Coliziuni proiectile inamice -> player
    public static void handleEnemyBullets(Player player, List<Inamic> enemies) {
        for (Inamic inamic : enemies) {
            if (inamic instanceof Foca_Cowboy) {
                ArrayList<EnemyBullet> bullets = ((Foca_Cowboy) inamic).getBullets();
                for (EnemyBullet b : bullets) {
                    if (b.isActive() && player.getHitbox().intersects(b.getHitbox())) {
                        player.takeDamage();
                        b.setActive(false);
                    }
                }
            }
        }
    }
}
