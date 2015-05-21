package com.mygdx.gameobjects;

import java.util.Random;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Pipe extends Scrollable {
	private Random r;
	private Rectangle barEndUp, barEndDown, barUp, barDown;
	
	public static int VERTICAL_GAP = 45;
	public static int BAR_END_HEIGHT = 11;
	public static int BAR_END_WIDTH = 24;
	private float yGround;
	
	private boolean isScored;
	
	public Pipe(float x, float y, int width, int height, float scrollSpeed, float yGround) {
		super(x, y, width, height, scrollSpeed);
		r = new Random();
		this.yGround = yGround;
		barEndUp = new Rectangle();
		barEndDown = new Rectangle();
		barUp = new Rectangle();
		barDown = new Rectangle();
		isScored = false;
	}
	
	@Override
	public void reset(float newX) {
		super.reset(newX);
		height = r.nextInt(90) + 15;
		isScored = false;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		barUp.set(position.x, position.y, width, height);
		barDown.set(position.x, position.y + height + VERTICAL_GAP, width, yGround - (position.y + height + VERTICAL_GAP));
		barEndUp.set(position.x - (BAR_END_WIDTH - width) / 2, position.y + height - BAR_END_HEIGHT, BAR_END_WIDTH, BAR_END_HEIGHT);
		barEndDown.set(position.x - (BAR_END_WIDTH - width) / 2, barDown.y, BAR_END_WIDTH, BAR_END_HEIGHT);
	}

	public Rectangle getBarEndUp() {
		return barEndUp;
	}

	public Rectangle getBarEndDown() {
		return barEndDown;
	}

	public Rectangle getBarUp() {
		return barUp;
	}

	public Rectangle getBarDown() {
		return barDown;
	}
	
	public boolean collides(Bird bird) {
		if (position.x < bird.getX() + bird.getWidth()) {
			return (Intersector.overlaps(bird.getCircle(), barUp) || Intersector.overlaps(bird.getCircle(), barDown) ||
					Intersector.overlaps(bird.getCircle(), barEndUp) || Intersector.overlaps(bird.getCircle(), barEndDown));
		}
		return false;
	}

	public boolean isScored() {
		return isScored;
	}
	
	public void setScored(boolean scored) {
		isScored = true;
	}

	public void restart(float x, float scrollSpeed) {
		velocity.x = scrollSpeed;
		reset(x);
		
	}
}
