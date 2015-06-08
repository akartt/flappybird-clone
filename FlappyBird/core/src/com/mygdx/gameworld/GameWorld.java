package com.mygdx.gameworld;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.fbhelpers.AssetLoader;
import com.mygdx.gameobjects.Bird;
import com.mygdx.gameobjects.ScrollHandler;

/**
 * This class implements the game world that contains all game objects as well as the game scroller.
 *
 */
public class GameWorld {
	private Bird bird;
	private ScrollHandler scrollHandler;
	private Rectangle ground;
	private float runTime = 0;
	private int score = 0;
	private int yMidPoint;

	// enum for different game states
	public enum GameState {
		READY, RUNNING, GAMEOVER, HIGHSCORE, MENU;
	}

	private final int BIRD_HEIGHT = 12;
	private final int BIRD_WIDTH = 17;
	private final int BIRD_X_POSITION = 33;
	
	private GameState current;

	/**
	 * Constructor for the game world, which contains the game state, game objects such as the bird
	 * and the ground, and
	 * @param yMidPoint
	 * 			vertical mid-point of the screen
	 */
	public GameWorld(int yMidPoint) {
		current = GameState.MENU; // start on the menu screen
		bird = new Bird(BIRD_X_POSITION, yMidPoint - 5, BIRD_WIDTH, BIRD_HEIGHT);
		scrollHandler = new ScrollHandler(yMidPoint + 66, this);
		ground = new Rectangle(0, yMidPoint + 66, 137, 11);
		this.yMidPoint = yMidPoint;
	}

	/**
	 * Updates the game, depending on which state the game is in
	 *
	 * @param delta
	 * 			the time since the last update() call
	 */
	public void update (float delta) {
		runTime += delta;
		switch (current) {
			case READY:
				updateReady(delta);
				break;
			case MENU:
				updateReady(delta);
				break;
			case RUNNING:
				updateRunning(delta);
				break;
			default:
				break;
		}
			
	}

	/**
	 * Updates the screen when the game is in the READY state
	 * until the user starts running the game
	 * @param delta
	 * 			the time since the last update() call
	 */
	private void updateReady(float delta) {
		bird.updateReady(runTime);
		scrollHandler.updateReady(delta);
	}

	/**
	 * Updates the screen when the game is in RUNNIG state
	 * @param delta
	 */
	public void updateRunning(float delta) {
		if (delta > 0.15f) {
			delta = 0.15f; // delta cap for when the game takes too long to update, collision detection is not affected
		}

		// call update on the bird and the scroll handler
		bird.update(delta);
		scrollHandler.update(delta);

		// when collision is detected
		if (scrollHandler.collides(bird) && bird.isAlive()) {
			scrollHandler.stop();
			bird.die();
			AssetLoader.dead.play();
			AssetLoader.fall.play(0.5f);
		}

		// when the bird hits the ground
		if (Intersector.overlaps(bird.getCircle(), ground)) {
			if (bird.isAlive()) {
				AssetLoader.dead.play();
				bird.die();
			}
			scrollHandler.stop();
			bird.decelerate();
			current = GameState.GAMEOVER;
			// set high-score
			if (score > AssetLoader.getHighScore()) {
				AssetLoader.setHighScore(score);
				current = GameState.HIGHSCORE;
			}
		}
	}

	/**
	 * Restarts the game.
	 * Score is reset, bird is returned to its original position, scroll handler restarts and
	 * game is put in ready state
	 */
	public void restart() {
		score = 0;
		bird.restart(yMidPoint - 5);
		scrollHandler.restart();
		ready();
	}

	/**
	 * Adds to the total score
	 * @param increment
	 * 			score to be added
	 */
	public void addScore(int increment) {
		score += increment;
	}

	// Getters, setters, observers
	public Bird getBird() {
		return bird;
	}
	
	public ScrollHandler getScrollHandler() {
		return scrollHandler;
	}
	
	public int getScore() {
		return score;
	}
	
	public void ready() {
		current = GameState.READY;
	}
	
	public void start() {
		current = GameState.RUNNING;
	}

	public boolean isGameOver() {
		return current == GameState.GAMEOVER;
	}
	
	public boolean isHighScore() {
		return current == GameState.HIGHSCORE;
	}
	
	public boolean isMenu() {
		return current == GameState.MENU;
	}
	
	public boolean isReady() {
		return current == GameState.READY;
	}
	
	public boolean isRunning() {
		return current == GameState.RUNNING;
	}

	public int getYMidPoint() {
		return yMidPoint;
	}
}
