package PaooGame;

import java.awt.*;
import PaooGame.Tiles.Tile;
import java.util.Scanner;
import java.io.File;

public class Map
{
    private int width, height;
    private int[][] tiles;

    public Map(String path)
    {
        LoadWorld(path);
    }

    private void LoadWorld(String path)
    {
        try {
            File file = new File(path);
            Scanner sc = new Scanner(file);

            width = sc.nextInt();
            height = sc.nextInt();//citim dimensiunile hartii
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

    public void Draw(Graphics g)
    {
        for(int j=0;j<height;++j)
        {
            for(int i=0;i<width;++i)
            {
                Tile t=Tile.tiles[tiles[i][j]];
                if(t!=null)
                {
                    t.Draw(g,i*Tile.TILE_WIDTH,j*Tile.TILE_HEIGHT);//desenam tile-urile
                }
            }
        }
    }
}
