package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class CatchyGameMessages {
	
	protected Catchy p;
	
	protected PGraphics _playerDetectionText;
	
	protected float _messageX = 0;
	protected float _messageYHidden = 0;
	protected float _messageYShowing = 0;
		
	protected ElasticFloat _messageY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);

	public CatchyGameMessages() {
		p = (Catchy)P.p;
		
		// calculate responsive sizes
		_messageX = p.width / 2f;
		_messageYHidden = p.scaleV(-150);
		_messageYShowing = p.height * .2f;
		
	}
	
	public void update() {
		_messageY.update();

		if( _playerDetectionText == null ) {
			// build text 
			CustomFontText2D messageFontRenderer = new CustomFontText2D( p, p.gameGraphics.font, p.scaleV(60), ColorUtil.colorFromHex("#000000"), CustomFontText2D.ALIGN_CENTER, (int) p.scaleV(550), (int)p.scaleV(80) );
			messageFontRenderer.updateText( "step into a zone to join" );
			
			_playerDetectionText = p.createGraphics( (int) p.scaleV(550), (int) p.scaleV(90), P.OPENGL );
			_playerDetectionText.beginDraw();
			_playerDetectionText.clear();
			
			DrawUtil.setDrawCenter( _playerDetectionText );
			float centerX = _playerDetectionText.width/2f;
			_playerDetectionText.image( messageFontRenderer.getTextPImage(), centerX, p.scaleV(60/2), messageFontRenderer.getTextPImage().width, messageFontRenderer.getTextPImage().height );
			
			_playerDetectionText.fill(0);
			_playerDetectionText.rect( centerX, _playerDetectionText.height - p.scaleV(10)/2f, p.scaleV(526), p.scaleV(10) );
			
			_playerDetectionText.endDraw();
		}
		if( _messageY.val() > 0 ) {
			DrawUtil.setDrawCenter(p);
			p.pushMatrix();
			p.translate( _messageX, _messageY.val() );
			p.image( _playerDetectionText, 0, 0, _playerDetectionText.width, _playerDetectionText.height );
			p.popMatrix();
		}
	}
	
	public void show() {
		_messageY.setTarget(_messageYShowing);
	}
	
	public void hide() {
		_messageY.setTarget(_messageYHidden);
	}
	
}
