package mathis.game.Ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mathis.game.DungeonConstruction.Map;
import mathis.game.Screens.StandardScreen;

public class GoldUi {

    public class LifeBar
    {
        private Texture goldSprite;

        public LifeBar()
        {
            goldSprite = new Texture("Player/heart.png");
        }

        public void Draw(SpriteBatch batch, int nbGold)
        {
            for (int x = 0; x < nbGold; x++)
                batch.draw(goldSprite, x * 75 + Map.startPosX, StandardScreen.heightScreen - 75, 75, 75);
        }
    }

}
