package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.math.MathUtil;

public class TinkerBotLayout {

	protected TinkerBot p;
	protected PGraphics pg;

	public static int NUM_POSITIONS = 9; // must be an odd number
	public static int HALF_POSITIONS = (NUM_POSITIONS - 1) / 2;
	public static int PLAYER_Y_CENTER;
	public static int PLAYER_Y_INC;
	public static float PLAYER_Y_GAP;

	public TinkerBotLayout() {
		p = (TinkerBot)P.p;
		pg = p.pg;

		PLAYER_Y_CENTER = P.round( pg.height * 0.55f );
		PLAYER_Y_INC = P.round( pg.height * 0.60f / NUM_POSITIONS );
		PLAYER_Y_GAP = p.scaleV(45);
	}
	
	public static int randomPosition( int excludeCurPosition ) {
		int newPosition = excludeCurPosition;
		while( P.abs( newPosition - excludeCurPosition ) < 2 ) {
			newPosition = MathUtil.randRange( -TinkerBotLayout.HALF_POSITIONS + 1, TinkerBotLayout.HALF_POSITIONS - 1 );	// never go to last position on either end. allow players to go 1 notch further
		}
		return newPosition;
	}
	
	public static float yForPosition( float position ) {
		return TinkerBotLayout.PLAYER_Y_CENTER + position * TinkerBotLayout.PLAYER_Y_INC;
	}
}
