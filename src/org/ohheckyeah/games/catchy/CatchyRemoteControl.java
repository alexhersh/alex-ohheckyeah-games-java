package org.ohheckyeah.games.catchy;

import hypermedia.net.UDP;
import processing.core.PApplet;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.hardware.kinect.KinectRegion;
import com.haxademic.core.hardware.kinect.KinectRegionGrid;
import com.haxademic.core.system.FileUtil;

public class CatchyRemoteControl
extends PAppletHax  
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Auto-initialization of the main class.
	 * @param args
	 */
	public static void main(String args[]) {
		// _isFullScreen = true;
		PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "org.ohheckyeah.games.catchy.CatchyRemoteControl" });
	}

	// input
	public static float KINECT_MIN_DIST = 1500;
	public static float KINECT_MAX_DIST = 2000;
	public static int KINECT_TOP = 0;
	public static int KINECT_BOTTOM = 480;
	
	public static int NUM_PLAYERS = 2;
	protected KinectRegionGrid _kinectGrid;
	
	protected UDP _udp;
	protected boolean _debugging = false;
	protected String _receiverIp = "";
	protected int _receiverPort = 0;

	public void setup() {
		_customPropsFile = FileUtil.getHaxademicDataPath() + "properties/catchy.properties";
		super.setup();
		initControls();
		initUDP();
	}
	
	protected void initControls() {
		setKinectProperties();
		_kinectGrid = new KinectRegionGrid(p, NUM_PLAYERS, 1, (int)KINECT_MIN_DIST, (int)KINECT_MAX_DIST, 250, (int)KINECT_TOP, (int)KINECT_BOTTOM);
	}
	
	protected void setKinectProperties() {
		// default kinect camera distance is for up-close indoor testing. not good for real games - suggested use is 2300-3300
		// default pixel rows are the center 200 kinect data rows
		KINECT_MIN_DIST = _appConfig.getInt( "kinect_min_mm", 1500 );
		KINECT_MAX_DIST = _appConfig.getInt( "kinect_max_mm", 2000 );
		KINECT_TOP = _appConfig.getInt( "kinect_top_pixel", 240 );
		KINECT_BOTTOM = _appConfig.getInt( "kinect_bottom_pixel", 400 );
		NUM_PLAYERS = _appConfig.getInt( "num_players", 2 );
		
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
			if( i > 0 ) kinectOutput += "|"; 
			kinectOutput += kinectRegion.controlX() + ":" + kinectRegion.controlZ() + ":" + kinectRegion.pixelCount();
		}
		kinectOutput += "   ";
		return kinectOutput;
	}
	
	protected void sendKinectGrid( String kinectData ) {
		//message = message+";\n";
		if( _debugging == true ) P.println("kinectData = "+kinectData);
		_udp.send( kinectData, _receiverIp, _receiverPort );
	}
}
