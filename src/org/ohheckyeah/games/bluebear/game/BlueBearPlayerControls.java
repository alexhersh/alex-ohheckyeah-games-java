package org.ohheckyeah.games.bluebear.game;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.assets.BlueBearSounds;

import com.haxademic.core.app.P;
import com.haxademic.core.hardware.kinect.KinectRegion;
import com.haxademic.core.math.MathUtil;

public class BlueBearPlayerControls {

	protected BlueBear p;
	protected KinectRegion _kinectRegion;
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

	public BlueBearPlayerControls( KinectRegion kinectRegion, boolean isRemoteKinect ) {
		p = (BlueBear) P.p;
		_kinectRegion = kinectRegion;
		_isRemoteKinect = isRemoteKinect;
	}
	
	public boolean hasPlayer() {
		return _hasPlayer;
	}
	
	public int lane() {
		return _lane;
	}
	
	public void updateControls() {
		if( p.kinectWrapper != null || _isRemoteKinect == true ) {
			if( _kinectRegion.controlZ() < -0.1666 ) {				
				_lane = LANE_TOP;
			} else if( _kinectRegion.controlZ() > 0.1666 ) {
				_lane = LANE_BOTTOM;
			} else {
				_lane = LANE_MIDDLE;
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
	
	public boolean detectPlayer() {
		if( _detectedPlayer == false ) {
			if( _kinectRegion.pixelCount() >= 20 || (p.kinectWrapper == null && _isRemoteKinect == false) ) {
				_detectedPlayerTime = p.millis();
				_detectedPlayer = true;
				// _waitingSpinner.playerEntered();
				p.sounds.playSound( BlueBearSounds.STEP_IN );
			}
		} else {
			if( _kinectRegion.pixelCount() < 20 && (p.kinectWrapper != null || _isRemoteKinect == true) ) {
				_detectedPlayer = false;
				_hasPlayer = false;
				// _waitingSpinner.playerLeft();
				// _character.setWaitingState();
				p.sounds.playSound( BlueBearSounds.STEP_OUT );
			}
		}
		if( _detectedPlayer == true && p.millis() - _detectedPlayerTime > 2000 ) {
			if( _hasPlayer == false ) {
				_hasPlayer = true;
				// _waitingSpinner.playerDetected();
				// _character.setSelectedState();
				p.sounds.playSound( BlueBearSounds.PLAYER_LOCKED );
			}
		}
		return _hasPlayer;
	}

}