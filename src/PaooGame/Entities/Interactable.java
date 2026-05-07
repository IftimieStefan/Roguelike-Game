package PaooGame.Entities;

import PaooGame.Entities.Player.Player;

// Tasta E
public interface Interactable {
    boolean tryInteract(Player player); 
    float getInteractRange();           
}
