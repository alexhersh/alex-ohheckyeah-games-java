package org.ohheckyeah.shared.screens;

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
		
		canvas = p.createGraphics( p.width, p.height, P.OPENGL );
		canvas.smooth(OpenGLUtil.SMOOTH_MEDIUM);
	}

	public void reset() {
//		// draw message once, each time the text changes
//		canvas.beginDraw();
//		canvas.clear();
//		canvas.background(255);
//
//		canvas.endDraw();
	}
	
	public void update() {
	}

	public void animateIn() {
		_showTime = p.millis();
	}
	
	public void animateOut() {
	}
	
}
