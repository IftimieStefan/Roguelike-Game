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
    public static BufferedImage ice,ices,icen,icee,icene,icew,icenw,icese,icesw;
    public static BufferedImage pereteorizontal;
    public static BufferedImage peretevertical;
    public static BufferedImage peretecoltds;
    public static BufferedImage peretecoltdj;
    public static BufferedImage peretecoltss;
    public static BufferedImage peretecoltsj;
    public static BufferedImage peretecolt;
    public static BufferedImage grass;
    public static BufferedImage rock;
    public static BufferedImage water;
    public static BufferedImage stick;
    public static BufferedImage usaorizontal;
    public static BufferedImage usavertical;
    public static BufferedImage rockRight;
    public static BufferedImage tree;
    public static BufferedImage shore;

    public  static BufferedImage inamic_samurai;
    public  static BufferedImage inamic_cowboy;
    public  static BufferedImage inamic_mehico;
    public  static BufferedImage foca_escroca;

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

        SpriteSheet inamici = new SpriteSheet(ImageLoader.LoadImage("/textures/inamici.png"));
        SpriteSheet boss = new SpriteSheet(ImageLoader.LoadImage("/textures/foca_escrocaa.png"));


        inamic_samurai = inamici.cropAbsolute(0,0,69,64);
        inamic_mehico = inamici.cropAbsolute(1,139,69,64);
        inamic_cowboy = inamici.cropAbsolute(3,277,69,64);
        foca_escroca = boss.cropAbsolute(0,2,67,64);



        /// Se obtin subimaginile corespunzatoare elementelor necesare.
        snow = sheet.crop(0, 0);
        ice = sheet.crop(1, 7);
        icen=sheet.crop(1,6);
        ices=sheet.crop(1,8);
        icee=sheet.crop(2,7);
        icew=sheet.crop(0,7);
        icene=sheet.crop(2,6);
        icenw=sheet.crop(0,6);
        icese=sheet.crop(2,8);
        icesw=sheet.crop(0,8);
        peretecoltdj=sheet.crop(2,2);
        peretecoltds = sheet.crop(2,0);
        peretecoltss = sheet.crop(0,2);
        peretecoltsj = sheet.crop(1,2);
        water = sheet.crop(1, 1);
        pereteorizontal = sheet.crop(1,0);
        peretevertical = sheet.crop(2,1);
        peretecolt = sheet.crop(2,0);
        usaorizontal = sheet.crop(1,3);
        usavertical=sheet.crop(2,4);
        stick=sheet.crop(0,3);
        rock=sheet.crop(0,4);
        grass=sheet.crop(0,5);
        shore=sheet.crop(0,1);

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



//        water = sheet.crop(2, 0);
//        townGrassDestroyed = sheet.crop(1, 1);
//        townSoil = sheet.crop(2, 1);
//        tree = sheet.crop(3, 1);
//        rockUp = sheet.crop(2, 2);
//        rockDown = sheet.crop(3, 2);
//        rockLeft = sheet.crop(0, 3);
//        rockRight = sheet.crop(1, 3);
    }
}