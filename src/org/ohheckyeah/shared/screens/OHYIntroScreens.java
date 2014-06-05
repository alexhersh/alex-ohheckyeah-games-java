package org.ohheckyeah.shared.screens;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.assets.BlueBearColors;
import org.ohheckyeah.games.bluebear.screens.BlueBearTitleScreen;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class OHYIntroScreens {

	protected BlueBear p;
	public PGraphics pg;
	public PGraphics canvas;
	
	protected EasingFloat _drawYOffset = new EasingFloat(0, 6);
	
	protected OHYBaseIntroScreen _screens[];
	
	protected BlueBearTitleScreen _logoScreen;
	protected OHYPartnersCreditsScreen _partnerCreditsScreen;
	protected OHYHugItOutScreen _hugItOutScreen;
	
	public enum Screen {
	    HUG_IT_OUT, TITLE, CREDITS, DONE 
	}
	protected Screen _mode;
	
	protected int _bgColor;
	
	protected int _introScreensStartTime = 0;

	public OHYIntroScreens() {
		p = (BlueBear) P.p;
		pg = p.pg;

		canvas = p.createGraphics( pg.width, pg.height, P.OPENGL );
		canvas.smooth(OpenGLUtil.SMOOTH_LOW);
		
		_screens = new OHYBaseIntroScreen[1];
		// build sub-screens
		_logoScreen = new BlueBearTitleScreen();
		_partnerCreditsScreen = new OHYPartnersCreditsScreen();
		_hugItOutScreen = new OHYHugItOutScreen();
		
		reset();
	}
	
	public void reset() {
		_bgColor = BlueBearColors.INTRO_SCREENS_BG;
		_introScreensStartTime = p.millis();
		
		_logoScreen.reset();
		_partnerCreditsScreen.reset();
		_hugItOutScreen.reset();
		
		_introScreensStartTime = p.millis();
		
		_mode = Screen.HUG_IT_OUT;
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
		if( _mode == Screen.HUG_IT_OUT && p.millis() > _introScreensStartTime + 1000 ) {
			_mode = Screen.TITLE;
			_drawYOffset.setTarget(-pg.height);
			p.sounds.playIntro();
			_logoScreen.reset();
		}
		if( _mode == Screen.TITLE && p.millis() > _introScreensStartTime + 2500 && p.millis() < _introScreensStartTime + 2600 ) {
			_logoScreen.outroDown();
		}
		if( _mode == Screen.TITLE && p.millis() > _introScreensStartTime + 2600 && p.millis() < _introScreensStartTime + 3000 ) {
			_logoScreen.outroUp();
		}
		if( _mode == Screen.TITLE && p.millis() > _introScreensStartTime + 3000 ) {
			_mode = Screen.CREDITS;
			_drawYOffset.setTarget(-pg.height * 2f);
		}
		if( _mode == Screen.CREDITS && p.millis() > _introScreensStartTime + 4000 ) {
			_mode = Screen.DONE;
			_drawYOffset.setTarget(-pg.height * 3f);
			p.setGameState( GameState.GAME_INTRO_OUTRO );
		}
		if( _mode == Screen.DONE && _drawYOffset.value() < _drawYOffset.target() + 0.1f ) {
			p.setGameState( GameState.GAME_WAITING_FOR_PLAYERS );
		}
	}
	
	protected void drawSubScreens() {
		DrawUtil.setDrawCenter(canvas);
		if( _drawYOffset.value() > -pg.height + 1 ) {
			_hugItOutScreen.update();
			canvas.image( _hugItOutScreen.canvas, canvas.width/2f, canvas.height/2f + 0 + _drawYOffset.value(), _hugItOutScreen.canvas.width, _hugItOutScreen.canvas.height );
		}
		if( _drawYOffset.value() > -pg.height * 2 + 1 && _drawYOffset.value() < -1 ) {
			_logoScreen.update();
			canvas.image( _logoScreen.canvas, canvas.width/2f, canvas.height/2f + canvas.height + _drawYOffset.value(), _logoScreen.canvas.width, _logoScreen.canvas.height );
		}
		if( _drawYOffset.value() < -pg.height - 1 ) {
			_partnerCreditsScreen.update();
			canvas.image( _partnerCreditsScreen.canvas, canvas.width/2f, canvas.height/2f + canvas.height * 2 + _drawYOffset.value(), _partnerCreditsScreen.canvas.width, _partnerCreditsScreen.canvas.height );
		}
	}
		
}
