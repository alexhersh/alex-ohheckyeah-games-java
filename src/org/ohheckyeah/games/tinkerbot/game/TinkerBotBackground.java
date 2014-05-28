package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;

public class TinkerBotBackground {

	protected TinkerBot p;
	protected PGraphics pg;

	protected PGraphics _bg;
	protected PGraphics _mask;
	protected float _maskRadius;

	public TinkerBotBackground() {
		p = (TinkerBot)P.p;
		pg = p.pg;
		
		
		_mask = p.createGraphics( p.width, p.height, P.OPENGL );
		_mask.smooth(OpenGLUtil.SMOOTH_MEDIUM);

		_bg = p.createGraphics( p.width, p.height, P.OPENGL );
		_bg.smooth(OpenGLUtil.SMOOTH_MEDIUM);

		showFullMask();
	}
	
	public void startUnmasking() {
		_maskRadius = 0;
	}
	
	public void showFullMask() {
		_maskRadius = pg.width * 1.99f;
	}
	
	public void update() {
		if( _maskRadius < pg.width * 2f ) {
			_maskRadius += pg.width * 0.035f;
			DrawUtil.setDrawCenter(_mask);
			_mask.beginDraw();
			_mask.background(0);
			_mask.fill(255);
			_mask.pushMatrix();
			_mask.translate( _mask.width / 2f, _mask.height / 2f );
			_mask.shape( p.gameGraphics.gearBackgroundMask, 0, 0, _maskRadius, _maskRadius );
			_mask.popMatrix();
			_mask.endDraw();
			
			_bg.beginDraw();
			_bg.image(p.gameGraphics.gameplayBackgroundImage, 0, 0);
			_bg.endDraw();
			
			_bg.mask( _mask );
		}
		
		pg.image( _bg, pg.width * 0.5f, pg.height * 0.5f, _bg.width, _bg.height );
	}

}
