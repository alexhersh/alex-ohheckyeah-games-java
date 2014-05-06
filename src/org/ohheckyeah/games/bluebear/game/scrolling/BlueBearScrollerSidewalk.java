package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearRoad;


public class BlueBearScrollerSidewalk 
extends BlueBearScroller{

	public BlueBearScrollerSidewalk() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearRoad.ROAD_Y - p.scaleV(10);		
		super.initLayer(y, 30, 250, 1);
	}
}
