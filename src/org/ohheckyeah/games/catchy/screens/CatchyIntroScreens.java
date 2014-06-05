package org.ohheckyeah.games.catchy.screens;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.assets.CatchyColors;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;
import org.ohheckyeah.shared.screens.OHYHugItOutScreen;

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
	protected OHYHugItOutScreen _hugItOutScreen;
	
	public enum Screen {
	    HUG_IT_OUT, TITLE, CREDITS, DONE 
	}
	protected Screen _mode;
	
	protected int _bgColor;
	
	protected int _introScreensStartTime = 0;

	public CatchyIntroScreens( String sponsorsImagePath ) {
		p = (Catchy) P.p;

		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
		
		bgPadX = (int) p.scaleV(20);
		bgPadY = (int) p.scaleV(20);
		
		// build sub-screens
		_logoScreen = new CatchyTitleScreen();
		_creditsScreen = new CatchyCreditsScreen( sponsorsImagePath );
		_hugItOutScreen = new OHYHugItOutScreen();
		
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
		if( _mode == Screen.HUG_IT_OUT && p.millis() > _introScreensStartTime + 6000 ) {
			_mode = Screen.TITLE;
			_drawYOffset.setTarget(-p.height);
			p.sounds.playIntro();
			_logoScreen.reset();
		}
		if( _mode == Screen.TITLE && p.millis() > _introScreensStartTime + 11000 ) {
			_mode = Screen.CREDITS;
			_drawYOffset.setTarget(-p.height * 2f);
		}
		if( _mode == Screen.CREDITS && p.millis() > _introScreensStartTime + 15000 ) {
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
		pg.rect( bgPadX, P.round(bgPadY + _drawYOffset.value()), pg.width - bgPadX * 2, pg.height * 3 - bgPadY * 2 );
	}
	
	protected void drawSubScreens() {
		DrawUtil.setDrawCenter(pg);
		int drawYRound = P.round(_drawYOffset.value());
		if( drawYRound > -p.height + 1 ) {
			_hugItOutScreen.update();
			pg.image( _hugItOutScreen.canvas, pg.width/2f, pg.height/2f + 0 + drawYRound, _hugItOutScreen.canvas.width, _hugItOutScreen.canvas.height );
		}
		if( drawYRound > -p.height * 2 + 1 && drawYRound < -1 ) {
			_logoScreen.update();
			pg.image( _logoScreen.pg, pg.width/2f, pg.height/2f + pg.height + drawYRound, _logoScreen.pg.width, _logoScreen.pg.height );
		}
		if( drawYRound < -p.height - 1 ) {
			_creditsScreen.update();
			pg.image( _creditsScreen.pg, pg.width/2f, pg.height/2f + pg.height * 2 + drawYRound, _creditsScreen.pg.width, _creditsScreen.pg.height );
		}
	}
		
}
