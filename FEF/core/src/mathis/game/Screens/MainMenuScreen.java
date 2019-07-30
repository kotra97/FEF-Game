package mathis.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import mathis.game.MainGame;

public class MainMenuScreen extends StandardScreen{

    private MainGame game;
    private Texture runTexture;
    private Texture titleTexture;
    private Texture runActiveTexture;
    private boolean isTouch = true;
    private int buttonWidth = 320;
    private int buttonHeight = 200;

    public MainMenuScreen(MainGame   _game)
    {
        super(_game);
        game = _game;
        runTexture = new Texture("Menu/Run.png");
        runActiveTexture = new Texture("Menu/RunActive.png");
        titleTexture = new Texture("Menu/Title.png");
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button)
    {
        Gdx.app.log("Debug", "Down") ;
        if (screenX > widthScreen / 3 && screenX < widthScreen / 3 + buttonWidth &&
                heightScreen - screenY > heightScreen / 3 && heightScreen - screenY < heightScreen / 3 + buttonHeight)
            isTouch = true;
        return false;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button)
    {
        if (isTouch && screenX > widthScreen / 3 && screenX < widthScreen / 3 + buttonWidth &&
                heightScreen - screenY > heightScreen / 3 && heightScreen - screenY < heightScreen / 3 + buttonHeight)
            game.ChangeScreen(2);
        isTouch = false;
        return false;
    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.draw(titleTexture, widthScreen / 4 , heightScreen / 2, 500, 500);
        if (isTouch)
            game.batch.draw(runTexture, widthScreen / 3 , heightScreen / 3);
        else
            game.batch.draw(runActiveTexture, widthScreen / 3 , heightScreen / 3);
        game.batch.end();

    }
    @Override
    public void dispose() {        Gdx.app.log("Debug", "End") ;}

}
