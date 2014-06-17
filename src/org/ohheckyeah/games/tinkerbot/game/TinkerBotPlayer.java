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


	public TinkerBotPlayer( KinectRegion kinectRegion, boolean isRemoteKinect, float xPositionDetection, float xPosition ) {
		p = (TinkerBot)P.p;
		pg = p.pg;

		_kinectRegion = kinectRegion;
		_isRemoteKinect = isRemoteKinect;

		_playerDetectionX = P.round( pg.width * xPositionDetection );
		_waitingSpinner = new TinkerBotWaitingSpinner( _playerDetectionX );
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
		// pick player parts for the round
		_barOverlayIndexTop = MathUtil.randRange(0, p.gameGraphics.playerParts.length - 1);
		_barOverlayIndexBot = MathUtil.randRange(0, p.gameGraphics.playerParts.length - 1);
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
	
	public void update( boolean shouldUpdateControls, boolean isError ) {
		if( p.gameState() == GameState.GAME_WAITING_FOR_PLAYERS || p.gameState() == GameState.GAME_PRE_COUNTDOWN ) {
			detectPlayer( detectPlayerCurState() );
			_waitingSpinner.update();
		} else if( p.gameState() != GameState.GAME_WAITING_FOR_PLAYERS && p.gameState() != GameState.GAME_INTRO_OUTRO && p.gameState() != GameState.GAME_INTRO ) {
			if( shouldUpdateControls ) updateControls();
			updateGameplay( isError );
		}
	}
	
	public void updateControls() {
		if( p.kinectWrapper != null || _isRemoteKinect == true ) {
			float controlZ = _kinectRegion.controlZ();
			if( p.kinectWrapper != null && p.kinectWrapper.isMirrored() == false ) controlZ = _kinectRegion.controlZ() * -1f;
			// _position = P.round( NUM_POSITIONS * MathUtil.getPercentWithinRange( -0.5f, 0.5f, controlZ ) );
			// turn -0.5, 0.5 into number of positions int
			_position = P.round( P.map( controlZ, -0.5f, 0.5f, -TinkerBotLayout.HALF_POSITIONS * 0.1f, TinkerBotLayout.HALF_POSITIONS * 0.1f ) * 10 );
			_playerY.setTarget( TinkerBotLayout.yForPosition( _position ) );
		} else {
			if( _isRemoteKinect == false ) {
				// fake test controls
				if( p.millis() > _autoControlTime + 1000 ) {
					_autoControlTime = p.millis();
					_position = TinkerBotLayout.randomPosition( _position );
					_playerY.setTarget( TinkerBotLayout.yForPosition( _position ) );
				}
			}
		}
	}
	
	protected void updateGameplay( boolean isError ) {
		// update easing
		_playerY.update();
		_playerYBuildOffset.update();
		
		// set error rotation when error first happens in a level
		if( isError == true ) {
			if( _wasError == false ) {
				_errorRotation = MathUtil.randRangeDecimal(-0.1f, 0.1f);
			}
		} else {
			_errorRotation = 0;
		}
		_wasError = isError;
		
		// draw top & bottom bar
		DrawUtil.setDrawCenter(pg);
		PShape barSvg = ( isError ) ? p.gameGraphics.playerBarError : p.gameGraphics.playerBar;
		float barWidth = p.svgWidth( barSvg );
		float topBarHeight = _playerY.value() - TinkerBotLayout.PLAYER_Y_GAP;
		PShape overlay = ( isError ) ? p.gameGraphics.playerPartsError[_barOverlayIndexTop] : p.gameGraphics.playerParts[_barOverlayIndexTop];
		float bottomBarHeight = pg.height - _playerY.value() - TinkerBotLayout.PLAYER_Y_GAP;
		PShape overlayBottom = ( isError ) ? p.gameGraphics.playerPartsError[_barOverlayIndexBot] : p.gameGraphics.playerParts[_barOverlayIndexBot];
		
		// translate build in/out y offset
		pg.pushMatrix();
		pg.translate( 0, -_playerYBuildOffset.value() );
		// draw top bar
		pg.shape(barSvg, _playerX, topBarHeight / 2f, barWidth, topBarHeight );
		// and overlay, with rotation if errored
		pg.pushMatrix();
		pg.translate( _playerX, _playerY.value() - p.scaleV(overlay.height / 2f) - TinkerBotLayout.PLAYER_Y_GAP );
		pg.rotate(P.PI);
		pg.rotate(_errorRotation);
		pg.shape(overlay, 0, 0, p.scaleV(overlay.width + 2), p.scaleV(overlay.height + 2) );
		pg.popMatrix();
		pg.popMatrix();
		
		// translate build in/out y offset
		pg.pushMatrix();
		pg.translate( 0, _playerYBuildOffset.value() );
		// draw bottom bar
		pg.shape(barSvg, _playerX, pg.height - bottomBarHeight / 2f, barWidth, bottomBarHeight );
		// and overlay, with rotation if errored
		pg.pushMatrix();
		pg.translate( _playerX, _playerY.value() + p.scaleV(overlayBottom.height / 2f) + TinkerBotLayout.PLAYER_Y_GAP );
		pg.rotate(_errorRotation);
		pg.shape(overlayBottom, 0, 0, p.scaleV(overlayBottom.width + 2), p.scaleV(overlayBottom.height + 2) );
		pg.popMatrix();
		pg.popMatrix();
	}
}
