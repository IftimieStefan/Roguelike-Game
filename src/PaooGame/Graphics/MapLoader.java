package PaooGame.Graphics;

import java.awt.*;

import PaooGame.Core.GameCamera;
import PaooGame.Tiles.Tile;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapLoader
{
    private int width, height;
    private int[][] tiles;
    private List<String> entityData;

    public MapLoader(String path)
    {
        LoadWorld(path);
    }

    private void LoadWorld(String path)
    {
        try {
            File file = new File(path);
            Scanner sc = new Scanner(file);

            sc.useDelimiter("[,\\s\\n\\r]+");

            width=sc.nextInt();
            height=sc.nextInt();
            tiles = new int[width][height];

            for (int j = 0; j < height; ++j) {
                for (int i = 0; i < width; ++i) {
                    tiles[i][j] = sc.nextInt();//citim valorile tile-urilor de pe harta
                }
            }

            entityData = new ArrayList<>();
            // Citim restul fisierului pentru entitati
            while(sc.hasNext()) {
                String line = sc.nextLine().trim();
                if(!line.isEmpty()) {
                    entityData.add(line);
                }
            }
            sc.close();
        }catch(Exception e)
        {
            System.err.println("Eroare la incarcarea hartii");
            e.printStackTrace();
        }
    }

    public void Update()
    {

    }

    public List<String> getEntityData() {
        return entityData;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void Draw(Graphics g, GameCamera camera)
    {
        int xOff = (camera != null) ? (int)camera.getxOff() : 0;
        int yOff = (camera != null) ? (int)camera.getyOff() : 0;

        int xStart = Math.max(0, xOff / Tile.TILE_WIDTH);
        int xEnd = Math.min(width, (xOff + 1920) / Tile.TILE_WIDTH + 1); // 800 e latimea ecranului

        int yStart = Math.max(0, yOff / Tile.TILE_HEIGHT);
        int yEnd = Math.min(height, (yOff + 1080) / Tile.TILE_HEIGHT + 1); // 600 e ianaltimea ecranului

        for(int j = yStart; j < yEnd; ++j)
        {
            for(int i = xStart; i < xEnd; ++i)
            {
                Tile t = Tile.tiles[tiles[i][j]];
                if(t != null)
                {
                    // dimensiune tile latime si scadem offset
                    t.Draw(g, i * Tile.TILE_WIDTH - xOff, j * Tile.TILE_HEIGHT - yOff);
                }
            }
        }
    }
}
