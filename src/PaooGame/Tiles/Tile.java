package PaooGame.Tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

/*! \class public class Tile
    \brief Retine toate dalele intr-un vector si ofera posibilitatea regasirii dupa un id.
 */
public class Tile
{
    private static final int NO_TILES   = 32;
    public static Tile[] tiles          = new Tile[NO_TILES];       /*!< Vector de referinte de tipuri de dale.*/

        /// De remarcat ca urmatoarele dale sunt statice si publice. Acest lucru imi permite sa le am incarcate
        /// o singura data in memorie
    public static Tile snowTile        = new SnowTile(0);
    public static Tile pereteorizontal = new PereteOrizontal(1);
    public static Tile peretecoltds = new PereteColtDS(2);
    public static Tile shoreTile = new ShoreTile(3);
    public static Tile waterTile        = new WaterTile(4);
    public static Tile peretevertical = new PereteVertical(5);
    public static Tile peretecoltss = new PereteColtSS(6);
    public static Tile peretecoltsj = new PereteColtSJ(7);
    public static Tile peretecoltdj = new PereteColtDJ(8);
    public static Tile stickTile = new StickTile(9);
    public static Tile usaOrizontal = new UsaOrizontal(10);
    public static Tile rockTile=new RockTile(12);
    public static Tile usaVertical = new UsaVertical(14);
    public static Tile grassTile= new GrassTile(15);
    public static Tile iceTileNW=new IceTileNW(18);
    public static Tile iceTileN=new IceTileN(19);
    public static Tile iceTileNE=new IceTileNE(20);
    public static Tile iceTileW=new IceTileW(21);
    public static Tile iceTile=new IceTile(22);
    public static Tile iceTileE=new IceTileE(23);
    public static Tile iceTileSW=new IceTileSW(24);
    public static Tile iceTileS=new IceTileS(25);
    public static Tile iceTileSE=new IceTileSE(26);



    public static final int TILE_WIDTH  = 48;                       /*!< Latimea unei dale.*/
    public static final int TILE_HEIGHT = 48;                       /*!< Inaltimea unei dale.*/

    protected BufferedImage img;                                    /*!< Imaginea aferenta tipului de dala.*/
    protected final int id;                                         /*!< Id-ul unic aferent tipului de dala.*/

    /*! \fn public Tile(BufferedImage texture, int id)
        \brief Constructorul aferent clasei.

        \param image Imaginea corespunzatoare dalei.
        \param id Id-ul dalei.
     */
    public Tile(BufferedImage image, int idd)
    {
        img = image;
        id = idd;

        tiles[id] = this;
    }

    /*! \fn public void Update()
        \brief Actualizeaza proprietatile dalei.
     */
    public void Update()
    {

    }

    /*! \fn public void Draw(Graphics g, int x, int y)
        \brief Deseneaza in fereastra dala.

        \param g Contextul grafic in care sa se realizeze desenarea
        \param x Coordonata x in cadrul ferestrei unde sa fie desenata dala
        \param y Coordonata y in cadrul ferestrei unde sa fie desenata dala
     */
    public void Draw(Graphics g, int x, int y)
    {
            /// Desenare dala
        g.drawImage(img, x, y, null);
    }

    /*! \fn public boolean IsSolid()
        \brief Returneaza proprietatea de dala solida (supusa coliziunilor) sau nu.
     */
    public boolean IsSolid()
    {
        return false;
    }
    public boolean isDoor(){ return false;}

    /*! \fn public int GetId()
        \brief Returneaza id-ul dalei.
     */
    public int GetId()
    {
        return id;
    }
}
