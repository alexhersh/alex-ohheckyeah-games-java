package org.ohheckyeah.games.bluebear.game.nonscrolling;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorHaxEasing;
import com.haxademic.core.draw.util.DrawUtil;

public class BlueBearGround {

	protected BlueBear p;
	protected PGraphics pg;
	
	protected ColorHaxEasing _color;
	protected int height;

	public BlueBearGround() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		_color = new ColorHaxEasing("#ffffff", 20);
		height = Math.round(p.scaleV(122));
	}
	
	public void setColor( int newColor ) {		
		_color.setTargetColorInt( newColor );
	}
	
	public void update() {
		_color.update();
		
		// draw to fill width of screen
		DrawUtil.setDrawCorner(pg);
		pg.pushMatrix();
		pg.fill( _color.colorInt() );
		float y = BlueBearScreenPositions.ROAD_Y - height;
		pg.rect( 0, y, pg.width, height );
		pg.popMatrix();
	}

}
