package org.ohheckyeah.games.kacheout;

import java.util.ArrayList;
import java.util.Date;

import org.ohheckyeah.games.kacheout.game.GamePlay;
import org.ohheckyeah.games.kacheout.game.KacheOutTracking;
import org.ohheckyeah.games.kacheout.game.Soundtrack;
import org.ohheckyeah.games.kacheout.media.AssetLoader;
import org.ohheckyeah.games.kacheout.screens.IntroScreen;
import org.ohheckyeah.shared.app.OHYKinectApp;

import processing.core.PApplet;

import com.haxademic.core.app.P;
import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.cameras.CameraDefault;
import com.haxademic.core.cameras.common.ICamera;
import com.haxademic.core.data.FloatRange;
import com.haxademic.core.draw.color.ColorGroup;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.hardware.kinect.KinectWrapper;
import com.haxademic.core.system.FileUtil;

public class KacheOut
extends OHYKinectApp  
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
		_isFullScreen = false;
		_hasChrome = false;
		boolean isSecondScreen = false;
		if( isSecondScreen ) {
			PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "--location=-1280,0", "--display=1", KacheOut.class.getName() });		// requires hiding os x 2nd screen menu bar: http://www.cultofmac.com/255910/hide-the-menu-bar-on-your-secondary-monitor-with-mavericks-os-x-tips/
		} else {
			PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", KacheOut.class.getName() });
		}
	}

	// input
	public static float KINECT_MIN_DIST = 1500;
	public static float KINECT_MAX_DIST = 2000;
	public static int KINECT_TOP = 0;
	public static int KINECT_BOTTOM = 150;
	protected boolean _isDebuggingKinect = false;

	// tracking
	protected KacheOutTracking _tracking;
	protected String _trackingDateStr;
	
	// audio
	public AudioPool sounds;
	public Soundtrack soundtrack;
	
	// debug 
	protected boolean _isDebugging = false;
	
	// dimensions and stuff
	protected int _stageWidth;
	protected int _stageHeight;	
	protected int _gameWidth;
	protected int _numAverages = 32;

	protected ICamera _curCamera = null;
		
	// mesh IDs
	public static String CREATE_DENVER_LOGO = "CREATE_DENVER_LOGO";
	public static String CREATE_DENVER_TEXT = "CREATE_DENVER";
	public static String PRESENTS_TEXT = "PRESENTS_TEXT";
	public static String KACHEOUT_LOGO = "KACHEOUT_LOGO";
	public static String KACHEOUT_LOGO_ALT = "KACHEOUT_LOGO_ALT";
	public static String BUILT_BY_TEXT = "BUILT_BY_TEXT";
	public static String UFO_1 = "UFO_1";
	public static String UFO_2 = "UFO_2";
	public static String UFO_3 = "UFO_3";
	public static String MODE_SET_LOGO = "MODE_SET_LOGO";
	public static String MODE_SET_LOGOTYPE = "MODE_SET_LOGOTYPE";
	public static String CACHEFLOWE_LOGO = "CACHEFLOWE_LOGO";
	public static String CACHEFLOWE_LOGOTYPE = "CACHEFLOWE_LOGOTYPE";
	public static String DESIGN_BY = "DESIGN_BY";
	public static String JON_DESIGN = "JON_DESIGN";
	public static String RYAN_DESIGN = "RYAN_DESIGN";
	public static String STEP_UP_TEXT = "STEP_UP_TEXT";
	public static String READY_TEXT = "READY_TEXT";
	public static String COUNTDOWN_TEXT_1 = "COUNTDOWN_TEXT_1";
	public static String COUNTDOWN_TEXT_2 = "COUNTDOWN_TEXT_2";
	public static String COUNTDOWN_TEXT_3 = "COUNTDOWN_TEXT_3";
	public static String WIN_TEXT = "WIN_TEXT";
	public static String LOSE_TEXT = "LOSE_TEXT";
	
	// sound IDs
	public static String PADDLE_BOUNCE = "PADDLE_BOUNCE";
	public static String WALL_BOUNCE = "WALL_BOUNCE";
	public static String INSERT_COIN = "INSERT_COIN";
	public static String COUNTDOWN_1 = "COUNTDOWN_1";
	public static String COUNTDOWN_2 = "COUNTDOWN_2";
	public static String COUNTDOWN_3 = "COUNTDOWN_3";
	public static String COUNTDOWN_1_VOX = "COUNTDOWN_1_VOX";
	public static String COUNTDOWN_2_VOX = "COUNTDOWN_2_VOX";
	public static String COUNTDOWN_3_VOX = "COUNTDOWN_3_VOX";
	public static String WIN_SOUND = "WIN_SOUND";
	public static String LAUNCH_SOUND = "LAUNCH_SOUND";
	public static String READY_SOUND = "READY_SOUND";
	public static String STEP_UP_SOUND = "STEP_UP_SOUND";
	public static String LOSE_BALL_SOUND = "LOSE_BALL_SOUND";
	public static String SFX_DOWN = "SFX_DOWN";
	
	// game state
	protected int _gameStartTime;
	protected int _curMode;
	protected ColorGroup _gameColors;
	protected final int NUM_PLAYERS = 2;
	protected ArrayList<GamePlay> _gamePlays;
	protected GamePlay _player1;
	protected GamePlay _player2;
	
	// non-gameplay screens
	protected IntroScreen _screenIntro;
	
	// game state
	protected int _gameState;
	protected int _gameStateQueued;	// wait until beginning on the next frame to switch modes to avoid mid-frame conflicts
	public static int GAME_ON = 3;
	public static int GAME_OVER = 4;
	public static int GAME_INTRO = 5;
	public static int GAME_INSTRUCTIONS = 6;
	public static int GAME_COUNTDOWN = 7;
	
	protected final float CAMERA_Z_WIDTH_MULTIPLIER = 0.888888f;	// 1280x720
	protected float _cameraZFromHeight = 0;
	
	public void setup() {
		_customPropsFile = FileUtil.getHaxademicDataPath() + "properties/kacheout.properties";
		_useLegacyAudio = true;
		super.setup();
		setKinectProperties();	// replace with BaseGame setup in the future
		initGame();
	}

	public void initGame() {
		_stageWidth = width;
		_stageHeight = height;
		_gameWidth = _stageWidth / NUM_PLAYERS;
//		_cameraZFromHeight = (float)_stageHeight * CAMERA_Z_WIDTH_MULTIPLIER;
				
		_audioInput.setNumAverages( _numAverages );
		_audioInput.setDampening( .13f );
		
		sounds = new AudioPool( p, p.minim );
		soundtrack = new Soundtrack();
		
		AssetLoader loader = new AssetLoader();
		loader.createMeshPool();
		loader.loadAudio( sounds );		
		
		_tracking = new KacheOutTracking();

		
		_screenIntro = new IntroScreen( _appConfig.getString("sponsor_images", "" ) );
		
		// set flags and props	
		pickNewColors();
		newCamera();
		
		// default kinect camera distance is for up-close indoor testing. not good for real games - suggested use is 2300-3300
		// default pixel rows are the center 200 kinect data rows
		KINECT_MIN_DIST = _appConfig.getInt( "kinect_min_mm", 1500 );
		KINECT_MAX_DIST = _appConfig.getInt( "kinect_max_mm", 2000 );
		KINECT_TOP = _appConfig.getInt( "kinect_top_pixel", 240 );
		KINECT_BOTTOM = _appConfig.getInt( "kinect_bottom_pixel", 400 );
		int KINECT_PLAYER_GAP = p.appConfig.getInt( "kinect_player_gap", 0 );

		float kinectRangeWidth = KinectWrapper.KWIDTH / 2f - KINECT_PLAYER_GAP / 2f;
		_player1 = new GamePlay( 0, 0, _gameWidth, new FloatRange( 0, kinectRangeWidth ), _kinectGrid.getRegion(0) );
		_player2 = new GamePlay( 1, _gameWidth, _gameWidth * 2, new FloatRange( KinectWrapper.KWIDTH - kinectRangeWidth, KinectWrapper.KWIDTH ), _kinectGrid.getRegion(1) );
		_gamePlays = new ArrayList<GamePlay>();
		_gamePlays.add( _player1 );
		_gamePlays.add( _player2 );
		
		// it's opposite day, since game mode triggers the next action
		if( _appConfig.getBoolean( "starts_on_game", true ) == true ) {
			setGameMode( GAME_INSTRUCTIONS );
		} else {
			setGameMode( GAME_INTRO );
		}
	}
		
	// HAXADEMIC STUFF --------------------------------------------------------------------------------------
	void newCamera() {
		_curCamera = new CameraDefault( p, 0, 0, 0 );
		_curCamera.setPosition( _stageWidth/2f, _stageHeight/2f, 0 );	// _cameraZFromHeight
		_curCamera.setTarget( _stageWidth/2f, _stageHeight/2f, 0 );
		_curCamera.reset();
	}
	
	protected void debugCameraPos() {
		P.println(-_stageWidth + p.mouseX*2);
		_curCamera.setPosition( _stageWidth/2, _stageHeight/2, -_stageWidth + p.mouseX*2 );
	}

	public void beatDetect( int isKickCount, int isSnareCount, int isHatCount, int isOnsetCount ) {
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
	public int stageWidth() { return _stageWidth; }
	public int stageHeight() { return _stageHeight; }
	public float gameBaseZ() { return -_stageHeight; }
	public int gameState() { return _gameState; }
	public ColorGroup gameColors() { return _gameColors; }
	public boolean isDebugging() { return _isDebugging; }
	
	
	// GAME LOGIC --------------------------------------------------------------------------------------
	
	public void setGameMode( int mode ) {
//		p.println("next mode: "+mode);
		_gameStateQueued = mode;
	}
	
	public void swapGameMode() {
		_gameState = _gameStateQueued;
		if( _gameState == GAME_INTRO ) {
			_screenIntro.reset();
			soundtrack.playIntro();
		} else if( _gameState == GAME_INSTRUCTIONS ) {
			for( int i=0; i < NUM_PLAYERS; i++ ) {
				_gamePlays.get( i ).reset();
			}
			soundtrack.stop();
			sounds.playSound( SFX_DOWN );
			soundtrack.playInstructions();
		} else if( _gameState == GAME_COUNTDOWN ) {
			for( int i=0; i < NUM_PLAYERS; i++ ) {
				_gamePlays.get( i ).startCountdown();
			}
			soundtrack.stop();
		} else if( _gameState == GAME_ON ) {
			for( int i=0; i < NUM_PLAYERS; i++ ) {
				_gamePlays.get( i ).launchBall();
			}
			soundtrack.playNext();
			_gameStartTime = p.millis();
		} else if( _gameState == GAME_OVER ) {
			for( int i=0; i < NUM_PLAYERS; i++ ) _gamePlays.get( i ).gameOver();
			soundtrack.stop();
			sounds.playSound( WIN_SOUND );
		}
		// mute sounds while stress-testing
		if( _isDebugging == true ) {
//			sounds.mute( true );
//			soundtrack.mute( true );
		}
	}
		
	// FRAME LOOP --------------------------------------------------------------------------------------
	
	public void drawApp() {
		super.drawApp();
		
		p.resetMatrix(); // why did I ever decide to do this?! :(
		DrawUtil.resetGlobalProps( p );
		p.resetMatrix();
		DrawUtil.setCenter( p );

		p.shininess(1000f); 
		p.lights();
		p.background(0);
				
		_curCamera.update();

		if( _gameState != _gameStateQueued ) swapGameMode();
		if( _gameState == GAME_INTRO ) {
			_screenIntro.update();
		} else {
			p.pushMatrix();
			if( _gameState == GAME_INSTRUCTIONS ) checkGameStart();
			updateGames();
			p.popMatrix();
		}
		
		if( _isDebugging == true ) displayDebug();
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
			// track game!
		    Date gameDate = new Date();
		    _trackingDateStr = gameDate.toString();
		    
		    // store data
		    String winIndexes = "";
		    if(_player1.didWin() == true && _player2.didWin() == true) winIndexes = "0,1";
		    else if(_player1.didWin() == true) winIndexes = "0";
		    else if(_player2.didWin() == true) winIndexes = "1";
		    
		    _tracking.trackGameResult(_trackingDateStr, 2, winIndexes, P.round((p.millis() - _gameStartTime)/1000));
		    
		    // take photo
			_tracking.saveCameraImage(_trackingDateStr);
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
	
	protected void drawDebug() {
		// draw depth image
		DrawUtil.setCenter( p );
		p.translate( 0, 0, -1350 );
		p.fill(255, 255);
		p.noStroke();
		p.rect(0, 0, KinectWrapper.KWIDTH*1.1f, KinectWrapper.KHEIGHT*1.1f);
		p.translate( 0, 0, 100 );
		p.rotateY( (float)Math.PI );
//		p.image( _kinectWrapper.getDepthImage(), 0, 0, _kinectWrapper.KWIDTH, _kinectWrapper.KHEIGHT );
		p.image( kinectWrapper.getDepthImage(), 0, 0, _stageWidth, _stageHeight );

	}
	
	// Visual fun
	protected void pickNewColors() {
		// get themed colors
		if( _gameColors == null ) {
			_gameColors = new ColorGroup( ColorGroup.KACHE_OUT );
		}
		_gameColors.setRandomGroup();
	}

}
