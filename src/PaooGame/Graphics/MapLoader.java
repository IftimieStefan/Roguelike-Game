package PaooGame.Graphics;

import java.awt.*;
import PaooGame.Tiles.Tile;
import java.util.Scanner;
import java.io.File;

public class MapLoader
{
    private int width, height;
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

    public void Draw(Graphics g, int screenWidth, int screenHeight)
    {
        int scaledSize = screenHeight/11;

        int mapWidth = 24*scaledSize;

        int offsetX=(screenWidth-mapWidth)/2;

        for(int j=0;j<height;++j)
        {
            for(int i=0;i<width;++i)
            {
                Tile t=Tile.tiles[tiles[i][j]];
                if(t!=null)
                {
                    t.Draw(g,offsetX+i*scaledSize,j*scaledSize,scaledSize,scaledSize);//desenam tile-urile
                }
            }
        }
    }
}
