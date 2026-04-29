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
    private int width=50, height=40;
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

    public void Draw(Graphics g, GameCamera camera)
    {
        for(int j = 0; j < height; ++j)
        {
            for(int i = 0; i < width; ++i)
            {
                Tile t = Tile.tiles[tiles[i][j]];
                if(t != null)
                {
                    int xOff = 0, yOff = 0;

                    // luam offseturi
                    if (camera != null) {
                        xOff = (int)camera.getxOff();
                        yOff = (int)camera.getyOff();
                    }

                    // dimensiune tile latime si scadem offset
                    t.Draw(g, i * Tile.TILE_WIDTH - xOff, j * Tile.TILE_HEIGHT - yOff);
                }
            }
        }
    }
}
