package org.ohheckyeah.shared.app;

import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.hardware.kinect.KinectRegionGrid;

@SuppressWarnings("serial")
public class OHYKinectApp
extends PAppletHax {

	// input
	public static float KINECT_MIN_DIST = 1500;
	public static float KINECT_MAX_DIST = 2000;
	public static int KINECT_TOP = 0;
	public static int KINECT_BOTTOM = 480;
	public static int KINECT_PLAYER_GAP = 0;

	// game state
	public static int NUM_PLAYERS = 2;
	protected KinectRegionGrid _kinectGrid;

	protected void setKinectProperties() {
		// default kinect camera distance is for up-close indoor testing. not good for real games - suggested use is 2300-3300
		// default pixel rows are the center 200 kinect data rows
		KINECT_MIN_DIST = _appConfig.getInt( "kinect_min_mm", 1500 );
		KINECT_MAX_DIST = _appConfig.getInt( "kinect_max_mm", 2000 );
		KINECT_TOP = _appConfig.getInt( "kinect_top_pixel", 240 );
		KINECT_BOTTOM = _appConfig.getInt( "kinect_bottom_pixel", 400 );
		KINECT_PLAYER_GAP = _appConfig.getInt( "kinect_player_gap", 0 );
		NUM_PLAYERS = _appConfig.getInt( "num_players", 2 );
		
		// build kinect grid!
		_kinectGrid = new KinectRegionGrid(p, NUM_PLAYERS, 1, (int)KINECT_MIN_DIST, (int)KINECT_MAX_DIST, (int)KINECT_PLAYER_GAP, (int)KINECT_TOP, (int)KINECT_BOTTOM);
	}
}
