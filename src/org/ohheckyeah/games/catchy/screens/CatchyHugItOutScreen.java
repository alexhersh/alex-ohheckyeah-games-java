package org.ohheckyeah.games.catchy.screens;

import java.util.ArrayList;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.game.CatchyTextMessage;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;

public class CatchyHugItOutScreen {
	
	protected Catchy p;
	public PGraphics pg;
	
	protected ArrayList<CatchyTextMessage> _messages;
	protected int _messagesIndex = 0;
	
	public CatchyHugItOutScreen() {
		p = (Catchy) P.p;

		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
	}
	
	public void reset() {
		if( _messages == null ) return;
		_messagesIndex++;
		if( _messagesIndex > _messages.size() - 1 ) _messagesIndex = 0;
	}
	
	public void update() {
		pg.beginDraw();
		pg.clear();
		
		DrawUtil.setDrawCenter(pg);
		
		drawMessage();
		pg.shape( p.gameGraphics.logoOHY, pg.width * 0.5f, pg.height * 0.75f, p.scaleV(p.gameGraphics.logoOHY.width), p.scaleV(p.gameGraphics.logoOHY.height) );

		pg.endDraw();
	}
	
	protected void drawMessage() {
		if( _messages == null ) {
			_messages = new ArrayList<CatchyTextMessage>();
			_messages.add( new CatchyTextMessage( "try meeting someone new!", 60, 650, 90, 650 ) );
			_messages.add( new CatchyTextMessage( "now hug it out", 60, 450, 90, 410 ) );
			_messages.add( new CatchyTextMessage( "how about a high five?", 60, 650, 90, 650 ) );
		}
		pg.pushMatrix();
		pg.translate( pg.width/2, pg.height * 0.4f );
		pg.image( 
			_messages.get(_messagesIndex).image(), 
			0, 
			0, 
			_messages.get(_messagesIndex).image().width, 
			_messages.get(_messagesIndex).image().height 
		);
		pg.popMatrix();
	}
	
}
