package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearRoad;


public class BlueBearBuildingsScroller 
extends BlueBearScrollingGraphicsLayer{

	public BlueBearBuildingsScroller() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearRoad.ROAD_Y - p.scaleV(60);		
		super.initLayer(y, 20, 50, 1);
	}
	
}
