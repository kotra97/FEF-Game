package mathis.game.Character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import mathis.game.DungeonConstruction.Map;

public class Enemy1 extends Enemy {

    public Enemy1(int X, int Y, Map  _map)
    {
        super(X, Y, _map);
        enTexture = new Texture("Enemies/Ghost1.png");
        isHovering = true;
        fightDiago = false;
        knockCount = 1;
        threatLvl = 2;
        action = 3;
        range = 1;
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
