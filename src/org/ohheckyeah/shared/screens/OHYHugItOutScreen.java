package org.ohheckyeah.shared.screens;

import java.util.ArrayList;

import org.ohheckyeah.shared.OHYTextMessage;
import org.ohheckyeah.shared.assets.OHYColors;

import com.haxademic.core.draw.util.DrawUtil;

public class OHYHugItOutScreen
extends OHYBaseIntroScreen {
	
	protected ArrayList<String> _messages;
	protected OHYTextMessage _text;
	protected int _messagesIndex = 0;
	
	public OHYHugItOutScreen() {
		super();

		_messages = new ArrayList<String>();
		_messages.add( "#OhHeckYeah" );
		_messages.add( "Now hug it out" );
		_messages.add( "How about a high five?" );
		_messages.add( "Oh. Heck. Yeah." );
		_messages.add( "Meet someone new today!" );
		_messages.add( "Nice match!" );
		_messages.add( "Everybody wins, sometimes" );
		
		int fontSize = 80;
		_text = new OHYTextMessage( _messages.get(0), p.scaleV(fontSize), pg.width, p.scaleV(fontSize) * 1.5f );
	}
	
	public void reset() {
		// switch to next message
		_messagesIndex++;
		if( _messagesIndex > _messages.size() - 1 ) _messagesIndex = 0;
		_text.setText( _messages.get(_messagesIndex) );
		
		// draw message once, each time the text changes
		canvas.beginDraw();
		canvas.clear();
		canvas.background(255);
		DrawUtil.setDrawCenter(canvas);
		
		// draw OHY logo
		drawMessage();
		p.ohyGraphics.logoOHY.disableStyle();
		canvas.fill( OHYColors.LOGO_COLOR );
		canvas.shape( p.ohyGraphics.logoOHY, canvas.width * 0.5f, canvas.height * 0.75f, p.scaleV(p.ohyGraphics.logoOHY.width), p.scaleV(p.ohyGraphics.logoOHY.height) );

		canvas.endDraw();
	}
	
	public void animateIn() {
		super.animateIn();
	}
	
	public void animateOut() {
	}
	
	public void update() {
		// no need to update since we've cached the screen
	}
	
	protected void drawMessage() {
		canvas.pushMatrix();
		canvas.translate( canvas.width * 0.5f, canvas.height * 0.4f );
		canvas.image( _text.image(), 0, 0, _text.image().width, _text.image().height );
		canvas.popMatrix();
	}
	
}