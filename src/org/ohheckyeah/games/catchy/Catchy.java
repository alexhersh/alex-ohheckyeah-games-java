package org.ohheckyeah.games.catchy;

import java.util.ArrayList;

import org.ohheckyeah.games.catchy.assets.CatchyColors;
import org.ohheckyeah.games.catchy.assets.CatchyGraphics;
import org.ohheckyeah.games.catchy.assets.CatchySounds;
import org.ohheckyeah.games.catchy.game.CatchyGameMessages;
import org.ohheckyeah.games.catchy.game.CatchyGamePlay;
import org.ohheckyeah.games.catchy.game.CatchyGameTimer;
import org.ohheckyeah.games.catchy.screens.CatchyIntroScreens;

import processing.core.PApplet;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.color.ColorGroup;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
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
		_isFullScreen = true;
		PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "org.ohheckyeah.games.catchy.Catchy" });
	}

	// input
	public static float KINECT_MIN_DIST = 1500;
	public static float KINECT_MAX_DIST = 2000;
	public static int KINECT_TOP = 0;
	public static int KINECT_BOTTOM = 480;

	// debug 
	protected boolean _isDebugging = false;
	
	// dimensions
	protected float _gameOrigHeight = 680.0f;
	public float gameScaleV = 1;
	protected int _gameWidth = 0;

	// graphics
	protected int _bgColor;
	public CatchyGraphics gameGraphics;
	
	// audio 
	public CatchySounds sounds; 
	
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
	public enum GameState {
		GAME_PLAYING,
		GAME_FINISHING,
		GAME_OVER,
		GAME_INTRO,
		GAME_WAITING_FOR_PLAYERS,
		GAME_COUNTDOWN,
		GAME_PRE_COUNTDOWN
	}
	protected GameState _gameState;
	protected GameState _gameStateQueued;	// wait until beginning on the next frame to switch states to avoid mid-frame conflicts

	
	// timers/timing
	public TimeFactoredFps timeFactor;
	public CatchyGameTimer gameTimer;
	protected int _gameOverTime = 0;
	protected int _preCountdownStartTime = 0;
	protected int _countdownStartTime = 0;
	protected int _countdownSeconds = 3;
	protected int _lastCountdownTime = 0;

	// non-gameplay screens
	protected CatchyIntroScreens _introScreens;
	protected CatchyGameMessages _gameMessages;
	
	
	
	public void setup() {
		_customPropsFile = FileUtil.getHaxademicDataPath() + "properties/catchy.properties";
		super.setup();
		initGame();
	}

	public void initGame() {
		p.smooth( OpenGLUtil.SMOOTH_MEDIUM );
		_bgColor = CatchyColors.STAGE_BG;

		timeFactor = new TimeFactoredFps( p, 50 );
		gameScaleV = p.height / _gameOrigHeight;
				
		loadMedia();
		
		_introScreens = new CatchyIntroScreens();
		_gameMessages = new CatchyGameMessages();
		
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
		_kinectGrid = new KinectRegionGrid(p, NUM_PLAYERS, 1, (int)KINECT_MIN_DIST, (int)KINECT_MAX_DIST, 250, (int)KINECT_TOP, (int)KINECT_BOTTOM);
		_gameWidth = P.ceil( p.width / (float) NUM_PLAYERS );
		_gamePlays = new ArrayList<CatchyGamePlay>();
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.add( new CatchyGamePlay( i, _gameWidth, _kinectGrid.getRegion(i) ) );
		}
	}
	
	protected void loadMedia() {
		sounds = new CatchySounds();
		
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
	public GameState gameState() { return _gameState; }
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
			setGameState( GameState.GAME_WAITING_FOR_PLAYERS );
		} else {
			setGameState( GameState.GAME_INTRO );
		}
	}
	
	public void setGameState( GameState state ) {
		P.println("next state: "+state);
		_gameStateQueued = state;
	}
	
	protected void swapGameState() {
		_gameState = _gameStateQueued;
		switch( _gameState ) {
			case GAME_INTRO : setGameStateIntro(); break;
			case GAME_WAITING_FOR_PLAYERS : setGameStateWaitingForPlayers(); break;
			case GAME_PRE_COUNTDOWN : setGameStatePreCountdown(); break;
			case GAME_COUNTDOWN : setGameStateCountdown(); break;
			case GAME_PLAYING : setGameStatePlaying(); break;
			case GAME_FINISHING : setGameStateFinishing(); break;
			case GAME_OVER : setGameStateGameOver(); break;
			default: break;
		}
	}
	
	protected void runGameState() {
		if( _gameState != _gameStateQueued ) swapGameState();
		
		switch( _gameState ) {
			case GAME_INTRO : runGameStateIntro(); break;
			case GAME_WAITING_FOR_PLAYERS : runGameStateWaitingForPlayers(); break;
			case GAME_PRE_COUNTDOWN : runGameStatePreCountdown(); break;
			case GAME_COUNTDOWN : runGameStateCountdown(); break;
			case GAME_PLAYING : 
			case GAME_FINISHING : runGameStatePlaying(); break;
			case GAME_OVER : runGameStateGameOver(); break;
			default: break;
		}
	}
	
	protected void setGameStateIntro() {
		_introScreens.reset();
		gameGraphics.shuffleCharacters();
		sounds.playIntro();
	}

	protected void runGameStateIntro() {
		DrawUtil.setDrawCorner(p);
		_introScreens.update();
		p.image( _introScreens.pg, 0, 0, _introScreens.pg.width, _introScreens.pg.height );
	}
	
	protected void setGameStateWaitingForPlayers() {
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.get( i ).reset();
			_gamePlays.get( i ).startPlayerDetection();
			_gamePlays.get( i ).animateToWinState();
		}
		_gameMessages.showWaiting();
		sounds.playWaiting();
	}

	protected void runGameStateWaitingForPlayers() {
		updateGameplays();
		// if we have all players, then start!
		boolean hasPlayers = true;
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			if( _gamePlays.get(i).hasPlayer() == false ) {
				hasPlayers = false;
			}
		}
		if( hasPlayers == true ) {
			setGameState( GameState.GAME_PRE_COUNTDOWN );
			_gameMessages.hideWaiting();
		}
	}
	
	protected void setGameStatePreCountdown() {
		_preCountdownStartTime = p.millis();
		_gameMessages.showCountdown();
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.get( i ).playersLockedIn();
		}
		sounds.playSound( CatchySounds.PLAYERS_DETECTED );
		sounds.stopSoundtrack();
	}

	protected void runGameStatePreCountdown() {
		updateGameplays();
		if( p.millis() > _preCountdownStartTime + 2500 ) {
			setGameState( GameState.GAME_COUNTDOWN );
		}
	}

	protected void setGameStateCountdown() {
		_countdownStartTime = p.millis();
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.get( i ).showCountdown( _countdownSeconds );
			_gamePlays.get( i ).animateToGameState();
		}
		gameTimer.show();
	}

	protected void runGameStateCountdown() {
		updateGameplays();
		int countdownSecondsElapsed = P.ceil( ( p.millis() - _countdownStartTime ) / 1000 );
		int countdownTime = _countdownSeconds - countdownSecondsElapsed;
		if( countdownTime > 0 ) {
			for( int i=0; i < NUM_PLAYERS; i++ ) {
				_gamePlays.get(i).updateCountdown( countdownTime );
			}
		} else {
			_gameMessages.hideCountdown();
			setGameState( GameState.GAME_PLAYING );
		}
		if( _lastCountdownTime != countdownTime ) {
			sounds.playSound( CatchySounds.COUNTDOWN );
		}
		_lastCountdownTime = countdownTime;
	}

	protected void setGameStatePlaying() {
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.get( i ).hideCountdown();
			_gamePlays.get( i ).startGame();
		}
		gameTimer.startTimer();
		sounds.playGameplay();
	}

	protected void setGameStateFinishing() {
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.get( i ).stopDropping();
		}
	}

	protected void runGameStatePlaying() {
		gameTimer.update();
		updateGameplays();
	}
	
	protected void setGameStateGameOver() {
		// find high scores & set winners
		int highScore = 0;
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			if( _gamePlays.get( i ).getScore() > highScore ) {
				highScore = _gamePlays.get( i ).getScore();
			}
		}
		int numWinners = 0;
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			if( _gamePlays.get( i ).getScore() == highScore ) {
				numWinners++;
				_gamePlays.get( i ).gameOver( true );
				_gameMessages.setWinnerX( _gameWidth * i + _gameWidth / 2f );
			} else {
				_gamePlays.get( i ).gameOver( false );
			}
		}
		if( numWinners == 1 ) {
			_gameMessages.showWinner();
		} else {
			_gameMessages.showTie();
		}
		
		// update game shell 
		gameTimer.hide();

		// set time to advance back to intro screen
		_gameOverTime = p.millis();
		
		sounds.playWin();
	}

	protected void runGameStateGameOver() {
		updateGameplays();
		if( p.millis() > _gameOverTime + 4000 ) {
			_gameMessages.hideWinner();
			_gameMessages.hideTie();
			setGameState( GameState.GAME_INTRO );
		}
	}
	
	
	// FRAME LOOP --------------------------------------------------------------------------------------
	
	public void drawApp() {
		p.background(_bgColor);

		// update timing
		timeFactor.update();
		// P.println("target_fps: "+timeFactor.targetFps()+" / actual_fps: "+timeFactor.actualFps()+" / timeFactor: "+timeFactor.multiplier());
		
		runGameState();
	}
	
	protected void updateGameplays() {
		_kinectGrid.update();

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

		// draw in-game messages
		_gameMessages.update();
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
//			if( _gamePlays.get( i ).hasClearedBoard() == true && _gameState == GameState.GAME_ON ) {
//				setGameState( GameState.GAME_OVER );
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
			if( _gameState == GameState.GAME_WAITING_FOR_PLAYERS ) {
				setGameState( GameState.GAME_COUNTDOWN );
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
