package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;


public class BlueBearScrollerSidewalk 
extends BlueBearScroller{

	public BlueBearScrollerSidewalk() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearScreenPositions.ROAD_Y - p.scaleV(10);		
		super.initLayer(y, 30, 250, 1);
	}
}
