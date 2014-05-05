package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearRoad;


public class BlueBearSidewalkScroller 
extends BlueBearScrollingGraphicsLayer{

	public BlueBearSidewalkScroller() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearRoad.ROAD_Y - p.scaleV(10);		
		super.initLayer(y, 200, 1);
	}
}
