package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.managers.GameStateManager;

public class Application extends ApplicationAdapter {

	// Jedes Mal, wenn Informationen aus der World bezogen werden, und nicht,
	// wenn man der World Informationen gibt, muss diese Information mit PPM
	// multipliziert werden. Wenn man einem Box2D-Objekt Werte gibt, müssen
	// diese mit PPM dividiert werden (runterskaliert)
	private static final float PPM = 32;
	public static final String TITLE = "Testlauf";
	public static final int V_WIDTH = 720;
	public static final int V_HEIGHT = 480;
	public static final float SCALE = 2.0f;

	private boolean DEBUG = false;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	
	private GameStateManager gsm;
	
	@Override
	public void create() {
		// Objekte werden vor Aufruf erzeugt, um sie z.B. in render() zu
		// zeichnen
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w / SCALE, h / SCALE);
		
		gsm = new GameStateManager(this);
	}

	@Override
	public void render() {
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
	}

	@Override
	public void dispose() {
		gsm.dispose();
	}

	@Override
	public void resize(int width, int height) {
		gsm.resize((int) (width/SCALE), (int) (height/SCALE));
	}

	public OrthographicCamera getCamera(){
		return camera;
	}
	
	public SpriteBatch getBatch(){
		return batch;
	}
}
