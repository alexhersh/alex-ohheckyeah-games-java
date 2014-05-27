package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;
import org.ohheckyeah.games.tinkerbot.assets.TinkerBotSounds;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;
import org.ohheckyeah.shared.app.OHYBaseGame.PlayerDetectedState;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.hardware.kinect.KinectRegion;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.math.easing.EasingFloat;


public class TinkerBotPlayer {

	protected TinkerBot p;
	protected PGraphics pg;

	protected KinectRegion _kinectRegion;
	protected boolean _isRemoteKinect = false;

	protected TinkerBotWaitingSpinner _waitingSpinner;
	protected PShape _detectionSvg;
	protected int _playerX = 0;
	protected EasingFloat _playerY = new EasingFloat(0,2);
	protected int KINECT_PIXEL_DETECT_THRESH = 10;
	public static int NUM_POSITIONS = 7;
	public static int HALF_POSITIONS = (NUM_POSITIONS - 1) / 2;
	public static int PLAYER_Y_CENTER;
	public static int PLAYER_Y_INC;
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

		_playerX = P.round( pg.width * xPosition );
		_waitingSpinner = new TinkerBotWaitingSpinner( _playerX );
		PLAYER_Y_CENTER = P.round( pg.height * 0.5f );
		PLAYER_Y_INC = P.round( pg.height * 0.75f / NUM_POSITIONS );
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
			// _position = P.round( NUM_POSITIONS * MathUtil.getPercentWithinRange( -0.5f, 0.5f, _kinectRegion.controlZ() ) );
			_position = P.round( _kinectRegion.controlZ() * 10 );
			_playerY.setTarget( PLAYER_Y_CENTER + p.scaleV( _position * PLAYER_Y_INC ) );
		} else {
			if( _isRemoteKinect == false ) {
				// fake test controls
				if( p.millis() > _autoControlTime + 40 ) {
					_autoControlTime = p.millis();
					_position = MathUtil.randRange(-HALF_POSITIONS, HALF_POSITIONS);
					_playerY.setTarget( PLAYER_Y_CENTER + p.scaleV( _position * PLAYER_Y_INC ) );
				}
			}
		}
	}
	
	protected void updateGameplay() {
		_playerY.update();
		
		// draw top & bottom bar
		DrawUtil.setDrawCorner(p);
		pg.shape(p.gameGraphics.playerBar, _playerX - p.gameGraphics.playerBar.width / 2, 0, p.scaleV( p.gameGraphics.playerBar.width ), _playerY.value() - p.scaleV(30) );
		pg.shape(p.gameGraphics.playerBar, _playerX - p.gameGraphics.playerBar.width / 2, _playerY.value() + p.scaleV(30), p.scaleV( p.gameGraphics.playerBar.width ), pg.height - _playerY.value() );
	}
}
