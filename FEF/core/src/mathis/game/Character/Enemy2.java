package mathis.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import mathis.game.DungeonConstruction.Map;

public class Enemy2 extends Enemy {

    public Enemy2(int X, int Y, Map  _map)
    {
        super(X, Y, _map);
        enTexture = new Texture("Enemies/Ghost3.png");
        isHovering = false;
        fightDiago = false;
        knockCount = 2;
        threatLvl = 1;
        action = 4;
        range = 3;
        life = 3;
    }

    @Override
    public void Attack(Player pl)
    {
        if (pointAction != 0)
            pl.GetHit(1, new Vector2(posX, posY), map);
        pointAction = 0;
        state = END;
    }
}
