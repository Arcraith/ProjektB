package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.managers.GameStateManager;

public class PlayState extends GameState {

	// If you're giving Box2D Units, divide by PPM. If you're getting Box2D Unity, multiply by PPM.
	private static final float PPM = 32;

	// Renderer, der die Objekte in der World anzeigt (f�r Debug-Zwecke)
	private Box2DDebugRenderer b2dr;
	private World world;
	private Body player, platform;
	
	// Variablen f�r den Spielablauf
	// TODO: Character muss in die Richtung im oder gegen den Uhrzeigersinn schauen --> facingRight: im Uhrzeigersinn
	private boolean facingRight = true;

	public PlayState(GameStateManager gsm) {
		super(gsm);
		// Erstellen einer World mit realer Gravitation
		world = new World(new Vector2(0, -9.8f), false);
		b2dr = new Box2DDebugRenderer();

		player = createBox(8, 10, 32, 32, false);
		platform = createBox(0, 0, 640, 320, true);
	}

	@Override
	public void update(float delta) {
		// die typische Abfolge f�r Spiele ist, Gamelogic --> Render (repeat).
		// Gamelogic beinhaltet Dinge, die sich ver�ndert haben. Hierf�r wird
		// update() ben�tigt

		world.step(1 / 60f, 6, 2);

		inputUpdate(delta);

		cameraUpdate(delta);

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		b2dr.render(world, camera.combined.scl(PPM));
	}

	@Override
	public void dispose() {
		// Loslassen von Ressourcen
		world.dispose();
		b2dr.dispose();

	}

	public void cameraUpdate(float delta) {
		Vector3 position = camera.position;
		// simple Interpolation camera effect
		// a + (b-a) * lerp
		// b = target
		// a = current camera position
		position.x = camera.position.x + (player.getPosition().x * PPM - camera.position.x) * .1f;
		position.y = camera.position.y + (player.getPosition().y * PPM - camera.position.y) * .1f;
		camera.position.set(position);
		camera.update();
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
		// setAsBox(32,32) erzeugt ein Shape, welches insgesamt 64 * 64 gro�
		// ist, Box2D startet immer in der Mitte von Objekten
		shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

		pBody.createFixture(shape, 1.0f);
		shape.dispose();
		return pBody;
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
			// just --> kein Stacking von Spr�ngen
			player.applyForceToCenter(0, 300, false);
		}

		player.setLinearVelocity(horizontalForce * 5,
				player.getLinearVelocity().y);
	}

}
