package org.ohheckyeah.shared.screens;

import org.ohheckyeah.games.bluebear.assets.BlueBearColors;
import org.ohheckyeah.games.bluebear.assets.BlueBearGraphics;

import processing.core.PImage;

import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.system.FileUtil;


public class OHYPartnersCreditsScreen
extends OHYBaseIntroScreen {

	
	protected int _bgColor = BlueBearColors.CREDITS_BG;
	protected int _bgDotColor = BlueBearColors.CREDITS_DOTS;
	
	protected PImage _sponsorImage;
	protected float _sponsorScale = 0;
	protected boolean _drawn = false;
	
	public OHYPartnersCreditsScreen() {
		super();
		
		String sponsorsImagePath = p.appConfig.getString("partners_image", null);
		if( sponsorsImagePath != null ) {
			_sponsorImage = p.loadImage( FileUtil.getHaxademicDataPath() + sponsorsImagePath );
			float sponsorTargetH = p.scaleV(180f);
			_sponsorScale = sponsorTargetH / _sponsorImage.height;
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
		BlueBearGraphics.drawBgDots( p, canvas, _bgDotColor );
		drawMessage();
		drawLogos();
		if( _sponsorScale != 0 ) drawSponsors();
		
		canvas.endDraw();
		_drawn = true;
	}
	
	protected void drawMessage() {
		canvas.pushMatrix();
		canvas.translate( canvas.width/2, canvas.height * 0.2f );
		// canvas.shape( p.ohyGraphics.textBroughtToYou, 0, 0, p.svgWidth(p.ohyGraphics.textBroughtToYou), p.svgHeight(p.ohyGraphics.textBroughtToYou) );
		canvas.popMatrix();
	}
	
	protected void drawLogos() {
		canvas.shape( p.ohyGraphics.logoOhyTeam, canvas.width * 0.5f, canvas.height * 0.5f, p.scaleV(p.ohyGraphics.logoOhyTeam.width), p.scaleV(p.ohyGraphics.logoOhyTeam.height) );
	}
	
	protected void drawSponsors() {
		canvas.pushMatrix();
		canvas.translate( canvas.width/2, canvas.height * 0.79f );
		canvas.image( _sponsorImage, 0, 0, _sponsorImage.width * _sponsorScale, _sponsorImage.height * _sponsorScale );
		canvas.popMatrix();
	}
	
}