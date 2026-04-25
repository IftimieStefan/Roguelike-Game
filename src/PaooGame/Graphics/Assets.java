package PaooGame.Graphics;

import java.awt.image.BufferedImage;

/*! \class public class Assets
    \brief Clasa incarca fiecare element grafic necesar jocului.

    Game assets include tot ce este folosit intr-un joc: imagini, sunete, harti etc.
 */
public class Assets
{
    /// Referinte catre elementele grafice (dale) utilizate in joc.
    public static BufferedImage[] playerLeft=new BufferedImage[3];
    public static BufferedImage[] playerRight=new BufferedImage[3];
    public static BufferedImage[] playerDown=new BufferedImage[3];
    public static BufferedImage[] playerUp=new BufferedImage[3];
    public static BufferedImage snow;
    public static BufferedImage ice;
    public static BufferedImage pereteorizontal;
    public static BufferedImage peretevertical;
    public static BufferedImage peretecoltds;
    public static BufferedImage peretecoltdj;
    public static BufferedImage peretecoltss;
    public static BufferedImage peretecoltsj;
    public static BufferedImage townGrassDestroyed;
    public static BufferedImage townSoil;
    public static BufferedImage water;
    public static BufferedImage rockUp;
    public static BufferedImage rockDown;
    public static BufferedImage rockLeft;
    public static BufferedImage rockRight;
    public static BufferedImage tree;

    /*! \fn public static void Init()
        \brief Functia initializaza referintele catre elementele grafice utilizate.

        Aceasta functie poate fi rescrisa astfel incat elementele grafice incarcate/utilizate
        sa fie parametrizate. Din acest motiv referintele nu sunt finale.
     */
    public static void Init()
    {
        /// Se creaza temporar un obiect SpriteSheet initializat prin intermediul clasei ImageLoader
        SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage("/textures/TileMap.png"));
        SpriteSheet dinu = new SpriteSheet(ImageLoader.LoadImage("/textures/dinu_cu_esarfa.png"));

        /// Se obtin subimaginile corespunzatoare elementelor necesare.
        snow = sheet.crop(0, 0);
        ice = sheet.crop(1, 4);
        pereteorizontal = sheet.crop(1,0);
        peretevertical = sheet.crop(2,1);
        peretecoltdj=sheet.crop(2,2);
        peretecoltds = sheet.crop(2,0);
        peretecoltss = sheet.crop(0,2);
        peretecoltsj = sheet.crop(1,2);
        water = sheet.crop(1, 1);
        playerRight[0] = dinu.crop(1,1);
        playerRight[1]=dinu.crop(2,1);
        playerRight[2]=dinu.crop(3,1);
        playerLeft[0] = dinu.crop(4,1);
        playerLeft[1]=dinu.crop(0,2);
        playerLeft[2]=dinu.crop(1,2);
        playerDown[0] = dinu.crop(0,0);
        playerDown[1]=dinu.crop(1,0);
        playerDown[2]=dinu.crop(2,0);
        playerUp[0] = dinu.crop(3,0);
        playerUp[1]=dinu.crop(4,0);
        playerUp[2]=dinu.crop(0,1);

    }
}