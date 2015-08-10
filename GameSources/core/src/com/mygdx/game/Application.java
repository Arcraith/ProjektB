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
	
	// Renderer, der die Objekte in der World anzeigt (für Debug-Zwecke)
	private Box2DDebugRenderer b2dr;
	private World world;
	private Body player, platform;

	@Override
	public void create() {
		// Objekte werden vor Aufruf erzeugt, um sie z.B. in render() zu
		// zeichnen
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w / SCALE, h / SCALE);
		
		gsm = new GameStateManager(this);

		// Erstellen einer World mit realer Gravitation
		world = new World(new Vector2(0, -9.8f), false);
		b2dr = new Box2DDebugRenderer();

		player = createBox(8, 10, 32, 32, false);
		platform = createBox(0, 0, 64, 32, true);

	}

	@Override
	public void render() {
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		
//		update(Gdx.graphics.getDeltaTime());
//
//		// Funktion, die das Rendern übernimmt
//		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//		b2dr.render(world, camera.combined.scl(PPM));
	}

	@Override
	public void dispose() {
		gsm.dispose();
		
		// Loslassen von Ressourcen
		world.dispose();
		b2dr.dispose();
	}

	@Override
	public void resize(int width, int height) {
		gsm.resize((int) (width/SCALE), (int) (height/SCALE));
	}

	public void update(float delta) {
		// die typische Abfolge für Spiele ist, Gamelogic --> Render (repeat).
		// Gamelogic beinhaltet Dinge, die sich verändert haben. Hierfür wird
		// update() benötigt

		world.step(1 / 60f, 6, 2);

		inputUpdate(delta);

		cameraUpdate(delta);
	}

	public void inputUpdate(float delta) {
		// Jede Wiederholung resettet
		int horizontalForce = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			horizontalForce -= 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			horizontalForce += 1;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			// just --> kein Stacking von Sprüngen
			player.applyForceToCenter(0,300,false);
		}

		player.setLinearVelocity(horizontalForce * 5,
				player.getLinearVelocity().y);
	}

	public Body createBox(int x, int y, int width, int height, boolean isStatic) {
		Body pBody;
		// Erstellen der Eigenschaften eines Bodys
		BodyDef definition = new BodyDef();

		if (isStatic)
			definition.type = BodyDef.BodyType.StaticBody;
		else
			definition.type = BodyDef.BodyType.DynamicBody;

		definition.position.set(x / PPM, y / PPM);
		definition.fixedRotation = true;
		// Initialisieren des Bodys in die World
		pBody = world.createBody(definition);

		PolygonShape shape = new PolygonShape();
		// setAsBox(32,32) erzeugt ein Shape, welches insgesamt 64 * 64 groß
		// ist, Box2D startet immer in der Mitte von Objekten
		shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

		pBody.createFixture(shape, 1.0f);
		shape.dispose();
		return pBody;
	}

	public void cameraUpdate(float delta) {
		Vector3 position = camera.position;
		position.x = player.getPosition().x * PPM;
		position.y = player.getPosition().y * PPM;
		camera.position.set(position);
		camera.update();
	}
	
	public OrthographicCamera getCamera(){
		return camera;
	}
	
	public SpriteBatch getBatch(){
		return batch;
	}
}
