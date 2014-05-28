package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;
import org.ohheckyeah.games.tinkerbot.assets.TinkerBotSounds;
import org.ohheckyeah.games.tinkerbot.game.display.TinkerBotGameTimer;
import org.ohheckyeah.games.tinkerbot.game.display.TinkerBotScoreDisplay;
import org.ohheckyeah.games.tinkerbot.screens.TinkerBotPlayerDetectionScreen;
import org.ohheckyeah.shared.app.OHYBaseGame;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.hardware.kinect.KinectRegionGrid;
import com.haxademic.core.math.MathUtil;

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
	
	protected TinkerBotPlayerDetectionScreen _playerDetectBackground;
	protected TinkerBotBackground _background;
	protected TinkerBotPlayer[] _players;
	protected TinkerBotGameTimer _gameTimer;
	protected TinkerBotScoreDisplay _scoreDisplay;
	protected TinkerBotGameMessages _gameMessages;

	protected boolean _gameplayStarted = false;
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
		float playerSpacing = 1f / ( OHYBaseGame.NUM_PLAYERS + 1 ); // +2 from last index for spacing on the sides
		_players = new TinkerBotPlayer[OHYBaseGame.NUM_PLAYERS];
		for( int i=0; i < OHYBaseGame.NUM_PLAYERS; i++ ) _players[i] = new TinkerBotPlayer(_kinectGrid.getRegion(i), _isRemoteKinect, (float) (i+1) * playerSpacing );
		_scoreDisplay = new TinkerBotScoreDisplay();
		_gameTimer = new TinkerBotGameTimer( this, p.appConfig.getInt( "game_seconds", 30 ) );
		_gameMessages = new TinkerBotGameMessages();
	}
	
	public void reset() {
		_gameIsActive = false;
		_gameShouldEnd = false;
		
		_scoreDisplay.reset();
		_gameplayStarted = false;
		_gameMessages.hideWin();
	}
	
		
	// start/end game methods ------------
	public void startPlayerDetection() {
		_playerDetectBackground.show();
	}
	
	public void startGame() {
		_gameTimer.startTimer();
		_gameTimer.newLevel();
		_gameTimer.show();
		_scoreDisplay.show();
		_gameStartTime = p.millis();
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
	}
	
	public void animateToPostGameOverState() {
		_gameMessages.hideWin();
		_gameMessages.hideFail();
	}
	
	public void animateToPlayerDetection() {
		_gameMessages.showWaiting();
		for( TinkerBotPlayer player: _players ) player.startDetection();
	}
	
	public void update() {
		if( p.gameState() == GameState.GAME_WAITING_FOR_PLAYERS ) detectPlayers();
		if( p.gameState() == GameState.GAME_PLAYING ) {}
		checkPlayersLineup();
		drawGraphicsLayers();
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
	
	protected void updateGameStarted() {
		if( _gameplayStarted == false ) {
			_gameplayStarted = true;
		}
	}
		
	// gameplay logic ------------------------------------------------------------------
	public void newLevel() {
		// make sure we get a different goal position
		int oldPosition = _curGoalPosition;
		while( _curGoalPosition == oldPosition ) {
			_curGoalPosition = MathUtil.randRange( -TinkerBotPlayer.HALF_POSITIONS, TinkerBotPlayer.HALF_POSITIONS );
		}
		_gameMessages.hideWin();
		_gameMessages.hideFail();
	}
	
	public void win() {
		p.sounds.playSound(TinkerBotSounds.LASER);
		_gameMessages.showWin();
	}
	
	public void fail() {
		p.sounds.playSound(TinkerBotSounds.ERROR);
		_gameMessages.showFail();
	}
	
	public void checkPlayersLineup() {
		if( _gameTimer.isActiveControl() == false ) return;
		boolean isLinedUp = true;
		for( TinkerBotPlayer player: _players ) {
			if( player.position() != _curGoalPosition ) isLinedUp = false;
		}
		if( isLinedUp == true ) {
			_scoreDisplay.addScore();
			_gameTimer.lineUpWin();
		}
	}
	
	// draw graphics ------------------------------------------------------------------
	protected void drawGraphicsLayers() {
		_playerDetectBackground.update();
		if( p.gameState() != GameState.GAME_WAITING_FOR_PLAYERS && p.gameState() != GameState.GAME_INTRO_OUTRO ) _background.update();

		if( _gameTimer.isActiveControl() == true ) {
			DrawUtil.setDrawCenter(pg);
			pg.shape(p.gameGraphics.targetLine, pg.width / 2, TinkerBotPlayer.PLAYER_Y_CENTER + _curGoalPosition * TinkerBotPlayer.PLAYER_Y_INC );
		}
		
		for( TinkerBotPlayer player: _players ) player.update( _gameTimer.isActiveControl(), _gameTimer.isError() );
		_gameTimer.update();
		_scoreDisplay.update();
		_gameMessages.update();
	}
	
}