package mathis.game.DungeonConstruction;

import com.badlogic.gdx.math.Vector2;

public class TilePath {
    private TilePath[] surroundedTiles = new TilePath[4];
    public Vector2 position;
    public int priorityTile = 1000;

    boolean PriorityChange()
    {
        int tmpPriority;
        int priorityEnd = priorityTile;
        if (priorityTile == 0)
            return (false);
        for (int i = 0;i < 4;i++)
            if (surroundedTiles[i] != null)
            {
                tmpPriority = surroundedTiles[i].priorityTile;
                if (tmpPriority < priorityEnd)
                    priorityEnd = tmpPriority;
            }
        if (priorityEnd < priorityTile - 1)
        {
            priorityTile = priorityEnd + 1;
            return (true);
        }
        return (false);
    }

    TilePath getTileCloser()
    {
        int tmpPriority;
        if (priorityTile == 0)
            return null;
        for (int i = 0; i < 4; i++)
            if (surroundedTiles[i] != null)
            {
                tmpPriority = surroundedTiles[i].priorityTile;
                if (priorityTile > tmpPriority)
                    return surroundedTiles[i];
            }
        return (null);
    }

    void addTileSurrounding(TilePath obj, int side)
    {
        surroundedTiles[side] = obj;
    }

}
