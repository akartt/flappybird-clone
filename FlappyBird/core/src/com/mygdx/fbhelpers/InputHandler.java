package com.mygdx.fbhelpers;

import com.badlogic.gdx.InputProcessor;
import com.mygdx.gameobjects.Bird;
import com.mygdx.gameworld.GameWorld;
import com.mygdx.ui.Button;

/**
 * This class implements the input handler for the Android device inputs, such as touch-down or touch-up
 */
public class InputHandler implements InputProcessor {
	private Bird bird;
	private GameWorld world;
	private Button play;
	private float scaleX;
	private float scaleY;

	/**
	 * Constructor for the input handler
	 * @param world
	 * 			The game world containing all game objects
	 * @param scaleX
	 *			Scale of device's screen width to the game width
	 * @param scaleY
	 * 			Scale of device's screen height to the game height
	 */
	public InputHandler(GameWorld world, float scaleX, float scaleY) {
		this.world = world;
		bird = world.getBird();
		int yMidPoint = world.getYMidPoint();
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		play = new Button(82 - (AssetLoader.startButton.getRegionWidth() / 2), yMidPoint + 25, 29, 16, AssetLoader.startButton);
	}

	/**
	 * Handles the touch-down input, when the user touches down on the device's screen
	 * @param screenX
	 * 		screen width scaled to the game screen width
	 * @param screenY
	 * 		screen height scaled to the game screen height
	 * @param pointer
	 * 		unused parameter, from the interface
	 * @param button
	 * 		unused parameter, from the interface
	 * @return
	 * 		true if handled successfully, false otherwise
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);
		if (world.isMenu()) {
			play.isTouchDown(screenX, screenY);
		}
		else if (world.isReady()) {
			world.start();
			bird.onClick();
		}
		else if (world.isRunning()) {
			bird.onClick();
		}
		
		if (world.isGameOver() || world.isHighScore()) {
			world.restart();
		}
		return true; // return true after handling the touch
	}

	/**
	 * Handles the touch-up input
	 * @param screenX
	 * 		screen width scaled to the game screen width
	 * @param screenY
	 * 		screen height scaled to the game screen height
	 * @param pointer
	 * 		unused parameter, from the interface
	 * @param button
	 * 		unused parameter, from the interface
	 * @return
	 * 		true if handled successfully, false otherwise
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);
		if (world.isMenu()) {
			if (play.isTouchUp(screenX, screenY)) {
				world.ready();
				return true;
			}
		}
		return false; 
	}

	/**
	 * Sets the width scale for the device
	 * @param screenX
	 * 		screen width
	 * @return
	 * 		scaled width
	 */
	private int scaleX(int screenX) {
		return (int) (screenX / scaleX);
	}

	/**
	 * Sets the height scale for the device
	 * @param screenY
	 * 		screen height
	 * @return
	 * 		scaled height
	 */
	private int scaleY(int screenY) {
		return (int) (screenY / scaleY);
	}

	// Getters, setters, observers
	public Button getStartButton() {
		return play;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
