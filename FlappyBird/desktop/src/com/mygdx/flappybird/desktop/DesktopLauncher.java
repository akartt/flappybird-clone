package com.mygdx.flappybird.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.flappybird.FBGame;

/**
 * This class launches a desktop version of the app, used for testing
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		// LightWeight Java Game Library, used to enable cross-platform access to the game
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "FlappyBird";
		config.width = 272;
		config.height = 408;
		new LwjglApplication(new FBGame(), config); // Create the game
	}
}
