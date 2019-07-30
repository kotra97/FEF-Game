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
import mathis.game.Ui.ActionUI;
import mathis.game.Ui.LifeBar;
import mathis.game.Ui.MovementUI;

public class Player
{
    //Action / Movement

    public int pointAction; // Take each turn the value of action
    public int pointMovement; //Take each turn the value of range
    public int posX = 0;
    public  int posY = 0;
    private int range = 4; // Range movement
    private int rangeAction = 2; // Range Attack
    private int action = 4; // point of attack

    // life
    private Sound sound;
    public boolean dead;
    private int life;

    // Animation
    private int ticX;
    private int ticY;
    private int ticAdd;
    public boolean isMoving; // Not let the player move during the movement anim
    private Vector2 currentAim; // current tile aim by the sprite
    private Stack<Vector2> listPosition; // list of position which be aim until the final tile
    private Texture plTexture;

    //UI
    private LifeBar lifeMeter;
    private PathFinding path;
    private ActionUI actionUI;
    private MovementUI movementUi;


    public Player()
    {
        sound = Gdx.audio.newSound(Gdx.files.internal("Sound/HurtPlayer.mp3"));
        actionUI = new ActionUI();
        movementUi = new MovementUI();
        plTexture = new Texture("Player/Soldier.png");
        lifeMeter = new LifeBar();
        life = 3;
        dead = false;
        isMoving = false;
        currentAim = null;
        ticAdd = Map.cellHeightWidth / 4;
    }

    public void Draw(SpriteBatch batch, int x, int y, int cellSize)
    {
        batch.draw(plTexture, x + ticX, StandardScreen.heightScreen - y - ticY, cellSize, cellSize);
        actionUI.Draw(batch, pointAction);
        movementUi.Draw(batch, pointMovement);
        lifeMeter.Draw(batch, life);
        if (life <= 0)
            dead = true;
    }

    public void NewTurn(Map _map)
    {
        pointAction = action;
        pointMovement = range;
        SwitchMovementAttack(true, _map);
    }

    // WIP
    public void ProgressOneTile(Map map)
    {
        if (currentAim != null)
            Move(ticAdd);
        else {
            if (listPosition.empty())
            {
                isMoving = false;
                SwitchMovementAttack(pointMovement != 0, map);
                return;
            }
            currentAim = listPosition.pop();
            if (currentAim.x < posX)
                ticX = Map.cellHeightWidth;
            else if (currentAim.x > posX)
                ticX = -Map.cellHeightWidth;
            else if (currentAim.y < posY)
                ticY = Map.cellHeightWidth;
            else if (currentAim.y > posY)
                ticY = -Map.cellHeightWidth;
            posX = (int)currentAim.x;
            posY = (int)currentAim.y;
        }
    }

    private void Move(int tic)
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
            currentAim = null;
    }

    public void ChangePlayerPosition(int x, int y, Map map)
    {
        map.tileMap[posY][posX].SomeoneDisappear();
        pointMovement -= path.isTileExist(new Vector2(x, y)).priorityTile;
        map.tileMap[y][x].SomeoneAppear();
        listPosition = path.ListPosition(new Vector2(x, y));
        listPosition.pop();
        isMoving = true;
        ProgressOneTile(map);
        map.ResetTile();
    }

    private void ShowPlayerTiles(List<TilePath> tiles, Map map)
    {
        TilePath tmp;

        Gdx.app.log("Debug", "Nb " + tiles.size());
        for (int i = 0; i < tiles.size(); i++)
        {
            tmp = tiles.get(i);
            map.tileMap[(int)tmp.position.y][(int)tmp.position.x].cellState = Tile.PROPOSED;
        }
    }

    public void SwitchMovementAttack(boolean Movement, Map _map)
    {
        List<TilePath> tmpTiles;

        _map.ResetTile();
        if (Movement)
        {
            path = new PathFinding(_map);
            path.CreateMapMovement(posX, posY, true, false);
            tmpTiles = path.getInRangeTile(pointMovement);
            ShowPlayerTiles(tmpTiles, _map);
        }
        else
        {
            path = new PathFinding(_map);
            path.CreateMapMovement(posX, posY, false, false);
            tmpTiles = path.getInRangeTile(rangeAction, posX, posY);
            ShowPlayerTiles(tmpTiles, _map);
        }
    }

    public void Attack(Enemy enemy)
    {
        if (pointAction == 0)
            return;
        if (enemy != null)
            enemy.GetHit(posX, posY);
        pointAction--;
    }

    private void GetKnockback(Vector2 incidence, Map map)
    {
        if (incidence.x == posX)
        {
            if (incidence.y > posY)
            {
                if (posY != 0 && !map.tileMap[posY - 1][posX].isHole && !map.tileMap[posY - 1][posX].hasSomeone)
                {
                    map.tileMap[posY][posX].SomeoneDisappear();
                    posY--;
                    map.tileMap[posY][posX].SomeoneAppear();
                }
            }
            else
            {
                if (posY + 1 != Map.mapHeight && !map.tileMap[posY + 1][posX].isHole && !map.tileMap[posY + 1][posX].hasSomeone)
                {
                    map.tileMap[posY][posX].SomeoneDisappear();
                    posY++;
                    map.tileMap[posY][posX].SomeoneAppear();
                }
            }
        }
        else
        {
            if (incidence.x > posX)
            {
                if (posX != 0 && !map.tileMap[posY][posX - 1].isHole && !map.tileMap[posY][posX - 1].hasSomeone)
                {
                    map.tileMap[posY][posX].SomeoneDisappear();
                    posX--;
                    map.tileMap[posY][posX].SomeoneAppear();
                }
            }
            else
            {
                if (posX + 1 != Map.mapWidth && !map.tileMap[posY][posX + 1].isHole && !map.tileMap[posY][posX + 1].hasSomeone)
                {
                    map.tileMap[posY][posX].SomeoneDisappear();
                    posX++;
                    map.tileMap[posY][posX].SomeoneAppear();
                }
            }
        }
    }

    void GetHit(int damage, Vector2 incidence, Map map)
    {
        life -= damage;
        sound.play();
        GetKnockback(incidence, map);
    }
}
