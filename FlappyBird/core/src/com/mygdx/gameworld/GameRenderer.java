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

public class GameRenderer {
	private GameWorld world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher;
	
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

	private void drawScore() {
		String score = world.getScore() + "";
		AssetLoader.shadow.draw(batcher, "" + world.getScore(), 68 - (3 * score.length()), 12);
		AssetLoader.font.draw(batcher, "" + world.getScore(), 68 - (3 * score.length() - 1), 11);
	}
	
	private void initGameObjects() {
        bird = world.getBird();
        scrollHandler = world.getScrollHandler();
        frontGrass = scrollHandler.getFrontGrass();
        backGrass = scrollHandler.getBackGrass();
        firstPipe = scrollHandler.getFirstPipe();
        secondPipe = scrollHandler.getSecondPipe();
        thirdPipe = scrollHandler.getThirdPipe();
    }

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
    
    private void drawGrass() {
    	batcher.draw(grass, frontGrass.getX(), frontGrass.getY(), frontGrass.getWidth() + 1, frontGrass.getHeight());
    	batcher.draw(grass, backGrass.getX(), backGrass.getY(), backGrass.getWidth() + 1, backGrass.getHeight());
    }
    
    private void drawPipeEnds() {
    	batcher.draw(barUp, firstPipe.getX() - 1, firstPipe.getY() +  firstPipe.getHeight() - 14, 24, 14);
    	batcher.draw(barDown, firstPipe.getX() - 1, firstPipe.getY() +  firstPipe.getHeight() + 45, 24, 14);
    	
    	batcher.draw(barUp, secondPipe.getX() - 1, secondPipe.getY() +  secondPipe.getHeight() - 14, 24, 14);
    	batcher.draw(barDown, secondPipe.getX() - 1, secondPipe.getY() +  secondPipe.getHeight() + 45, 24, 14);
    	
    	batcher.draw(barUp, thirdPipe.getX() - 1, thirdPipe.getY() +  thirdPipe.getHeight() - 14, 24, 14);
    	batcher.draw(barDown, thirdPipe.getX() - 1, thirdPipe.getY() +  thirdPipe.getHeight() + 45, 24, 14);
    }
    
    private void drawPipes() {
    	batcher.draw(bar, firstPipe.getX(), firstPipe.getY(), firstPipe.getWidth(), firstPipe.getHeight());
    	batcher.draw(bar, firstPipe.getX(), firstPipe.getY() + firstPipe.getHeight() + 45, firstPipe.getWidth(), yMidPoint + 66 - (firstPipe.getHeight() + 45));
    	
    	batcher.draw(bar, secondPipe.getX(), secondPipe.getY(), secondPipe.getWidth(), secondPipe.getHeight());
    	batcher.draw(bar, secondPipe.getX(), secondPipe.getY() + secondPipe.getHeight() + 45, secondPipe.getWidth(), yMidPoint + 66 - (secondPipe.getHeight() + 45));
    	
    	batcher.draw(bar, thirdPipe.getX(), thirdPipe.getY(), thirdPipe.getWidth(), thirdPipe.getHeight());
    	batcher.draw(bar, thirdPipe.getX(), thirdPipe.getY() + thirdPipe.getHeight() + 45, thirdPipe.getWidth(), yMidPoint + 66 - (thirdPipe.getHeight() + 45));
    }
    
    private void drawBirdCentered(float runTime) {
        batcher.draw(animation.getKeyFrame(runTime), 
        			 59, 
        			 bird.getY() - 15,
                	 bird.getWidth() / 2.0f, bird.getHeight() / 2.0f,
                	 bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());
    }
    
    public void drawBird(float runTime) {
		if (bird.noFlap()) {
			batcher.draw(birdMid, bird.getX(), bird.getY(), bird.getWidth() / 2.0f, bird.getHeight() / 2.0f, bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());
		}
		else {
			batcher.draw(AssetLoader.animation.getKeyFrame(runTime), bird.getX(), bird.getY(), bird.getWidth() / 2.0f,
						 bird.getHeight() / 2.0f, bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());
		}
    }
    
    public void drawMenuUI() {
    	batcher.draw(fbLogo, 12, yMidPoint - 50, 
    				 fbLogo.getRegionWidth() / 1.2f,
    				 fbLogo.getRegionHeight() / 1.2f);

    	startButton.draw(batcher);
    	
    }
    
	private void drawReady() {
		batcher.draw(ready, 36, yMidPoint - 50, 68, 14);
	}
	
	private void drawGameOver() {
		batcher.draw(gameOver, 24, yMidPoint - 50, 92, 14);
	}
    
}

/* Simple Rectangle Drawing Example
// 1. Draw a black background (prevents flickering)
Gdx.gl.glClearColor(0, 0, 0, 1);
Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

// 2. Draw the filled rectangle
// Tell shape renderer to begin drawing shapes
shapeRenderer.begin(ShapeType.Filled);
// Choose RGB Color of 87, 109, and 120 at full opacity
shapeRenderer.setColor(0 / 255.0f, 0 / 255.0f, 255 / 255.0f, 1);
// Draw rectangle from GameWorld world (Using ShapeType.Filled)
shapeRenderer.rect(world.getRect().x, world.getRect().y, world.getRect().width, world.getRect().height);
// Tells shape renderer to finish rendering (must be called every time)
shapeRenderer.end();

// 3. Draw the rectangle's outline
// Tells shape renderer to draw an outline of the following shapes
shapeRenderer.begin(ShapeType.Line);
// Choose RGB Color of 255, 109, 120 at full opacity
shapeRenderer.setColor(255 / 255.0f, 0 / 255.0f, 0 / 255.0f, 1);
// Draw rectangle from GameWorld world (Using ShapeType.Line)
shapeRenderer.rect(world.getRect().x, world.getRect().y, world.getRect().width, world.getRect().height);
shapeRenderer.end();
*/