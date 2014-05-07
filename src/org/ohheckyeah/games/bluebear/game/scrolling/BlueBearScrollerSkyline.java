package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;


public class BlueBearScrollerSkyline 
extends BlueBearScroller{

	public BlueBearScrollerSkyline() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearScreenPositions.ROAD_Y - p.scaleV(60);		
		super.initLayer(y, 0, 0, 0.85f);
	}
}
