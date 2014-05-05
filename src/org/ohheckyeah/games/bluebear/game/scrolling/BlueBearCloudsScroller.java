package org.ohheckyeah.games.bluebear.game.scrolling;

import org.ohheckyeah.games.bluebear.game.BlueBearRoad;


public class BlueBearCloudsScroller 
extends BlueBearScrollingGraphicsLayer{

	public BlueBearCloudsScroller() {
		super();
	}
	
	protected void configureLayer() {
		float y = BlueBearRoad.ROAD_Y - p.scaleV(60);		
		super.initLayer("games/bluebear/svg/scene/sky/", y, 0);
	}
}
