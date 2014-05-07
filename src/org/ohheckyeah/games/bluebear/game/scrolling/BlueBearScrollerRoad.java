package org.ohheckyeah.games.bluebear.game.scrolling;

public class BlueBearScrollerRoad 
extends BlueBearScroller{

	public BlueBearScrollerRoad() {
		super();
	}
	
	protected void configureLayer() {
		float y = pg.height;		
		super.initLayer(y, 0, 0, 1);
	}
}
