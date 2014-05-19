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
	
	protected ElasticFloat _logoY = new ElasticFloat(0, 0.75f, 0.1f);
	
	public BlueBearTitleScreen() {
		p = (BlueBear) P.p;
		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
	}
	
	public void reset() {
		_bgColor = BlueBearColors.TITLE_SCREEN_BG;
		_logoY.setValue(pg.height + p.svgHeight(p.gameGraphics.blueBearLogo));
		_logoY.setTarget(pg.height / 2f);
	}
	
	public void outroDown() {
		_logoY.setTarget(pg.height / 2f + p.scaleV(150f));
	}
	
	public void outroUp() {
		_logoY.setTarget( -pg.height );
	}
	
	public void update() {
		pg.beginDraw();
		
		pg.background( _bgColor );
		drawLogo();
		
		pg.endDraw();
	}
	
	protected void drawLogo() {
		DrawUtil.setDrawCenter(pg);
		_logoY.update();
		pg.shape( p.gameGraphics.blueBearLogo, pg.width * 0.5f, _logoY.val(), p.scaleV(p.gameGraphics.blueBearLogo.width), p.scaleV(p.gameGraphics.blueBearLogo.height) );
	}
	
}