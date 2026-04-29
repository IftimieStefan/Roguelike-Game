package PaooGame.Managers;

import PaooGame.Game;
import PaooGame.Graphics.MapLoader;
import PaooGame.Entities.Player.Player;
import PaooGame.Entities.Enemies.EnemyFactory;
import PaooGame.Entities.NPCs.Npc;
import PaooGame.Entities.NPCs.Bity;
import PaooGame.Core.GameCamera;
import java.io.File;

public class LevelManager {
    private int currentLevel = 1;
    private int currentRoom = 1;

    private MapLoader currentMap;
    private EntityManager entityManager;
    private Player player;
    private GameCamera camera;

    private boolean mapExists = true;

    public LevelManager(Player player, GameCamera camera) {
        this.player = player;
        this.camera = camera;
        loadRoom();
    }

    public void loadRoom() {
        String mapPath = "res/harti/Level" + currentLevel + "/room" + currentRoom + ".csv";
        
        File f = new File(mapPath);
        if(!f.exists()) {
            if(currentRoom > 1) {
                // Nu mai exista camere in acest nivel, trecem la nivelul urmator
                currentLevel++;
                currentRoom = 1;
                mapPath = "res/harti/Level" + currentLevel + "/room" + currentRoom + ".csv";
                f = new File(mapPath);
                if(!f.exists()) {
                    System.out.println("Ai terminat jocul!");
                    mapExists = false;
                    return;
                }
            } else {
                System.out.println("Nu am gasit harta: " + mapPath);
                mapExists = false;
                return;
            }
        }

        currentMap = new MapLoader(mapPath);
        entityManager = new EntityManager();

        // Generam entitatile din fisierul hartii
        for (String line : currentMap.getEntityData()) {
            String[] parts = line.split(" ");
            if (parts.length < 3) continue;

            String type = parts[0];
            if (type.equals("Player")) {
                player.setX(Integer.parseInt(parts[1]));
                player.setY(Integer.parseInt(parts[2]));
            } else if (type.equals("Enemy") && parts.length >= 4) {
                String enemyType = parts[1];
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);
                entityManager.addEnemy(EnemyFactory.createEnemy(enemyType, x, y, player));
            } else if (type.equals("Npc") && parts.length >= 4) {
                String npcType = parts[1];
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);
                //TODO sa pun npc la enemyfcatory?
//                entityManager.addNpc(new Bity(x, y));
            }
        }
    }

    public void Update() {
        if(!mapExists || currentMap==null) return;

        currentMap.Update();
        entityManager.Update();
        entityManager.checkInteractions(player);

        // Verificam daca am omorat toti inamicii
        if(entityManager.getEnemies().isEmpty()) {
            System.out.println("Toti inamicii au fost eliminati. Trecem la urmatoarea camera!");
            currentRoom++;
            loadRoom();
        }
    }

    public boolean isFogOfWarActive() {
        if(currentLevel == 1 && currentRoom == 1) {
            for(Npc npc : entityManager.getNpcs()) {
                if(npc instanceof Bity) {
                    if(npc.isTalkedTo()) {
                        return false; // Bity remov FOW
                    } else {
                        return true;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public MapLoader getCurrentMap() { return currentMap; }
    public EntityManager getEntityManager() { return entityManager; }
}
