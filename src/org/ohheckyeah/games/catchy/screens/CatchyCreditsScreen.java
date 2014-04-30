package org.ohheckyeah.games.catchy.screens;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.assets.CatchyColors;
import org.ohheckyeah.games.catchy.game.CatchyTextMessage;

import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.system.FileUtil;


public class CatchyCreditsScreen {
	
	protected Catchy p;
	public PGraphics pg;
	
	protected int _bgColor = CatchyColors.CREDITS_BG;;
	
	protected CatchyTextMessage _message;
	protected PImage _sponsorImage;
	protected float _sponsorScale = 0;
	protected float _noSponsorOffsetY = 0;
	
	public CatchyCreditsScreen( String sponsorsImagePath ) {
		p = (Catchy) P.p;

		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_HIGH);
		
		if( sponsorsImagePath != null ) {
			_sponsorImage = p.loadImage( FileUtil.getHaxademicDataPath() + sponsorsImagePath );
			float sponsorTargetH = p.scaleV(180f);
			_sponsorScale = sponsorTargetH / _sponsorImage.height;
		} else {
			_noSponsorOffsetY = pg.height * 0.15f;
		}
	}
	
	public void reset() {
		
	}
	
	public void update() {
		pg.beginDraw();
		pg.clear();
		
		DrawUtil.setDrawCenter(pg);
		
		drawMessage();
		drawLogos();
		if( _sponsorScale != 0 ) drawSponsors();
		pg.shape( p.gameGraphics.redDivider, pg.width * 0.5f, pg.height * 0.6f + _noSponsorOffsetY, p.scaleV(p.gameGraphics.redDivider.width), p.scaleV(p.gameGraphics.redDivider.height) );
		
		pg.endDraw();
	}
	
	protected void drawMessage() {
		if( _message == null ) {
			_message = new CatchyTextMessage( "brought to you by", 60, 450, 90, 410 );
		}
		pg.pushMatrix();
		pg.translate( pg.width/2, pg.height * 0.15f + _noSponsorOffsetY );
		pg.image( _message.image(), 0, 0, _message.image().width, _message.image().height );
		pg.popMatrix();
	}
	
	protected void drawLogos() {
		pg.shape( p.gameGraphics.logoOHY, pg.width * 0.25f, pg.height * 0.4f + _noSponsorOffsetY, p.scaleV(p.gameGraphics.logoOHY.width), p.scaleV(p.gameGraphics.logoOHY.height) );
		pg.shape( p.gameGraphics.logoLegwork, pg.width * 0.5f, pg.height * 0.4f + _noSponsorOffsetY, p.scaleV(p.gameGraphics.logoLegwork.width), p.scaleV(p.gameGraphics.logoLegwork.height) );
		pg.shape( p.gameGraphics.logoModeSet, pg.width * 0.75f, pg.height * 0.4f + _noSponsorOffsetY, p.scaleV(p.gameGraphics.logoModeSet.width), p.scaleV(p.gameGraphics.logoModeSet.height) );
	}
	
	protected void drawSponsors() {
		pg.pushMatrix();
		int sponsorY = P.round(pg.height * 0.79f);
		if( sponsorY % 2 == 1 ) sponsorY++;
		int imageH = P.round(_sponsorImage.height * _sponsorScale);
		if( imageH % 2 == 1 ) imageH++;
		pg.translate( P.round(pg.width/2), sponsorY );
		pg.image( _sponsorImage, 0, 0, P.round(_sponsorImage.width * _sponsorScale), imageH );
		pg.popMatrix();
	}
	
}
