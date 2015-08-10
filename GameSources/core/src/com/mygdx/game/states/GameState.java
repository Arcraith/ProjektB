package com.mygdx.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Application;
import com.mygdx.game.managers.GameStateManager;

public abstract class GameState {
	
	// Referenzen
	protected GameStateManager gsm;
	protected Application app;
	protected SpriteBatch batch;
	protected OrthographicCamera camera;
	
	protected GameState(GameStateManager gsm){
		this.gsm = gsm;
		this.app = gsm.application();
		batch = app.getBatch();
		camera = app.getCamera();
	}

	public abstract void update(float delta);

	public abstract void render();

	public abstract void dispose();

	public void resize(int w, int h) {
		camera.setToOrtho(false, w, h);
	}

}
