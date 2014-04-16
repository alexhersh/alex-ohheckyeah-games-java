package org.ohheckyeah.games.catchy.screens;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.game.CatchyTextMessage;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;


public class CatchyCreditsScreen {
	
	protected Catchy p;
	public PGraphics pg;
	
	protected int _bgColor;
	protected int _borderColor;
	
	protected CatchyTextMessage _message;
	
	protected int _introScreenStartTime = 0;

	public CatchyCreditsScreen() {
		p = (Catchy) P.p;

		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_HIGH);
	}
	
	public void reset() {
		_bgColor = ColorUtil.colorFromHex("#94C65B");
		_borderColor = ColorUtil.colorFromHex("#393427");
		_introScreenStartTime = p.millis();
	}
	
	public void update() {
		pg.beginDraw();
		pg.clear();
		
		DrawUtil.setDrawCenter(pg);
		
		drawMessage();
		drawLogos();
		pg.shape( p.gameGraphics.redDivider, pg.width * 0.5f, pg.height * 0.75f, p.scaleV(p.gameGraphics.redDivider.width), p.scaleV(p.gameGraphics.redDivider.height) );
		
		pg.endDraw();
	}
	
	protected void drawMessage() {
		if( _message == null ) {
			_message = new CatchyTextMessage( "brought to you by", 60, 450, 90, 410 );
		}
		pg.pushMatrix();
		pg.translate( pg.width/2, pg.height * 0.2f );
		pg.image( _message.image(), 0, 0, _message.image().width, _message.image().height );
		pg.popMatrix();
	}
	
	protected void drawLogos() {
		pg.shape( p.gameGraphics.logoOHY, pg.width * 0.25f, pg.height * 0.5f, p.scaleV(p.gameGraphics.logoOHY.width), p.scaleV(p.gameGraphics.logoOHY.height) );
		pg.shape( p.gameGraphics.logoLegwork, pg.width * 0.5f, pg.height * 0.5f, p.scaleV(p.gameGraphics.logoLegwork.width), p.scaleV(p.gameGraphics.logoLegwork.height) );
		pg.shape( p.gameGraphics.logoModeSet, pg.width * 0.75f, pg.height * 0.5f, p.scaleV(p.gameGraphics.logoModeSet.width), p.scaleV(p.gameGraphics.logoModeSet.height) );
	}
	
}