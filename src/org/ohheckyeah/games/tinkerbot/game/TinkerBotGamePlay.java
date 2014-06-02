package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;
import org.ohheckyeah.games.tinkerbot.assets.TinkerBotSounds;
import org.ohheckyeah.games.tinkerbot.game.display.TinkerBotGameTimer;
import org.ohheckyeah.games.tinkerbot.game.display.TinkerBotLevelTimer;
import org.ohheckyeah.games.tinkerbot.game.display.TinkerBotScoreDisplay;
import org.ohheckyeah.games.tinkerbot.screens.TinkerBotPlayerDetectionScreen;
import org.ohheckyeah.shared.app.OHYBaseGame;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.hardware.kinect.KinectRegionGrid;

public class TinkerBotGamePlay {
	
	protected TinkerBot p;
	protected PGraphics pg;

	protected KinectRegionGrid _kinectGrid;
	protected boolean _gameIsActive = false;
	protected boolean _gameShouldEnd = false;
	
	protected boolean _isRemoteKinect;
	protected int _playersDetectedTime = 0;
	protected int _countdownTime = 0;
	protected boolean _countdownShowing = false;
	protected int _gameStartTime = 0;
	protected boolean _controlsActive = false;
	protected boolean _isError = false;
	
	protected TinkerBotPlayerDetectionScreen _playerDetectBackground;
	protected TinkerBotBackground _background;
	protected TinkerBotRobots _robots;
	protected TinkerBotPlayer[] _players;
	protected TinkerBotGameTimer _gameTimer;
	protected TinkerBotLevelTimer _levelTimer;
	protected TinkerBotScoreDisplay _scoreDisplay;
	protected TinkerBotGameMessages _gameMessages;
	protected TinkerBotDashedLine _dashedLine;

	protected int _curGoalPosition = 999;

	
	public TinkerBotGamePlay( KinectRegionGrid kinectGrid, boolean isRemoteKinect ) {
		p = (TinkerBot) P.p;
		pg = p.pg;
		
		_kinectGrid = kinectGrid;
		_isRemoteKinect = isRemoteKinect;
		
		buildGraphicsLayers();
		
		reset();
	}
	
	protected void buildGraphicsLayers() {
		_background = new TinkerBotBackground();
		_playerDetectBackground = new TinkerBotPlayerDetectionScreen();
		float detectionSpacing = 1f / ( OHYBaseGame.NUM_PLAYERS + 1 ); // +2 from last index for spacing on the sides
		float playerSpacing = 1f / ( OHYBaseGame.NUM_PLAYERS + 3 ); // +4 from last index for spacing on the sides
		_players = new TinkerBotPlayer[OHYBaseGame.NUM_PLAYERS];
		for( int i=0; i < OHYBaseGame.NUM_PLAYERS; i++ ) _players[i] = new TinkerBotPlayer(_kinectGrid.getRegion(i), _isRemoteKinect, (float) (i+1) * detectionSpacing, (float) (i+2) * playerSpacing );
		_robots = new TinkerBotRobots();
		_scoreDisplay = new TinkerBotScoreDisplay();
		_gameTimer = new TinkerBotGameTimer( this, p.appConfig.getInt( "game_seconds", 30 ) );
		_levelTimer = new TinkerBotLevelTimer();
		_gameMessages = new TinkerBotGameMessages();
		_dashedLine = new TinkerBotDashedLine();
	}
	
	public void reset() {
		_gameIsActive = false;
		_gameShouldEnd = false;
		
		_scoreDisplay.reset();
		_gameMessages.hideWin();
		_gameMessages.hideFail();
	}
	
		
	// start/end game methods ------------
	public void startPlayerDetection() {
		_playerDetectBackground.show();
		_controlsActive = true;
		_isError = false;
	}
	
	public void startGame() {
		_levelTimer.startTimer();
		_levelTimer.show();
		_gameTimer.startTimer();
		newLevel();
		_gameTimer.show();
		_scoreDisplay.show();
		_gameStartTime = p.millis();
		_isError = false;
	}
	
	public void gameOver() {
		p.setGameState( GameState.GAME_OVER );
	}
	
	// handle countdown timer ------------
	public void playersLockedIn() {
		_gameIsActive = true;
		_playerDetectBackground.hide();
		for( TinkerBotPlayer player: _players ) player.prepareForGameplay();
		_gameMessages.hideWaiting();
		_gameMessages.showCountdown();
		_background.startUnmasking();
	}
	
	public void showCountdown( int countdownTime ) {
		_countdownTime = countdownTime;
	}
	
	public void hideCountdown() {
	}
	
	public void updateCountdown( int countdownTime ) {
		_countdownTime = countdownTime;
	}
	
