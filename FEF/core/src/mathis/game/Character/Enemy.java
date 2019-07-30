package mathis.game.Character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.List;
import java.util.Stack;

import mathis.game.DungeonConstruction.Map;
import mathis.game.DungeonConstruction.PathFinding;
import mathis.game.DungeonConstruction.Tile;
import mathis.game.DungeonConstruction.TilePath;
import mathis.game.Screens.StandardScreen;

public class Enemy
{
    public int threatLvl;
    //Action (Fight + movement)
    public int posX;
    public  int posY;
    boolean isHovering; // affected by holes
    boolean fightDiago; // is it straight line also around the character
    int range; // of attack
    int pointAction; // movement + attack point
    int action; // start each turn with this number of pointAction
    PathFinding pathFinding; // used for find a way and attack
    Map map;

    //Damage
    private int currentHit; // number hit in a round
    private Sound sound; // sound played when it
    public boolean isDead;
    int life;
    int knockCount; // number of it before getting knockback

    //Animation (tic are used for the progressive movement
    private int ticX;
    private int ticY;
    private int ticToAdd; // speed of movement
    private int ticToAddKnock; // speed when getting hit
    private int size; // Falling size
    Texture enTexture;

    //State of animation
    public int state;
    public static final int WAIT = 0;
    private static final int MOVE = 1;
    private static final int MOVING = 2;
    public static final int END = 3;
    private static final int KNOCKBACK = 4;
    private static final int FALLING = 5;

    Enemy(int X, int Y, Map _map)
    {
        threatLvl = 1;
        knockCount = 1;
        fightDiago = false;
        size = Map.cellHeightWidth;
        ticToAdd = Map.cellHeightWidth / 5;
        ticToAddKnock = Map.cellHeightWidth / 2;
        ticX = ticY = 0;
        isDead = false;
        isHovering = false;
        life = 5;
        action = 2;
        posX = X;
        posY = Y;
        map = _map;
        pathFinding = new PathFinding(map);
        state = WAIT;
        currentHit = 0;
        sound = Gdx.audio.newSound(Gdx.files.internal("Sound/HurtEnemy.mp3"));
    }

    public void Draw(SpriteBatch batch, int x, int y)
    {
        batch.draw(enTexture, x + ticX + (Map.cellHeightWidth / 2 - size / 2),
                StandardScreen.heightScreen - y - ticY + ((float)Map.cellHeightWidth / 2 - (float)size / 2),
                size, size);
    }

    public void Attack(Player pl)
    {}

    public void Action(Player pl)
    {
        if (state == MOVE)
            Act(pl);
        else if (state == MOVING)
            Move(MOVE, ticToAdd);
        else if (state == KNOCKBACK)
            Move(WAIT, ticToAddKnock);
        else if (state == FALLING)
            Fall();
    }

    private void Fall()
    {
        size -= 5;
        if (size <= 0)
        {
            Die();
            state = WAIT;
        }
    }

    void ProgressOneTile(Vector2 tmp, int newState)
    {
        map.tileMap[posY][posX].SomeoneDisappear();
        if (tmp.x < posX)
            ticX = Map.cellHeightWidth;
        else if (tmp.x > posX)
            ticX = -Map.cellHeightWidth;
        else if (tmp.y < posY)
            ticY = Map.cellHeightWidth;
        else if (tmp.y > posY)
            ticY = -Map.cellHeightWidth;
        posX = (int) tmp.x;
        posY = (int) tmp.y;
        map.tileMap[posY][posX].SomeoneAppear();
        state = newState;
    }

    private void Move(int newState, int tic)
    {
        if (ticY < 0)
            ticY += tic;
        else if (ticY > 0)
            ticY -= tic;
        else if (ticX < 0)
            ticX += tic;
        else if (ticX > 0)
            ticX -= tic;
        if (ticY == 0 && ticX == 0)
        {
            state = newState;
            if (isHovering == false && state == WAIT && map.tileMap[posY][posX].isHole)
                state = FALLING;
        }
    }

