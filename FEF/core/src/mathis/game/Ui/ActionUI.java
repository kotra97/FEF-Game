package mathis.game.Ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mathis.game.Screens.StandardScreen;

public class ActionUI
{
    private Texture actionSprite;
    private BitmapFont font = new BitmapFont();

    public ActionUI()
    {
        font.getData().setScale(5);
        actionSprite = new Texture("UI/ActionIcon.png");
    }

    public void Draw(SpriteBatch batch, int action)
    {
        if (action != 0)
            font.setColor(1, 1, 1, 1);
        else
            font.setColor(1, 0.2f, 0.2f, 1);
        batch.draw(actionSprite, StandardScreen.widthScreen - 260, StandardScreen.heightScreen - 85, 100, 75);
        font.draw(batch, "" + action, StandardScreen.widthScreen - 140, StandardScreen.heightScreen - 20);
    }
}

