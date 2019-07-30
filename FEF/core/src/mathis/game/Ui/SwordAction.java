package mathis.game.Ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mathis.game.DungeonConstruction.Map;
import mathis.game.Screens.StandardScreen;

public class SwordAction
{
    private Texture unPressed;
    private Texture pressed;
    public boolean isPressed;

    public SwordAction()
    {
        isPressed = false;
        unPressed = new Texture("UI/Sword.png");
        pressed = new Texture("UI/SwordPressed.png");
    }

    public void Draw(SpriteBatch batch)
    {
        if (isPressed)
            batch.draw(pressed, Map.startPosX, 49, 250, 250);
        else
            batch.draw(unPressed, Map.startPosX, 49, 250, 250);
    }
}
