package mathis.game.DungeonConstruction;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PathFinding {
    private Map map;
    private List<TilePath> listTile = new ArrayList<TilePath>();
    private Vector2 origin;

     final static int Up = 0;
     final static int Down = 1;
     final static int Left = 2;
     final static int Right = 3;

    public PathFinding(Map _map)
    {
        map = _map;
    }

    // create way function

    public Stack<Vector2> ListPosition(Vector2 finalPosition)
    {
        Stack<Vector2> returnPath = new Stack<Vector2>();
        TilePath tmpTile;
        TilePath currentTile;

        currentTile = isTileExist(finalPosition);
        returnPath.push(finalPosition);
        if (currentTile == null)
            return returnPath;
        while ((tmpTile = currentTile.getTileCloser()) != null)
        {
            currentTile = tmpTile;
            returnPath.push(currentTile.position);
        }
        return returnPath;
    }

    // Search tiles function

    public TilePath isTileExist(Vector2 pos)
    {
        for (int i = 0; i < listTile.size(); i++)
        {
            if (listTile.get(i) != null)
                if (listTile.get(i).position.x == pos.x &&
                        listTile.get(i).position.y == pos.y)
                    return listTile.get(i);
        }
        return null;
    }

    public List<TilePath> getInRangeTile(int range, int x, int y)
    {
        List<TilePath> listReturn = new ArrayList<TilePath>();

        for (int i = 0; i < listTile.size(); i++)
        {
            if (listTile.get(i) != null && listTile.get(i).priorityTile <= range &&
                    (listTile.get(i).position.x == x ||
                            listTile.get(i).position.y == y))
                listReturn.add(listTile.get(i));
        }
        return listReturn;
    }

    public List<TilePath> getInRangeTile(int range)
    {
        List<TilePath> listReturn = new ArrayList<TilePath>();

        for (int i = 0; i < listTile.size(); i++)
        {
            if (listTile.get(i) != null && listTile.get(i).priorityTile <= range)
                listReturn.add(listTile.get(i));
        }
        return listReturn;
    }

    public Vector2 getClosestTile(Vector2 position, int space, boolean diag)
    {
        TilePath tmp;
        Vector2 positionReturn = null;
        int smallestPriority = 66;
        if (!diag) {
            for (int i = space; i >= -space; i--) {
                if ((tmp = isTileExist(new Vector2(position.x + i, position.y))) != null)
                    if (tmp.priorityTile < smallestPriority) {
                        positionReturn = tmp.position;
                        smallestPriority = tmp.priorityTile;
                    }
            }
            for (int i = space; i >= -space; i--) {
                if ((tmp = isTileExist(new Vector2(position.x, position.y + i))) != null)
                    if (tmp.priorityTile < smallestPriority){
                        positionReturn = tmp.position;
                        smallestPriority = tmp.priorityTile;
                    }
            }
        }
        else
        {
            for (int x = space; x > -space; x--)
            {
                int ySpace = space - Math.abs(x);
                for (int y = ySpace; y >= ySpace; y--)
                {
                    if ((tmp = isTileExist(new Vector2(position.x + x, position.y + y))) != null)
                        if (tmp.priorityTile < smallestPriority){
                            positionReturn = tmp.position;
                            smallestPriority = tmp.priorityTile;
                        }
                }
            }
        }
        return positionReturn;
    }

    //Create pathfinding functions

    private List<TilePath> ListSurrounded(int positionX, int positionY)
    {
        List<TilePath> listSurrounding = new ArrayList<TilePath>();

        listSurrounding.add(isTileExist(new Vector2(positionX + 1, positionY)));
        listSurrounding.add(isTileExist(new Vector2(positionX - 1, positionY)));
        listSurrounding.add(isTileExist(new Vector2(positionX, positionY + 1)));
        listSurrounding.add(isTileExist(new Vector2(positionX, positionY - 1)));
        return listSurrounding;
    }

    private void addSurroundings(TilePath currentTile)
    {
        List<TilePath> listSurrounding =
                ListSurrounded((int)currentTile.position.x, (int)currentTile.position.y);
        if (listSurrounding.get(0) != null)
        {
            currentTile.addTileSurrounding(listSurrounding.get(0), Right);
            listSurrounding.get(0).addTileSurrounding(currentTile, Left);
        }
        if (listSurrounding.get(1) != null)
        {
            currentTile.addTileSurrounding(listSurrounding.get(1), Left);
            listSurrounding.get(1).addTileSurrounding(currentTile, Right);
        }
        if (listSurrounding.get(2) != null)
        {
            currentTile.addTileSurrounding(listSurrounding.get(2), Up);
            listSurrounding.get(2).addTileSurrounding(currentTile, Down);
        }
        if (listSurrounding.get(3) != null)
        {
            currentTile.addTileSurrounding(listSurrounding.get(3), Down);
            listSurrounding.get(3).addTileSurrounding(currentTile, Up);
        }
    }

    private TilePath setFinaleTileAspect(TilePath currentTile)
    {
        if (currentTile == null)
            return null;
        return currentTile;
    }

    private void SmoothMap()
    {
        boolean change = true;

        while (change)
        {
            change = false;
            for (int i = 0; i < listTile.size(); i++)
            {
                if (listTile.get(i) != null)
                    if (listTile.get(i).PriorityChange())
                        change = true;
            }
        }
        for (int i = 0; i < listTile.size(); i++)
            listTile.set(i, setFinaleTileAspect(listTile.get(i)));
    }

    private boolean UpDownTile(int xF, int yF, boolean obstacle, boolean hovering)
    {
        boolean hasCreated = CreateTileMouvement(xF, yF);

        for (int x = xF; x < Map.mapWidth; x++)
        {
            for (int y = 0; y < Map.mapHeight ; y++)
            {
                if (!obstacle)
                {
                    if (CreateTileMouvement(x, y))
                        hasCreated = true;
                }
                else if ((!map.tileMap[y][x].isHole || hovering) &&
                        (!map.tileMap[y][x].hasSomeone ||
                                (!map.tileMap[y][x].hasSomeone
                                && x == origin.x && y == origin.y)))
                        if (CreateTileMouvement(x, y))
                            hasCreated = true;
            }
            for (int y = 0; y >= 0; y--)
            {
                if (!obstacle)
                {
                    if (CreateTileMouvement(x, y))
                        hasCreated = true;
                }
                else if ((!map.tileMap[y][x].isHole || hovering) &&
                        (!map.tileMap[y][x].hasSomeone ||(!map.tileMap[y][x].hasSomeone
                                && x == origin.x && y == origin.y)))
                    if (CreateTileMouvement(x, y))
                        hasCreated = true;
            }
        }
        for (int x = xF; x >= 0; x--)
        {
            for (int y = 0; y < Map.mapHeight ; y++)
            {
                if (!obstacle)
                {
                    if (CreateTileMouvement(x, y))
                        hasCreated = true;
                }
                else if ((!map.tileMap[y][x].isHole || hovering) &&
                        (!map.tileMap[y][x].hasSomeone ||(!map.tileMap[y][x].hasSomeone
                                && x == origin.x && y == origin.y)))
                    if (CreateTileMouvement(x, y))
                        hasCreated = true;
            }
            for (int y = 0; y >= 0; y--)
            {
                if (!obstacle)
                {
                    if (CreateTileMouvement(x, y))
                        hasCreated = true;
                }
                else if ((!map.tileMap[y][x].isHole || hovering) &&
                        (!map.tileMap[y][x].hasSomeone ||(!map.tileMap[y][x].hasSomeone
                                && x == origin.x && y == origin.y)))
                    if (CreateTileMouvement(x, y))
                        hasCreated = true;
            }
        }
        return hasCreated;
    }

    private boolean CreateTileMouvement(int positionX, int positionY)
    {
        if (isTileExist(new Vector2(positionX, positionY)) != null)
            return false;

        TilePath currentTile = new TilePath();

        currentTile.position = new Vector2(positionX, positionY);
        if (positionX == origin.x && positionY == origin.y)
            currentTile.priorityTile = 0;
        else
            currentTile.priorityTile = 1000;
        addSurroundings(currentTile);
        listTile.add(currentTile);
        return true;
    }

    public void CreateMapMovement(int x, int y, boolean obstacle, boolean hovering)
    {
        origin = new Vector2(x, y);
        while (UpDownTile(x, y, obstacle, hovering));
        SmoothMap();
    }
}