    private void GetKnockback(Vector2 incidence)
    {
        if (incidence.x == posX)
        {
            if (incidence.y > posY)
            {
                if (posY != 0 && !map.tileMap[posY - 1][posX].hasSomeone)
                    ProgressOneTile(new Vector2(posX, posY - 1), KNOCKBACK);
            }
            else
            {
                if (posY + 1 != Map.mapHeight && !map.tileMap[posY + 1][posX].hasSomeone)
                    ProgressOneTile(new Vector2(posX, posY + 1), KNOCKBACK);
            }
        }
        else
        {
            if (incidence.x > posX)
            {
                if (posX != 0 && !map.tileMap[posY][posX - 1].hasSomeone)
                    ProgressOneTile(new Vector2(posX - 1, posY), KNOCKBACK);
            }
            else
            {
                if (posX + 1 != Map.mapWidth && !map.tileMap[posY][posX + 1].hasSomeone)
                    ProgressOneTile(new Vector2(posX + 1, posY), KNOCKBACK);
            }
        }
    }

    void GetHit(int x, int y)
    {
        life -= 1;
        if (life <= 0)
            Die();
        currentHit++;
        sound.play();
        if (currentHit >= knockCount)
            GetKnockback(new Vector2(x, y));
    }

    public void Act(Player pl)
    {
        int rangeCount = range;
        Vector2 tmp;
        Stack<Vector2> listPath;

        tmp = null;
        if (pointAction == 0)
        {
            state = END;
            return;
        }
        pathFinding = null;
        pathFinding = new PathFinding(map);
        pathFinding.CreateMapMovement(posX, posY, true, isHovering);
        while (tmp == null)
        {
            tmp = pathFinding.getClosestTile(new Vector2(pl.posX, pl.posY), rangeCount, false);
            rangeCount++;
            if (rangeCount > 19)
                break;
        }
        if (tmp == null || rangeCount > 19 || pathFinding.isTileExist(tmp) == null)
        {
            state = END;
            return;
        }
        listPath = pathFinding.ListPosition(tmp);
        listPath.pop();
        if (listPath.empty())
        {
            // if rangeCount == 1 it means that the enemy is in range
            // otherwise the player is surrounded and this enemy can't reach him
            if (rangeCount >= range + 1)
                Attack(pl);
            else
                state = END;
        }
        else
        {
            pointAction--;
            ProgressOneTile(listPath.pop(), MOVING);
            state = MOVING;
        }
    }

    private void Die()
    {
        isDead = true;
        map.tileMap[posY][posX].SomeoneDisappear();
    }

    public void Reset()
    {
        state = MOVE;
        pointAction = action;
        currentHit = 0;
    }

    public void displayRange(boolean movement)
    {
        TilePath tmp;
        List<TilePath> tmpTiles;


        if (movement)
        {
            pathFinding = new PathFinding(map);
            pathFinding.CreateMapMovement(posX, posY, true, isHovering);
            tmpTiles = pathFinding.getInRangeTile(action);
            for (int i = 0; i < tmpTiles.size(); i++)
            {
                tmp = tmpTiles.get(i);
                map.tileMap[(int)tmp.position.y][(int)tmp.position.x].cellState = Tile.ENEMYRANGE;
            }
        }
        else
        {
            pathFinding = new PathFinding(map);
            pathFinding.CreateMapMovement(posX, posY, false, true);
            if (fightDiago)
                tmpTiles = pathFinding.getInRangeTile(range, posX, posY);
            else
                tmpTiles = pathFinding.getInRangeTile(range);
            for (int i = 0; i < tmpTiles.size(); i++)
            {
                tmp = tmpTiles.get(i);
                map.tileMap[(int)tmp.position.y][(int)tmp.position.x].cellState = Tile.ENEMYRANGE;
            }
        }
    }

    public void unDisplayRange()
    {
        map.ResetTile(Tile.ENEMYRANGE);
        map.ResetTile(Tile.ENEMYATTACK);
    }
}
