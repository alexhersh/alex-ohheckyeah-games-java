package org.ohheckyeah.games.tinkerbot.screens;

import org.ohheckyeah.games.tinkerbot.TinkerBot;
import org.ohheckyeah.games.tinkerbot.assets.TinkerBotColors;

import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.system.FileUtil;


public class TinkerBotCreditsScreen {
	
	protected TinkerBot p;
	public PGraphics pg;
	
	protected int _bgColor = TinkerBotColors.CREDITS_BG;
	protected int _bgDotColor = TinkerBotColors.CREDITS_DOTS;
	
	protected PImage _sponsorImage;
	protected float _sponsorScale = 0;
	
	public TinkerBotCreditsScreen( String sponsorsImagePath ) {
		p = (TinkerBot) P.p;

		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
		
		if( sponsorsImagePath != null ) {
			_sponsorImage = p.loadImage( FileUtil.getHaxademicDataPath() + sponsorsImagePath );
			float sponsorTargetH = p.scaleV(180f);
			_sponsorScale = sponsorTargetH / _sponsorImage.height;
		}
	}
	
	public void reset() {
		
	}
	
	public void update() {
		pg.beginDraw();
		pg.clear();
		
		DrawUtil.setDrawCenter(pg);
		
		pg.background(_bgColor);
		drawMessage();
		drawLogos();
		if( _sponsorScale != 0 ) drawSponsors();
		
		pg.endDraw();
	}
	
	protected void drawMessage() {
		pg.pushMatrix();
		pg.translate( pg.width/2, pg.height * 0.2f );
		pg.shape( p.gameGraphics.textBroughtToYou, 0, 0, p.svgWidth(p.gameGraphics.textBroughtToYou), p.svgHeight(p.gameGraphics.textBroughtToYou) );
		pg.popMatrix();
	}
	
	protected void drawLogos() {
		pg.shape( p.ohyGraphics.logoOhyTeam, pg.width * 0.5f, pg.height * 0.5f, p.scaleV(p.ohyGraphics.logoOhyTeam.width), p.scaleV(p.ohyGraphics.logoOhyTeam.height) );
	}
	
	protected void drawSponsors() {
		pg.pushMatrix();
		pg.translate( pg.width/2, pg.height * 0.79f );
		pg.image( _sponsorImage, 0, 0, _sponsorImage.width * _sponsorScale, _sponsorImage.height * _sponsorScale );
		pg.popMatrix();
	}
	
}