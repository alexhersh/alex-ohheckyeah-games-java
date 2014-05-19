package org.ohheckyeah.games.bluebear.screens;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.assets.BlueBearColors;

import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.system.FileUtil;


public class BlueBearCreditsScreen {
	
	protected BlueBear p;
	public PGraphics pg;
	
	protected int _bgColor = BlueBearColors.CREDITS_BG;
	protected int _bgDotColor = BlueBearColors.CREDITS_DOTS;
	protected int _bgDotSize = 45;
	protected int _bgDotSpaceX = 130;
	protected int _bgDotSpaceY = 110;
	
	protected PImage _sponsorImage;
	protected float _sponsorScale = 0;
	
	public BlueBearCreditsScreen( String sponsorsImagePath ) {
		p = (BlueBear) P.p;

		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
		
		if( sponsorsImagePath != null ) {
			_sponsorImage = p.loadImage( FileUtil.getHaxademicDataPath() + sponsorsImagePath );
			float sponsorTargetH = p.scaleV(180f);
			_sponsorScale = sponsorTargetH / _sponsorImage.height;
		}
		
		_bgDotSize = P.round(p.scaleV(_bgDotSize));
		_bgDotSpaceX = P.round(p.scaleV(_bgDotSpaceX));
		_bgDotSpaceY = P.round(p.scaleV(_bgDotSpaceY));
	}
	
	public void reset() {
		
	}
	
	public void update() {
		pg.beginDraw();
		pg.clear();
		
		DrawUtil.setDrawCenter(pg);
		
		pg.background(_bgColor);
		drawBgDots();
		drawMessage();
		drawLogos();
		if( _sponsorScale != 0 ) drawSponsors();
		
		pg.endDraw();
	}
	
	protected void drawBgDots() {
		pg.fill( _bgDotColor );
		pg.noStroke();
		int row = 0;
		for( int y=0; y < pg.height + _bgDotSize; y += _bgDotSpaceY ) {
			int startX = (row % 2 == 0) ? 20 : 20 + _bgDotSpaceX / 2;
			for( int x=startX; x < pg.width + _bgDotSize; x += _bgDotSpaceX ) {
				pg.ellipse(x, y, _bgDotSize, _bgDotSize);
			}
			row++;
		}
	}
	
	protected void drawMessage() {
		pg.pushMatrix();
		pg.translate( pg.width/2, pg.height * 0.2f );
		pg.shape( p.gameGraphics.textBroughtToYou, 0, 0, p.svgWidth(p.gameGraphics.textBroughtToYou), p.svgHeight(p.gameGraphics.textBroughtToYou) );
		pg.popMatrix();
	}
	
	protected void drawLogos() {
		pg.shape( p.gameGraphics.logoOhyTeam, pg.width * 0.5f, pg.height * 0.5f, p.scaleV(p.gameGraphics.logoOhyTeam.width), p.scaleV(p.gameGraphics.logoOhyTeam.height) );
	}
	
	protected void drawSponsors() {
		pg.pushMatrix();
		pg.translate( pg.width/2, pg.height * 0.79f );
		pg.image( _sponsorImage, 0, 0, _sponsorImage.width * _sponsorScale, _sponsorImage.height * _sponsorScale );
		pg.popMatrix();
	}
	
}