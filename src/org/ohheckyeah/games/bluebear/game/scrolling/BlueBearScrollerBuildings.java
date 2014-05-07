package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;


public class BlueBearScrollerBuildings 
extends BlueBearScroller{

	public BlueBearScrollerBuildings() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearScreenPositions.ROAD_Y - p.scaleV(40);		
		super.initLayer(y, 20, 50, 1);
	}
	
}
