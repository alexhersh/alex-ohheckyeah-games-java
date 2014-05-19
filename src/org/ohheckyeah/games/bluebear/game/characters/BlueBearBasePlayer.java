package org.ohheckyeah.games.bluebear.game.characters;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.assets.BlueBearSounds;
import org.ohheckyeah.games.bluebear.game.characters.BlueBearPlayerControls.PlayerDetectedState;

import processing.core.PGraphics;

import com.haxademic.core.app.P;


public class BlueBearBasePlayer {

	protected BlueBear p;
	protected PGraphics pg;

	protected BlueBearPlayerControls _playerControls;
	protected BlueBearWaitingSpinner _waitingSpinner;
	protected int _lane = 0;

	protected PlayerDetectedState _playerDetectedState;

	public BlueBearBasePlayer( BlueBearPlayerControls playerControls, float xPosition ) {
		p = (BlueBear)P.p;
		pg = p.pg;

		_playerControls = playerControls;
		_waitingSpinner = new BlueBearWaitingSpinner( P.round(pg.width * xPosition ) );
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
					// _character.setWaitingState();
					p.sounds.playSound( BlueBearSounds.STEP_OUT );
					break;
				case PLAYER_LOCKED:
					_waitingSpinner.playerDetected();
					// _character.setSelectedState();
					p.sounds.playSound( BlueBearSounds.PLAYER_LOCKED );
					break;
				default:
					break;
			}
			_playerDetectedState = updatedState;
		}
		return (_playerDetectedState == PlayerDetectedState.PLAYER_LOCKED);
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
	
	public void setLane( int lane ) {
		_lane = lane;
	}
	
	public void update() {
		_waitingSpinner.update();
	}
}
