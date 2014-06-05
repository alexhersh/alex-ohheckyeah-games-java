package org.ohheckyeah.shared.screens;

import org.ohheckyeah.shared.assets.OHYColors;

import processing.core.PImage;

import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.system.FileUtil;


public class OHYSponsorsScreen
extends OHYBaseIntroScreen {

	
	protected int _bgColor = OHYColors.INTRO_SCREENS_BG;
	
	protected PImage _sponsorImage;
	protected float _sponsorScale = 0;
	protected boolean _drawn = false;
	
	public OHYSponsorsScreen() {
		super();
		
		String sponsorsImagePath = p.appConfig.getString("sponsors_image", null);
		if( sponsorsImagePath != null ) {
			_sponsorImage = p.loadImage( FileUtil.getHaxademicDataPath() + sponsorsImagePath );
			
			// letterbox sponsors to fill screen
			float imageFillRatioW = (float) pg.width / _sponsorImage.width;
			float imageFillRatioH = (float) pg.height / _sponsorImage.height;
			_sponsorScale = (imageFillRatioW > imageFillRatioH) ? imageFillRatioH : imageFillRatioW;
		}
	}
	
	public void reset() {
		
	}
	
	public void update() {
		if( _drawn ) return;
		
		canvas.beginDraw();
		canvas.clear();
		
		DrawUtil.setDrawCenter(canvas);
		
		canvas.background(_bgColor);
//		drawMessage();
		if( _sponsorScale != 0 ) drawPartners();
		
		canvas.endDraw();
		_drawn = true;
	}
	
	protected void drawMessage() {
		canvas.pushMatrix();
		canvas.translate( canvas.width * 0.5f, canvas.height * 0.2f );
		// canvas.shape( p.ohyGraphics.textBroughtToYou, 0, 0, p.svgWidth(p.ohyGraphics.textBroughtToYou), p.svgHeight(p.ohyGraphics.textBroughtToYou) );
		canvas.popMatrix();
	}
	
	protected void drawPartners() {
		canvas.pushMatrix();
		canvas.translate( canvas.width * 0.5f, canvas.height * 0.5f );
		canvas.image( _sponsorImage, 0, 0, _sponsorImage.width * _sponsorScale, _sponsorImage.height * _sponsorScale );
		canvas.popMatrix();
	}
	
}