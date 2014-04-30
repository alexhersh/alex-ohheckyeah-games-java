package org.ohheckyeah.games.bluebear.game;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;

public class BlueBearRoad {

	protected BlueBear p;
	protected PGraphics pg;
	
	public static float LANE_H = 88f;
	public static float ROAD_Y = 0;


	public BlueBearRoad() {
		p = (BlueBear)P.p;
		pg = p.pg;

		LANE_H = p.scaleV(LANE_H);
		ROAD_Y = pg.height * 0.5f;
	}
	
	public void update() {
		int distance = -p.frameCount * 3;
		
		// responsive sizing/placement
		int tileW = P.round(p.scaleV(p.gameGraphics.roadTile.width));
		int tileH = P.round(p.scaleV(p.gameGraphics.roadTile.height));
		int tileX = P.round(distance % tileW);
		
		// draw to fill width of screen
		DrawUtil.setDrawCorner(pg);
		pg.pushMatrix();
		for( int i = tileX; i < pg.width; i += tileW ) {
			pg.shape( p.gameGraphics.roadTile, i, ROAD_Y, tileW, tileH );
		}
		pg.popMatrix();
	}
}
