package org.ohheckyeah.games.bluebear.game;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.assets.BlueBearSounds;

import com.haxademic.core.app.P;
import com.haxademic.core.hardware.kinect.KinectRegion;

public class BlueBearPlayerControls {

	protected BlueBear p;
	protected KinectRegion _kinectRegion;
	protected boolean _isRemoteKinect = false;
	protected boolean _controlsActive = false;
	protected boolean _hasPlayer = false;
	protected boolean _detectedPlayer = false;
	protected int _detectedPlayerTime = 0;
	protected float _autoControl;

	public BlueBearPlayerControls( KinectRegion kinectRegion, boolean isRemoteKinect ) {
		p = (BlueBear) P.p;
		_kinectRegion = kinectRegion;
		_isRemoteKinect = isRemoteKinect;
		_autoControl = p.random(0.001f, 0.005f);
	}
	
	public boolean hasPlayer() {
		return _hasPlayer;
	}
	
	public float controlZ() {
		return _kinectRegion.controlZ();
	}
	
	public void updateControls() {
		if( p.kinectWrapper != null || _isRemoteKinect == true ) {
			// _easedControlX.setTarget( _kinectRegion.controlX() * 2f );					
		} else {
			if( _isRemoteKinect == false ) {
				// fake test controls
				// _easedControlX.setTarget( P.sin(p.millis() * _autoControl) );
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