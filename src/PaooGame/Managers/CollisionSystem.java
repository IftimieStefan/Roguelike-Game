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

import PaooGame.Graphics.MapLoader;
import PaooGame.Tiles.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Toata logica de coliziuni/interacțiuni
public class CollisionSystem {

    private static MapLoader currentMap;

    public static void setCurrentMap(MapLoader map) {
        currentMap = map;
    }

    // Verifica daca bounding box-ul atinge un tile solid
    public static boolean collidesWithWall(float px, float py, int hitboxOffset, int hitboxWidth, int hitboxHeight) {
        if (currentMap == null) return false;

        int left   = (int)(px + hitboxOffset);
        int right  = (int)(px + hitboxOffset + hitboxWidth - 1);
        int top    = (int)(py + hitboxOffset);
        int bottom = (int)(py + hitboxOffset + hitboxHeight - 1);

        int tileLeft   = left   / Tile.TILE_WIDTH;
        int tileRight  = right  / Tile.TILE_WIDTH;
        int tileTop    = top    / Tile.TILE_HEIGHT;
        int tileBottom = bottom / Tile.TILE_HEIGHT;

        for (int ty = tileTop; ty <= tileBottom; ty++) {
            for (int tx = tileLeft; tx <= tileRight; tx++) {
                int tileId = currentMap.getTileId(tx, ty);
                Tile t = (tileId >= 0 && tileId < Tile.tiles.length) ? Tile.tiles[tileId] : null;
                if (t != null && t.IsSolid()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Verifica daca a apasat playerul E
    public static void handleInteractions(Player player, EntityManager em, LevelManager lm) {
        if (player == null || !player.consumeInteract()) return;
        for (Entity e : em.getEntities()) {
            if (e instanceof Interactable) {
                Interactable it = (Interactable) e;
                if (it.tryInteract(player)) {
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
            } else if (inamic instanceof PaooGame.Entities.Enemies.Foca_Escroca) {
                ArrayList<EnemyBullet> bullets = ((PaooGame.Entities.Enemies.Foca_Escroca) inamic).getBullets();
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
