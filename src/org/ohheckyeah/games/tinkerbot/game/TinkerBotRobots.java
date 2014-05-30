package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class TinkerBotRobots {

	protected TinkerBot p;
	protected PGraphics pg;

	protected PShape _barSvg;
	protected float _barSvgWidth;
	protected float _robotX;
	protected EasingFloat _robotXOffset;
	protected float _robotXOffsetHidden;

	public TinkerBotRobots() {
		p = (TinkerBot)P.p;
		pg = p.pg;

		_barSvg = p.gameGraphics.robotBar;
		_barSvgWidth = p.svgWidth( p.gameGraphics.robotBar );
		_robotX = pg.width * 0.1f;
		_robotXOffsetHidden = pg.width * -0.3f;
		_robotXOffset = new EasingFloat( _robotXOffsetHidden, 6 );
	}
	
	public void update() {
		// easing
		_robotXOffset.update();
		
		// draw bars
		DrawUtil.setDrawCenter(pg);
		
		// draw left bar
		pg.pushMatrix();
		pg.translate( _robotX + _robotXOffset.value(), pg.height * 0.5f );
		pg.shape(_barSvg, 0, 0, _barSvgWidth, pg.height );
		pg.popMatrix();
		
		// draw right bar
		pg.pushMatrix();
		pg.translate( (pg.width - _robotX) - _robotXOffset.value(), pg.height * 0.5f );
		pg.rotate( P.PI );
		pg.shape(_barSvg, 0, 0, _barSvgWidth, pg.height );
		pg.popMatrix();
	}
	
	public void show() {
		_robotXOffset.setTarget(0);
	}

	public void hide() {
		_robotXOffset.setTarget(_robotXOffsetHidden);
	}
	
}
