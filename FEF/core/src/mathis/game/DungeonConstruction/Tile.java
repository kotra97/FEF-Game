package mathis.game.DungeonConstruction;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mathis.game.Screens.StandardScreen;

public class Tile
{
    private Texture textureTile;
    public static final int ACCEPTED = 0;
    private static final int REFUSED = 1;
    public static final int PROPOSED = 2;
    public static final int ENEMYRANGE = 3;
    public static final int ENEMYATTACK = 4;
    static final int NORMAL = 3;
    public int cellState;
    public boolean isHole;
    public boolean hasSomeone;

    Tile(Texture _tex, boolean hole)
    {
        isHole = false;
        cellState = NORMAL;
        textureTile = _tex;
        isHole = hole;
        hasSomeone = false;
    }

    public void Draw(SpriteBatch batch, int x, int y, int cellSize, Texture[] selectionList)
    {
        batch.draw(textureTile, x, StandardScreen.heightScreen - y, cellSize, cellSize);
        if (cellState != NORMAL)
            batch.draw(selectionList[cellState], x, StandardScreen.heightScreen - y, cellSize, cellSize);
    }

    void SelectTile()
    {
        if (cellState == NORMAL)
            cellState = REFUSED;
        if (cellState == PROPOSED)
            cellState = ACCEPTED;
    }

    void UnselectTile()
    {
        if (cellState == REFUSED)
            cellState = NORMAL;
        if (cellState == ACCEPTED)
            cellState = PROPOSED;
    }

    public void SomeoneAppear()
    {
        hasSomeone = true;
    }

    public void SomeoneDisappear()
    {
        hasSomeone = false;
    }
}
