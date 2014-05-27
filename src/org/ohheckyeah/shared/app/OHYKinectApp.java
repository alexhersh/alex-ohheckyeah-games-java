package org.ohheckyeah.shared.app;

import hypermedia.net.UDP;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.data.ConvertUtil;
import com.haxademic.core.hardware.kinect.KinectRegionGrid;

@SuppressWarnings("serial")
public class OHYKinectApp
extends PAppletHax {

	// Kinect input ----------------------------------------------------------------------------------------------
	
	public static float KINECT_MIN_DIST = 1500;
	public static float KINECT_MAX_DIST = 2000;
	public static int KINECT_TOP = 0;
	public static int KINECT_BOTTOM = 480;
	public static int KINECT_PLAYER_GAP = 0;
	public static int NUM_PLAYERS = 2;
	protected KinectRegionGrid _kinectGrid;

	protected void setKinectProperties() {
		KINECT_MIN_DIST = _appConfig.getInt( "kinect_min_mm", 1500 );
		KINECT_MAX_DIST = _appConfig.getInt( "kinect_max_mm", 2000 );
		KINECT_TOP = _appConfig.getInt( "kinect_top_pixel", 240 );
		KINECT_BOTTOM = _appConfig.getInt( "kinect_bottom_pixel", 400 );
		KINECT_PLAYER_GAP = _appConfig.getInt( "kinect_player_gap", 0 );
		NUM_PLAYERS = _appConfig.getInt( "num_players", 2 );
		
		// build kinect grid!
		_kinectGrid = new KinectRegionGrid(p, NUM_PLAYERS, 1, (int)KINECT_MIN_DIST, (int)KINECT_MAX_DIST, (int)KINECT_PLAYER_GAP, (int)KINECT_TOP, (int)KINECT_BOTTOM);
	}
	
	
	// Kinect remote control --------------------------------------------------------------------------------------
	
	protected UDP _udp;
	protected boolean _remoteDebugging = false;
	protected String _receiverIp = "";
	protected int _receiverPort = 0;
	protected boolean _isRemoteKinect = false;
	
	protected void setRemoteKinectReceiverProperties() {
		_remoteDebugging = _appConfig.getBoolean( "kinect_remote_debug", false );
		_receiverIp = _appConfig.getString( "kinect_remote_receiver_ip", "" );
		_receiverPort = _appConfig.getInt( "kinect_remote_receiver_port", 0 );
		_isRemoteKinect = _appConfig.getBoolean("kinect_remote_active", false);
		if( _isRemoteKinect == true ) initRemoteKinect();
	}
	
	protected void initRemoteKinect() {
		_udp = new UDP( this, _receiverPort );
		_udp.log( _remoteDebugging );
		_udp.listen( true );	
	}
	
	public void receive( byte[] data, String ip, int port ) {
		String message = new String( data );
		if( _remoteDebugging == true ) P.println( "received: \""+message+"\" from "+ip+" on port "+port );
		
		String[] remoteKinectPlayersData = message.split("~");
		for( int i=0; i < remoteKinectPlayersData.length; i++ ) {
			// P.println("PLAYER "+i+" = "+remoteKinectPlayersData[i]);
			String[] kinectPlayerData = remoteKinectPlayersData[i].split(":");
			_kinectGrid.getRegion(i).controlX( ConvertUtil.stringToFloat( kinectPlayerData[0].trim() ) );
			_kinectGrid.getRegion(i).controlZ( ConvertUtil.stringToFloat( kinectPlayerData[1].trim() ) );
			_kinectGrid.getRegion(i).pixelCount( ConvertUtil.stringToInt( kinectPlayerData[2].trim() ) );
		}
	}


}
