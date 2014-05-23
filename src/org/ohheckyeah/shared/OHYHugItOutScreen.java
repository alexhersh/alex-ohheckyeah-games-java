package org.ohheckyeah.shared;

import java.util.ArrayList;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;

public class OHYHugItOutScreen {
	
	protected OHYBaseGame p;
	public PGraphics pg;
	
	protected ArrayList<OHYTextMessage> _messages;
	protected int _messagesIndex = 0;
	
	public OHYHugItOutScreen() {
		p = (OHYBaseGame) P.p;

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
		pg.background(255);
		DrawUtil.setDrawCenter(pg);
		
		drawMessage();
		pg.shape( p.ohyGraphics.logoOHY, pg.width * 0.5f, pg.height * 0.75f, p.scaleV(p.ohyGraphics.logoOHY.width), p.scaleV(p.ohyGraphics.logoOHY.height) );

		pg.endDraw();
	}
	
	protected void drawMessage() {
		if( _messages == null ) {
			_messages = new ArrayList<OHYTextMessage>();
			_messages.add( new OHYTextMessage( "#OhHeckYeah", 60, 450, 90, 410 ) );
			_messages.add( new OHYTextMessage( "Now hug it out", 60, 450, 90, 410 ) );
			_messages.add( new OHYTextMessage( "How about a high five?", 60, 1050, 90, 1050 ) );
			_messages.add( new OHYTextMessage( "Meet someone new today!", 60, 850, 90, 850 ) );
			_messages.add( new OHYTextMessage( "Nice match!", 60, 850, 90, 850 ) );
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