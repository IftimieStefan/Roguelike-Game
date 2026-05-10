package PaooGame.Managers;

import PaooGame.Game;
import PaooGame.Graphics.MapLoader;
import PaooGame.Graphics.FogOfWar;
import PaooGame.Entities.Player.Player;
import PaooGame.Entities.Door;
import PaooGame.Entities.Enemies.EnemyFactory;
import PaooGame.Entities.Enemies.Inamic;
import PaooGame.Entities.NPCs.Bity;
import PaooGame.Core.GameCamera;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private int currentLevel = 1;
    private int currentRoom = 1;

    private MapLoader currentMap;
    private EntityManager entityManager;
    private Player player;
    private GameCamera camera;

    private boolean mapExists = true;
    private boolean forceEmptyRoom = false; //ca sa nu loadam inamicii in camera in care dam load la save
    private FogOfWar fogOfWar;

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

        // Setam limitele camerei pe baza dimensiunilor hartii
        if (camera != null) {
            camera.setMapBounds(
                currentMap.getWidth() * PaooGame.Tiles.Tile.TILE_WIDTH,
                currentMap.getHeight() * PaooGame.Tiles.Tile.TILE_HEIGHT
            );
        }

        // Fog of war pe toate nivelurile
        fogOfWar = new FogOfWar(currentMap.getWidth(), currentMap.getHeight());

        // Generam entitatile din fisierul hartii
        for (String line : currentMap.getEntityData()) {
            String[] parts = line.split(" ");
            if (parts.length < 3) continue;

            String type = parts[0];
            if (type.equals("Player")) {
                player.setX(Integer.parseInt(parts[1]));
                player.setY(Integer.parseInt(parts[2]));
            } else if (type.equals("Enemy"))
            {
                if (forceEmptyRoom) continue;
                spawnEntity(parts);
            } else if (type.equals("Door"))
            {
                spawnEntity(parts);
            } else if (type.equals("Npc") && parts.length >= 4) {
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);
                entityManager.addEntity(new Bity(x, y));
            }
        }
        this.forceEmptyRoom=false;
    }

    // Spawneaza o entitate din datele parsate
    private void spawnEntity(String[] parts) {
        String type = parts[0];
        if (type.equals("Enemy") && parts.length >= 4) {
            String enemyType = parts[1];
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[3]);
            Inamic enemy = EnemyFactory.createEnemy(enemyType, x, y, player);
            if (parts.length >= 5) {
                enemy = EnemyFactory.applyDecorators(enemy, parts[4]);
            }
            entityManager.addEntity(enemy);
        } else if (type.equals("Door") && parts.length >= 3) {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            Door door = new Door(x, y);
            door.setOnActivate(() -> {
                currentRoom++;
                loadRoom();
            });
            entityManager.addEntity(door);
        }
    }

    public FogOfWar getFogOfWar() { return fogOfWar; }

    public void Update() {
        if(!mapExists || currentMap==null) return;

        currentMap.Update();
        entityManager.Update();

        // Update fog of war cu pozitia playerului (pe toate nivelurile)
        if (fogOfWar != null) {
            fogOfWar.updateVisibility(player.getX(), player.getY());
        }

        // Coliziuni si interacțiuni — delegate la CollisionSystem
        CollisionSystem.handleInteractions(player, entityManager, this);
        CollisionSystem.handleProjectiles(player, entityManager.getEnemies());
        CollisionSystem.handleMelee(player, entityManager.getEnemies());
        CollisionSystem.handleContactDamage(player, entityManager.getEnemies());
        CollisionSystem.handleEnemyBullets(player, entityManager.getEnemies());
    }

    public int getCurrentLevel(){return currentLevel;}
    public int getCurrentRoom(){return currentRoom;}

    public void setLevelAndRoom(int level, int room)
    {
        this.currentLevel=level;
        this.currentRoom=room;
        this.forceEmptyRoom=true;
        loadRoom();
    }

    public MapLoader getCurrentMap() { return currentMap; }
    public EntityManager getEntityManager() { return entityManager; }
}
