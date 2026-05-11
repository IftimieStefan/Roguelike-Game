package PaooGame.Graphics;

import PaooGame.Core.GameCamera;
import PaooGame.Tiles.Tile;

import java.awt.*;

public class FogOfWar {
    private boolean[][] visible;    // matricea de vizibilitate per tile
    private int width, height;      // dimensiunea hartii in tile-uri
    private int viewRadius = 4;     // raza de vizibilitate clara
    private int playerTileX, playerTileY; // pozitia curenta a playerului in tile-uri

    // Culoare fog
    private static final int FOG_R = 15;
    private static final int FOG_G = 15;
    private static final int FOG_B = 40;

    public FogOfWar(int width, int height) {
        this.width = width;
        this.height = height;
        visible = new boolean[width][height];
    }

    // Reactualizeaza vizibilitatea in functie de pozitia playerului
    public void updateVisibility(float playerX, float playerY) {
        playerTileX = (int)(playerX / Tile.TILE_WIDTH);
        playerTileY = (int)(playerY / Tile.TILE_HEIGHT);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (distance(playerTileX, playerTileY, x, y) <= viewRadius) {
                    visible[x][y] = true;
                }
                // tile-urile deja vizitate raman vizibile
            }
        }
    }

    private double distance(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Deseneaza fog-ul cu gradient de opacitate
    public void Draw(Graphics2D g2d, GameCamera camera, int screenW, int screenH) {
        int xOff = (camera != null) ? (int) camera.getxOff() : 0;
        int yOff = (camera != null) ? (int) camera.getyOff() : 0;
        float zoom = (camera != null) ? camera.getZoom() : 2f;

        int xStart = Math.max(0, xOff / Tile.TILE_WIDTH);
        int xEnd = Math.min(width, (xOff + screenW) / Tile.TILE_WIDTH + 1);
        int yStart = Math.max(0, yOff / Tile.TILE_HEIGHT);
        int yEnd = Math.min(height, (yOff + screenH) / Tile.TILE_HEIGHT + 1);

        for (int x = xStart; x < xEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                double dist = distance(playerTileX, playerTileY, x, y);
                int alpha = getAlpha(dist, visible[x][y]);

                if (alpha > 0) {
                    int drawX = (int)((x * Tile.TILE_WIDTH - xOff) * zoom);
                    int drawY = (int)((y * Tile.TILE_HEIGHT - yOff) * zoom);
                    
                    int nextX = (int)(((x + 1) * Tile.TILE_WIDTH - xOff) * zoom);
                    int nextY = (int)(((y + 1) * Tile.TILE_HEIGHT - yOff) * zoom);
                    
                    int drawW = nextX - drawX;
                    int drawH = nextY - drawY;
                    
                    g2d.setColor(new Color(FOG_R, FOG_G, FOG_B, alpha));
                    g2d.fillRect(drawX, drawY, drawW, drawH);
                }
            }
        }
    }

    // Calculeaza alpha-ul in functie de distanta si vizibilitate
    private int getAlpha(double dist, boolean explored) {
        if (dist <= viewRadius) {
            // In raza de vizibilitate — clar, fara fog
            return 0;
        } else if (dist <= viewRadius + 2) {
            // Zona de tranzitie — fog partial (gradient)
            double t = (dist - viewRadius) / 2.0;
            return (int)(t * 160);
        } else if (explored) {
            // Explorat dar departe — fog mediu (se vede vag)
            return 160;
        } else {
            // Neexplorat — fog complet opac
            return 255;
        }
    }

    public boolean isVisible(int tileX, int tileY) {
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height) return false;
        return visible[tileX][tileY];
    }

    public void setViewRadius(int radius) { this.viewRadius = radius; }
}
