package org.ohheckyeah.games.bluebear.game;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorHaxEasing;
import com.haxademic.core.draw.util.DrawUtil;

public class BlueBearBackground {

	protected BlueBear p;
	protected PGraphics pg;
	
	protected ColorHaxEasing _color;

	public BlueBearBackground() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		_color = new ColorHaxEasing("#ffffff", 20);
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
		pg.rect( 0, 0, pg.width, pg.height );
		pg.popMatrix();
	}

}
