package org.ohheckyeah.shared;

import hypermedia.net.UDP;

import org.ohheckyeah.shared.assets.OHYGraphics;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.data.ConvertUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.hardware.kinect.KinectRegionGrid;
import com.haxademic.core.system.FileUtil;

@SuppressWarnings("serial")
public class OHYBaseGame
extends PAppletHax {
	
	// input
	public static float KINECT_MIN_DIST = 1500;
	public static float KINECT_MAX_DIST = 2000;
	public static int KINECT_TOP = 0;
	public static int KINECT_BOTTOM = 480;

	// remote kinect
	protected UDP _udp;
	protected boolean _remoteDebugging = false;
	protected String _receiverIp = "";
	protected int _receiverPort = 0;
	protected boolean _isRemoteKinect = false;
	
	// debug 
	protected boolean _isDebugging = false;
	
	// game state
	public static int NUM_PLAYERS = 2;
	protected KinectRegionGrid _kinectGrid;

	// game canvas & responsive scale
	public PGraphics pg;
	public float gameScaleV = 1;

	// game state
	public enum GameState {
		GAME_INTRO,
		GAME_INTRO_OUTRO,
		GAME_WAITING_FOR_PLAYERS,
		GAME_PRE_COUNTDOWN,
		GAME_COUNTDOWN,
		GAME_PLAYING,
		GAME_FINISHING,
		GAME_OVER,
		GAME_OVER_OUTRO
	}
	protected GameState _gameState;
	protected GameState _gameStateQueued;	// wait until beginning on the next frame to switch states to avoid mid-frame conflicts

	// media
	public OHYGraphics ohyGraphics;

	
	// shared setup methods --------------------------------------------------------------------------------------------
	
	public void setup( String propertiesFile, int gameOriginalHeight ) {
		_customPropsFile = FileUtil.getHaxademicDataPath() + "properties/" + propertiesFile;
		super.setup();
		gameScaleV = p.height / gameOriginalHeight;
		ohyGraphics = new OHYGraphics();
		setKinectProperties();
	}
	
	// Getters ---------------------------------------------------------------------------------------------------------
	
	public GameState gameState() { return _gameState; }

	
	// Responsive scaling & canvas --------------------------------------------------------------------------------------
	
	public float scaleV( float input ) {
		return input * gameScaleV;
	}

	public int svgWidth( PShape shape ) {
		return P.round(scaleV(shape.width));
	}
	
	public int svgHeight( PShape shape ) {
		return P.round(scaleV(shape.height));
	}

	protected void buildCanvas() {
		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
	}
	
	// Kinect input --------------------------------------------------------------------------------------
	
	protected void setKinectProperties() {
		// default kinect camera distance is for up-close indoor testing. not good for real games - suggested use is 2300-3300
		// default pixel rows are the center 200 kinect data rows
		KINECT_MIN_DIST = _appConfig.getInt( "kinect_min_mm", 1500 );
		KINECT_MAX_DIST = _appConfig.getInt( "kinect_max_mm", 2000 );
		KINECT_TOP = _appConfig.getInt( "kinect_top_pixel", 240 );
		KINECT_BOTTOM = _appConfig.getInt( "kinect_bottom_pixel", 400 );
		NUM_PLAYERS = _appConfig.getInt( "num_players", 2 );
		
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
