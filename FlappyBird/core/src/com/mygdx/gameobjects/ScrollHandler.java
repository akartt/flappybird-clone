package com.mygdx.gameobjects;

import com.mygdx.fbhelpers.AssetLoader;
import com.mygdx.gameworld.GameWorld;

public class ScrollHandler {
	private Grass frontGrass, backGrass;
	private Pipe firstPipe, secondPipe, thirdPipe;
	
	public static final int SCROLL_SPEED = -59; // Scroll speed
	public static final int PIPE_GAP = 49; // Size of gap between pipes, empirically determined
	
	private GameWorld world;
	
	/**
	 * Constructor receives a float which tells where Grass and Pipes are created
	 * @param yPos
	 */
	public ScrollHandler(float yPos, GameWorld world) {
		frontGrass = new Grass(0, yPos, 143, 87, SCROLL_SPEED);
		backGrass = new Grass(frontGrass.getTailX(), yPos, 143, 87, SCROLL_SPEED);
		firstPipe = new Pipe(210, 0, 22, 60, SCROLL_SPEED, yPos);
		secondPipe = new Pipe(firstPipe.getTailX() + PIPE_GAP, 0, 22, 70, SCROLL_SPEED, yPos);
		thirdPipe = new Pipe(secondPipe.getTailX() + PIPE_GAP, 0, 22, 60, SCROLL_SPEED, yPos);
		this.world = world;
	}
	
    public void updateReady(float delta) {
        frontGrass.update(delta);
        backGrass.update(delta);

        // Same with grass
        if (frontGrass.isScrolledLeft()) {
            frontGrass.reset(backGrass.getTailX());
        } 
        else if (backGrass.isScrolledLeft()) {
            backGrass.reset(frontGrass.getTailX());
        }
    }
    
	public void update(float delta) {
		frontGrass.update(delta);
		backGrass.update(delta);
		firstPipe.update(delta);
		secondPipe.update(delta);
		thirdPipe.update(delta);
		
		if (firstPipe.isScrolledLeft()) {
			firstPipe.reset(thirdPipe.getTailX() + PIPE_GAP);
		}
		else if (secondPipe.isScrolledLeft()) {
			secondPipe.reset(firstPipe.getTailX() + PIPE_GAP);
		}
		else if (thirdPipe.isScrolledLeft()) {
			thirdPipe.reset(secondPipe.getTailX() + PIPE_GAP);
		}
		
		if (frontGrass.isScrolledLeft()) {
			frontGrass.reset(backGrass.getTailX());
		}
		else if (backGrass.isScrolledLeft()) {
			backGrass.reset(frontGrass.getTailX());
		}
	}
	
	public Grass getFrontGrass() {
		return frontGrass;
	}
	
	public Grass getBackGrass() {
		return backGrass;
	}
	
	public Pipe getFirstPipe() {
		return firstPipe;
	}
	
	public Pipe getSecondPipe() {
		return secondPipe;
	}
	
	public Pipe getThirdPipe() {
		return thirdPipe;
	}
	
	public boolean collides(Bird bird) {
		if (!firstPipe.isScored() && firstPipe.getX() + firstPipe.getWidth() / 2 < (bird.getX() + bird.getWidth())) {
			world.addScore(1);
			firstPipe.setScored(true);
			AssetLoader.coin.play(0.10f);
		}
		else if (!secondPipe.isScored() && secondPipe.getX() + secondPipe.getWidth() / 2 < (bird.getX() + bird.getWidth())) {
			world.addScore(1);
			secondPipe.setScored(true);
			AssetLoader.coin.play(0.10f);
		}
		else if (!thirdPipe.isScored() && thirdPipe.getX() + thirdPipe.getWidth() / 2 < (bird.getX() + bird.getWidth())) {
			world.addScore(1);
			thirdPipe.setScored(true);
			AssetLoader.coin.play(0.10f);
		}
		return (firstPipe.collides(bird) || secondPipe.collides(bird) || thirdPipe.collides(bird));
	}
	
	public void stop() {
		frontGrass.stop();
		backGrass.stop();
		firstPipe.stop();
		secondPipe.stop();
		thirdPipe.stop();
	}
	
	public void restart() {
		frontGrass.restart(0, SCROLL_SPEED);
		backGrass.restart(frontGrass.getTailX(), SCROLL_SPEED);
		firstPipe.restart(210, SCROLL_SPEED);
		secondPipe.restart(firstPipe.getTailX() + PIPE_GAP, SCROLL_SPEED);
		thirdPipe.restart(secondPipe.getTailX() + PIPE_GAP, SCROLL_SPEED);
	}
}
