package mathis.game.Ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mathis.game.Screens.StandardScreen;

public class MovementUI {
    private Texture movementSprite;
    private BitmapFont font = new BitmapFont();

    public MovementUI()
    {
        movementSprite = new Texture("UI/MovementIcon.png");
        font.getData().setScale(5);
    }

    public void Draw(SpriteBatch batch, int movement)
    {
        if (movement != 0)
            font.setColor(1, 1, 1, 1);
        else
            font.setColor(1, 0.2f, 0.2f, 1);
        batch.draw(movementSprite, StandardScreen.widthScreen - 260, StandardScreen.heightScreen - 165, 100, 75);
        font.draw(batch, "" + movement, StandardScreen.widthScreen - 140, StandardScreen.heightScreen - 100);
    }

}
