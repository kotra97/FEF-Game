package mathis.game.Ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mathis.game.Screens.StandardScreen;

public class LevelUI {
    private BitmapFont font = new BitmapFont();

    public LevelUI()
    {
        font.getData().setScale(10);
    }

    public void Draw(SpriteBatch batch, int action)
    {
        font.draw(batch, "" + action, StandardScreen.widthScreen / 2, StandardScreen.heightScreen - 20);
    }
}
