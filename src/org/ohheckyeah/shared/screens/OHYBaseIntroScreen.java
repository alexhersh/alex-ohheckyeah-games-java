package org.ohheckyeah.shared.screens;

import org.ohheckyeah.shared.OHYTextMessage;
import org.ohheckyeah.shared.app.OHYBaseGame;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.OpenGLUtil;

public class OHYBaseIntroScreen {
	
	protected OHYBaseGame p;
	public PGraphics pg;
	public PGraphics canvas;
	
	protected int _showTime;
	
	public OHYBaseIntroScreen() {
		p = (OHYBaseGame) P.p;
		pg = p.pg;
		
		canvas = p.createGraphics( pg.width, pg.height, P.OPENGL );
		canvas.smooth(OpenGLUtil.SMOOTH_MEDIUM);
	}
	
	protected OHYTextMessage buildIntroText( String text ) {
		float fontSize = p.scaleH(80);
		return new OHYTextMessage( text, fontSize, pg.width, fontSize * 2f );
	}

	public void reset() {
	}
	
	public void update() {
	}

	public void animateIn() {
		_showTime = p.millis();
	}
	
	public void animateOut() {
	}
	
}
