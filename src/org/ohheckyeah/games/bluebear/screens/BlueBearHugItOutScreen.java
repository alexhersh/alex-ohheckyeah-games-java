package org.ohheckyeah.games.bluebear.screens;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.game.BlueBearTextMessage;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;

public class BlueBearHugItOutScreen {
	
	protected BlueBear p;
	public PGraphics pg;
	
	protected ArrayList<BlueBearTextMessage> _messages;
	protected int _messagesIndex = 0;
	
	public BlueBearHugItOutScreen() {
		p = (BlueBear) P.p;

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
			_messages = new ArrayList<BlueBearTextMessage>();
			_messages.add( new BlueBearTextMessage( "now hug it out", 60, 450, 90, 410 ) );
			_messages.add( new BlueBearTextMessage( "how about a high five for your opponent?", 60, 1050, 90, 1050 ) );
			_messages.add( new BlueBearTextMessage( "try meeting someone new tonight!", 60, 850, 90, 850 ) );
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