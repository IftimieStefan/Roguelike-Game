package PaooGame.Tiles;

import java.awt.Rectangle;

public class Door {
    private Rectangle bounds;
    private int destinationRoom; // camera in care apari
    private float spawnX, spawnY; // coord unde apari in camera noua

    public Door(int x, int y, int width, int height, int dest, float sX, float sY)
    {
        this.bounds = new Rectangle(x, y, width, height);
        this.destinationRoom = dest;
        this.spawnX = sX;
        this.spawnY = sY;
    }

    public Rectangle getBounds() { return bounds; }
    public int getDestinationRoom() { return destinationRoom; }
    public float getSpawnX() { return spawnX; }
    public float getSpawnY() { return spawnY; }
}
