package org.ohheckyeah.games.graduationday.game;

import org.ohheckyeah.games.graduationday.GraduationDay;
import org.ohheckyeah.games.graduationday.screens.GraduationDayPlayerDetectionScreen;
import org.ohheckyeah.shared.app.OHYBaseGame;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.hardware.joystick.IJoystickCollection;

public class GraduationDayGamePlay {
	
	protected GraduationDay p;
	protected PGraphics pg;

	protected IJoystickCollection _joysticksGrid;
	protected boolean _gameIsActive = false;
	
	protected int _playersDetectedTime = 0;
	protected int _countdownTime = 0;
	protected boolean _countdownShowing = false;
	protected int _gameStartTime = 0;
	protected boolean _controlsActive = false;
	
	protected GraduationDayPlayerDetectionScreen _playerDetectBackground;
	protected GraduationDayBackground _background;
	protected GraduationDayFinale _finale;
	protected GraduationDayPlayer[] _players;
	protected GraduationDayGameMessages _gameMessages;

	protected int _curGoalPosition = 999;

	
	public GraduationDayGamePlay(IJoystickCollection kinectGrid) {
		p = (GraduationDay) P.p;
		pg = p.pg;
		
		_joysticksGrid = kinectGrid;
		
		buildGraphicsLayers();
		
		reset();
	}
	
	protected void buildGraphicsLayers() {
		_background = new GraduationDayBackground();
		_finale = new GraduationDayFinale();
		_playerDetectBackground = new GraduationDayPlayerDetectionScreen();
		float detectionSpacing = 1f / ( OHYBaseGame.NUM_PLAYERS + 1 ); // +2 from last index for spacing on the sides
		float playerSpacing = 1f / ( OHYBaseGame.NUM_PLAYERS + 3 ); // +4 from last index for spacing on the sides
		_players = new GraduationDayPlayer[OHYBaseGame.NUM_PLAYERS];
		for( int i=0; i < OHYBaseGame.NUM_PLAYERS; i++ ) _players[i] = new GraduationDayPlayer(_joysticksGrid.getRegion(i), (float) (i+1) * detectionSpacing, (float) (i+2) * playerSpacing );
		_gameMessages = new GraduationDayGameMessages();
	}
	
	public void reset() {
		_gameIsActive = false;
		
		_gameMessages.hideWin();
		_gameMessages.hideFail();
	}
	
	// getters for tracking
	public int score() {
		return 0;
	}
	
	// start/end game methods ------------
	public void startPlayerDetection() {
		_playerDetectBackground.show();
		_controlsActive = true;
	}
	
	public void startGame() {
		newLevel();
		_gameStartTime = p.millis();
	}
	
	// handle countdown timer ------------
	public void playersLockedIn() {
		_gameIsActive = true;
		_playerDetectBackground.hide();
		for( GraduationDayPlayer player: _players ) player.prepareForGameplay();
		_gameMessages.hideWaiting();
		_gameMessages.showCountdown();
		_background.showIntro();
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
	
	public void animateToGameOverState() {
		for( GraduationDayPlayer player: _players ) player.gameOver();
		_gameMessages.showGameOver();
	}
	
	public void animateToPostGameOverState() {
		_gameMessages.hideGameOver();
	}
	
	public void animateToPlayerDetection() {
		_gameMessages.showWaiting();
		for( GraduationDayPlayer player: _players ) player.startDetection();
	}
	
	public void update() {
		if( p.gameState() == GameState.GAME_WAITING_FOR_PLAYERS ) detectPlayers();
		if( p.gameState() == GameState.GAME_PLAYING ) {			
			checkPlayersLineup();
			checkGameOver();
		}
		drawGraphicsLayers();
	}
	
	public void detectPlayers() {
		boolean hasPlayers = true;
		for( int i=0; i < GraduationDay.NUM_PLAYERS; i++ ) {
			if( _players[i].detectedPlayer() == false ) {
				hasPlayers = false;
			}
		}
		if( hasPlayers == true ) {
			if( _playersDetectedTime == 0 ) _playersDetectedTime = p.millis();
			if( p.millis() > _playersDetectedTime + 1500 ) {
				_playersDetectedTime = 0;
				p.setGameState( GameState.GAME_PRE_COUNTDOWN );
				p.sounds.stopSoundtrack();
			}
		} else {
			_playersDetectedTime = 0;
		}
	}
	
	// gameplay logic ------------------------------------------------------------------
	public void newLevel() {
		_background.gameStart();
		_controlsActive = true;
	}
	
	public void win() {
		if( _controlsActive == true ) {
			_controlsActive = false;
			_background.gameEnd();
		}
	}
	
	public void fail() {
		if( _controlsActive == true ) {
			_controlsActive = false;
			_background.gameEnd();
		}
	}
	
	protected float getLeftmostPlayerError() {
		for( int i=0; i < GraduationDay.NUM_PLAYERS; i++ ) {
			if( _players[i].position() != _curGoalPosition ) return _players[i].playerX();
		}
		return _players[GraduationDay.NUM_PLAYERS - 1].playerX();
	}
	
	protected float getRightmostPlayerError() {
		for( int i=GraduationDay.NUM_PLAYERS - 1; i >= 0; i-- ) {
			if( _players[i].position() != _curGoalPosition ) return _players[i].playerX();
		}
		return _players[0].playerX();
	}
	
	public void checkPlayersLineup() {
		if( _controlsActive == false ) return;
		boolean isLinedUp = true;
		for( GraduationDayPlayer player: _players ) {
			if( player.position() != _curGoalPosition ) isLinedUp = false;
		}
		if( isLinedUp == true ) {
			win();
		}
	}
	
	protected void checkGameOver() {
		if( p.gameState() == GameState.GAME_PLAYING ) {
//			if( _gameTimer.isGameOver() == true && _robots.isAnimating() == false ) {
//				p.setGameState( GameState.GAME_OVER );
//				p.sounds.playGameOver();
//			}
		}
	}
	
	// draw graphics ------------------------------------------------------------------
	protected void drawGraphicsLayers() {
		_playerDetectBackground.update();
		if( p.gameState() != GameState.GAME_WAITING_FOR_PLAYERS && p.gameState() != GameState.GAME_INTRO_OUTRO ) {
			_background.update();
			_finale.update(); // we'll move this later once we have a game over state
		}

		for( GraduationDayPlayer player: _players ) player.update( _controlsActive );
		_gameMessages.update();
	}
	
}