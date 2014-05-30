package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;
import org.ohheckyeah.games.tinkerbot.assets.TinkerBotColors;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.math.easing.LinearFloat;

public class TinkerBotBackground {

	protected TinkerBot p;
	protected PGraphics pg;

	protected PGraphics _bg;
	protected PGraphics _mask;
	protected float _maskRadius;
	
	protected LinearFloat _levelEndOpacity = new LinearFloat(0, 0.025f);

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
	
	public void levelEnd() {
		_levelEndOpacity.setTarget(0.4f);
	}
	
	public void levelStart() {
		_levelEndOpacity.setTarget(0);
	}
	
	public void update() {
		// add mask if it's expanding to fill the screen
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
		
		// draw background
		pg.image( _bg, pg.width * 0.5f, pg.height * 0.5f, _bg.width, _bg.height );
		
		// draw color overlay if needed
		_levelEndOpacity.update();
		if( _levelEndOpacity.value() > 0.01f ) {
			DrawUtil.setDrawCorner(pg);
			pg.fill( TinkerBotColors.LEVEL_SWAP_OVERLAY, _levelEndOpacity.value() * 255f );
			pg.rect(0, 0, pg.width, pg.height);
		}
	}

}
