package org.ohheckyeah.shared;

import org.ohheckyeah.shared.app.OHYBaseGame;
import org.ohheckyeah.shared.assets.OHYColors;

import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.draw.util.DrawUtil;

public class OHYTextMessage {
		
	protected OHYBaseGame p;
	protected PGraphics canvas;
	
	CustomFontText2D messageFontRenderer;
	float _fontSize;
	float _lineWidth;

	public OHYTextMessage( String text, float fontSize, float baseWidth, float baseHeight, float lineWidth ) {
		p = (OHYBaseGame) P.p;
		
		_fontSize = fontSize;
		_lineWidth = lineWidth;
		
		// build text 
		messageFontRenderer = new CustomFontText2D( p, p.ohyGraphics.font, p.scaleV(fontSize), OHYColors.MAIN_TEXT_COLOR, CustomFontText2D.ALIGN_CENTER, (int) p.scaleV(baseWidth), (int)p.scaleV(fontSize * 2) );
		messageFontRenderer.updateText( text );

		canvas = p.createGraphics( (int) p.scaleV(baseWidth), (int) p.scaleV(baseHeight), P.OPENGL );
		redraw();
	}
	
	public void redraw() {
		canvas.beginDraw();
		canvas.clear();

		DrawUtil.setDrawCenter( canvas );
		float centerX = canvas.width/2f;
		canvas.image( messageFontRenderer.getTextPImage(), centerX, p.scaleV(_fontSize/2f), messageFontRenderer.getTextPImage().width, messageFontRenderer.getTextPImage().height );

		canvas.fill(0);
		canvas.rect( centerX, canvas.height - p.scaleV(10)/2f, p.scaleV(_lineWidth), p.scaleV(10) );

		canvas.endDraw();
	}
	
	public void setText( String text ) {
		messageFontRenderer.updateText( text );
		redraw();
	}

	public PImage image() {
		return canvas;
	}
}