	// handle game states ----------------
	public void animateToGameState() {
		_gameMessages.hideCountdown();
		_robots.show();
	}
	
	public void animateToGameOverState() {
		_levelTimer.hide();
		_scoreDisplay.hide();
		_gameTimer.hide();
		for( TinkerBotPlayer player: _players ) player.gameOver();
		_robots.hide();
		_dashedLine.hide();
	}
	
	public void animateToPostGameOverState() {
	}
	
	public void animateToPlayerDetection() {
		_gameMessages.showWaiting();
		for( TinkerBotPlayer player: _players ) player.startDetection();
	}
	
	public void update() {
		if( p.gameState() == GameState.GAME_WAITING_FOR_PLAYERS ) detectPlayers();
		if( p.gameState() == GameState.GAME_PLAYING ) {}
		if( _robots.readyForNewLevel() == true ) newLevel();
		checkPlayersLineup();
		drawGraphicsLayers();
		extraWinFailResponse();
		if( _gameShouldEnd == true ) {
			gameOver();
			_gameShouldEnd = false;
		}
	}
	
	public void detectPlayers() {
		boolean hasPlayers = true;
		for( int i=0; i < TinkerBot.NUM_PLAYERS; i++ ) {
			if( _players[i].detectedPlayer() == false ) hasPlayers = false;
		}
		if( hasPlayers == true ) {
			if( _playersDetectedTime == 0 ) _playersDetectedTime = p.millis();
			if( p.millis() > _playersDetectedTime + 2000 ) {
				_playersDetectedTime = 0;
				p.setGameState( GameState.GAME_PRE_COUNTDOWN );
			}
		} else {
			_playersDetectedTime = 0;
		}
	}
	
	// gameplay logic ------------------------------------------------------------------
	public void newLevel() {
		// make sure we get a different goal position
		_curGoalPosition =  TinkerBotLayout.randomPosition( _curGoalPosition );
		_gameTimer.newLevel();
		_levelTimer.startTimer();
		_background.levelStart();
		_robots.levelStart();
		_isError = false;
		_controlsActive = true;
		_dashedLine.show();
	}
	
	public void win() {
		if( _controlsActive == true ) {
			P.println("== win");
			_controlsActive = false;
			_levelTimer.reset();
			_background.levelEnd();
			_robots.shootBeam( _curGoalPosition, pg.width, 0 );
			_scoreDisplay.addScore();
			_dashedLine.hide();
		}
	}
	
	public void fail() {
		if( _controlsActive == true ) {
			P.println("== fail");
			_controlsActive = false;
			_levelTimer.reset();
			_background.levelEnd();
			_robots.shootBeam( _curGoalPosition, getLeftmostPlayerError(), getRightmostPlayerError() );
			_isError = true;
			_dashedLine.hide();
		}
	}
	
	protected float getLeftmostPlayerError() {
		for( int i=0; i < TinkerBot.NUM_PLAYERS; i++ ) {
			if( _players[i].position() != _curGoalPosition ) return _players[i].playerX();
		}
		return _players[TinkerBot.NUM_PLAYERS - 1].playerX();
	}
	
	protected float getRightmostPlayerError() {
		for( int i=TinkerBot.NUM_PLAYERS - 1; i >= 0; i-- ) {
			if( _players[i].position() != _curGoalPosition ) return _players[i].playerX();
		}
		return _players[0].playerX();
	}
	
	public void checkPlayersLineup() {
		if( _controlsActive == false ) return;
		boolean isLinedUp = true;
		for( TinkerBotPlayer player: _players ) {
			if( player.position() != _curGoalPosition ) isLinedUp = false;
		}
		if( isLinedUp == true ) {
			win();
		}
	}
	
	protected void extraWinFailResponse() {
		if( _robots.isBeaming() == true ) {
			if( _isError ) {
				p.sounds.playSound(TinkerBotSounds.ERROR);
				_gameMessages.showFail();
			} else {
				p.sounds.playSound(TinkerBotSounds.LASER);
				_gameMessages.showWin();
			}
		}
	}
	
	// draw graphics ------------------------------------------------------------------
	protected void drawGraphicsLayers() {
		_playerDetectBackground.update();
		if( p.gameState() != GameState.GAME_WAITING_FOR_PLAYERS && p.gameState() != GameState.GAME_INTRO_OUTRO ) _background.update();

		_dashedLine.update( _curGoalPosition );
		_robots.update();
		for( TinkerBotPlayer player: _players ) player.update( _controlsActive, _isError && _robots.isErrorShowable() );
		_levelTimer.update();
		_gameTimer.update();
		_scoreDisplay.update();
		_gameMessages.update();
	}
	
}