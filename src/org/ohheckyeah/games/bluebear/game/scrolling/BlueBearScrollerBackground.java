package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;


public class BlueBearScrollerBackground 
extends BlueBearScroller{

	public BlueBearScrollerBackground() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearScreenPositions.ROAD_Y - p.scaleV(100);		
		super.initLayer(y, 0, 0, 0.93f);
	}
}
