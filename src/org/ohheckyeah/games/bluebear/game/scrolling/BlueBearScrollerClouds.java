package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearRoad;


public class BlueBearScrollerClouds 
extends BlueBearScroller{

	public BlueBearScrollerClouds() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearRoad.ROAD_Y - p.scaleV(60);		
		super.initLayer(y, 0, 100, 0.4f);
	}
}
