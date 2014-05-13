package org.ohheckyeah.games.bluebear.screens;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.assets.BlueBearColors;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class BlueBearTitleScreen {
	
	protected BlueBear p;
	public PGraphics pg;
	
	protected int _bgColor;
	
	protected ElasticFloat _logoScale = new ElasticFloat(0, 0.87f, 0.08f);
	
	public BlueBearTitleScreen() {
		p = (BlueBear) P.p;
		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
	}
	
	public void reset() {
		_bgColor = BlueBearColors.TITLE_SCREEN_BG;
		_logoScale.setValue(0);
		_logoScale.setTarget(1);
	}
	
	public void update() {
		pg.beginDraw();
		
		pg.background( _bgColor );
		drawLogo();
		
		pg.endDraw();
	}
	
	protected void drawLogo() {
		DrawUtil.setDrawCenter(pg);
		_logoScale.update();
		pg.shape( p.gameGraphics.blueBearLogo, pg.width * 0.5f, pg.height * 0.5f, p.gameGraphics.blueBearLogo.width * p.scaleV(_logoScale.val()), p.gameGraphics.blueBearLogo.height * p.scaleV(_logoScale.val()) );
	}
	
}