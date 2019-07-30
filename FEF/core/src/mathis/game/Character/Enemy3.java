package mathis.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import mathis.game.DungeonConstruction.Map;

public class Enemy3 extends Enemy {

    public Enemy3(int X, int Y, Map  _map)
    {
        super(X, Y, _map);
        enTexture = new Texture("Enemies/Ghost4.png");
        isHovering = false;
        fightDiago = true;
        knockCount = 1;
        threatLvl = 3;
        action = 3;
        range = 2;
        life = 2;
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
