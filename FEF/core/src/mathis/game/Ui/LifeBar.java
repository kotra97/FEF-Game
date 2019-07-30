package mathis.game.Ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mathis.game.DungeonConstruction.Map;
import mathis.game.Screens.StandardScreen;

public class LifeBar
{
    private Texture lifeSprite;

    public LifeBar()
    {
        lifeSprite = new Texture("Player/heart.png");
    }

    public void Draw(SpriteBatch batch, int nbLife)
    {
        for (int x = 0; x < nbLife; x++)
            batch.draw(lifeSprite, x * 75 + Map.startPosX, StandardScreen.heightScreen - 75, 75, 75);
    }
}
