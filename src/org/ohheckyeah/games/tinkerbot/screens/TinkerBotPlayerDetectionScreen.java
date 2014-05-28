package org.ohheckyeah.games.tinkerbot.screens;

import org.ohheckyeah.games.tinkerbot.TinkerBot;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class TinkerBotPlayerDetectionScreen {
	
	protected TinkerBot p;
	protected PGraphics pg;
	
	protected EasingFloat _backgroundY = new EasingFloat(0, 6);

	public TinkerBotPlayerDetectionScreen() {
		p = (TinkerBot)P.p;
		pg = p.pg;
	}
	
	public void update() {
		_backgroundY.update();		
		
		if( _backgroundY.value() < pg.height ) {
			DrawUtil.setDrawCenter(pg);
			pg.shape( p.gameGraphics.gameplayBackgroundWaiting, pg.width * 0.5f, _backgroundY.value() + pg.height * 0.5f, p.svgWidth(p.gameGraphics.gameplayBackgroundWaiting), p.svgHeight(p.gameGraphics.gameplayBackgroundWaiting) );
		}
	}
	
	public void show() {
		_backgroundY.setCurrent(pg.height);
		_backgroundY.setTarget(0);
	}
	
	public void hide() {
		// _backgroundY.setTarget(-pg.height);
	}

}
