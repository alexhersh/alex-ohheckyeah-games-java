package org.ohheckyeah.shared.screens;

import java.util.ArrayList;

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
	
	protected ArrayList<OHYBaseIntroScreen> _screens;
	protected int _numScreens;
	protected int _screenIndex;
	protected int SCREEN_TIME = 2 * 1000;
	protected boolean _outroReady = false;
	protected OHYBaseIntroScreen _titleScreen;
	
	protected int _bgColor;
	
	protected int _introScreensStartTime = 0;

	public OHYIntroScreens( OHYBaseIntroScreen titleScreen ) {
		p = (OHYBaseGame) P.p;
		pg = p.pg;

		canvas = p.createGraphics( pg.width, pg.height, P.OPENGL );
		canvas.smooth(OpenGLUtil.SMOOTH_MEDIUM);
		_bgColor = OHYColors.INTRO_SCREENS_BG;
		
		SCREEN_TIME = p.appConfig.getInt("intro_screens_time", SCREEN_TIME) * 1000;
		
		// conditionally build array of intro screens
		String sponsorsImagePath = p.appConfig.getString("sponsors_image", null);
		boolean hasTextMessageLineService = p.appConfig.getBoolean("has_onereach", false);
		
		_screens = new ArrayList<OHYBaseIntroScreen>();
		_screens.add( new OHYHugItOutScreen() );
		if( hasTextMessageLineService ) _screens.add( new OHYOnereachScreen() );
		_titleScreen = titleScreen;
		if( titleScreen != null ) _screens.add( titleScreen );
		_screens.add( new OHYPartnersCreditsScreen() );
		if( sponsorsImagePath != null ) _screens.add( new OHYSponsorsScreen() );

		_numScreens = _screens.size();
	}
	
	public void reset() {
		_screenIndex = 0;
		_introScreensStartTime = p.millis();
		for( OHYBaseIntroScreen screen : _screens ) screen.reset();
		_screens.get(0).animateIn();
		
		_drawYOffset.setCurrent(pg.height);
		_drawYOffset.setTarget(0);
		
		p.ohySounds.playOHY();
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
		if( _outroReady == false && p.millis() > _introScreensStartTime + SCREEN_TIME - 600 ) {
			_outroReady = true;
			if( _screenIndex < _numScreens ) _screens.get(_screenIndex).animateOut();
		}
		
		// switch to next screen when time is up
		if( p.millis() > _introScreensStartTime + SCREEN_TIME ) {
			_introScreensStartTime = p.millis();
			_screenIndex++;
			_outroReady = false;
			if( _screenIndex < _numScreens ) {
				_screens.get(_screenIndex).animateIn();
				if( _screens.get(_screenIndex) == _titleScreen ) p.ohySounds.playIntro(); 
			}
			if( _screenIndex == _numScreens ) {
				p.setGameState( GameState.GAME_INTRO_OUTRO ); // tell app we're about to slide away to reveal game
				p.ohySounds.fadeOutIntro();
			}
			_drawYOffset.setTarget(-pg.height * _screenIndex);
		}
		
		// once last screen is finished scrolling away, we're done!
		if( _drawYOffset.value() < -canvas.height * _numScreens + 1 ) {
			p.setGameState( GameState.GAME_WAITING_FOR_PLAYERS );
		}
	}
	
	protected void drawSubScreens() {
		DrawUtil.setDrawCorner(canvas);
		OHYBaseIntroScreen screen;
		for (int i=0; i < _numScreens; i++) {
			float screenY = _drawYOffset.value() + canvas.height * i;
			screen = _screens.get(i);
			if( screenY > -pg.height && screenY < pg.height ) {
				screen.update();
				canvas.image( screen.canvas, 0, screenY, screen.canvas.width, screen.canvas.height );
			}
		}
	}
		
}
