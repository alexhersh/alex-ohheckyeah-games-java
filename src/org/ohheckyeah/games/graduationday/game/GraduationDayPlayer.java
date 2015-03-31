package org.ohheckyeah.games.graduationday.game;

import org.ohheckyeah.games.graduationday.GraduationDay;
import org.ohheckyeah.games.graduationday.assets.GraduationDaySounds;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;
import org.ohheckyeah.shared.app.OHYBaseGame.PlayerDetectedState;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.hardware.joystick.IJoystickControl;
import com.haxademic.core.math.easing.EasingFloat;


public class GraduationDayPlayer {

	protected GraduationDay p;
	protected PGraphics pg;

	protected IJoystickControl _joystick;

	protected GraduationDayWaitingSpinner _waitingSpinner;
	protected PShape _detectionSvg;
	protected int _playerDetectionX = 0;
	protected int _playerX = 0;
	protected EasingFloat _playerY = new EasingFloat(0,4);
	protected EasingFloat _playerYBuildOffset = new EasingFloat(0,7);
	protected int KINECT_PIXEL_DETECT_THRESH = 10;
	protected PlayerDetectedState _playerDetectedState;

	protected boolean _controlsActive = false;
	protected boolean _hasPlayer = false;
	protected boolean _detectedPlayer = false;
	protected int _detectedPlayerTime = 0;
	protected int _autoControlTime = 0;
	protected int _position = 0;
	
	protected int _barOverlayIndexTop = 0;
	protected int _barOverlayIndexBot = 0;
	protected boolean _wasError = false;
	protected float _errorRotation = 0;


	public GraduationDayPlayer( IJoystickControl joystick, float xPositionDetection, float xPosition ) {
		p = (GraduationDay)P.p;
		pg = p.pg;

		_joystick = joystick;

		_playerDetectionX = P.round( pg.width * xPositionDetection );
		_waitingSpinner = new GraduationDayWaitingSpinner( _playerDetectionX );
		_playerX = P.round( pg.width * xPosition );
	}
	
	public boolean detectedPlayer() {
		return (_playerDetectedState == PlayerDetectedState.PLAYER_LOCKED);
	}
	
	public boolean hasPlayer() {
		return _hasPlayer;
	}
	
	public int position() {
		return _position;
	}
	
	public int playerX() {
		return _playerX;
	}
	
	public void resetDetection() {
		_detectedPlayer = false;
		_hasPlayer = false;
	}

	public void prepareForGameplay() {
		_waitingSpinner.hide();
		// send the parts in via offset animation
		_playerYBuildOffset.setCurrent(pg.height);
		_playerYBuildOffset.setTarget(0);
	}
	
	public void startGameplay() {
	}
	
	public void startDetection() {
		resetDetection();
		_playerDetectedState = null;
		_waitingSpinner.show();
	}

	public void gameOver() {
		_playerYBuildOffset.setTarget(pg.height);
	}

	protected PlayerDetectedState detectPlayerCurState() {
		PlayerDetectedState curState = null;
		if( p.gameState() != GameState.GAME_WAITING_FOR_PLAYERS ) return curState; 
		if( _detectedPlayer == false ) {
			if( _joystick.isActive() == true ) {
				_detectedPlayerTime = p.millis();
				_detectedPlayer = true;
				curState = PlayerDetectedState.PLAYER_DETECTED;
			}
		} else {
			if( _joystick.isActive() == false ) {
				_detectedPlayer = false;
				_hasPlayer = false;
				curState = PlayerDetectedState.PLAYER_LOST;
			}
		}
		if( _detectedPlayer == true && p.millis() - _detectedPlayerTime > 2000 ) {
			if( _hasPlayer == false ) {
				_hasPlayer = true;
				curState = PlayerDetectedState.PLAYER_LOCKED;
			}
		}
		return curState;
	}

	public void detectPlayer( PlayerDetectedState updatedState ) {
		if( updatedState != null && _playerDetectedState != updatedState ) {
			switch ( updatedState ) {
				case PLAYER_DETECTED:
					_waitingSpinner.playerEntered();
					p.sounds.playSound( GraduationDaySounds.STEP_IN );
					break;
				case PLAYER_LOST:
					_waitingSpinner.playerLeft();
					p.sounds.playSound( GraduationDaySounds.STEP_OUT );
					break;
				case PLAYER_LOCKED:
					_waitingSpinner.playerDetected();
					p.sounds.playSound( GraduationDaySounds.PLAYER_LOCKED );
					break;
				default:
					break;
			}
			_playerDetectedState = updatedState;
		}
	}
	
	public void update( boolean shouldUpdateControls ) {
		if( p.gameState() == GameState.GAME_WAITING_FOR_PLAYERS || p.gameState() == GameState.GAME_PRE_COUNTDOWN ) {
			detectPlayer( detectPlayerCurState() );
			_waitingSpinner.update();
		} else if( p.gameState() != GameState.GAME_WAITING_FOR_PLAYERS && p.gameState() != GameState.GAME_INTRO_OUTRO && p.gameState() != GameState.GAME_INTRO ) {
			if( shouldUpdateControls ) updateControls();
			updateGameplay( false );
		}
	}
	
	public void updateControls() {
		if(_joystick.isActive() == true) {
			float controlZ = _joystick.controlZ();
			if( (p.kinectWrapper != null && p.kinectWrapper.isMirrored() == false) || p.leapMotion != null ) controlZ = _joystick.controlZ() * -1f;
			// _position = P.round( NUM_POSITIONS * MathUtil.getPercentWithinRange( -0.5f, 0.5f, controlZ ) );
			// turn -1, 1 into number of positions int
//			_position = P.round( P.map( controlZ, -1f, 1f, -GraduationDayLayout.HALF_POSITIONS * 0.1f, GraduationDayLayout.HALF_POSITIONS * 0.1f ) * 10 );
//			_playerY.setTarget( GraduationDayLayout.yForPosition( _position ) );
		}
	}
	
	protected void updateGameplay( boolean isError ) {
		// update easing
		_playerY.update();
	}
}
