package bouncingballs;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class Simulator implements ApplicationListener, InputProcessor {
	
	private Model model;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;

	@Override
	public void create() {
		model = new Model();
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.update();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		// Clear screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// Update the model
		model.update(Gdx.graphics.getDeltaTime());
		
		// Draw borders
		shapeRenderer.begin(ShapeType.FilledRectangle);
		shapeRenderer.setColor(Color.BLACK);
		// Left border
		shapeRenderer.filledRect(-Constants.WIDTH / 2 - 20, -Constants.HEIGHT / 2 - 20, 20, Constants.HEIGHT + 40);
		// Top border
		shapeRenderer.filledRect(-Constants.WIDTH / 2 - 20, Constants.HEIGHT / 2, Constants.WIDTH + 40, 20);
		// Right border
		shapeRenderer.filledRect(Constants.WIDTH / 2, -Constants.HEIGHT / 2 - 20, 20, Constants.HEIGHT + 40);
		// Bottom border
		shapeRenderer.filledRect(-Constants.WIDTH / 2 - 20, -Constants.HEIGHT / 2 - 20, Constants.WIDTH + 40, 20);
		shapeRenderer.end();
		
		// Draw balls
		shapeRenderer.begin(ShapeType.FilledCircle);
		for (Ball ball : model.getBalls()) {
			shapeRenderer.setColor(ball.getColor());
			Vector2 position = ball.getPosition();
			shapeRenderer.filledCircle(position.x, position.y, ball.getRadius());
		}
		shapeRenderer.end();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = (width > Constants.WIDTH) ? width : Constants.WIDTH;
		camera.viewportHeight = (height > Constants.HEIGHT) ? height : Constants.HEIGHT;
		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (Input.Keys.ESCAPE == keycode) {
			Gdx.app.exit();
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
}
