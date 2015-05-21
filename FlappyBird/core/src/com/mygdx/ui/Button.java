package com.mygdx.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.fbhelpers.AssetLoader;

public class Button {
	private float x, y, width, height;
	private TextureRegion startButton;
	private Rectangle bounds;
	private boolean isPressed = false;
	
	public Button(float x, float y, float width, float height, TextureRegion startButton) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.startButton = startButton;
		bounds = new Rectangle(x, y, width, height);
	}
	
	public boolean isPressed(int screenX, int screenY) {
		return bounds.contains(screenX, screenY);
	}
	
	public void draw(SpriteBatch batcher) {
		if (isPressed) {
			batcher.draw(startButton, x, y, width, height);
		}
		else {
			batcher.draw(startButton, x, y, width, height);
		}
	}
	

	
	public boolean isTouchDown(int screenX, int screenY) {
		if (bounds.contains(screenX, screenY)) {
			isPressed = true;
			return true;
		}
		return false;
	}
	
	public boolean isTouchUp(int screenX, int screenY) {
		if (bounds.contains(screenX, screenY) && isPressed) {
			isPressed = false;
			AssetLoader.flap.play();
			return true;
		}
		isPressed = false;
		return false;
	}
}
