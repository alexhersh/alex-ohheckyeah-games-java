package org.ohheckyeah.games.catchy;

import java.util.ArrayList;

import org.ohheckyeah.games.catchy.assets.CatchyCharacters;
import org.ohheckyeah.games.catchy.assets.CatchyColors;
import org.ohheckyeah.games.kacheout.game.GamePlay;

import processing.core.PApplet;
import processing.core.PConstants;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.draw.color.ColorGroup;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.system.FileUtil;

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
		_isFullScreen = true;
		PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "org.ohheckyeah.games.catchy.Catchy" });
	}

	// input
	public static float KINECT_MIN_DIST = 1500;
	public static float KINECT_MAX_DIST = 2000;
	public static int KINECT_TOP = 0;
	public static int KINECT_BOTTOM = 150;
	public static float KINECT_GAP_PERCENT = 0.5f;
	protected boolean _isDebuggingKinect = false;

	// audio
	public AudioPool sounds;
//	public Soundtrack soundtrack;
	
	// debug 
	protected boolean _isDebugging = false;
	
	// dimensions
	protected int _gameWidth = 0;

	// graphics
	public CatchyCharacters characters;
	
	// mesh IDs
	public static String WIN_TEXT = "WIN_TEXT";
	
	// sound IDs
	public static String PADDLE_BOUNCE = "PADDLE_BOUNCE";
	
	// game state
	protected int _curMode;
	protected ColorGroup _gameColors;
	protected final int NUM_PLAYERS = 2;
	protected ArrayList<GamePlay> _gamePlays;
	protected GamePlay _player1;
	protected GamePlay _player2;
	
	// non-gameplay screens
