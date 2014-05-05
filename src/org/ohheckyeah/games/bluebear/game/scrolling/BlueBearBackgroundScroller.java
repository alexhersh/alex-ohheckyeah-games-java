package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearRoad;


public class BlueBearBackgroundScroller 
extends BlueBearScrollingGraphicsLayer{

	public BlueBearBackgroundScroller() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearRoad.ROAD_Y - p.scaleV(60);		
		super.initLayer(y, 0, 0.6f);
	}
}
