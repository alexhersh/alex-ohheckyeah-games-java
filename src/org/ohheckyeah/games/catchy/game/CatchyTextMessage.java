package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.assets.CatchyColors;

import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.draw.util.DrawUtil;

public class CatchyTextMessage {
		
	protected PGraphics pg;
	protected Catchy p;

	public CatchyTextMessage( String text, float fontSize, float baseWidth, float baseHeight, float lineWidth ) {
		p = (Catchy) P.p;
		
		// build text 
		CustomFontText2D messageFontRenderer = new CustomFontText2D( p, p.gameGraphics.font, p.scaleV(fontSize), CatchyColors.MAIN_TEXT_COLOR, CustomFontText2D.ALIGN_CENTER, (int) p.scaleV(baseWidth), (int)p.scaleV(fontSize * 2) );
		messageFontRenderer.updateText( text );

		pg = p.createGraphics( (int) p.scaleV(baseWidth), (int) p.scaleV(baseHeight), P.OPENGL );
		pg.beginDraw();
		pg.clear();

		DrawUtil.setDrawCenter( pg );
		float centerX = pg.width/2f;
		pg.image( messageFontRenderer.getTextPImage(), centerX, p.scaleV(fontSize/2), messageFontRenderer.getTextPImage().width, messageFontRenderer.getTextPImage().height );

		pg.fill(0);
		pg.rect( centerX, pg.height - p.scaleV(10)/2f, p.scaleV(lineWidth), p.scaleV(10) );

		pg.endDraw();

	}

	public PImage image() {
		return pg;
	}

}
