package com.mygdx.fbhelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**
 * This class loads assets/data that the game will use, from the texture file as well as audio files.
 * @author Park
 *
 */
public class AssetLoader {
	public static Texture texture; // Texture from which the game graphics will be created
	public static TextureRegion bg, grass, bird, birdDown, birdUp, barUp, barDown, bar, logo, fbLogo, startButton, ready, gameOver, scoreBoard; // Texture regions created from the texture file
	public static Animation animation; // Animation of the bird when it flies
	public static Sound dead, flap, coin, fall; // Sound files
	public static BitmapFont font, shadow; // Fonts for displaying scores and messages
	public static Preferences prefs; // Used for saving high score. It is a map between a string and an int in this case.
	
	public static void load() {
		texture = new Texture(Gdx.files.internal("data/texture.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		bg = new TextureRegion(texture, 0, 0, 225, 400); // Background
		bg.flip(false, true);
		
		startButton = new TextureRegion(texture, 562, 196, 62, 22);
		startButton.flip(false, true);
		ready = new TextureRegion(texture, 462, 94, 133, 30);
		ready.flip(false, true);
		gameOver = new TextureRegion(texture, 618, 93, 147, 27);
		gameOver.flip(false, true);
		fbLogo = new TextureRegion(texture, 550, 145, 132, 25);
		fbLogo.flip(false, true);
		
		grass = new TextureRegion(texture, 455, 1, 265, 87);
		grass.flip(false, true);
		
		birdDown = new TextureRegion(texture, 180, 555, 25, 18);
		birdDown.flip(false, true);
		
		bird = new TextureRegion(texture, 180, 514, 25, 18);
		bird.flip(false, true);
		
		birdUp = new TextureRegion(texture, 180, 595, 25, 18);
		birdUp.flip(false, true);
		
		TextureRegion[] birds = {birdDown, bird, birdUp};
		animation = new Animation(0.06f, birds); // Animation will play using the 3 texture regions in the birds array, every 0.06s
		animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG); // Play the animation in the specified way
		
		barUp = new TextureRegion(texture, 1, 506, 40, 18);
		barDown = new TextureRegion(barUp);
		barDown.flip(false, true);
		
		bar = new TextureRegion(texture, 1, 535, 40, 3);
		bar.flip(false, true);
		
		dead = Gdx.audio.newSound(Gdx.files.internal("data/dead.wav"));
		flap = Gdx.audio.newSound(Gdx.files.internal("data/flap.wav"));
		coin = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
		fall = Gdx.audio.newSound(Gdx.files.internal("data/fall.wav"));
		
		font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
		font.getData().setScale(0.25f, -0.25f);
		shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
		shadow.getData().setScale(0.25f, -0.25f);
		
		prefs = Gdx.app.getPreferences("FlappyBird");
		if (!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		}
	}
	
	public static void dispose() {
		texture.dispose();
		dead.dispose();
		flap.dispose();
		coin.dispose();
		fall.dispose();
		font.dispose();
		shadow.dispose();
	}
	
	public static void setHighScore(int score) {
		prefs.putInteger("highScore", score);
		prefs.flush();
	}
	
	public static int getHighScore() {
		return prefs.getInteger("highScore");
	}
}
