package PaooGame.Managers;

import PaooGame.Entities.Enemies.*;
import PaooGame.Entities.NPCs.*;
import PaooGame.Entities.Projectiles.*;
import PaooGame.Entities.Player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// liste logiq
public class EntityManager {

    private final List<Inamic> enemies; // Lista inamicilor existenti in nivel
    private final List<Npc> npcs; // Lista de npc-uri din zona

    public EntityManager() {
        this.enemies = new ArrayList<>();
        this.npcs = new ArrayList<>();
    }

    // inamici logiq
    public void addEnemy(Inamic inamic) {
        if (inamic != null) {
            enemies.add(inamic);
        }
    }

    public void removeEnemy(Inamic inamic) {
        enemies.remove(inamic);
    }

    // npc logiq
    public void addNpc(Npc npc) {
        if (npc != null) {
            npcs.add(npc);
        }
    }

    public void removeNpc(Npc npc) {
        npcs.remove(npc);
    }

    // update && draw
    public void Update() {
        // Update pentru Inamici
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).Update();
            // nu mai are hp se scoate
            if (enemies.get(i).getHealth() <= 0) {
                enemies.remove(i);
                i--;
            }
        }

        // apdait npcis
        for (int i = 0; i < npcs.size(); i++) {
            npcs.get(i).Update();
        }
    }

    public void checkInteractions(Player player) {
        if (player != null && player.consumeInteract()) {
            for (Npc npc : npcs) {
                float dx = npc.getX() - player.getX();
                float dy = npc.getY() - player.getY();
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                if (distance <= 120) { // asprites sunt 48x48 so ma imare distanta
                    npc.toggleDialog();
                    System.out.println("Toggled dialog!");
                }
            }
        }

        //coliziune bulgari inamici 2
        ArrayList<Snowball> snowballs = player.getSnowballs();

        if (snowballs != null) {
            for (Inamic inamic : enemies) {
                for (Snowball snowball : snowballs) {
                    if (snowball.isActive()) {
                        if (inamic.getHitbox().intersects(snowball.getHitbox())) {
                            inamic.takeDamage(player.getRangeDMG());
                            snowball.setActive(false);
                            break;
                        }
                    }
                }
            }
        }

        //coliziune atac melee inamici
        if (player.isMeleeAttacking()) {
            for (Inamic inamic : enemies) {
                if (player.getMeleeHitbox().intersects(inamic.getHitbox())) {
                    inamic.takeDamage(player.getMeleeDMG());
                    player.stopMelee();
                    break;
                }
            }
        }

        //coliziune inamici player
        for (Inamic inamic : enemies) {
            if (player.getHitbox().intersects(inamic.getHitbox())) {
                player.takeDamage();
            }
        }

    }

    //coliziune explozie bomba
    public void processExplosion(Rectangle exp)
    {
        if (enemies != null) {
            for (Inamic inamic : enemies)
            {
                if (exp.intersects(inamic.getHitbox()))
                {
                    inamic.takeDamage(20);
                }
            }
        }
    }

    public void Draw(Graphics g) {
        for (Inamic e : enemies) {
            e.Draw(g);
        }
        for (Npc n : npcs) {
            n.Draw(g);
        }
    }

    public List<Inamic> getEnemies() {
        return enemies;
    }

    public List<Npc> getNpcs() {
        return npcs;
    }
}

