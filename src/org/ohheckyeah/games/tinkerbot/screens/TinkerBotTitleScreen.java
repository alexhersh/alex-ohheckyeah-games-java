package org.ohheckyeah.games.tinkerbot.screens;

import org.ohheckyeah.games.tinkerbot.TinkerBot;
import org.ohheckyeah.games.tinkerbot.assets.TinkerBotColors;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class TinkerBotTitleScreen {
	
	protected TinkerBot p;
	public PGraphics pg;
	public boolean _bgCached = false;
	
	protected int _bgColor;
	
	protected ElasticFloat _logoScale = new ElasticFloat(0, 0.75f, 0.1f);
	protected ElasticFloat _logoRot = new ElasticFloat(0, 0.75f, 0.1f);
	
	public TinkerBotTitleScreen() {
		p = (TinkerBot) P.p;
		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
	}
	
	public void reset() {
		_bgColor = TinkerBotColors.TITLE_SCREEN_BG;
		_logoScale.setValue(0);
		_logoScale.setTarget(1);
		_logoRot.setValue(-P.TWO_PI * 2f);
		_logoRot.setTarget(0);
	}
	
	public void outroDown() {
		_logoScale.setTarget(1);
	}
	
	public void outroUp() {
		_logoScale.setTarget(0);
	}
	
	public void update() {
		pg.beginDraw();
		pg.clear();
		
		drawBackground();
		drawLogo();
		
		pg.endDraw();
	}
	
	protected void drawBackground() {
		DrawUtil.setDrawCenter(pg);
		pg.image( p.gameGraphics.gameplayBackgroundImage, pg.width * 0.5f, pg.height * 0.5f, p.gameGraphics.gameplayBackgroundImage.width, p.gameGraphics.gameplayBackgroundImage.height );
	}
	
	protected void drawLogo() {
		DrawUtil.setDrawCenter(pg);
		_logoScale.update();
		_logoRot.update();
		if( _logoScale.val() > 0 ) {
			pg.pushMatrix();
			pg.translate( pg.width * 0.5f, pg.height * 0.5f );
			pg.rotate( _logoRot.val() );
			pg.shape( p.gameGraphics.tinkerBotLogo, 0, 0, p.scaleV(p.gameGraphics.tinkerBotLogo.width) * _logoScale.val(), p.scaleV(p.gameGraphics.tinkerBotLogo.height) * _logoScale.val() );
			pg.popMatrix();
		}
	}
	
}