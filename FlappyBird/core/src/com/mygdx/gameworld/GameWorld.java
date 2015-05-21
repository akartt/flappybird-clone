package com.mygdx.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.fbhelpers.AssetLoader;
import com.mygdx.gameobjects.Bird;
import com.mygdx.gameobjects.ScrollHandler;

public class GameWorld {
	private Bird bird;
	private ScrollHandler scrollHandler;
	private Rectangle ground;
	private float runTime = 0;
	private int score = 0;
	private int yMidPoint;
	
	public enum GameState {
		READY, RUNNING, GAMEOVER, HIGHSCORE, MENU;
	}
	
	private GameState current;
	
	public GameWorld(int yMidPoint) {
		current = GameState.MENU;
		bird = new Bird(33, yMidPoint - 5, 17, 12);
		scrollHandler = new ScrollHandler(yMidPoint + 66, this);
		ground = new Rectangle(0, yMidPoint + 66, 137, 11);
		this.yMidPoint = yMidPoint;
	}
	
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
	
	private void updateReady(float delta) {
		bird.updateReady(runTime);
		scrollHandler.updateReady(delta);
	}
	
	public void updateRunning(float delta) {
		if (delta > 0.15f) {
			delta = 0.15f; // delta cap for when the game takes too long to update, collision detection is not affected
		}
		
		bird.update(delta);
		scrollHandler.update(delta);
		
		if (scrollHandler.collides(bird) && bird.isAlive()) {
			scrollHandler.stop();
			bird.die();
			AssetLoader.dead.play();
			AssetLoader.fall.play(0.5f);
		}
		if (Intersector.overlaps(bird.getCircle(), ground)) {
			if (bird.isAlive()) {
				AssetLoader.dead.play();
				bird.die();
			}
			scrollHandler.stop();
			bird.decelerate();
			current = GameState.GAMEOVER;
			if (score > AssetLoader.getHighScore()) {
				AssetLoader.setHighScore(score);
				current = GameState.HIGHSCORE;
			}
		}
	}
	
	public Bird getBird() {
		return bird;
	}
	
	public ScrollHandler getScrollHandler() {
		return scrollHandler;
	}
	
	public int getScore() {
		return score;
	}
	
	public void addScore(int increment) {
		score += increment;
	}
	
	public void ready() {
		current = GameState.READY;
	}
	
	public void start() {
		current = GameState.RUNNING;
	}
	
	public void restart() {
		score = 0;
		bird.restart(yMidPoint - 5);
		scrollHandler.restart();
		ready();
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
