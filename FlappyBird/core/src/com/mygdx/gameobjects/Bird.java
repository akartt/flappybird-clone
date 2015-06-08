package com.mygdx.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.fbhelpers.AssetLoader;

/**
 * This class implements the bird game object
 */
public class Bird {
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 acceleration;
	
	private float rotation; // Handling bird rotation
	private int width;
	private int height;
	
	private float originalY;
	
	private Circle circle;
	
	private boolean isAlive;

	/**
	 * Bird constructor
	 * @param x
	 * 		x position
	 * @param y
	 * 		y position
	 * @param width
	 * 		bird width
	 * @param height
	 * 		bird height
	 */
	public Bird(float x, float y, int width, int height) {
		this.width = width;
		this.height = height;
		originalY = y;
		position = new Vector2(x, y);
		velocity = new Vector2(0, 0);
		acceleration = new Vector2(0, 460);
		circle = new Circle(); // Used for collision detection
		isAlive = true;
	}

	/**
	 * Updates the bird in real time, while the game is in READY state
	 * @param runTime
	 * 		runtime of the game
	 */
	public void updateReady(float runTime) {
        position.y = 2 * (float) Math.sin(7 * runTime) + originalY;
    }

	/**
	 * Updates the bird while the game is running
	 * @param delta
	 * 		time since the last update() call
	 */
	public void update(float delta) {
		velocity.add(acceleration.cpy().scl(delta));
		if (velocity.y > 200) { // Set max velocity
			velocity.y = 200;
		}
		if (velocity.y < 0) { // When the bird is flying up
			rotation -= 600 * delta; // Rotate bird
			if (rotation < -20) { // Do not rotate more than 20 degrees
				rotation = -20;
			}
		}
		if (isFalling() || !isAlive) { // When the bird is falling
			rotation += 480 * delta;
			if (rotation > 90) {
				rotation = 90;
			}
		}
		
		if (position.y < -13) { // Ceiling check (cap how far up the bird can go up)
			position.y = -13;
			velocity.y = 0;
		}
		position.add(velocity.cpy().scl(delta)); // Move the bird forward constantly
		circle.set(position.x + 9, position.y + 6, 6.5f); // Set the circle to the new position of the bird
	}

	/**
	 * Resets the bird upon restart
	 * @param originalPos
	 * 		the original position of the bird
	 */
	public void restart(int originalPos) {
		rotation = 0;
		position.y = originalPos;
		position.x = 25;
		velocity.y = 0;
		velocity.x = 0;
		acceleration.y = 460;
		acceleration.x = 0;
		isAlive = true;
	}

	/**
	 * When the user taps the device, move the bird up
	 */
	public void onClick() {
		if (isAlive) {
			AssetLoader.flap.play(0.75f);
			velocity.y = -140;
		}
	}

	/**
	 * Kills the bird (make it stop moving)
	 */
	public void die() {
		isAlive = false;
		velocity.y = 0;
	}

	// Getters, setters, observers
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}

	public float getRotation() {
		return rotation;
	}

	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}
	
	// Determines whether the bird should start rotating
	public boolean isFalling() {
		return velocity.y > 110;
	}
	
	// Determines whether the bird should stop flapping
	public boolean noFlap() {
		return velocity.y > 70 || !isAlive();
	}
	
	public Circle getCircle() {
		return circle;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public void decelerate() {
		acceleration.y = 0;
	}
}
