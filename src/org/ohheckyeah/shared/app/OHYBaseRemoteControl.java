package org.ohheckyeah.shared.app;

import org.ohheckyeah.shared.OHYGameTracking;

import com.haxademic.core.app.P;
import com.haxademic.core.hardware.kinect.KinectRegion;
import com.haxademic.core.system.FileUtil;

@SuppressWarnings("serial")
public class OHYBaseRemoteControl
extends OHYKinectApp {

	public static final String DATA_GRID_DELIMITER = "~";
	public static final String DATA_REGION_DELIMITER = ":";
	public static final String ACTION_TAKE_PHOTO = "photo:";
	
	protected OHYGameTracking _tracking;
	
	public void setup( String propertiesFile, OHYGameTracking tracking ) {
		_tracking = tracking;
		if( propertiesFile != null ) _customPropsFile = FileUtil.getHaxademicDataPath() + "properties/" + propertiesFile;
		super.setup();
		setKinectProperties();
		setRemoteKinectProperties();
		initSenderUDP();
	}
	
	public void drawApp() {
		super.drawApp();
		sendKinectGrid( buildKinectString() );
	}
	
	protected String buildKinectString() {
		String kinectOutput = "";
		KinectRegion kinectRegion = null;
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			kinectRegion = _kinectGrid.getRegion(i);
			if( i > 0 ) kinectOutput += DATA_GRID_DELIMITER; 
			float controlZ = kinectRegion.controlZ();
			if( p.kinectWrapper.isMirrored() == false ) controlZ = kinectRegion.controlZ() * -1f;
			kinectOutput += kinectRegion.controlX() + DATA_REGION_DELIMITER + controlZ + DATA_REGION_DELIMITER + kinectRegion.pixelCount();
		}
		return kinectOutput;
	}
	
	protected void sendKinectGrid( String kinectData ) {
		if( _remoteDebugging == true ) P.println("sending data: = "+kinectData);
		_udp.send( kinectData, _receiverIp, _receiverPort );
	}

	public void receive( byte[] data, String ip, int port ) {
		String message = new String( data );
		if( _remoteDebugging == true ) P.println( "received: \""+message+"\" from "+ip+" on port "+port );
		
		if( message.indexOf(ACTION_TAKE_PHOTO) != -1 ) {
			if( _tracking != null ) {
				_tracking.saveCameraImage( message.replace(ACTION_TAKE_PHOTO, "") );
			}
		}
	}

}
