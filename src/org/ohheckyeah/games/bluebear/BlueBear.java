package org.ohheckyeah.games.bluebear;

import hypermedia.net.UDP;

import java.util.Date;

import org.ohheckyeah.games.bluebear.assets.BlueBearColors;
import org.ohheckyeah.games.bluebear.assets.BlueBearGraphics;
import org.ohheckyeah.games.bluebear.assets.BlueBearSounds;
import org.ohheckyeah.games.bluebear.game.BlueBearGamePlay;
import org.ohheckyeah.games.bluebear.game.BlueBearTracking;
import org.ohheckyeah.games.bluebear.screens.BlueBearIntroScreens;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.data.ConvertUtil;
import com.haxademic.core.draw.color.ColorGroup;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.hardware.kinect.KinectRegionGrid;
import com.haxademic.core.system.FileUtil;
import com.haxademic.core.system.TimeFactoredFps;

public class BlueBear
extends PAppletHax  
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Auto-initialization of the main class.
	 * @param args
	 */
	public static void main(String args[]) {
		// _isFullScreen = true;
		PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "org.ohheckyeah.games.bluebear.BlueBear" });
	}

	// input
	public static float KINECT_MIN_DIST = 1500;
	public static float KINECT_MAX_DIST = 2000;
	public static int KINECT_TOP = 0;
	public static int KINECT_BOTTOM = 480;

	// remote kinect
	protected UDP _udp;
	protected boolean _remoteDebugging = false;
	protected String _receiverIp = "";
	protected int _receiverPort = 0;
	protected boolean _isRemoteKinect = false;
	
	// debug 
	protected boolean _isDebugging = false;
	
	// dimensions
	protected float _gameOrigHeight = 630.0f;
	public float gameScaleV = 1;
	protected int _gameWidth = 0;

	// graphics
	protected int _bgColor;
	public BlueBearGraphics gameGraphics;

	// audio 
	public BlueBearSounds sounds; 
	
	// game state
	protected ColorGroup _gameColors;
	public static int NUM_PLAYERS = 2;
	protected KinectRegionGrid _kinectGrid;

	// game state
	public enum GameState {
		GAME_INTRO,
		GAME_INTRO_OUTRO,
		GAME_WAITING_FOR_PLAYERS,
		GAME_PRE_COUNTDOWN,
		GAME_COUNTDOWN,
		GAME_PLAYING,
		GAME_FINISHING,
		GAME_OVER,
		GAME_OVER_OUTRO
	}
	protected GameState _gameState;
	protected GameState _gameStateQueued;	// wait until beginning on the next frame to switch states to avoid mid-frame conflicts

	public PGraphics pg;

	protected BlueBearGamePlay _gamePlay;
	protected BlueBearTracking _tracking;
	protected String _trackingDateStr;
	
	// timers/timing
	public TimeFactoredFps timeFactor;
	protected int _gameOverTime = 0;
	protected boolean _gameOverRecorded = false;
	protected int _preCountdownStartTime = 0;
	protected int _countdownStartTime = 0;
	protected int _countdownSeconds = 3;
	protected int _lastCountdownTime = 0;

	// non-gameplay screens
	protected BlueBearIntroScreens _introScreens;

	
	public void setup() {
		_customPropsFile = FileUtil.getHaxademicDataPath() + "properties/bluebear.properties";
		super.setup();
		initGame();
	}

	public void initGame() {
		buildCanvas();
		_bgColor = BlueBearColors.STAGE_BG;

		timeFactor = new TimeFactoredFps( p, 50 );
		gameScaleV = pg.height / _gameOrigHeight;
				
		loadMedia();
		
		_introScreens = new BlueBearIntroScreens( _appConfig.getString( "sponsor_images", null ) );
		
		// set flags and props	
		setKinectProperties();
		buildGameplay();
		setInitialGameState();
		_tracking = new BlueBearTracking();
	}
	
	protected void setKinectProperties() {
		// default kinect camera distance is for up-close indoor testing. not good for real games - suggested use is 2300-3300
		// default pixel rows are the center 200 kinect data rows
		KINECT_MIN_DIST = _appConfig.getInt( "kinect_min_mm", 1500 );
		KINECT_MAX_DIST = _appConfig.getInt( "kinect_max_mm", 2000 );
		KINECT_TOP = _appConfig.getInt( "kinect_top_pixel", 240 );
		KINECT_BOTTOM = _appConfig.getInt( "kinect_bottom_pixel", 400 );
		NUM_PLAYERS = _appConfig.getInt( "num_players", 2 );
		
		_remoteDebugging = _appConfig.getBoolean( "kinect_remote_debug", false );
		_receiverIp = _appConfig.getString( "kinect_remote_receiver_ip", "" );
		_receiverPort = _appConfig.getInt( "kinect_remote_receiver_port", 0 );
		_isRemoteKinect = _appConfig.getBoolean("kinect_remote_active", false);
		if( _isRemoteKinect == true ) initRemoteKinect();
	}
	
	public float scaleV( float input ) {
		return input * gameScaleV;
	}

	public int svgWidth( PShape shape ) {
		return P.round(scaleV(shape.width));
	}
	
	public int svgHeight( PShape shape ) {
		return P.round(scaleV(shape.height));
	}

	protected void buildCanvas() {
		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
	}
	
	protected void buildGameplay() {
		_kinectGrid = new KinectRegionGrid(p, NUM_PLAYERS, 1, (int)KINECT_MIN_DIST, (int)KINECT_MAX_DIST, 250, (int)KINECT_TOP, (int)KINECT_BOTTOM);
		_gamePlay = new BlueBearGamePlay( _kinectGrid, _isRemoteKinect );
	}
	
	protected void loadMedia() {
		sounds = new BlueBearSounds();
		gameGraphics = new BlueBearGraphics();
		
//		gameGraphics.shuffleCharacters();
	}
	
	// INPUT --------------------------------------------------------------------------------------
	
	protected void initRemoteKinect() {
		_udp = new UDP( this, _receiverPort );
		_udp.log( _remoteDebugging );
		_udp.listen( true );	
	}
	
	public void receive( byte[] data, String ip, int port ) {
		String message = new String( data );
		if( _remoteDebugging == true ) P.println( "received: \""+message+"\" from "+ip+" on port "+port );
		
		String[] remoteKinectPlayersData = message.split("~");
		for( int i=0; i < remoteKinectPlayersData.length; i++ ) {
			// P.println("PLAYER "+i+" = "+remoteKinectPlayersData[i]);
			String[] kinectPlayerData = remoteKinectPlayersData[i].split(":");
			_kinectGrid.getRegion(i).controlX( ConvertUtil.stringToFloat( kinectPlayerData[0].trim() ) );
			_kinectGrid.getRegion(i).controlZ( ConvertUtil.stringToFloat( kinectPlayerData[1].trim() ) );
			_kinectGrid.getRegion(i).pixelCount( ConvertUtil.stringToInt( kinectPlayerData[2].trim() ) );
		}
	}

	
	// PUBLIC ACCESSORS FOR GAME OBJECTS --------------------------------------------------------------------------------------
	
	public GameState gameState() { return _gameState; }
			
	// GAME STATE --------------------------------------------------------------------------------------
	
	public void setInitialGameState() {
		if( _appConfig.getBoolean( "starts_on_game", true ) == true ) {
			setGameState( GameState.GAME_PRE_COUNTDOWN );
		} else {
			setGameState( GameState.GAME_INTRO );
		}
	}
	
	public void setGameState( GameState state ) {
		_gameStateQueued = state;
		// P.println("GameState: ",_gameStateQueued);
	}
	
	protected void swapGameState() {
		_gameState = _gameStateQueued;
		switch( _gameState ) {
			case GAME_INTRO : setGameStateIntro(); break;
			case GAME_INTRO_OUTRO : setGameStateIntroOutro(); break;
			case GAME_WAITING_FOR_PLAYERS : setGameStateWaitingForPlayers(); break;
			case GAME_PRE_COUNTDOWN : setGameStatePreCountdown(); break;
			case GAME_COUNTDOWN : setGameStateCountdown(); break;
			case GAME_PLAYING : setGameStatePlaying(); break;
			case GAME_FINISHING : setGameStateFinishing(); break;
			case GAME_OVER : setGameStateGameOver(); break;
			case GAME_OVER_OUTRO : setGameStateGameOverOutro(); break;
			default: break;
		}
	}
	
	protected void runGameState() {
		if( _gameState != _gameStateQueued ) swapGameState();
		
		switch( _gameState ) {
			case GAME_INTRO : 
			case GAME_INTRO_OUTRO : runGameStateIntro(); break;
			case GAME_WAITING_FOR_PLAYERS : runGameStateWaitingForPlayers(); break;
			case GAME_PRE_COUNTDOWN : runGameStatePreCountdown(); break;
			case GAME_COUNTDOWN : runGameStateCountdown(); break;
			case GAME_PLAYING : 
			case GAME_FINISHING : runGameStatePlaying(); break;
			case GAME_OVER : 
			case GAME_OVER_OUTRO : runGameStateGameOver(); break;
			default: break;
		}
	}
	
	protected void setGameStateIntro() {
		_introScreens.reset();
		sounds.playOHY();
	}

	protected void runGameStateIntro() {
//		if( _gameState == GameState.GAME_INTRO_OUTRO ) {
			updateGameplay();
//		}
		_introScreens.update();
		DrawUtil.setDrawCorner(pg);
		pg.image( _introScreens.pg, 0, 0, _introScreens.pg.width, _introScreens.pg.height );
	}

	protected void setGameStateIntroOutro() {
		sounds.fadeOutIntro();
		_gamePlay.reset();
		_gamePlay.startPlayerDetection();
	}
	
	protected void setGameStateWaitingForPlayers() {
		_gamePlay.animateToPlayerDetection();
		sounds.playWaiting();
	}

	protected void runGameStateWaitingForPlayers() {
		updateGameplay();
		_gamePlay.detectPlayers();
	}
	
	protected void setGameStatePreCountdown() {
		_preCountdownStartTime = p.millis();
		_gamePlay.playersLockedIn();
		sounds.playSound( BlueBearSounds.PLAYERS_DETECTED );
		sounds.stopSoundtrack();
	}

	protected void runGameStatePreCountdown() {
		updateGameplay();
		if( p.millis() > _preCountdownStartTime + 2500 ) {
			setGameState( GameState.GAME_COUNTDOWN );
		}
	}

	protected void setGameStateCountdown() {
		_countdownStartTime = p.millis();
		_gamePlay.showCountdown( _countdownSeconds );
		_gamePlay.animateToGameState();
	}

	protected void runGameStateCountdown() {
		updateGameplay();
		int countdownSecondsElapsed = P.ceil( ( p.millis() - _countdownStartTime ) / 1000 );
		int countdownTime = _countdownSeconds - countdownSecondsElapsed;
		if( countdownTime > 0 ) {
			_gamePlay.updateCountdown( countdownTime );
			if( _lastCountdownTime != countdownTime ) {
				sounds.playSound( BlueBearSounds.COUNTDOWN );
			}
		} else {
			_gamePlay.updateCountdown( 0 );
			setGameState( GameState.GAME_PLAYING );
		}
		_lastCountdownTime = countdownTime;
	}

	protected void setGameStatePlaying() {
		_gamePlay.hideCountdown();
		_gamePlay.startGame();
		sounds.playGameplay();
		sounds.playSound( BlueBearSounds.GAMEPLAY_START );
	}

	protected void setGameStateFinishing() {
	}

	protected void runGameStatePlaying() {
		updateGameplay();
	}
	
	protected void setGameStateGameOver() {
		// set up tracking vars
		String winIndexes = "";
	    Date gameDate = new Date();
	    _trackingDateStr = gameDate.toString();

		// find high/low score
	    int highScore = 0;
	    int lowScore = 999999;
		// show overall game message
	    //		if( numWinners == 1 ) {
	    //			_gameMessages.showWinner();
	    //		} else {
	    //			_gameMessages.showTie();
	    //		}

		// set time to advance back to intro screen
		_gameOverTime = p.millis();
		_gameOverRecorded = false;

		// track gameplay!
		_tracking.trackGameResult(_trackingDateStr, 1, winIndexes, highScore, lowScore);
	}

	protected void runGameStateGameOver() {
		if( _gameState == GameState.GAME_OVER && p.millis() > _gameOverTime + _gamePlay.gameOverTime() ) {
			setGameState( GameState.GAME_OVER_OUTRO );
		}
		if( p.millis() > _gameOverTime + _gamePlay.gameOverTime() + 1000 ) {
			setGameState( GameState.GAME_INTRO );
		}
		// take a screenshot & kinect image
		if( p.kinectWrapper != null && p.kinectWrapper.isActive() ) {
			if( p.millis() > _gameOverTime + 1000 && _gameOverRecorded == false ) {
				_gameOverRecorded = true;
				_tracking.saveCameraImage( _trackingDateStr );
			}
		}
		updateGameplay();
	}
	
	protected void setGameStateGameOverOutro() {
		_gamePlay.animateToPostGameOverState();
	}
	
	// FRAME LOOP --------------------------------------------------------------------------------------
	
	public void drawApp() {
		p.background(0);
		timeFactor.update();
		
		// draw game to off-screen canvas
		pg.beginDraw();
		pg.background( _bgColor );
		pg.noStroke();
		runGameState();
		pg.endDraw();
		
		// draw canvas to screen
		p.image(pg, 0, 0, pg.width, pg.height);
	}
	
	protected void updateGameplay() {
		_kinectGrid.update();
		_gamePlay.update();
	}
	
}
