package PaooGame;

import java.awt.*;

import PaooGame.Core.GameCamera;
import PaooGame.Tiles.Tile;
import java.util.Scanner;
import java.io.File;

public class MapLoader
{
    private int width=60, height=40;
    private int[][] tiles;

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

                    // Dacă avem cameră, preluăm offset-urile ei
                    if (camera != null) {
                        xOff = (int)camera.getxOff();
                        yOff = (int)camera.getyOff();
                    }

                    // Desenăm folosind Tile.TILE_WIDTH (48) și scădem camera
                    // Astfel, dacă pinguinul merge la dreapta, camera scade din X și harta "vine" spre noi
                    t.Draw(g, i * Tile.TILE_WIDTH - xOff, j * Tile.TILE_HEIGHT - yOff);
                }
            }
        }
    }
}
