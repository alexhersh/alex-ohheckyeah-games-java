package org.ohheckyeah.games.bluebear.screens;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.assets.BlueBearColors;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class BlueBearPlayerDetectionBg {
	
	protected BlueBear p;
	protected PGraphics pg;
	
	protected EasingFloat _backgroundY = new EasingFloat(0, 5);
	protected EasingFloat _floorY = new EasingFloat(0, 8);

	public BlueBearPlayerDetectionBg() {
		p = (BlueBear)P.p;
		pg = p.pg;
	}
	
	public void update() {
		DrawUtil.setDrawCorner(pg);
		_backgroundY.update();		
		_floorY.update();		
		
		if( _backgroundY.value() < pg.height ) {
			pg.fill(BlueBearColors.DETECT_SCREEN_TOP);
			pg.rect(0, _backgroundY.value(), pg.width, pg.height);
			pg.fill(BlueBearColors.DETECT_SCREEN_BOTTOM);
			pg.rect(0, _floorY.value(), pg.width, pg.height * 0.34f);
		}
	}
	
	public void show() {
		_backgroundY.setCurrent(pg.height);
		_backgroundY.setTarget(0);
		_floorY.setCurrent(pg.height);
		_floorY.setTarget(pg.height * 0.66f);
	}
	
	public void hide() {
		_backgroundY.setTarget(-pg.height);
		_floorY.setTarget(pg.height);
	}

}
