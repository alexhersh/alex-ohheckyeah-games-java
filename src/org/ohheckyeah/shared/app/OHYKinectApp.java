package org.ohheckyeah.shared.app;

import hypermedia.net.UDP;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.data.ConvertUtil;
import com.haxademic.core.hardware.joystick.IJoystickCollection;
import com.haxademic.core.hardware.kinect.KinectRegion;
import com.haxademic.core.hardware.kinect.KinectRegionGrid;
import com.haxademic.core.hardware.leap.LeapRegionGrid;

@SuppressWarnings("serial")
public class OHYKinectApp
extends PAppletHax {

	// Kinect input ----------------------------------------------------------------------------------------------
	
	public static int KINECT_MIN_DIST;
	public static int KINECT_MAX_DIST;
	public static int KINECT_TOP;
	public static int KINECT_BOTTOM;
	public static int KINECT_PLAYER_GAP;
	public static int NUM_PLAYERS;
	public static int KINECT_PIXEL_SKIP;
	public static int PLAYER_MIN_PIXELS;
	protected IJoystickCollection _joysticks;

	protected void setInputProperties() {
		KINECT_MIN_DIST = 	_appConfig.getInt( "kinect_min_mm", 1500 );
		KINECT_MAX_DIST = 	_appConfig.getInt( "kinect_max_mm", 2000 );
		KINECT_TOP = 		_appConfig.getInt( "kinect_top_pixel", 0 );
		KINECT_BOTTOM = 	_appConfig.getInt( "kinect_bottom_pixel", 480 );
		KINECT_PLAYER_GAP = _appConfig.getInt( "kinect_player_gap", 0 );
		NUM_PLAYERS = 		_appConfig.getInt( "num_players", 2 );
		KINECT_PIXEL_SKIP = _appConfig.getInt( "kinect_pixel_skip", 20 );
		PLAYER_MIN_PIXELS = _appConfig.getInt( "player_min_pixels", 10 );
		
		// build input!
		if(_appConfig.getBoolean("kinect_active", false) == true) {
			_joysticks = new KinectRegionGrid(NUM_PLAYERS, 1, KINECT_MIN_DIST, KINECT_MAX_DIST, KINECT_PLAYER_GAP, KINECT_TOP, KINECT_BOTTOM, KINECT_PIXEL_SKIP, PLAYER_MIN_PIXELS);
		} else if(_appConfig.getBoolean("leap_active", false) == true) {
			_joysticks = new LeapRegionGrid(NUM_PLAYERS, 1, 1, 0.2f);
		} else if(_appConfig.getBoolean("kinect_remote_active", false) == true) {
//			_joysticks = new RemoteControlJoysticks(NUM_PLAYERS, 1, 1, 0.2f);
		} else {
//			_joysticks = new TestAutoJoysticks();
		}
	}
	
	public void drawApp() {
		if( _joysticks != null ) _joysticks.update();
	}
	
	// Kinect remote control --------------------------------------------------------------------------------------
	
	protected UDP _udp;
	protected boolean _remoteDebugging = false;
	protected String _receiverIp = "";
	protected int _receiverPort = 0;
	protected String _senderIp = "";
	protected int _senderPort = 0;
	protected boolean _isRemoteControl = false;

	protected void setRemoteControlProperties() {
		_remoteDebugging = _appConfig.getBoolean( "kinect_remote_debug", false );
		_receiverIp = _appConfig.getString( "kinect_remote_receiver_ip", "" );
		_receiverPort = _appConfig.getInt( "kinect_remote_receiver_port", 0 );
		_senderIp = _appConfig.getString( "kinect_remote_sender_ip", "" );
		_senderPort = _appConfig.getInt( "kinect_remote_sender_port", 0 );
		_isRemoteControl = _appConfig.getBoolean("kinect_remote_active", false);
	}
	
	protected void initReceiverUDP() {
		_udp = new UDP( this, _receiverPort );
		_udp.log( _remoteDebugging );
		_udp.listen( true );	
	}
	
	protected void initSenderUDP() {
		_udp = new UDP( this, _senderPort );
		_udp.log( _remoteDebugging );
		_udp.listen( true );	
	}
	
	public void receive( byte[] data, String ip, int port ) {
		String message = new String( data );
		if( _remoteDebugging == true ) P.println( "received: \""+message+"\" from "+ip+" on port "+port );
		
		String[] remoteKinectPlayersData = message.split( OHYBaseRemoteControl.DATA_GRID_DELIMITER );
		for( int i=0; i < remoteKinectPlayersData.length; i++ ) {
			// P.println("PLAYER "+i+" = "+remoteKinectPlayersData[i]);
			String[] kinectPlayerData = remoteKinectPlayersData[i].split( OHYBaseRemoteControl.DATA_REGION_DELIMITER );
			_joysticks.getRegion(i).controlX( ConvertUtil.stringToFloat( kinectPlayerData[0].trim() ) );
			_joysticks.getRegion(i).controlZ( ConvertUtil.stringToFloat( kinectPlayerData[1].trim() ) );
			((KinectRegion)_joysticks.getRegion(i)).pixelCount( ConvertUtil.stringToInt( kinectPlayerData[2].trim() ) );
		}
	}

}
