package com.mygdx.gameobjects;

import com.badlogic.gdx.math.Vector2;

/**
 * This class implements the scrollable objects, pipes and grass.
 */
public class Scrollable {
	protected Vector2 position;
	protected Vector2 velocity;
	protected int width;
	protected int height;
	protected boolean isScrolledLeft;

	/**
	 * Constructor for the scrollable objects
	 * @param x
	 * 		x position of the objects
	 * @param y
	 * 		y position of the objects
	 * @param width
	 * 		width of the objects
	 * @param height
	 *		height of the objects
	 * @param scrollSpeed
	 * 		scroll speed of the objects
	 */
	public Scrollable(float x, float y, int width, int height, float scrollSpeed) {
		position = new Vector2(x, y);
		velocity = new Vector2(scrollSpeed, 0);
		this.width = width;
		this.height = height;
		isScrolledLeft = false;
	}

	/**
	 * Updates the scrollable objects on the screen with new coordinates
	 * @param delta
	 * 			time since the last update() was called
	 */
	public void update(float delta) {
		position.add(velocity.cpy().scl(delta));
		if (position.x + width < 0) {
			isScrolledLeft = true;
		}
	}

	/**
	 * Resets the scrollable objects
	 * @param newX
	 * 			the new x-position to be reset to
	 */
	public void reset(float newX) {
		position.x = newX;
		isScrolledLeft = false;
	}

	/**
	 * Stops scrolling the objects
	 */
	public void stop() {
		velocity.x = 0;
	}

	// Getters, observers
	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getVelocity() {
		return velocity;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isScrolledLeft() {
		return isScrolledLeft;
	}
	
	public float getTailX() {
		return position.x + width;
	}
}
