package org.ohheckyeah.games.bluebear.game.characters;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.shared.app.OHYBaseGame.PlayerDetectedState;

import com.haxademic.core.app.P;
import com.haxademic.core.hardware.joystick.IJoystickControl;
import com.haxademic.core.math.MathUtil;

public class BlueBearPlayerControls {

	protected BlueBear p;
	protected IJoystickControl _joystick;
	protected boolean _isRemoteKinect = false;
	protected boolean _controlsActive = false;
	protected boolean _hasPlayer = false;
	protected boolean _detectedPlayer = false;
	protected int _detectedPlayerTime = 0;
	protected int _autoControlTime = 0;
	
	public final static int LANE_TOP = 0;
	public final static int LANE_MIDDLE = 1;
	public final static int LANE_BOTTOM = 2;
	protected int _lane = 0;
	
	public BlueBearPlayerControls( IJoystickControl kinectRegion, boolean isRemoteKinect ) {
		p = (BlueBear) P.p;
		_joystick = kinectRegion;
		_isRemoteKinect = isRemoteKinect;
	}
	
	public boolean detectedPlayer() {
		return _detectedPlayer;
	}
	
	public boolean hasPlayer() {
		return _hasPlayer;
	}
	
	public int lane() {
		return _lane;
	}
	
	public void resetDetection() {
		_detectedPlayer = false;
		_hasPlayer = false;
	}
	
	public void updateControls() {
		if( p.kinectWrapper != null || p.leapMotion != null || _isRemoteKinect == true ) {
			if(_joystick.isActive() == true) {
				float controlZ = _joystick.controlZ();
				if( p.leapMotion != null || (p.kinectWrapper != null && p.kinectWrapper.isMirrored() == false) ) controlZ = _joystick.controlZ() * -1f;
				if( controlZ < -0.1666 ) {				
					_lane = LANE_TOP;
				} else if( controlZ > 0.1666 ) {
					_lane = LANE_BOTTOM;
				} else {
					_lane = LANE_MIDDLE;
				}
			}
		} else {
			if( _isRemoteKinect == false ) {
				// fake test controls
				if( p.millis() > _autoControlTime + 1500 ) {
					_autoControlTime = p.millis();
					_lane = MathUtil.randRange(0, 2);
				}
			}
		}
	}

	public PlayerDetectedState detectPlayer() {
		PlayerDetectedState curState = null; // PlayerDetectedState.PLAYER_WAITING;
		if( _detectedPlayer == false ) {
			if( _joystick.isActive() == true || (p.leapMotion == null && p.kinectWrapper == null && _isRemoteKinect == false) ) {
				_detectedPlayerTime = p.millis();
				_detectedPlayer = true;
				curState = PlayerDetectedState.PLAYER_DETECTED;
			}
		} else {
			if( _joystick.isActive() == false && (p.leapMotion != null || (p.kinectWrapper != null || _isRemoteKinect == true)) ) {
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

}