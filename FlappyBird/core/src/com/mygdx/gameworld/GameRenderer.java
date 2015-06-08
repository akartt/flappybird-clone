package com.mygdx.gameworld;

import com.mygdx.fbhelpers.InputHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.fbhelpers.AssetLoader;
import com.mygdx.gameobjects.Bird;
import com.mygdx.gameobjects.Grass;
import com.mygdx.gameobjects.Pipe;
import com.mygdx.gameobjects.ScrollHandler;
import com.mygdx.ui.Button;

/**
 * This class implements the game renderer, which renders the game from the texture models
 */
public class GameRenderer {
	private GameWorld world;
	private OrthographicCamera cam; // Used for orthographic projection
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher; // Draws batched quads using indices
	
	private int gameHeight;
	private int yMidPoint;
	
	private TextureRegion bg, grass, birdMid, barUp, barDown, bar, ready, fbLogo, gameOver;
	private Animation animation;

	// Game Objects
	private Bird bird;
	private ScrollHandler scrollHandler;
	private Grass frontGrass, backGrass;
	private Pipe firstPipe, secondPipe, thirdPipe;
	
	private Button startButton;

	/**
	 * Constructor for game renderer
	 * @param world
	 * 			the game world containing all game objects
	 * @param gameHeight
	 * 			height of the game in pixels
	 * @param yMidPoint
	 * 			vertical mid-point of the game
	 */
	public GameRenderer(GameWorld world, int gameHeight, int yMidPoint) {
		this.world = world;
		this.gameHeight = gameHeight;
		this.yMidPoint = yMidPoint;
		startButton = ((InputHandler) Gdx.input.getInputProcessor()).getStartButton();
		
		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, gameHeight);
		
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined); // Attach batcher to camera
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		initGameObjects();
		initAssets();
	}

	/**
	 * Draws all objects from the texture models and renders them to display on the game
	 * @param delta
	 * 			time since the last render() call
	 * @param runTime
	 * 			runtime of the game
	 */
	public void render(float delta, float runTime) {
		// Fill Screen with black to prevent flickering
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batcher.begin();
		// disable transparency to improve performance
		batcher.disableBlending();
		batcher.draw(AssetLoader.bg, 0, 0, 136, gameHeight);
		
		drawGrass();
		drawPipes();
		drawPipeEnds();
		
		batcher.enableBlending(); // bird needs transparency
		// Draw bird at its coordinates, retrieve animation from AssetLoader, pass runTime to get current frame
		if (world.isRunning() || world.isHighScore()) {
			drawBird(runTime);
			drawScore();
		}
		
		else if (world.isReady()) {
			drawBird(runTime);
			drawScore();
			drawReady();
		}
		
		else if (world.isGameOver()) {
			drawBird(runTime);
			drawGameOver();
		}
		
		else if (world.isMenu()) {
			drawBirdCentered(runTime);
			drawMenuUI();
		}

		
		if (world.isReady()) {
			AssetLoader.shadow.draw(batcher, "Touch Me", 26, 75);
			AssetLoader.font.draw(batcher, "Touch Me", 27, 75);
		}
		else {
			if (world.isGameOver() || world.isHighScore()) {
				if (world.isGameOver()) {
					AssetLoader.shadow.draw(batcher, "High Score:", 23, 106);
					AssetLoader.font.draw(batcher, "High Score:", 22, 105);
					String highScore = AssetLoader.getHighScore() + "";
					AssetLoader.shadow.draw(batcher, highScore, 68 - (3 * highScore.length()), 128);
					AssetLoader.font.draw(batcher, highScore, 68 - (3 * highScore.length() - 1), 127);
					AssetLoader.shadow.draw(batcher, "Tap to Retry", 10, 76);
					AssetLoader.font.draw(batcher, "Tap to Retry", 11, 75);
				}
				else {
                    AssetLoader.shadow.draw(batcher, "High Score!", 23, 56);
                    AssetLoader.font.draw(batcher, "High Score!", 22, 55);
				}
			}
		}
		batcher.end();
	}

	/**
	 * Draws the score using the font and shadow game assets
	 */
	private void drawScore() {
		String score = world.getScore() + "";
		AssetLoader.shadow.draw(batcher, "" + world.getScore(), 68 - (3 * score.length()), 12);
		AssetLoader.font.draw(batcher, "" + world.getScore(), 68 - (3 * score.length() - 1), 11);
	}

	/**
	 * Initializes game objects
	 */
	private void initGameObjects() {
        bird = world.getBird();
        scrollHandler = world.getScrollHandler();
        frontGrass = scrollHandler.getFrontGrass();
        backGrass = scrollHandler.getBackGrass();
        firstPipe = scrollHandler.getFirstPipe();
        secondPipe = scrollHandler.getSecondPipe();
        thirdPipe = scrollHandler.getThirdPipe();
    }

	/**
	 * Loads and initializes assets from the AssetLoader class
	 */
    private void initAssets() {
        bg = AssetLoader.bg;
        grass = AssetLoader.grass;
        animation = AssetLoader.animation;
        birdMid = AssetLoader.bird;
        barUp = AssetLoader.barUp;
        barDown = AssetLoader.barDown;
        bar = AssetLoader.bar;
        ready = AssetLoader.ready;
        fbLogo = AssetLoader.fbLogo;
        gameOver = AssetLoader.gameOver;
    }

	/**
	 * Draws the grass
	 */
    private void drawGrass() {
    	batcher.draw(grass, frontGrass.getX(), frontGrass.getY(), frontGrass.getWidth() + 1, frontGrass.getHeight());
    	batcher.draw(grass, backGrass.getX(), backGrass.getY(), backGrass.getWidth() + 1, backGrass.getHeight());
    }

	/**
	 * Draws the pipe ends
	 */
    private void drawPipeEnds() {
    	batcher.draw(barUp, firstPipe.getX() - 1, firstPipe.getY() +  firstPipe.getHeight() - 14, 24, 14);
    	batcher.draw(barDown, firstPipe.getX() - 1, firstPipe.getY() +  firstPipe.getHeight() + 45, 24, 14);
    	
    	batcher.draw(barUp, secondPipe.getX() - 1, secondPipe.getY() +  secondPipe.getHeight() - 14, 24, 14);
    	batcher.draw(barDown, secondPipe.getX() - 1, secondPipe.getY() +  secondPipe.getHeight() + 45, 24, 14);
    	
    	batcher.draw(barUp, thirdPipe.getX() - 1, thirdPipe.getY() +  thirdPipe.getHeight() - 14, 24, 14);
    	batcher.draw(barDown, thirdPipe.getX() - 1, thirdPipe.getY() +  thirdPipe.getHeight() + 45, 24, 14);
    }

	/**
	 * Draws the pipes
	 */
    private void drawPipes() {
    	batcher.draw(bar, firstPipe.getX(), firstPipe.getY(), firstPipe.getWidth(), firstPipe.getHeight());
    	batcher.draw(bar, firstPipe.getX(), firstPipe.getY() + firstPipe.getHeight() + 45, firstPipe.getWidth(), yMidPoint + 66 - (firstPipe.getHeight() + 45));
    	
    	batcher.draw(bar, secondPipe.getX(), secondPipe.getY(), secondPipe.getWidth(), secondPipe.getHeight());
    	batcher.draw(bar, secondPipe.getX(), secondPipe.getY() + secondPipe.getHeight() + 45, secondPipe.getWidth(), yMidPoint + 66 - (secondPipe.getHeight() + 45));
    	
    	batcher.draw(bar, thirdPipe.getX(), thirdPipe.getY(), thirdPipe.getWidth(), thirdPipe.getHeight());
    	batcher.draw(bar, thirdPipe.getX(), thirdPipe.getY() + thirdPipe.getHeight() + 45, thirdPipe.getWidth(), yMidPoint + 66 - (thirdPipe.getHeight() + 45));
    }

	/**
	 * Draws the bird in the center of the screen
	 * @param runTime
	 * 			runtime of the game
	 */
    private void drawBirdCentered(float runTime) {
        batcher.draw(animation.getKeyFrame(runTime), 
        			 59, 
        			 bird.getY() - 15,
                	 bird.getWidth() / 2.0f, bird.getHeight() / 2.0f,
                	 bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());
    }

	/**
	 * Draws the bird while the game is running
	 * @param runTime
	 * 			runtime of the game
	 */
    public void drawBird(float runTime) {
		if (bird.noFlap()) {
			batcher.draw(birdMid, bird.getX(), bird.getY(), bird.getWidth() / 2.0f, bird.getHeight() / 2.0f, bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());
		}
		else {
			batcher.draw(AssetLoader.animation.getKeyFrame(runTime), bird.getX(), bird.getY(), bird.getWidth() / 2.0f,
						 bird.getHeight() / 2.0f, bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());
		}
    }

	/**
	 * Draws the main menu UI, containing the startbutton and the logo
	 */
    public void drawMenuUI() {
    	batcher.draw(fbLogo, 12, yMidPoint - 50, 
    				 fbLogo.getRegionWidth() / 1.2f,
    				 fbLogo.getRegionHeight() / 1.2f);
    	startButton.draw(batcher);
    	
    }

	/**
	 * Draws the get ready message
	 */
	private void drawReady() {
		batcher.draw(ready, 36, yMidPoint - 50, 68, 14);
	}

	/**
	 * Draws the game over message
	 */
	private void drawGameOver() {
		batcher.draw(gameOver, 24, yMidPoint - 50, 92, 14);
	}
    
}