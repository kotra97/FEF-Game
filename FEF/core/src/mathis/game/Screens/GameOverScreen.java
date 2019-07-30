package mathis.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import mathis.game.MainGame;

public class GameOverScreen extends StandardScreen{

    private MainGame game;
    private Texture runTexture;
    private Texture runActiveTexture;
    private boolean isTouch = true;
    private int buttonWidth = 320;
    private int buttonHeight = 200;
    private BitmapFont font = new BitmapFont();
    private BitmapFont fontScore = new BitmapFont();

    public GameOverScreen(MainGame   _game)
    {
        super(_game);
        game = _game;
        runTexture = new Texture("Menu/Run.png");
        runActiveTexture = new Texture("Menu/RunActive.png");
        font.getData().setScale(10);
        fontScore.getData().setScale(10);
        font.setColor(1, 0, 0, 1);
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button)
    {
        Gdx.app.log("Debug", "Down") ;
        if (screenX > widthScreen / 3 && screenX < widthScreen / 3 + buttonWidth &&
                heightScreen - screenY > heightScreen / 3 && heightScreen - screenY < heightScreen / 4 + buttonHeight)
            isTouch = true;
        return false;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button)
    {
        if (isTouch && screenX > widthScreen / 3 && screenX < widthScreen / 3 + buttonWidth &&
                heightScreen - screenY > heightScreen / 4 && heightScreen - screenY < heightScreen / 4 + buttonHeight)
            game.ChangeScreen(2);
        isTouch = false;
        return false;
    }

    @Override
    public void render(float delta) {


        game.batch.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        font.draw(game.batch, "Game", widthScreen / 3 , heightScreen * 3 / 5);
        font.draw(game.batch, "Over", widthScreen / 3 - 20 , heightScreen / 2);
        fontScore.draw(game.batch, "Max level", widthScreen * 25/ 100 , heightScreen* 90 / 100);
        fontScore.draw(game.batch, ""+game.maxLvl, widthScreen * 45/ 100 , heightScreen * 80 / 100);
        if (isTouch)
            game.batch.draw(runTexture, widthScreen / 3 , heightScreen / 4);
        else
            game.batch.draw(runActiveTexture, widthScreen / 3 , heightScreen / 4);
        game.batch.end();

    }
    @Override
    public void dispose() {        Gdx.app.log("Debug", "End") ;}

}
