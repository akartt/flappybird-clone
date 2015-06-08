package com.mygdx.gameobjects;

import java.util.Random;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * This class implements the pipes in the game.
 * Pipes are scrollable game objects that kill the bird when it collides with them.
 */
public class Pipe extends Scrollable {
	private Random r; // Used to generate random numbers for pipe heights
	private Rectangle barEndUp, barEndDown, barUp, barDown; // Rectangles are used for collision detection with the bird
	
	public static int VERTICAL_GAP = 45; // Gap between upper and lower pipes
	public static int BAR_END_HEIGHT = 11; // height of the bar end (tip)
	public static int BAR_END_WIDTH = 24; // width of the bar end
	private float yGround; // y position of the ground
	
	private boolean isScored; // boolean value to keep track of whether the bird has scored a point by passing a scrollable object

    /**
     * Constructor for pipe objects
     * @param x
     *      x position
     * @param y
     *      y position
     * @param width
     *      pipe width
     * @param height
     *      pipe height
     * @param scrollSpeed
     *      pipe scroll speed
     * @param yGround
     *      y position of the ground
     */
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

	/**
	 * Resets the pipes to original position
	 */
	@Override
	public void reset(float newX) {
		super.reset(newX);
		height = r.nextInt(90) + 15;
		isScored = false;
	}

	/**
	 * Updates the pipe positions as the game continues, scrolls the pipes
	 * @param delta
	 *          time since the last update() was called
	 */
	@Override
	public void update(float delta) {
		super.update(delta);
		barUp.set(position.x, position.y, width, height);
		barDown.set(position.x, position.y + height + VERTICAL_GAP, width, yGround - (position.y + height + VERTICAL_GAP));
		barEndUp.set(position.x - (BAR_END_WIDTH - width) / 2, position.y + height - BAR_END_HEIGHT, BAR_END_WIDTH, BAR_END_HEIGHT);
		barEndDown.set(position.x - (BAR_END_WIDTH - width) / 2, barDown.y, BAR_END_WIDTH, BAR_END_HEIGHT);
	}

	/**
	 * Detects collision with the bird
	 * @param bird
	 * 		The bird game object
	 * @return
	 * 		True when collision is detected with the bird, false otherwise
	 */
	public boolean collides(Bird bird) {
		if (position.x < bird.getX() + bird.getWidth()) {
			return (Intersector.overlaps(bird.getCircle(), barUp) || Intersector.overlaps(bird.getCircle(), barDown) ||
					Intersector.overlaps(bird.getCircle(), barEndUp) || Intersector.overlaps(bird.getCircle(), barEndDown));
		}
		return false;
	}

	/**
	 * Resets the pipes once the game is restarted
	 */
	public void restart(float x, float scrollSpeed) {
		velocity.x = scrollSpeed;
		reset(x);
	}

    // Getters, setters, observers
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

    public boolean isScored() {
        return isScored;
    }

    public void setScored(boolean scored) {
        isScored = true;
    }
}
