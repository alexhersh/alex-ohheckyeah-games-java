package org.ohheckyeah.shared.screens;

import org.ohheckyeah.shared.assets.OHYColors;

import processing.core.PImage;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.system.FileUtil;


public class OHYOnereachScreen
extends OHYBaseIntroScreen {

	protected PImage _logo;
	protected PShape _textNumberText;
	protected float _letterboxScale = 0;
	protected boolean _drawn = false;
	
	public OHYOnereachScreen() {
		super();
		
		_logo = P.p.loadImage( FileUtil.getHaxademicDataPath() + "images/onereach.png" );
		_textNumberText = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/ohy-onereach-number.svg" );

		// letterbox text message to fill screen
		float imageFillRatioW = (float) pg.width / _textNumberText.width;
		float imageFillRatioH = (float) pg.height / _textNumberText.height;
		_letterboxScale = (imageFillRatioW > imageFillRatioH) ? imageFillRatioH : imageFillRatioW;
	}
	
	public void reset() {
		
	}
	
	public void update() {
		if( _drawn ) return;
		
		canvas.beginDraw();
		canvas.clear();
		canvas.background( OHYColors.INTRO_SCREENS_BG_BLUE );
		
		DrawUtil.setDrawCenter(canvas);
		canvas.pushMatrix();
		canvas.translate( canvas.width * 0.5f, canvas.height * 0.5f );
		canvas.shape( _textNumberText, 0, 0, _textNumberText.width * _letterboxScale, _textNumberText.height * _letterboxScale );
		canvas.popMatrix();
		
		DrawUtil.setDrawCorner(canvas);
		float logoScale = 0.3f;
		float logoW = p.scaleV(_logo.width) * logoScale;
		float logoH = p.scaleV(_logo.height) * logoScale;
		float logoPad = p.scaleV(20);
		canvas.image( _logo, canvas.width * 0.5f - logoW * 0.5f, canvas.height - logoH - logoPad, logoW, logoH );
		
		canvas.endDraw();
		
		_drawn = true;
	}
	
}