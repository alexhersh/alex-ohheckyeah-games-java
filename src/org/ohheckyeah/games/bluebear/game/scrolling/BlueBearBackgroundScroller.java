package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearRoad;


public class BlueBearBackgroundScroller 
extends BlueBearScrollingGraphicsLayer{

	public BlueBearBackgroundScroller() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearRoad.ROAD_Y - p.scaleV(10);		
		super.initLayer(y, 0, 0.3f);
	}
}
