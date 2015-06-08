package com.mygdx.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.fbhelpers.InputHandler;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;

/**
 * This class implements the game screen, where game objects are displayed
 */
public class GameScreen implements Screen {
	private GameWorld world; // world containing the game objects
	private GameRenderer renderer; // texture model renderer
	private float runTime = 0; // game runtime

	/**
	 * Constructor creates the game screen with the device's screen width, height and sets up
	 * the game world, renderer and input processor
	 */
	public GameScreen() {
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		float gameWidth = 136;
		float gameHeight = screenHeight / (screenWidth / gameWidth);
		int yMidPoint = (int) (gameHeight / 2);
		
		world = new GameWorld(yMidPoint);
		Gdx.input.setInputProcessor(new InputHandler(world, screenWidth / gameWidth, screenHeight /gameHeight));
		renderer = new GameRenderer(world, (int) gameHeight, yMidPoint);
	}

	/**
	 * Renders the texture models to display game objects
	 * @param delta
	 * 			how long the game has been running since the last render call
	 */
	@Override
	public void render(float delta) {
		runTime += delta;
		world.update(delta);
		renderer.render(delta, runTime);
	}

	// Unused methods from the Screen interface
	@Override
	public void show() {
	}
	@Override
	public void resize(int width, int height) {
	}
	@Override
	public void pause() {
	}
	@Override
	public void resume() {
	}
	@Override
	public void hide() {
	}
	@Override
	public void dispose() {
	}	
}
