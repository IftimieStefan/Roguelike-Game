package PaooGame.Managers;

import PaooGame.Entities.Entity;
import PaooGame.Entities.Door;
import PaooGame.Entities.Enemies.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Gestionează toate entitățile din nivel printr-o singură listă polimorfică
public class EntityManager {

    private final List<Entity> entities;          // Lista entitati
    private final List<Entity> pendingEntities;    // Lista temporara pentru a preveni ConcurrentModificationException
    private final List<EnemyDeathObs> observers;   // Observers pentru moartea inamicilor
    private boolean alrNotif;                      // Flag
    private boolean isUpdating = false;

    public EntityManager() {
        this.entities = new ArrayList<>();
        this.pendingEntities = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        if (entity == null) return;
        
        if (isUpdating) {
            pendingEntities.add(entity);
        } else {
            entities.add(entity);
        }

        if (entity instanceof EnemyDeathObs) {     // Door in observers
            addObserver((EnemyDeathObs) entity);
        }
        // Foca Mexic are nevoie de referința la EntityManager pentru buff
        if (entity instanceof Foca_Mexic) {
            ((Foca_Mexic) entity).setEntityManager(this);
        }
        if (entity instanceof Foca_Escroca) {
            ((Foca_Escroca) entity).setEntityManager(this);
        }
    }

    // ── Observer pattern ──

    public void addObserver(EnemyDeathObs obs) {
        if (obs != null) observers.add(obs);
    }

    public void removeObserver(EnemyDeathObs obs) {
        observers.remove(obs);
    }

    private void notifyAllD() {
        for (EnemyDeathObs obs : observers) obs.onDeadEnemies();  // Notify observers
        alrNotif = true;  // Modificam flag
    }
    public void Update() {
        isUpdating = true;
        for (Entity e : entities) e.Update();         // Update fiecare entitate
        isUpdating = false;
        
        entities.addAll(pendingEntities);
        pendingEntities.clear();
        
        entities.removeIf(e -> !e.isActive());        // Scoate entit moarte/activate

        boolean hasEnemies = false;
        for (Entity e : entities) {
            if (e instanceof Inamic) { hasEnemies = true; break; }
        }
        if (!hasEnemies && !alrNotif) notifyAllD();   // Deschide door
    }

    public void Draw(Graphics g) {
        for (Entity e : entities) e.Draw(g);
    }


    public List<Entity> getEntities() { return entities; }

    public List<Inamic> getEnemies() {
        List<Inamic> result = new ArrayList<>();
        for (Entity e : entities) {
            if (e instanceof Inamic) result.add((Inamic) e);
        }
        return result;
    }

    public Door getDoor() {
        for (Entity e : entities) {
            if (e instanceof Door) return (Door) e;
        }
        return null;
    }


    public void processExplosion(Rectangle exp) {
        CollisionSystem.processExplosion(exp, getEnemies());
    }
}
