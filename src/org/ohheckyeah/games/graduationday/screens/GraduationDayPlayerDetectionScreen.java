package org.ohheckyeah.games.graduationday.screens;

import org.ohheckyeah.games.graduationday.GraduationDay;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class GraduationDayPlayerDetectionScreen {
	
	protected GraduationDay p;
	protected PGraphics pg;
	
	protected EasingFloat _backgroundY = new EasingFloat(0, 6);

	public GraduationDayPlayerDetectionScreen() {
		p = (GraduationDay)P.p;
		pg = p.pg;
	}
	
	public void update() {
		_backgroundY.update();		
		
		DrawUtil.setDrawCorner(pg);
		pg.fill(127);
		pg.rect(0, _backgroundY.value(), pg.width, pg.height);
		
//		if( _backgroundY.value() < pg.height ) {
//			DrawUtil.setDrawCenter(pg);
//			pg.shape( p.gameGraphics.gameplayBackgroundWaiting, pg.width / 2f, pg.height / 2f, p.scaleV(p.gameGraphics.gameplayBackgroundWaiting.width), p.scaleV(p.gameGraphics.gameplayBackgroundWaiting.height) );
//		}
	}
	
	public void show() {
		_backgroundY.setCurrent(pg.height);
		_backgroundY.setTarget(0);
	}
	
	public void hide() {
		 _backgroundY.setTarget(-pg.height);
	}

}
