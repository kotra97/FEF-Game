package mathis.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;

import mathis.game.Screens.GameOverScreen;
import mathis.game.Screens.GameScreen;
import mathis.game.Screens.MainMenuScreen;
import mathis.game.Screens.StandardScreen;


public class MainGame extends Game {
	public int maxLvl;
	public SpriteBatch batch;
    private StandardScreen currentScreen;
	private static final float SCALE = 2.0f;

	// Called When you press start button (main menu/game over) or you die
	public void ChangeScreen(int screen)
	{
		switch (screen) {
			case 1:
				currentScreen = new MainMenuScreen(this);
				break;
			case 2:
				currentScreen = new GameScreen(this);
				break;
			case 3:
				currentScreen = new GameOverScreen(this);
				break;
				default:
				break;
		}
		setScreen(currentScreen);
		InputMultiplexer im = new InputMultiplexer();
		GestureDetector gd = new GestureDetector(currentScreen);
		im.addProcessor(gd);
		im.addProcessor(currentScreen);
		Gdx.input.setInputProcessor(im);
	}

	@Override
	public void create ()
	{
		maxLvl = 0;
		OrthographicCamera orthographicCamera = new OrthographicCamera();
		orthographicCamera.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);
		batch = new SpriteBatch();
		ChangeScreen(1);
	}

	@Override
	public void render ()
	{
		super.render();
	}
	
	@Override
	public void dispose () {

	}
	@Override
	public void resize(int width, int height) {
	}

}
