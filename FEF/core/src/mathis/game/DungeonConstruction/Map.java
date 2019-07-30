
package mathis.game.DungeonConstruction;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

import mathis.game.Character.Enemy;
import mathis.game.Character.Enemy1;
import mathis.game.Character.Enemy2;
import mathis.game.Character.Enemy3;

public class Map
{
    private Texture tileGround;
    private Texture tileHole;
    private Texture[] tileSelection;
    public Tile[][] tileMap;
    public static final int startPosY = 300;
    public static final int startPosX = 40;
    public static final int cellHeightWidth = 120;
    public static final int mapHeight = 12;
    public static final int mapWidth = 8;

    public Map()
    {
        tileGround = new Texture("Tile/Ground.png");
        tileHole = new Texture("Tile/Hole.png");
        tileSelection = new Texture[3];
        tileSelection[0] = new Texture("Tile/AcceptedMovement.png");
        tileSelection[1] = new Texture("Tile/RefusedMovement.png");
        tileSelection[2] = new Texture("Tile/ProposedMovement.png");
    }

    public void Draw(SpriteBatch batch)
    {
        for (int y = 0; y < mapHeight; y++)
            for (int x = 0; x < mapWidth; x++)
            {
                tileMap[y][x].Draw(batch, startPosX + (x * cellHeightWidth),
                        startPosY + (y * cellHeightWidth), cellHeightWidth, tileSelection);
            }
    }

    public void ResetTile()
    {
        for (int y = 0; y < mapHeight; y++)
            for (int x = 0; x < mapWidth; x++)
                tileMap[y][x].cellState = Tile.NORMAL;
    }

    public void ResetTile(int tileType)
    {
        for (int y = 0; y < mapHeight; y++)
            for (int x = 0; x < mapWidth; x++)
                if (tileMap[y][x].cellState == tileType)
                    tileMap[y][x].cellState = Tile.NORMAL;
    }


    public void GenerateMap(int typeOfMap)
    {
        Random r = new Random();

        if (typeOfMap == 1)
        {
            tileMap = new Tile[mapHeight][mapWidth];
            for (int y = 0; y < mapHeight; y++)
                for (int x = 0; x < mapWidth; x++) {
                    if (r.nextInt(100) < 10)
                    {
                        tileMap[y][x] = new Tile(tileHole, true);

                    }
                    else
                        tileMap[y][x] = new Tile(tileGround, false);
                }
        }
    }

    public Enemy[] CreateEnemies(int lvl)
    {
        int threatLevel = 0;
        Random r = new Random();
        Enemy[] enemies;
        int currentThreatAim;

        enemies = new Enemy[lvl];
        for (int i = 0; i < lvl && i < 14; i++)
        {
            int x =  r.nextInt(mapWidth);
            int y = r.nextInt(3);
            while (tileMap[y][x].isHole || tileMap[y][x].hasSomeone)
            {
                x =  r.nextInt(mapWidth);
                y =  r.nextInt(3);
            }
            if (lvl - threatLevel > 3)
                currentThreatAim = r.nextInt(3) + 1;
            else
                currentThreatAim = r.nextInt(lvl - threatLevel) + 1;
            if (currentThreatAim == 1)
                enemies[i] = new Enemy1(x, y, this);
            else if (currentThreatAim == 2)
                enemies[i] = new Enemy2(x, y, this);
            else if (currentThreatAim == 3)
                enemies[i] = new Enemy3(x, y, this);
            threatLevel += currentThreatAim;
            tileMap[y][x].SomeoneAppear();
            if (threatLevel >= lvl)
                break;
        }
        return enemies;
    }

    public int GetStateCell(int x, int y)
    {
        return (tileMap[y][x].cellState);
    }

    public void ChangeTileMap(int x, int y, boolean select)
    {
        if (select)
            tileMap[y][x].SelectTile();
        else
            tileMap[y][x].UnselectTile();
    }
}
