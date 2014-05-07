package org.ohheckyeah.games.bluebear.game;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;

import com.haxademic.core.app.P;

public class BlueBearScreenPositions {

	protected BlueBear p;
	protected PGraphics pg;
	
	public static float LANE_H = 88f;
	public static float ROAD_H = 0;
	public static float ROAD_Y = 0;
	public static int LANES_Y[] = {0,0,0};
	
	protected float distance = 0;

	public BlueBearScreenPositions() {
		p = (BlueBear)P.p;
		pg = p.pg;

		ROAD_H = p.scaleV(p.gameGraphics.roadTile.height);
		LANE_H = (float) ROAD_H / 3f;
		ROAD_Y = pg.height - ROAD_H;
		for( int i=0; i < LANES_Y.length; i++ ) {
			LANES_Y[i] = Math.round( ROAD_Y + LANE_H / 2f + i * LANE_H );
		}
	}
}
