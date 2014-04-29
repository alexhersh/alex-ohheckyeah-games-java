package org.ohheckyeah.games.bluebear.screens;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.BlueBear.GameState;
import org.ohheckyeah.games.bluebear.assets.BlueBearColors;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class BlueBearIntroScreens {

	protected BlueBear p;
	public PGraphics pg;
	
	public int bgPadX;
	public int bgPadY;
	protected EasingFloat _drawYOffset = new EasingFloat(0, 6);
	
	protected BlueBearTitleScreen _logoScreen;
	protected BlueBearCreditsScreen _creditsScreen;
	protected BlueBearHugItOutScreen _hugItOutScreen;
	
	public enum Screen {
	    HUG_IT_OUT, TITLE, CREDITS, DONE 
	}
	protected Screen _mode;
	
	protected int _bgColor;
	
	protected int _introScreensStartTime = 0;

	public BlueBearIntroScreens( String sponsorsImagePath ) {
		p = (BlueBear) P.p;

		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_HIGH);
		
		bgPadX = (int) p.scaleV(20);
		bgPadY = (int) p.scaleV(20);
		
		// build sub-screens
		_logoScreen = new BlueBearTitleScreen();
		_creditsScreen = new BlueBearCreditsScreen( sponsorsImagePath );
		_hugItOutScreen = new BlueBearHugItOutScreen();
		
		reset();
	}
	
	public int bgPadY() {
		return bgPadY;
	}
	
	public void reset() {
		_bgColor = BlueBearColors.INTRO_SCREENS_BG;
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
		
		pg.endDraw();

		advanceScreens();
	}
	
	protected void advanceScreens() {
		if( _mode == Screen.HUG_IT_OUT && p.millis() > _introScreensStartTime + 1000 ) {
			_mode = Screen.TITLE;
			_drawYOffset.setTarget(-p.height);
			p.sounds.playIntro();
			_logoScreen.reset();
		}
		if( _mode == Screen.TITLE && p.millis() > _introScreensStartTime + 2000 ) {
			_mode = Screen.CREDITS;
			_drawYOffset.setTarget(-p.height * 2f);
		}
		if( _mode == Screen.CREDITS && p.millis() > _introScreensStartTime + 3000 ) {
			_mode = Screen.DONE;
			_drawYOffset.setTarget(-p.height * 3f);
			p.setGameState( GameState.GAME_INTRO_OUTRO );
		}
		if( _mode == Screen.DONE && _drawYOffset.value() < _drawYOffset.target() + 0.1f ) {
			p.setGameState( GameState.GAME_WAITING_FOR_PLAYERS );
		}
	}
	
	protected void drawWhiteBg() {
		DrawUtil.setDrawCorner(pg);
		pg.fill( _bgColor );
		pg.noStroke();
		pg.rect( bgPadX, bgPadY + _drawYOffset.value(), pg.width - bgPadX * 2, pg.height * 3 - bgPadY * 2 );
	}
	
	protected void drawSubScreens() {
		DrawUtil.setDrawCenter(pg);
		if( _drawYOffset.value() > -p.height + 1 ) {
			_hugItOutScreen.update();
			pg.image( _hugItOutScreen.pg, pg.width/2f, pg.height/2f + 0 + _drawYOffset.value(), _hugItOutScreen.pg.width, _hugItOutScreen.pg.height );
		}
		if( _drawYOffset.value() > -p.height * 2 + 1 && _drawYOffset.value() < -1 ) {
			_logoScreen.update();
			pg.image( _logoScreen.pg, pg.width/2f, pg.height/2f + pg.height + _drawYOffset.value(), _logoScreen.pg.width, _logoScreen.pg.height );
		}
		if( _drawYOffset.value() < -p.height - 1 ) {
			_creditsScreen.update();
			pg.image( _creditsScreen.pg, pg.width/2f, pg.height/2f + pg.height * 2 + _drawYOffset.value(), _creditsScreen.pg.width, _creditsScreen.pg.height );
		}
	}
		
}
