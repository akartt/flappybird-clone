package com.mygdx.flappybird;

import com.badlogic.gdx.Game;
import com.mygdx.fbhelpers.AssetLoader;
import com.mygdx.screens.GameScreen;

/**
 * This class implements the game FlappyBird
 */
public class FBGame extends Game {
	/**
	 * Creates the game
	 */
	@Override
	public void create() {
		AssetLoader.load(); // Load all assets (game data)
		setScreen(new GameScreen()); // Create the game screen to display the game
	}

	/**
	 * Dispose the game
	 */
	@Override
	public void dispose() {
		super.dispose(); // Kill game process
		AssetLoader.dispose(); // Dispose all assets
	}
	
}
