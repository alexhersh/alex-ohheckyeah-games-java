package org.ohheckyeah.games.catchy.screens;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.Catchy.GameState;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class CatchyIntroScreens {

	protected Catchy p;
	public PGraphics pg;
	
	public int bgPadX;
	public int bgPadY;
	protected EasingFloat _drawYOffset = new EasingFloat(0, 6);
	
	protected CatchyTitleScreen _logoScreen;
	protected CatchyCreditsScreen _creditsScreen;
	
	public enum Screen {
	    TITLE, CREDITS 
	}
	protected Screen _mode;
	
	protected int _bgColor;
	
	protected int _introScreensStartTime = 0;

	public CatchyIntroScreens() {
		p = (Catchy) P.p;

		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_HIGH);
		
		bgPadX = (int) p.scaleV(20);
		bgPadY = (int) p.scaleV(20);
		
		// build sub-screens
		_logoScreen = new CatchyTitleScreen();
		_creditsScreen = new CatchyCreditsScreen();
		
		reset();
	}
	
	public void reset() {
		_bgColor = ColorUtil.colorFromHex("#ffffff");
		_introScreensStartTime = p.millis();
		
		_logoScreen.reset();
		_creditsScreen.reset();
		
		_introScreensStartTime = p.millis();
		
		_mode = Screen.TITLE;
		_drawYOffset.setCurrent(0);
		_drawYOffset.setTarget(0);
	}
	
	public void update() {
		_drawYOffset.update();
		
		pg.beginDraw();
		pg.clear();
		
		drawWhiteBg();
		drawSubScreens();
		// drawBlackLine();
		
		pg.endDraw();

		advanceScreens();
	}
	
	protected void advanceScreens() {
		if( _mode == Screen.TITLE && p.millis() > _introScreensStartTime + 4000 ) {
			_mode = Screen.CREDITS;
			_drawYOffset.setTarget(-p.height);
		}
		if( _mode == Screen.CREDITS && p.millis() > _introScreensStartTime + 9000 ) {
			p.setGameMode( GameState.GAME_WAITING_FOR_PLAYERS );
		}
	}
	
	protected void drawBlackLine() {
		pg.shape( p.gameGraphics.blackDivider, pg.width * 0.5f, pg.height * 0.95f + _drawYOffset.value(), p.scaleV(p.gameGraphics.blackDivider.width), p.scaleV(p.gameGraphics.blackDivider.height) );
	}
	
	protected void drawWhiteBg() {
		DrawUtil.setDrawCorner(pg);
		pg.fill( _bgColor );
		pg.noStroke();
		pg.rect( bgPadX, bgPadY + _drawYOffset.value(), pg.width - bgPadX * 2, pg.height * 2 - bgPadY * 2 );
	}
	
	protected void drawSubScreens() {
		DrawUtil.setDrawCenter(pg);
		_logoScreen.update();
		_creditsScreen.update();
		pg.image( _logoScreen.pg, pg.width/2f, pg.height/2f + _drawYOffset.value(), _logoScreen.pg.width, _logoScreen.pg.height );
		pg.image( _creditsScreen.pg, pg.width/2f, pg.height/2f + pg.height + _drawYOffset.value(), _creditsScreen.pg.width, _creditsScreen.pg.height );
	}
		
}
