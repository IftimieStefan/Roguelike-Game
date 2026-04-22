package PaooGame.Managers;

import PaooGame.Entities.Inamic;
import PaooGame.Entities.Npc;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// liste logiq
public class EntityManager {

    private final List<Inamic> enemies;
    private final List<Npc> npcs;

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

