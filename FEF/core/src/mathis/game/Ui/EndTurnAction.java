package mathis.game.Ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mathis.game.DungeonConstruction.Map;

public class EndTurnAction {
    private Texture unPressed;
    private Texture pressed;
    public boolean isPressed;
    private int posX;

    public EndTurnAction()
    {
        isPressed = false;
        unPressed = new Texture("UI/Stop.png");
        pressed = new Texture("UI/StopPressed.png");
        posX = Map.startPosX + (Map.mapWidth * Map.cellHeightWidth) - 250;
    }

    public void Draw(SpriteBatch batch)
    {
        if (isPressed)
            batch.draw(pressed, posX, 49, 250, 250);
        else
            batch.draw(unPressed, posX, 49, 250, 250);
    }
}
