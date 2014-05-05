package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearRoad;


public class BlueBearSkylineScroller 
extends BlueBearScrollingGraphicsLayer{

	public BlueBearSkylineScroller() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearRoad.ROAD_Y - p.scaleV(60);		
		super.initLayer("games/bluebear/svg/neighborhoods/baker/skyline/", y, 0);
	}
}
