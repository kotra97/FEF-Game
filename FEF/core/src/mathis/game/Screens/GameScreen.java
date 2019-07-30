package mathis.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import mathis.game.Character.Enemy;
import mathis.game.Character.Player;
import mathis.game.DungeonConstruction.Map;
import mathis.game.DungeonConstruction.Tile;
import mathis.game.MainGame;
import mathis.game.Ui.EndTurnAction;
import mathis.game.Ui.LevelUI;
import mathis.game.Ui.SwordAction;

public class GameScreen extends StandardScreen{

    private Map map;
    private int endXRectangleMap;
    private int endYRectangleMap;
    private int oldX;
    private int oldY;
    private Player player;
    private boolean playerTurn = false;
    private Enemy[] enemies;
    private Enemy focusEnemy;
    private int lvl;
    private SwordAction swordAction;
    private EndTurnAction endTurnAction;
    private boolean attackMode;
    private LevelUI levelUI;

    public GameScreen(MainGame _game) {
        super(_game);
        swordAction = new SwordAction();
        endTurnAction = new EndTurnAction();
        levelUI = new LevelUI();
        lvl = 0;
        endXRectangleMap = Map.startPosX + Map.cellHeightWidth * Map.mapWidth;
        endYRectangleMap = Map.startPosY + Map.cellHeightWidth * Map.mapHeight - Map.cellHeightWidth;
        player = new Player();
        NewLevel();
        focusEnemy = null;
        attackMode = false;
    }

    private void EndTurn()
    {
        for (Enemy enemy : enemies)
        {
            if (enemy != null)
                enemy.Reset();
        }
        playerTurn = false;
        swordAction.isPressed = false;
        endTurnAction.isPressed = false;
        attackMode = false;
        map.ResetTile();
    }

    private void NewLevel()
    {
        lvl++;
        map = new Map();
        map.GenerateMap(1);
        player.posY = Map.mapHeight - 1;
        player.posX = Map.mapWidth / 2;
        map.tileMap[player.posY][player.posX].SomeoneAppear();
        enemies = map.CreateEnemies(lvl);
        player.NewTurn(map);
        playerTurn = true;
        attackMode = false;
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button)
    {
        if (playerTurn) {
            if (screenX > Map.startPosX && screenY > Map.startPosY - Map.cellHeightWidth &&
                    screenX < endXRectangleMap && screenY < endYRectangleMap) {
                int x;
                int y;

                x = screenX - Map.startPosX;
                y = screenY - Map.startPosY + Map.cellHeightWidth;
                x = x / Map.cellHeightWidth;
                y = y / Map.cellHeightWidth;
                map.ChangeTileMap(x, y, true);
                oldX = x;
                oldY = y;
            }
            else if (screenX > Map.startPosX && screenY > endYRectangleMap &&
                    screenX < endXRectangleMap && screenY < endYRectangleMap + 250) {
                if (screenX < Map.startPosX + 250)
                {
                    swordAction.isPressed = !swordAction.isPressed;
                    player.SwitchMovementAttack(!swordAction.isPressed, map);
                    attackMode = swordAction.isPressed;
                }
                else if (screenX > endXRectangleMap - 250)
                    endTurnAction.isPressed = true;
            }
        }
        return false;
    }

    private void AttackMode(int x, int y)
    {
        if (player.pointAction > 0)
        {
            boolean hasFound = false;

            for (Enemy enemy : enemies)
                if (enemy != null && enemy.posY == y && enemy.posX == x)
                {
                    player.Attack(enemy);
                    focusEnemy = enemy;
                    hasFound = true;
                }
            if (!hasFound)
                player.Attack(null);
        }
        if (player.pointAction <= 0)
        {
            if (player.pointMovement == 0)
                EndTurn();
            else
            {
                swordAction.isPressed = false;
                player.SwitchMovementAttack(true, map);
                attackMode = swordAction.isPressed;
            }
        }
    }

    private void MovementMode(int x, int y)
    {
        if (player.pointMovement > 0)
            player.ChangePlayerPosition(x, y, map);
        if (player.pointMovement <= 0)
        {
            if (player.pointAction <= 0)
                EndTurn();
            else
                attackMode = swordAction.isPressed = true;
        }
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button)
    {
        if (playerTurn && focusEnemy == null)
        {
            if (screenX > Map.startPosX && screenY > Map.startPosY - Map.cellHeightWidth &&
                    screenX < endXRectangleMap && screenY < endYRectangleMap) {
                int x;
                int y;

                x = screenX - Map.startPosX;
                y = screenY - Map.startPosY + Map.cellHeightWidth;
                x = x / Map.cellHeightWidth;
                y = y / Map.cellHeightWidth;
                if (map.GetStateCell(x, y) == Tile.ACCEPTED &&  !player.isMoving)
                {
                    if (attackMode)
                        AttackMode(x, y);
                    else
                        MovementMode(x, y);
                }
                map.ChangeTileMap(oldX, oldY, false);
            }
            else if (screenX > Map.startPosX && screenY > endYRectangleMap &&
                    screenX < endXRectangleMap && screenY < endYRectangleMap + 250)
                if (screenX > endXRectangleMap - 250)
                    EndTurn();
        }
        return false;
    }

    private void EnemyTurn()
    {
        if (focusEnemy == null)
            for (Enemy enemy : enemies)
                if (enemy != null)
                    if (!enemy.isDead && enemy.state != Enemy.END)
                        focusEnemy = enemy;
        if (focusEnemy == null)
        {
            playerTurn = true;
            player.NewTurn(map);
            for (Enemy enemy : enemies) {
                if (enemy != null)
                    enemy.state = Enemy.WAIT;
            }
        }
        else
        {
            focusEnemy.Action(player);
            if (focusEnemy.state == Enemy.END)
                focusEnemy = null;
        }
    }

    private void EnemyFeedBack()
    {
        focusEnemy.Action(player);
        if (focusEnemy.state == Enemy.WAIT)
            focusEnemy = null;
    }

    @Override
    public void render(float delta)
    {
        boolean allAreDead;

        allAreDead = true;
        game.batch.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0,0, 0, 1);
        swordAction.Draw(game.batch);
        endTurnAction.Draw(game.batch);
        levelUI.Draw(game.batch, lvl);
        map.Draw(game.batch);
        player.Draw(game.batch, Map.startPosX + (player.posX * Map.cellHeightWidth),
                Map.startPosY + (player.posY * Map.cellHeightWidth), Map.cellHeightWidth);
        for (Enemy enemy: enemies)
        {
            if (enemy != null && !enemy.isDead) {
                enemy.Draw(game.batch, Map.startPosX + (enemy.posX * Map.cellHeightWidth),
                        Map.startPosY + (enemy.posY * Map.cellHeightWidth));
                allAreDead = false;
            }
        }
        if (allAreDead)
            NewLevel();
        else if (player.isMoving)
            player.ProgressOneTile(map);
        else if (player.dead)
        {
            if (game.maxLvl < lvl)
                game.maxLvl = lvl;
            game.ChangeScreen(3);
        }
        else if (!playerTurn)
            EnemyTurn();
        else if (focusEnemy != null) // One enemy is knockback
            EnemyFeedBack();
        game.batch.end();
    }
}
