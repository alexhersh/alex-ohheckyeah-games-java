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

	public OHYTextMessage( String text, float fontSize, float baseWidth, float baseHeight ) {
		p = (OHYBaseGame) P.p;
		
		_fontSize = fontSize;
		
		messageFontRenderer = new CustomFontText2D( p, p.ohyGraphics.font, (int) fontSize, OHYColors.MAIN_TEXT_COLOR, CustomFontText2D.ALIGN_CENTER, (int) baseWidth, (int) fontSize * 2 );
		messageFontRenderer.updateText( text );

		canvas = p.createGraphics( (int) baseWidth, (int) baseHeight, P.OPENGL );
		redraw();
	}
	
	public void redraw() {
		canvas.beginDraw();
		canvas.clear();

		// draw text
		DrawUtil.setDrawCenter( canvas );
		float centerX = canvas.width/2f;
		canvas.image( messageFontRenderer.getTextPImage(), centerX, _fontSize/2f, messageFontRenderer.getTextPImage().width, messageFontRenderer.getTextPImage().height );

		// draw underline
//		canvas.fill(0);
//		float lineW = messageFontRenderer.getRightmostPixel() - messageFontRenderer.getLeftmostPixel();
//		canvas.rect( centerX, canvas.height - p.scaleV(10)/2f, lineW, p.scaleV(10) );

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
