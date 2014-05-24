package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class TinkerBotPlayerDetectionBg {
	
	protected TinkerBot p;
	protected PGraphics pg;
	
	protected EasingFloat _backgroundY = new EasingFloat(0, 6);

	public TinkerBotPlayerDetectionBg() {
		p = (TinkerBot)P.p;
		pg = p.pg;
	}
	
	public void update() {
		DrawUtil.setDrawCorner(pg);
		_backgroundY.update();		
		
		if( _backgroundY.value() < pg.height ) {
			pg.shape( p.gameGraphics.gameplayBackgroundWaiting, 0, 0, p.svgWidth(p.gameGraphics.gameplayBackgroundWaiting), p.svgHeight(p.gameGraphics.gameplayBackgroundWaiting) );
		}
	}
	
	public void show() {
		_backgroundY.setCurrent(pg.height);
		_backgroundY.setTarget(0);
	}
	
	public void hide() {
		_backgroundY.setTarget(-pg.height);
	}

}
