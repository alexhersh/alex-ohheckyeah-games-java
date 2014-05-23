package org.ohheckyeah.games.bluebear;

import java.util.Date;

import org.ohheckyeah.games.bluebear.assets.BlueBearColors;
import org.ohheckyeah.games.bluebear.assets.BlueBearGraphics;
import org.ohheckyeah.games.bluebear.assets.BlueBearSounds;
import org.ohheckyeah.games.bluebear.game.BlueBearGamePlay;
import org.ohheckyeah.games.bluebear.game.BlueBearTracking;
import org.ohheckyeah.games.bluebear.screens.BlueBearIntroScreens;
import org.ohheckyeah.shared.OHYBaseGame;

import processing.core.PApplet;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.hardware.kinect.KinectRegionGrid;
import com.haxademic.core.system.TimeFactoredFps;

public class BlueBear
extends OHYBaseGame  
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

	// dimensions
	protected float _gameOrigHeight = 630.0f;

	// graphics
	public BlueBearGraphics gameGraphics;

	// audio 
	public BlueBearSounds sounds; 


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
		super.setup( "bluebear.properties", 630 );
		initGame();
	}

	public void initGame() {
		buildCanvas();
		
		timeFactor = new TimeFactoredFps( p, 50 );
				
		loadMedia();
		
		_introScreens = new BlueBearIntroScreens( _appConfig.getString( "sponsor_images", null ) );
		
		// set flags and props	
		buildGameplay();
		setInitialGameState();
		_tracking = new BlueBearTracking();
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
		updateGameplay();
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
		pg.background( BlueBearColors.STAGE_BG );
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
