package org.ohheckyeah.games.catchy.screens;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.game.CatchyTextMessage;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;

public class CatchyHugItOutScreen {
	
	protected Catchy p;
	public PGraphics pg;
	
	protected CatchyTextMessage _message;
	
	public CatchyHugItOutScreen() {
		p = (Catchy) P.p;

		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_HIGH);
	}
	
	public void reset() {

	}
	
	public void update() {
		pg.beginDraw();
		pg.clear();
		
		DrawUtil.setDrawCenter(pg);
		
		drawMessage();
		
		pg.endDraw();
	}
	
	protected void drawMessage() {
		if( _message == null ) {
//			_message = new CatchyTextMessage( "now hug it out", 60, 450, 90, 410 );
			_message = new CatchyTextMessage( "how about a high five for your opponent?", 60, 1050, 90, 1050 );
		}
		pg.pushMatrix();
		pg.translate( pg.width/2, pg.height * 0.5f );
		pg.image( _message.image(), 0, 0, _message.image().width, _message.image().height );
		pg.popMatrix();
	}
	
}