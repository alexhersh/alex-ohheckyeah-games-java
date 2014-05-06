package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearRoad;


public class BlueBearScrollerBuildings 
extends BlueBearScroller{

	public BlueBearScrollerBuildings() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearRoad.ROAD_Y - p.scaleV(60);		
		super.initLayer(y, 20, 50, 1);
	}
	
}
