package org.ohheckyeah.games.catchy;

import java.util.ArrayList;

import org.ohheckyeah.games.catchy.assets.CatchyColors;
import org.ohheckyeah.games.catchy.assets.CatchyGraphics;
import org.ohheckyeah.games.catchy.game.CatchyGamePlay;
import org.ohheckyeah.games.catchy.game.CatchyGameTimer;
import org.ohheckyeah.games.catchy.screens.CatchyTitleScreen;

import processing.core.PApplet;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.draw.color.ColorGroup;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.hardware.kinect.KinectRegionGrid;
import com.haxademic.core.system.FileUtil;
import com.haxademic.core.system.TimeFactoredFps;

public class Catchy
extends PAppletHax  
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Auto-initialization of the main class.
	 * @param args
	 */
	public static void main(String args[]) {
		//		_isFullScreen = true;
		PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "org.ohheckyeah.games.catchy.Catchy" });
	}

	// input
	public static float KINECT_MIN_DIST = 1500;
	public static float KINECT_MAX_DIST = 2000;
	public static int KINECT_TOP = 0;
	public static int KINECT_BOTTOM = 480;

	// audio
	public AudioPool sounds;
//	public Soundtrack soundtrack;
	
	// debug 
	protected boolean _isDebugging = false;
	
	// dimensions
	protected float _gameOrigHeight = 680.0f;
	public float gameScaleV = 1;
	protected int _gameWidth = 0;

	// graphics
	protected int _bgColor;
	public CatchyGraphics gameGraphics;
	
	// mesh IDs
	public static String WIN_TEXT = "WIN_TEXT";
	
	// sound IDs
	public static String PADDLE_BOUNCE = "PADDLE_BOUNCE";
	
	// game state
	protected ColorGroup _gameColors;
	public static int NUM_PLAYERS = 2;
	protected KinectRegionGrid _kinectGrid;
	protected ArrayList<CatchyGamePlay> _gamePlays;

	// game state
	protected int _gameState;
	protected int _gameStateQueued;	// wait until beginning on the next frame to switch modes to avoid mid-frame conflicts
	public static int GAME_PLAYING = 3;
	public static int GAME_FINISHING = 8;
	public static int GAME_OVER = 4;
	public static int GAME_INTRO = 5;
	public static int GAME_WAITING_FOR_PLAYERS = 6;
	public static int GAME_COUNTDOWN = 7;
	
	// timers/timing
	public TimeFactoredFps timeFactor;
	public CatchyGameTimer gameTimer;
	protected int _gameOverTime = 0;
	
	// non-gameplay screens
	protected CatchyTitleScreen _logoScreen;
	
	
	
	public void setup() {
		_customPropsFile = FileUtil.getHaxademicDataPath() + "properties/catchy.properties";
		super.setup();
		initGame();
	}

	public void initGame() {
		p.noSmooth(); //smooth(OpenGLUtil.SMOOTH_LOW);
		_bgColor = ColorUtil.colorFromHex("#E7E867");

		timeFactor = new TimeFactoredFps( p, 50 );
		gameScaleV = p.height / _gameOrigHeight;
				
		loadMedia();
		
		_logoScreen = new CatchyTitleScreen();
		
		// set flags and props	
		pickNewColors();
		setKinectProperties();
		buildGameplays();
		buildGameTimer();
		setInitialGameState();
	}
	
	protected void setKinectProperties() {
		// default kinect camera distance is for up-close indoor testing. not good for real games - suggested use is 2300-3300
		// default pixel rows are the center 200 kinect data rows
		KINECT_MIN_DIST = _appConfig.getInt( "kinect_min_mm", 1500 );
		KINECT_MAX_DIST = _appConfig.getInt( "kinect_max_mm", 2000 );
		KINECT_TOP = _appConfig.getInt( "kinect_top_pixel", 240 );
		KINECT_BOTTOM = _appConfig.getInt( "kinect_bottom_pixel", 400 );
		NUM_PLAYERS = _appConfig.getInt( "num_players", 2 );
	}
	
	public float scaleV( float input ) {
		return input * gameScaleV;
	}
		
	protected void buildGameTimer() {
		gameTimer = new CatchyGameTimer();
		gameTimer.startTimer();
	}
	
	protected void buildGameplays() {
		_kinectGrid = new KinectRegionGrid(p, NUM_PLAYERS, 1, (int)KINECT_MIN_DIST, (int)KINECT_MAX_DIST, 200, (int)KINECT_TOP, (int)KINECT_BOTTOM);
		_gameWidth = P.ceil( p.width / (float) NUM_PLAYERS );
		_gamePlays = new ArrayList<CatchyGamePlay>();
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.add( new CatchyGamePlay( i, _gameWidth, _kinectGrid.getRegion(i) ) );
		}
	}
	
	protected void loadMedia() {
		sounds = new AudioPool( p, _minim );
//		soundtrack = new Soundtrack();
		
//		AssetLoader loader = new AssetLoader();
//		loader.createMeshPool();
//		loader.loadAudio( sounds );	
		
		gameGraphics = new CatchyGraphics();
		gameGraphics.shuffleCharacters();
	}
	
	// INPUT --------------------------------------------------------------------------------------

	protected void handleInput( boolean isMidi ) {
		super.handleInput( isMidi );
		if ( p.key == 'd' || p.key == 'D' ) {
			_isDebugging = !_isDebugging;
			if( kinectWrapper != null ) {
				kinectWrapper.enableRGB( !_isDebugging );
				kinectWrapper.enableDepth( !_isDebugging );
			}
		}
	}
	
	// PUBLIC ACCESSORS FOR GAME OBJECTS --------------------------------------------------------------------------------------
	public int gameWidth() { return _gameWidth; }
	public int gameState() { return _gameState; }
	public ColorGroup gameColors() { return _gameColors; }
	public boolean isDebugging() { return _isDebugging; }
	
	public boolean isLastGameplay( CatchyGamePlay gameplay ) {
		if( gameplay == _gamePlays.get( _gamePlays.size() - 1) ) {
			return true;
		} else {
			return false;
		}
	}
		
	// GAME STATE --------------------------------------------------------------------------------------
	
	public void setInitialGameState() {
		if( _appConfig.getBoolean( "starts_on_game", true ) == true ) {
			setGameMode( GAME_PLAYING );
		} else {
			setGameMode( GAME_INTRO );
		}
	}
	
	public void setGameMode( int mode ) {
		P.println("next mode: "+mode);
		_gameStateQueued = mode;
	}
	
	public void swapGameMode() {
		_gameState = _gameStateQueued;
		if( _gameState == GAME_INTRO ) {
			_logoScreen.reset();
//			soundtrack.playIntro();
			gameGraphics.shuffleCharacters();
		} else if( _gameState == GAME_WAITING_FOR_PLAYERS ) {
			for( int i=0; i < NUM_PLAYERS; i++ ) {
				_gamePlays.get( i ).reset();
				_gamePlays.get( i ).startPlayerDetection();
			}
//			soundtrack.stop();
//			sounds.playSound( SFX_DOWN );
//			soundtrack.playInstructions();
		//		else if( _gameState == GAME_COUNTDOWN ) {
		//			for( int i=0; i < NUM_PLAYERS; i++ ) {
		//				_gamePlays.get( i ).startCountdown();
		//			}
		//			soundtrack.stop();
		} else if( _gameState == GAME_PLAYING ) {
			for( int i=0; i < NUM_PLAYERS; i++ ) {
				_gamePlays.get( i ).startGame();
			}
			gameTimer.startTimer();
			// soundtrack.playNext();
		} else if( _gameState == GAME_FINISHING ) {
			for( int i=0; i < NUM_PLAYERS; i++ ) _gamePlays.get( i ).stopDropping();
		} else if( _gameState == GAME_OVER ) {
			for( int i=0; i < NUM_PLAYERS; i++ ) _gamePlays.get( i ).gameOver();
			_gameOverTime = p.millis();
			// soundtrack.stop();
			// sounds.playSound( WIN_SOUND );
		}
	}
	
	protected void handleGameState() {
		if( _gameState != _gameStateQueued ) swapGameMode();
		
		if( _gameState == GAME_INTRO ) {
			DrawUtil.setDrawCenter(p);
			_logoScreen.update();
			p.image( _logoScreen.pg, p.width/2f, p.height/2f, _logoScreen.pg.width, _logoScreen.pg.height );
		} else if( _gameState == GAME_WAITING_FOR_PLAYERS ) {
			_kinectGrid.update();
			updateGameplays();
			// if we have all players, then start!
			boolean hasPlayers = true;
			for( int i=0; i < NUM_PLAYERS; i++ ) {
				if( _gamePlays.get(i).hasPlayer() == false ) {
					hasPlayers = false;
				}
			}
			if( hasPlayers == true ) setGameMode( GAME_PLAYING );
		} else if( _gameState == GAME_PLAYING || _gameState == GAME_FINISHING ) {
			_kinectGrid.update();
			gameTimer.update();
			updateGameplays();
		} else if( _gameState == GAME_OVER ) {
			updateGameplays();
			if( p.millis() > _gameOverTime + 2000 ) {
				setGameMode( GAME_INTRO );
			}
		}
	}
	
	protected void checkGameStart() {
		boolean gameIsReady = true;
		for( int i=0; i < NUM_PLAYERS; i++ ) {
//			if( _gamePlays.get( i ).isPlayerReady() == false ) gameIsReady = false;
		}
		if( gameIsReady == true ) {
			setGameMode( GAME_COUNTDOWN );
		}
	}
	
	
	// FRAME LOOP --------------------------------------------------------------------------------------
	
	public void drawApp() {
		p.background(_bgColor);

		// update timing
		timeFactor.update();
		// P.println("target_fps: "+timeFactor.targetFps()+" / actual_fps: "+timeFactor.actualFps()+" / timeFactor: "+timeFactor.multiplier());
		
		handleGameState();
	}
	
	protected void updateGameplays() {
		// update and draw gamePlays
		DrawUtil.setDrawCorner(p);
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			CatchyGamePlay gamePlay = _gamePlays.get(i); 
			gamePlay.update();
			p.image( gamePlay.pg, _gameWidth * i, 0, _gameWidth, p.height);
		}
		
		// draw dividers & timers
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			if(i > 0) {
				// draw white borders
				DrawUtil.setDrawCorner(p);
				p.fill(255);
				p.noStroke();
				float dividerX = _gameWidth * i - gameGraphics.gameDivider.width * 0.5f;
				float dividerH = ( gameScaleV > 1 ) ? scaleV( gameGraphics.gameDivider.height ) : gameGraphics.gameDivider.height;
				p.shape( gameGraphics.gameDivider, dividerX, 0, gameGraphics.gameDivider.width, dividerH );
				// draw timers
				DrawUtil.setDrawCenter(p);
				p.pushMatrix();
				p.translate( dividerX, p.height - ( scaleV(gameGraphics.timerBanner.height) ) / 1.7f );
				gameTimer.drawTimer();
				p.popMatrix();
			}
			if(i == 0 && NUM_PLAYERS == 1) {
				// draw timers
				DrawUtil.setDrawCenter(p);
				p.pushMatrix();
				p.translate( p.width - scaleV(100), 30 );
				gameTimer.drawTimer();
				p.popMatrix();
			}
		}
	}
	

	protected void updateGames(){
		// update all games before checking for complete. also take screenshot if the game's over and the time is right
		boolean takeScreenShot = false;
		for( int i=0; i < NUM_PLAYERS; i++ ) {
//			_gamePlays.get( i ).update( i );
//			if( _gamePlays.get( i ).shouldTakeScreenshot() == true ) takeScreenShot = true;
		}
		if( takeScreenShot == true ) {
			// PhotoBooth.snapGamePhoto( p, _stageWidth, _stageHeight );
		}
		// check for complete
		for( int i=0; i < NUM_PLAYERS; i++ ) {
//			if( _gamePlays.get( i ).hasClearedBoard() == true && _gameState == GAME_ON ) {
//				setGameMode( GAME_OVER );
//			}
		}
	}
	
	protected void displayDebug() {
//		debugCameraPos();
//		drawDebug();
		
		if( p.frameCount % ( _fps * 60 ) == 0 ) {
			P.println( "time: "+P.minute()+":"+P.second() );
		}
		if( p.frameCount % ( _fps * 15 ) == 0 ) {
			if( _gameState == GAME_WAITING_FOR_PLAYERS ) {
				setGameMode( GAME_COUNTDOWN );
			}
		}
	}
	
	// Visual fun
	protected void pickNewColors() {
		// get themed colors
		if( _gameColors == null ) {
			_gameColors = new CatchyColors(0);
		}
		_gameColors.setRandomGroup();
	}
	
}
