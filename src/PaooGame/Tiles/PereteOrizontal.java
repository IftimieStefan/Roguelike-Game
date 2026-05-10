package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

/*! \class public class SoilTile extends Tile
    \brief Abstractizeaza notiunea de dala de tip sol/pamant.
 */
public class PereteOrizontal extends Tile
{
    /*! \fn public SoilTile(int id)
        \brief Constructorul de initializare al clasei

        \param id Id-ul dalei util in desenarea hartii.
     */
    public PereteOrizontal(int id)
    {
        super(Assets.pereteorizontal, id);
    }

    @Override
    public boolean IsSolid() { return true; }
}
