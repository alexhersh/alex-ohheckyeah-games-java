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
	
	protected float _ballWidth;
	protected float _ballHeight;

	protected EasingFloat _ballYOffset;
	protected float _ballYOffsetHidden;
	protected float _ballYOffsetShowing;
	
	public TinkerBotRobots() {
		p = (TinkerBot)P.p;
		pg = p.pg;

		_barSvg = p.gameGraphics.robotBar;
		_barSvgWidth = p.svgWidth( p.gameGraphics.robotBar );
		_robotX = pg.width * 0.1f;
		_robotXOffsetHidden = pg.width * -0.3f;
		_robotXOffset = new EasingFloat( _robotXOffsetHidden, 6 );
		
		_ballWidth = p.svgWidth( p.gameGraphics.robotBallTop );
		_ballHeight = p.svgHeight( p.gameGraphics.robotBallTop );
		
		_ballYOffsetHidden = _ballHeight * 1f;
		_ballYOffsetShowing = _ballHeight * 0.1f;
		_ballYOffset = new EasingFloat( _ballYOffsetHidden, 6 );
	}
	
	public void show() {
		_robotXOffset.setTarget(0);
		_ballYOffset.setTarget(_ballYOffsetShowing);
	}

	public void hide() {
		_robotXOffset.setTarget(_robotXOffsetHidden);
		_ballYOffset.setTarget(_ballYOffsetHidden);
	}
	
	public void update() {
		// easing
		_robotXOffset.update();
		_ballYOffset.update();
		
		// draw bars
		DrawUtil.setDrawCenter(pg);
		
		// left robot ================================================================
		float leftX = _robotX + _robotXOffset.value();
		
		// draw left bar
		pg.pushMatrix();
		pg.translate( leftX, pg.height * 0.5f );
		pg.shape(_barSvg, 0, 0, _barSvgWidth, pg.height );
		pg.popMatrix();
		
		// draw top robot ball
		pg.pushMatrix();
		pg.translate( leftX, 0 - _ballYOffset.value() );
		pg.shape(p.gameGraphics.robotBallTop, 0, 0, _ballWidth, _ballHeight );
		pg.popMatrix();
		
		// draw bottom robot ball
		pg.pushMatrix();
		pg.translate( leftX, pg.height + _ballYOffset.value() );
		pg.shape(p.gameGraphics.robotBallBottom, 0, 0, _ballWidth, _ballHeight );
		pg.popMatrix();
		
		
		// right robot ================================================================
		float rightX = (pg.width - _robotX) - _robotXOffset.value();
		
		// draw right bar
		pg.pushMatrix();
		pg.translate( rightX, pg.height * 0.5f );
		pg.rotate( P.PI );
		pg.shape(_barSvg, 0, 0, _barSvgWidth, pg.height );
		pg.popMatrix();

		// draw top robot ball
		pg.pushMatrix();
		pg.translate( rightX, 0 - _ballYOffset.value() );
		pg.rotate( P.PI );
		pg.shape(p.gameGraphics.robotBallBottom, 0, 0, _ballWidth, _ballHeight );
		pg.popMatrix();
		
		// draw bottom robot ball
		pg.pushMatrix();
		pg.translate( rightX, pg.height + _ballYOffset.value() );
		pg.rotate( P.PI );
		pg.shape(p.gameGraphics.robotBallTop, 0, 0, _ballWidth, _ballHeight );
		pg.popMatrix();

	}
	
}
