package org.ohheckyeah.shared.app;

import hypermedia.net.UDP;

import com.haxademic.core.app.P;
import com.haxademic.core.hardware.kinect.KinectRegion;
import com.haxademic.core.system.FileUtil;

@SuppressWarnings("serial")
public class OHYBaseRemoteControl
extends OHYKinectApp {

	public static final String DATA_GRID_DELIMITER = "~";
	public static final String DATA_REGION_DELIMITER = ":";
	
	protected UDP _udp;
	protected boolean _debugging = false;
	protected String _receiverIp = "";
	protected int _receiverPort = 0;

	public void setup( String propertiesFile ) {
		_customPropsFile = FileUtil.getHaxademicDataPath() + "properties/" + propertiesFile;
		super.setup();
		setKinectProperties();
		setKinectRemoteProperties();
		initUDP();
	}
	
	protected void setKinectRemoteProperties() {
		_debugging = _appConfig.getBoolean( "kinect_remote_debug", false );
		_receiverIp = _appConfig.getString( "kinect_remote_receiver_ip", "" );
		_receiverPort = _appConfig.getInt( "kinect_remote_receiver_port", 0 );
	}
	
	protected void initUDP() {
		_udp = new UDP( this, 9999 );	// TODO: fix 9999????
		_udp.log( _debugging );
		_udp.listen( true );	
	}
	
	public void drawApp() {
		_kinectGrid.update();
		sendKinectGrid( buildKinectString() );
	}
	
	protected String buildKinectString() {
		String kinectOutput = "";
		KinectRegion kinectRegion = null;
		for( int i=0; i < NUM_PLAYERS; i++ ) {
			kinectRegion = _kinectGrid.getRegion(i);
			if( i > 0 ) kinectOutput += DATA_GRID_DELIMITER; 
			kinectOutput += kinectRegion.controlX() + DATA_REGION_DELIMITER + kinectRegion.controlZ() + DATA_REGION_DELIMITER + kinectRegion.pixelCount();
		}
		return kinectOutput;
	}
	
	protected void sendKinectGrid( String kinectData ) {
		if( _debugging == true ) P.println("kinectData = "+kinectData);
		_udp.send( kinectData, _receiverIp, _receiverPort );
	}

}
