package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;
import org.ohheckyeah.games.tinkerbot.assets.TinkerBotSounds;
import org.ohheckyeah.shared.OHYBaseGame.GameState;
import org.ohheckyeah.shared.OHYBaseGame.PlayerDetectedState;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.hardware.kinect.KinectRegion;
import com.haxademic.core.math.MathUtil;


public class TinkerBotPlayer {

	protected TinkerBot p;
	protected PGraphics pg;

	protected KinectRegion _kinectRegion;
	protected boolean _isRemoteKinect = false;

	protected TinkerBotWaitingSpinner _waitingSpinner;
	protected PShape _detectionSvg;
	protected int _waitingSpinnerX = 0;
	protected int KINECT_PIXEL_DETECT_THRESH = 10;
	protected PlayerDetectedState _playerDetectedState;

	protected boolean _controlsActive = false;
	protected boolean _hasPlayer = false;
	protected boolean _detectedPlayer = false;
	protected int _detectedPlayerTime = 0;
	protected int _autoControlTime = 0;
	protected int _position = 0;


	public TinkerBotPlayer( KinectRegion kinectRegion, boolean isRemoteKinect, float xPosition ) {
		p = (TinkerBot)P.p;
		pg = p.pg;

		_kinectRegion = kinectRegion;
		_isRemoteKinect = isRemoteKinect;

		_waitingSpinnerX = P.round( pg.width * xPosition );
		_waitingSpinner = new TinkerBotWaitingSpinner( _waitingSpinnerX );
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
	
	public void resetDetection() {
		_detectedPlayer = false;
		_hasPlayer = false;
	}

	public void prepareForGameplay() {
		_waitingSpinner.hide();
	}
	
	public void startGameplay() {
		resetDetection();
		_playerDetectedState = null;
	}
	
	public void startDetection() {
		_waitingSpinner.show();
	}

	protected PlayerDetectedState detectPlayerCurState() {
		PlayerDetectedState curState = null;
		if( p.gameState() != GameState.GAME_WAITING_FOR_PLAYERS ) return curState; 
		if( _detectedPlayer == false ) {
			if( _kinectRegion.pixelCount() >= KINECT_PIXEL_DETECT_THRESH || (p.kinectWrapper == null && _isRemoteKinect == false) ) {
				_detectedPlayerTime = p.millis();
				_detectedPlayer = true;
				curState = PlayerDetectedState.PLAYER_DETECTED;
			}
		} else {
			if( _kinectRegion.pixelCount() < KINECT_PIXEL_DETECT_THRESH && (p.kinectWrapper != null || _isRemoteKinect == true) ) {
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
//		P.println("updatedState",updatedState);
		if( updatedState != null && _playerDetectedState != updatedState ) {
			switch ( updatedState ) {
				case PLAYER_DETECTED:
					_waitingSpinner.playerEntered();
					p.sounds.playSound( TinkerBotSounds.STEP_IN );
					break;
				case PLAYER_LOST:
					_waitingSpinner.playerLeft();
					p.sounds.playSound( TinkerBotSounds.STEP_OUT );
					break;
				case PLAYER_LOCKED:
					_waitingSpinner.playerDetected();
					p.sounds.playSound( TinkerBotSounds.PLAYER_LOCKED );
					break;
				default:
					break;
			}
			_playerDetectedState = updatedState;
		}
	}
	
	public void update() {
		if( p.gameState() == GameState.GAME_WAITING_FOR_PLAYERS || p.gameState() == GameState.GAME_PRE_COUNTDOWN ) {
			detectPlayer( detectPlayerCurState() );
			_waitingSpinner.update();
		} else {
			updateControls();
			updateGameplay();
		}
	}
	
	public void updateControls() {
		if( p.kinectWrapper != null || _isRemoteKinect == true ) {
			_position = P.round( 10f * MathUtil.getPercentWithinRange( -0.5f, 0.5f, _kinectRegion.controlZ() ) );
		} else {
			if( _isRemoteKinect == false ) {
				// fake test controls
				if( p.millis() > _autoControlTime + 2500 ) {
					_autoControlTime = p.millis();
					_position = MathUtil.randRange(0, 2);
				}
			}
		}
	}
	
	protected void updateGameplay() {
		
	}
}