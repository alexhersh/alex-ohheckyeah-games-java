package org.ohheckyeah.games.catchy;

import java.util.ArrayList;
import java.util.Date;

import org.ohheckyeah.games.catchy.assets.CatchyColors;
import org.ohheckyeah.games.catchy.assets.CatchyGraphics;
import org.ohheckyeah.games.catchy.assets.CatchySounds;
import org.ohheckyeah.games.catchy.game.CatchyGameMessages;
import org.ohheckyeah.games.catchy.game.CatchyGamePlay;
import org.ohheckyeah.games.catchy.game.CatchyGameTimer;
import org.ohheckyeah.games.catchy.game.CatchyTracking;
import org.ohheckyeah.games.catchy.screens.CatchyTitleScreen;
import org.ohheckyeah.shared.app.OHYBaseGame;
import org.ohheckyeah.shared.app.OHYBaseRemoteControl;
import org.ohheckyeah.shared.screens.OHYIntroScreens;

import processing.core.PApplet;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.system.TimeFactoredFps;

public class Catchy
extends OHYBaseGame  
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
		// _isFullScreen = true;
		_hasChrome = false;
		boolean isSecondScreen = false;
		if( isSecondScreen ) {
			PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "--location=-1280,0", "--display=1", "org.ohheckyeah.games.catchy.Catchy" });		// requires hiding os x 2nd screen menu bar: http://www.cultofmac.com/255910/hide-the-menu-bar-on-your-secondary-monitor-with-mavericks-os-x-tips/
		} else {
			PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "org.ohheckyeah.games.catchy.Catchy" });
		}
	}

	protected int _gameWidth = 0;

	// graphics
	public CatchyGraphics gameGraphics;
	protected EasingFloat _dividerYOffset = new EasingFloat(0, 5);

	// audio 
	public CatchySounds sounds; 
	
	// game state
	protected ArrayList<CatchyGamePlay> _gamePlays;

	protected CatchyTracking _tracking;
	protected String _trackingDateStr;
	
	// timers/timing
	public TimeFactoredFps timeFactor;
	public CatchyGameTimer gameTimer;
	protected int _gameOverTime = 0;
	protected boolean _gameOverRecorded = false;
	protected int _preCountdownStartTime = 0;
	protected int _countdownStartTime = 0;
	protected int _countdownSeconds = 3;
	protected int _lastCountdownTime = 0;

	// non-gameplay screens
	protected CatchyGameMessages _gameMessages;
	
	
	public void setup() {
		super.setup( "catchy.properties", 680, 1536 );
		initGame();
	}

	public void initGame() {
		timeFactor = new TimeFactoredFps( p, 50 );
		loadMedia();
		
		// set flags and props	
		buildGameplays();
		buildGameTimer();
		_tracking = new CatchyTracking();
		
		_introScreens = new OHYIntroScreens( new CatchyTitleScreen() );
		_gameMessages = new CatchyGameMessages();
		
		setInitialGameState();
	}
	
	protected void buildGameTimer() {
		gameTimer = new CatchyGameTimer( _appConfig.getInt( "game_seconds", 30 ) );
		gameTimer.startTimer();
	}
	
	protected void buildGameplays() {
		_gameWidth = P.ceil( pg.width / (float) NUM_PLAYERS );
		_gamePlays = new ArrayList<CatchyGamePlay>();
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.add( new CatchyGamePlay( i, _gameWidth, _kinectGrid.getRegion(i), _isRemoteKinect ) );
		}
	}
	
	protected void loadMedia() {
		ohySounds = sounds = new CatchySounds();
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
	public boolean isDebugging() { return _isDebugging; }
	
	public boolean isLastGameplay( CatchyGamePlay gameplay ) {
		if( gameplay == _gamePlays.get( _gamePlays.size() - 1) && _gamePlays.size() == 2 ) {
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
		gameGraphics.shuffleCharacters();
		_dividerYOffset.setCurrent(pg.height);
		_dividerYOffset.setTarget(pg.height);
	}

	protected void runGameStateIntro() {
		if( _gameState == GameState.GAME_INTRO_OUTRO ) {
			updateGameplays();
		}
		DrawUtil.setDrawCorner(pg);
		_introScreens.update();
		pg.image( _introScreens.canvas, 0, 0, _introScreens.canvas.width, _introScreens.canvas.height );
	}

	protected void setGameStateIntroOutro() {
		_dividerYOffset.setTarget( 0 );
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
		}
	}
	
	protected void setGameStatePreCountdown() {
		_preCountdownStartTime = p.millis();
		_gameMessages.hideWaiting();
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
		// set up tracking vars
		String winIndexes = "";
	    Date gameDate = new Date();
	    _trackingDateStr = gameDate.toString();

		// find high/low score
	    int highScore = 0;
	    int lowScore = 999999;
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			if( _gamePlays.get( i ).getScore() > highScore ) {
				highScore = _gamePlays.get( i ).getScore();
			}
			if( _gamePlays.get( i ).getScore() < lowScore ) {
				lowScore = _gamePlays.get( i ).getScore();
			}
		}
		// set winner gameplays
		int numWinners = 0;
		boolean isWinner = false;
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			if( _gamePlays.get( i ).getScore() == highScore ) {
				numWinners++;
				isWinner = true;
				_gamePlays.get( i ).gameOver( true );
				_gameMessages.setWinnerX( _gameWidth * i + _gameWidth / 2f );
				winIndexes += ( winIndexes.length() == 0 ) ? i : "|"+i;
			} else {
				_gamePlays.get( i ).gameOver( false );
				isWinner = false;
			}
			_tracking.trackPlayerResult(_trackingDateStr, i, _gamePlays.get( i ).getScore(), isWinner, _gamePlays.get( i ).getCharacterName() );
		}
		// show overall game message
		if( numWinners == 1 ) {
			_gameMessages.showWinner();
		} else {
			_gameMessages.showTie();
		}
		
		// update game shell 
		gameTimer.hide();
		sounds.playWin();

		// set time to advance back to intro screen
		_gameOverTime = p.millis();
		_gameOverRecorded = false;

		// track gameplay!
		_tracking.trackGameResult(_trackingDateStr, _gamePlays.size(), winIndexes, highScore, lowScore);
	}

	protected void runGameStateGameOver() {
		updateGameplays();
		if( _gameState == GameState.GAME_OVER && p.millis() > _gameOverTime + 4000 ) {
			setGameState( GameState.GAME_OVER_OUTRO );
		}
		if( p.millis() > _gameOverTime + 5000 ) {
			setGameState( GameState.GAME_INTRO );
		}
		// take a screenshot & kinect image
		if( p.millis() > _gameOverTime + 1000 && _gameOverRecorded == false ) {
			_gameOverRecorded = true;
			if( p.kinectWrapper != null && p.kinectWrapper.isActive() ) {
				_tracking.saveCameraImage( _trackingDateStr );
			} else {
				_udp.send( OHYBaseRemoteControl.ACTION_TAKE_PHOTO + _trackingDateStr, _senderIp, _senderPort );	// send the  photo message back to the remote
			}
		}
	}
	
	protected void setGameStateGameOverOutro() {
		_gameMessages.hideWinner();
		_gameMessages.hideTie();
		_dividerYOffset.setTarget( -pg.height * 2f );
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			_gamePlays.get( i ).animateToHiddenState();
		}
	}
	
	// FRAME LOOP --------------------------------------------------------------------------------------
	
	public void drawApp() {
		super.drawApp();
		timeFactor.update();
		p.background( CatchyColors.STAGE_BG );
		DrawUtil.setDrawCorner(p);
		pg.beginDraw();
		pg.background( CatchyColors.STAGE_BG );
		pg.noStroke();
		runGameState();
		pg.endDraw();
		p.image(pg, 0, 0, pg.width, pg.height);
	}
	
	protected void updateGameplays() {
		// update and draw gamePlays
		DrawUtil.setDrawCorner(pg);
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			CatchyGamePlay gamePlay = _gamePlays.get(i); 
			gamePlay.update();
			pg.image( gamePlay.canvas, _gameWidth * i, 0, _gameWidth, pg.height);
		}
		
		drawGameDividersAndTimers();
		_gameMessages.update();
	}
	
	protected void drawGameDividersAndTimers() {
		// draw dividers & timers
		_dividerYOffset.update();
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			if(i > 0) {
				// draw white borders
				DrawUtil.setDrawCorner(pg);
				pg.fill(255);
				pg.noStroke();
				float dividerX = _gameWidth * i - gameGraphics.gameDivider.width * 0.5f;
				float dividerH = ( gameScaleV > 1 ) ? scaleV( gameGraphics.gameDivider.height ) : gameGraphics.gameDivider.height;
				pg.shape( gameGraphics.gameDivider, dividerX, _dividerYOffset.value(), gameGraphics.gameDivider.width, dividerH );
				// draw timers
				DrawUtil.setDrawCenter(pg);
				pg.pushMatrix();
				pg.translate( dividerX, pg.height - ( scaleV(gameGraphics.timerBanner.height) ) / 1.7f );
				gameTimer.drawTimer();
				pg.popMatrix();
			}
			if(i == 0 && NUM_PLAYERS == 1) {
				// draw timers
				DrawUtil.setDrawCenter(pg);
				pg.pushMatrix();
				pg.translate( pg.width - scaleV(100), 30 );
				gameTimer.drawTimer();
				pg.popMatrix();
			}
		}
	}
	
}
