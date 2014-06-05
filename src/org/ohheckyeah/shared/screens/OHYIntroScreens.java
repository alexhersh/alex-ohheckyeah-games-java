package org.ohheckyeah.shared.screens;

import org.ohheckyeah.shared.app.OHYBaseGame;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;
import org.ohheckyeah.shared.assets.OHYColors;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class OHYIntroScreens {

	protected OHYBaseGame p;
	public PGraphics pg;
	public PGraphics canvas;
	
	protected EasingFloat _drawYOffset = new EasingFloat(0, 5);
	
	protected OHYBaseIntroScreen _screens[];
	protected int _numScreens;
	protected int _screenIndex;
	protected int SCREEN_TIME = 2 * 1000;
	protected boolean _outroReady = false;
	
	protected int _bgColor;
	
	protected int _introScreensStartTime = 0;

	public OHYIntroScreens( OHYBaseIntroScreen titleScreen ) {
		p = (OHYBaseGame) P.p;
		pg = p.pg;

		canvas = p.createGraphics( pg.width, pg.height, P.OPENGL );
		canvas.smooth(OpenGLUtil.SMOOTH_LOW);
		_bgColor = OHYColors.INTRO_SCREENS_BG;
		
		// hug-it-out, title, partners, (sponsors,) onereach-dial
		String sponsorsImagePath = p.appConfig.getString("sponsors_image", null);
		if( sponsorsImagePath == null ) {
			_screens = new OHYBaseIntroScreen[]{
					new OHYHugItOutScreen(),
					titleScreen,
					new OHYPartnersCreditsScreen()
			};
		} else {
			_screens = new OHYBaseIntroScreen[]{
					new OHYHugItOutScreen(),
					titleScreen,
					new OHYPartnersCreditsScreen()
			};
		}
		_numScreens = _screens.length;
		
		reset();
	}
	
	public void reset() {
		_screenIndex = 0;
		_introScreensStartTime = p.millis();
		for( OHYBaseIntroScreen screen : _screens ) screen.reset();
		
		_drawYOffset.setCurrent(pg.height);
		_drawYOffset.setTarget(0);
	}
	
	public void update() {
		_drawYOffset.update();
		
		canvas.beginDraw();
		canvas.clear();
		drawSubScreens();
		canvas.endDraw();

		advanceScreens();
	}
	
	protected void advanceScreens() {
		// tell current screen to outro before the next comes in
		if( _outroReady == false && p.millis() > _introScreensStartTime + SCREEN_TIME - 400 ) {
			_outroReady = true;
			if( _screenIndex < _numScreens ) _screens[_screenIndex].animateOut();
		}
		
		// switch to next screen when time is up
		if( p.millis() > _introScreensStartTime + SCREEN_TIME ) {
			_introScreensStartTime = p.millis();
			_screenIndex++;
			_outroReady = false;
			if( _screenIndex < _numScreens ) _screens[_screenIndex].animateIn();
			if( _screenIndex == _numScreens ) p.setGameState( GameState.GAME_INTRO_OUTRO ); // tell app we're about to slide away to reveal game
			_drawYOffset.setTarget(-pg.height * _screenIndex);
		}
		
		// once last screen is finished scrolling away, we're done!
		if( _screenIndex >= _numScreens && _drawYOffset.value() < -canvas.height * _numScreens ) {
			p.setGameState( GameState.GAME_WAITING_FOR_PLAYERS );
		}
	}
	
	protected void drawSubScreens() {
		DrawUtil.setDrawCorner(canvas);
		OHYBaseIntroScreen screen;
		for (int i=0; i < _screens.length; i++) {
			float screenY = _drawYOffset.value() + canvas.height * i;
			screen = _screens[i];
			if( screenY > -pg.height && screenY < pg.height ) {
				screen.update();
				canvas.image( screen.canvas, 0, screenY, screen.canvas.width, screen.canvas.height );
			}
		}
	}
		
}