//	protected IntroScreen _screenIntro;
	
	// game state
	protected int _gameState;
	protected int _gameStateQueued;	// wait until beginning on the next frame to switch modes to avoid mid-frame conflicts
	public static int GAME_ON = 3;
	public static int GAME_OVER = 4;
	public static int GAME_INTRO = 5;
	public static int GAME_INSTRUCTIONS = 6;
	public static int GAME_COUNTDOWN = 7;
	
	
	public void setup() {
		_customPropsFile = FileUtil.getHaxademicDataPath() + "properties/catchy.properties";
		super.setup();
		initGame();
	}

	public void initGame() {
		p.smooth(OpenGLUtil.SMOOTH_HIGH);
		
		_gameWidth = p.width / NUM_PLAYERS;
				
		loadMedia();
		
//		_screenIntro = new IntroScreen( _appConfig.getString("sponsor_images", "" ) );
		
		// set flags and props	
		pickNewColors();
		
		// default kinect camera distance is for up-close indoor testing. not good for real games - suggested use is 2300-3300
		// default pixel rows are the center 200 kinect data rows
		KINECT_MIN_DIST = _appConfig.getInt( "kinect_min_mm", 1500 );
		KINECT_MAX_DIST = _appConfig.getInt( "kinect_max_mm", 2000 );
		KINECT_TOP = _appConfig.getInt( "kinect_top_pixel", 240 );
		KINECT_BOTTOM = _appConfig.getInt( "kinect_bottom_pixel", 400 );
				
		//		// it's opposite day, since game mode triggers the next action
		//		if( _appConfig.getBoolean( "starts_on_game", true ) == true ) {
		//			setGameMode( GAME_INSTRUCTIONS );
		//		} else {
		//			setGameMode( GAME_INTRO );
		//		}
	}
		
	protected void loadMedia() {
		sounds = new AudioPool( p, _minim );
//		soundtrack = new Soundtrack();
		
//		AssetLoader loader = new AssetLoader();
//		loader.createMeshPool();
//		loader.loadAudio( sounds );	
		
		characters = new CatchyCharacters();
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
		
	// GAME LOGIC --------------------------------------------------------------------------------------
	
	public void setGameMode( int mode ) {
		P.println("next mode: "+mode);
		_gameStateQueued = mode;
	}
	
	public void swapGameMode() {
		_gameState = _gameStateQueued;
		if( _gameState == GAME_INTRO ) {
//			_screenIntro.reset();
//			soundtrack.playIntro();
		} else if( _gameState == GAME_INSTRUCTIONS ) {
			for( int i=0; i < NUM_PLAYERS; i++ ) {
//				_gamePlays.get( i ).reset();
			}
//			soundtrack.stop();
//			sounds.playSound( SFX_DOWN );
//			soundtrack.playInstructions();
		} 
		//		else if( _gameState == GAME_COUNTDOWN ) {
		//			for( int i=0; i < NUM_PLAYERS; i++ ) {
		//				_gamePlays.get( i ).startCountdown();
		//			}
		//			soundtrack.stop();
		//		} else if( _gameState == GAME_ON ) {
		//			for( int i=0; i < NUM_PLAYERS; i++ ) {
		//				_gamePlays.get( i ).launchBall();
		//			}
		//			soundtrack.playNext();
		//		} else if( _gameState == GAME_OVER ) {
		//			for( int i=0; i < NUM_PLAYERS; i++ ) _gamePlays.get( i ).gameOver();
		//			soundtrack.stop();
		//			sounds.playSound( WIN_SOUND );
		//		}
	}
		
	// MOVE TO ColorUtil =========	
	public int colorFromHex( String hex ) {
		return P.unhex("FF"+hex.substring(1));
	}

	
	// FRAME LOOP --------------------------------------------------------------------------------------
	
	public void drawApp() {
		DrawUtil.resetGlobalProps( p );
//		DrawUtil.setCenter( p );

//		p.shininess(1000f); 
//		p.lights();
		p.background(0);
		
		// set game bg
		int bgColor = colorFromHex("#E7E867");
		p.background(bgColor);
		
		// test draw
		p.fill(90);
		p.noStroke();

//		P.println(characters.blueSquid.width+","+characters.blueSquid.height);
		DrawUtil.setDrawCenter(p);
		p.shapeMode(PConstants.CENTER);
		p.shape( characters.blueSquid, p.width * 0.25f + P.sin( p.millis() * 0.001f ) * 100, p.height * 0.5f, characters.blueSquid.width, characters.blueSquid.height );
		p.shape( characters.greenPanner, p.width * 0.75f + P.sin( p.millis() * 0.001f ) * -100, p.height * 0.5f, characters.greenPanner.width, characters.greenPanner.height );
		
		DrawUtil.setDrawCorner(p);
		p.rect(10,10,10,100);
				
//		if( _gameState != _gameStateQueued ) swapGameMode();
//		if( _gameState == GAME_INTRO ) {
////			_screenIntro.update();
//		} else {
//			p.pushMatrix();
//			if( _gameState == GAME_INSTRUCTIONS ) checkGameStart();
//			updateGames();
//			p.popMatrix();
//		}
//		
//		if( _isDebugging == true ) displayDebug();
	}
	
	protected void checkGameStart() {
		boolean gameIsReady = true;
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			if( _gamePlays.get( i ).isPlayerReady() == false ) gameIsReady = false;
		}
		if( gameIsReady == true ) {
			setGameMode( GAME_COUNTDOWN );
		}
	}
	
	protected void updateGames(){
		// update all games before checking for complete. also take screenshot if the game's over and the time is right
		boolean takeScreenShot = false;
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.get( i ).update( i );
			if( _gamePlays.get( i ).shouldTakeScreenshot() == true ) takeScreenShot = true;
		}
		if( takeScreenShot == true ) {
			// PhotoBooth.snapGamePhoto( p, _stageWidth, _stageHeight );
		}
		// check for complete
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			if( _gamePlays.get( i ).hasClearedBoard() == true && _gameState == GAME_ON ) {
				setGameMode( GAME_OVER );
			}
		}
	}
	
	protected void displayDebug() {
//		debugCameraPos();
//		drawDebug();
		
		if( p.frameCount % ( _fps * 60 ) == 0 ) {
			P.println( "time: "+P.minute()+":"+P.second() );
		}
		if( p.frameCount % ( _fps * 15 ) == 0 ) {
			if( _gameState == GAME_INSTRUCTIONS ) {
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
