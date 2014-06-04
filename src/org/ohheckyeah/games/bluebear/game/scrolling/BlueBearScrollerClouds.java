package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;


public class BlueBearScrollerClouds 
extends BlueBearScroller{

	public BlueBearScrollerClouds() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearScreenPositions.ROAD_Y - p.scaleV(40);		
		super.initLayer(y, 0, 70, 0.89f);
	}
}
