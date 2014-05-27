package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;
import org.ohheckyeah.games.tinkerbot.assets.TinkerBotColors;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.text.StringFormatter;

public class TinkerBotGameTimer {
	
	protected TinkerBot p;
	protected PGraphics pg;
	protected TinkerBotGamePlay _gamePlay;
	
	protected int _gameStartTime = 0;
	protected int _gameEndTime = 0;
	protected int _curGameTime = 0;
	protected boolean _active = false;
	public static int GAME_LENGTH_SECONDS;
	protected int GAME_LENGTH;
	public static int LEVEL_LENGTH = 5000;
	protected int _curLevel = 0;
	protected CustomFontText2D _timerFontRenderer;
	
	protected EasingFloat _offsetY;
	protected float _offsetYShowing;
	protected float _offsetYHiding;

	public TinkerBotGameTimer( TinkerBotGamePlay gamePlay, int gameSeconds ) {
		p = (TinkerBot)P.p;
		pg = p.pg;
		_gamePlay = gamePlay;
		
		float fontSize = 40;
		_timerFontRenderer = new CustomFontText2D( p, p.ohyGraphics.font, p.scaleV(fontSize), TinkerBotColors.MAIN_TEXT_COLOR, CustomFontText2D.ALIGN_CENTER, (int) p.scaleV(300), (int)p.scaleV(fontSize * 1.5f) );
		
		GAME_LENGTH_SECONDS = gameSeconds;
		GAME_LENGTH = GAME_LENGTH_SECONDS * 1000;
		
		_offsetYShowing = 0;
		_offsetYHiding = p.scaleV(100f);
		_offsetY = new EasingFloat(_offsetYHiding,4);
	}
	
	public void startTimer() {
		_gameStartTime = p.millis();
		_curGameTime = GAME_LENGTH;
		_gameEndTime = 0;
		_curLevel = 0;
		_active = true;
	}
	
	public void update() {
		// update game time and check game state
		_curGameTime = GAME_LENGTH - ( p.millis() - _gameStartTime );
		if( _curGameTime < 0 && _active == true ) {
			_active = false;
			_gameEndTime = 0;
			p.setGameState( GameState.GAME_OVER );
		}

		// check level
		if( p.gameState() == GameState.GAME_PLAYING ) {
			if( p.millis() > _gameStartTime + ( _curLevel + 1 ) * LEVEL_LENGTH ) {
				newLevel();
			}
		}
		
		// draw it!
		_offsetY.update();
		
		pg.pushMatrix();
		pg.translate( 0, _offsetY.value() );
		
		pg.shape( p.gameGraphics.timeBg, 0, 0, p.scaleV(p.gameGraphics.timeBg.width), p.scaleV(p.gameGraphics.timeBg.height) );
		int time = ( _curGameTime <= 0 ) ? 0 : _curGameTime;
		_timerFontRenderer.updateText( StringFormatter.timeFromMilliseconds( time, false) );
		pg.image( _timerFontRenderer.getTextPImage(), p.scaleV(55), p.scaleV(9), _timerFontRenderer.getTextPImage().width, _timerFontRenderer.getTextPImage().height );
		
		pg.popMatrix();

	}
	
	public void newLevel() {
		_curLevel++;
		_gamePlay.newLevel();
		P.println("LEVEL ", _curLevel);
	}
	
	public void show() {
		_offsetY.setTarget( _offsetYShowing );
		_curGameTime = GAME_LENGTH;
	}
	
	public void hide() {
		_offsetY.setTarget( _offsetYHiding );
	}
}
