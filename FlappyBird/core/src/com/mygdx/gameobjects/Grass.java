package com.mygdx.gameobjects;

/**
 * This class implements the grasss scrollable object
 */
public class Grass extends Scrollable {
    /**
     * Constructor for grass objects
     * @param x
     *      x position
     * @param y
     *      y position
     * @param width
     *      grass width
     * @param height
     *      grass height
     * @param scrollSpeed
     *      grass scroll speed
     */
	public Grass(float x, float y, int width, int height, float scrollSpeed) {
		super(x, y, width, height, scrollSpeed);
	}

    /**
     * Resets the grass upon restart
     * @param x
     *      x position
     * @param scrollSpeed
     *      grass scroll speed
     */
	public void restart(float x, float scrollSpeed) {
		position.x = x;
		velocity.x = scrollSpeed;
	}
	
}
