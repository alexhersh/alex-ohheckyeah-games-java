package org.ohheckyeah.games.bluebear.game.characters;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.BlueBear.GameState;
import org.ohheckyeah.games.bluebear.assets.BlueBearSounds;
import org.ohheckyeah.games.bluebear.game.characters.BlueBearPlayerControls.PlayerDetectedState;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.math.easing.EasingFloat3d;


public class BlueBearBasePlayer {

	protected BlueBear p;
	protected PGraphics pg;

	protected BlueBearPlayerControls _playerControls;
	protected BlueBearWaitingSpinner _waitingSpinner;
	protected PShape _detectionSvg;
	protected float _detectShadowYPercent = 0.89f;
	protected int _waitingSpinnerX = 0;
	protected PlayerDetectedState _playerDetectedState;

	protected int _lane = 0;
	protected EasingFloat _laneScale = new EasingFloat(1, 6);
	protected EasingFloat _detectedScale = new EasingFloat(0,4);

	protected EasingFloat3d _characterPosition = new EasingFloat3d(0,0,0,6);
	protected EasingFloat3d _shadowPosition = new EasingFloat3d(0,0,0,6);
	protected float _characterW = 0;
	protected float _characterH = 0;
	

	public BlueBearBasePlayer( BlueBearPlayerControls playerControls, float xPosition ) {
		p = (BlueBear)P.p;
		pg = p.pg;

		_playerControls = playerControls;
		_waitingSpinnerX = P.round( pg.width * xPosition );
		_waitingSpinner = new BlueBearWaitingSpinner( _waitingSpinnerX );
	}

	public boolean detectPlayer() {
		PlayerDetectedState updatedState = _playerControls.detectPlayer();
		if( updatedState != null && _playerDetectedState != updatedState ) {
			switch ( updatedState ) {
				case PLAYER_DETECTED:
					_waitingSpinner.playerEntered();
					p.sounds.playSound( BlueBearSounds.STEP_IN );
					break;
				case PLAYER_LOST:
					_waitingSpinner.playerLeft();
					p.sounds.playSound( BlueBearSounds.STEP_OUT );
					break;
				case PLAYER_LOCKED:
					_waitingSpinner.playerDetected();
					p.sounds.playSound( BlueBearSounds.PLAYER_LOCKED );
					break;
				default:
					break;
			}
			_playerDetectedState = updatedState;
		}
		return (_playerDetectedState == PlayerDetectedState.PLAYER_LOCKED);
	}

	public void prepareForGameplay() {
		setLane(0);
	}
	
	public void startGameplay() {
		_playerControls.resetDetection();
		_playerDetectedState = null;
	}
	
	public void showSpinner() {
		_waitingSpinner.show();
	}

	public void updateControls() {
		_playerControls.updateControls();
		setLane( _playerControls.lane() );
	}
	
	public boolean isPlayerDetection() {
		return ( p.gameState() == GameState.GAME_WAITING_FOR_PLAYERS || p.gameState() == GameState.GAME_INTRO || p.gameState() == GameState.GAME_INTRO_OUTRO );
	}
	
	public void setLane( int lane ) {
		_lane = lane;
	}
	
	public int lane() {
		return _lane;
	}
	
	public void update(float speed) {
		_waitingSpinner.update();
		_laneScale.update();

		if( isPlayerDetection() == true ) {
			updatePlayerDetection();
		} else {
			updateGameplay(speed);
		}
	}
	
	protected void updatePlayerDetection() {
		_laneScale.setTarget(1);
		_detectedScale.update();
		
		_characterPosition.setCurrentX( _waitingSpinnerX );
		_characterPosition.setTargetX( _waitingSpinnerX );
		_characterPosition.setTargetY( _waitingSpinner.y() + p.scaleV(8f) * P.sin( p.millis() * 0.004f ) );
		_characterPosition.update();
		
		_shadowPosition.setCurrentX( _waitingSpinnerX );
		_shadowPosition.setTargetX( _waitingSpinnerX );
		_shadowPosition.setTargetY( pg.height * _detectShadowYPercent );
		_shadowPosition.update();
		
		if( _playerDetectedState == PlayerDetectedState.PLAYER_LOCKED ) {
			_detectedScale.setTarget(1);
			pg.shape( 
					_detectionSvg, 
					_characterPosition.x(), 
					_characterPosition.y(), 
					p.scaleV(_detectionSvg.width) * _detectedScale.value(), 
					p.scaleV(_detectionSvg.height) * _detectedScale.value()
			);
			
		} else {
			_detectedScale.setTarget(0);
			
		}
		pg.shape( 
				p.gameGraphics.bearShadow, 
				_shadowPosition.x(), 
				_shadowPosition.y(), 
				p.scaleV(p.gameGraphics.bearShadow.width) * _laneScale.value(), 
				p.scaleV(p.gameGraphics.bearShadow.height) * _laneScale.value() 
		);
	}
	
	// override these
	protected void updateGameplay( float speed ) {}
	public void startMoving() {}
	public void stopMoving() {}
	
}
