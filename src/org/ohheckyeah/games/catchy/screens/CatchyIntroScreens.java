package org.ohheckyeah.games.catchy.screens;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.Catchy.GameState;
import org.ohheckyeah.games.catchy.assets.CatchyColors;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
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
	protected CatchyHugItOutScreen _hugItOutScreen;
	
	public enum Screen {
	    HUG_IT_OUT, TITLE, CREDITS, DONE 
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
		_hugItOutScreen = new CatchyHugItOutScreen();
		
		reset();
	}
	
	public int bgPadY() {
		return bgPadY;
	}
	
	public void reset() {
		_bgColor = CatchyColors.INTRO_SCREENS_BG;
		_introScreensStartTime = p.millis();
		
		_logoScreen.reset();
		_creditsScreen.reset();
		_hugItOutScreen.reset();
		
		_introScreensStartTime = p.millis();
		
		_mode = Screen.HUG_IT_OUT;
		_drawYOffset.setCurrent(p.height);
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
		if( _mode == Screen.HUG_IT_OUT && p.millis() > _introScreensStartTime + 3000 ) {	// 6000
			_mode = Screen.TITLE;
			_drawYOffset.setTarget(-p.height);
		}
		if( _mode == Screen.TITLE && p.millis() > _introScreensStartTime + 5000 ) {			// 10000
			_mode = Screen.CREDITS;
			_drawYOffset.setTarget(-p.height * 2f);
		}
		if( _mode == Screen.CREDITS && p.millis() > _introScreensStartTime + 8000 ) {		// 14000
			_mode = Screen.DONE;
			_drawYOffset.setTarget(-p.height * 3f);
			p.setGameState( GameState.GAME_INTRO_OUTRO );
		}
		if( _mode == Screen.DONE && _drawYOffset.value() < _drawYOffset.target() + 0.1f ) {
			p.setGameState( GameState.GAME_WAITING_FOR_PLAYERS );
		}
	}
	
	protected void drawBlackLine() {
		pg.shape( p.gameGraphics.blackDivider, pg.width * 0.5f, pg.height * 0.95f + _drawYOffset.value(), p.scaleV(p.gameGraphics.blackDivider.width), p.scaleV(p.gameGraphics.blackDivider.height) );
	}
	
	protected void drawWhiteBg() {
		DrawUtil.setDrawCorner(pg);
		pg.fill( _bgColor );
		pg.noStroke();
		pg.rect( bgPadX, bgPadY + _drawYOffset.value(), pg.width - bgPadX * 2, pg.height * 3 - bgPadY * 2 );
	}
	
	protected void drawSubScreens() {
		DrawUtil.setDrawCenter(pg);
		_logoScreen.update();
		_creditsScreen.update();
		_hugItOutScreen.update();
		pg.image( _hugItOutScreen.pg, pg.width/2f, pg.height/2f + 0 + _drawYOffset.value(), _hugItOutScreen.pg.width, _hugItOutScreen.pg.height );
		pg.image( _logoScreen.pg, pg.width/2f, pg.height/2f + pg.height + _drawYOffset.value(), _logoScreen.pg.width, _logoScreen.pg.height );
		pg.image( _creditsScreen.pg, pg.width/2f, pg.height/2f + pg.height * 2 + _drawYOffset.value(), _creditsScreen.pg.width, _creditsScreen.pg.height );
	}
		
}
