package com.mygdx.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.fbhelpers.AssetLoader;

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
	
	public Bird(float x, float y, int width, int height) {
		this.width = width;
		this.height = height;
		originalY = y;
		position = new Vector2(x, y);
		velocity = new Vector2(0, 0);
		acceleration = new Vector2(0, 460);
		circle = new Circle();
		isAlive = true;
	}
	
	public void updateReady(float runTime) {
        position.y = 2 * (float) Math.sin(7 * runTime) + originalY;
    }
	
	public void update(float delta) {
		velocity.add(acceleration.cpy().scl(delta));
		if (velocity.y > 200) {
			velocity.y = 200;
		}
		if (velocity.y < 0) {
			rotation -= 600 * delta;
			if (rotation < -20) {
				rotation = -20;
			}
		}
		if (isFalling() || !isAlive) {
			rotation += 480 * delta;
			if (rotation > 90) {
				rotation = 90;
			}
		}
		
		if (position.y < -13) { // Ceiling check
			position.y = -13;
			velocity.y = 0;
		}
		position.add(velocity.cpy().scl(delta));
		circle.set(position.x + 9, position.y + 6, 6.5f);
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public void onClick() {
		if (isAlive) {
			AssetLoader.flap.play(0.75f);
			velocity.y = -140;
		}
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
	
	public void die() {
		isAlive = false;
		velocity.y = 0;
	}
	
	public void decelerate() {
		acceleration.y = 0;
	}

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
}
