package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.assets.CatchyColors;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.text.StringFormatter;

public class CatchyGameTimer {
	
	protected Catchy p;
	protected PGraphics pg;
	protected int _gameStartTime = 0;
	protected int _gameEndTime = 0;
	protected int _curGameTime = 0;
	protected boolean _active = false;
	public static int GAME_LENGTH_SECONDS = 30;
	protected int GAME_LENGTH = GAME_LENGTH_SECONDS * 1000;
	protected CustomFontText2D _timerFontRenderer;
	
	protected EasingFloat _offsetY;
	protected float _offsetYShowing;
	protected float _offsetYHiding;

	public CatchyGameTimer( int gameSeconds ) {
		p = (Catchy)P.p;
		pg = p.pg;
		
		_timerFontRenderer = new CustomFontText2D( p, p.gameGraphics.font, p.scaleV(60), CatchyColors.MAIN_TEXT_COLOR, CustomFontText2D.ALIGN_LEFT, (int) p.scaleV(p.gameGraphics.timerBanner.width), (int)p.scaleV(80) );
		
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
		_active = true;
	}
	
	public void update() {
		_curGameTime = GAME_LENGTH - ( p.millis() - _gameStartTime );
		if( _curGameTime < 0 && _active == true ) {
			p.setGameState( GameState.GAME_FINISHING );
			_gameEndTime = p.millis();
			_active = false;
		} else if( _gameEndTime != 0 && p.millis() > _gameEndTime + 4000 ) {
			p.setGameState( GameState.GAME_OVER );
			_gameEndTime = 0;
		}
	}
	
	public void show() {
		_offsetY.setTarget( _offsetYShowing );
		_curGameTime = GAME_LENGTH;
	}
	
	public void hide() {
		_offsetY.setTarget( _offsetYHiding );
	}
	
	/**
	 * Draws the current game's time elapsed
	 */
	public void drawTimer() {
		_offsetY.update();
		
		pg.pushMatrix();
		pg.translate( 0, _offsetY.value() );
		
		pg.shape( p.gameGraphics.timerBanner, 0, 0, p.scaleV(p.gameGraphics.timerBanner.width), p.scaleV(p.gameGraphics.timerBanner.height) );
		int time = ( _curGameTime <= 0 ) ? 0 : _curGameTime;
		_timerFontRenderer.updateText( StringFormatter.timeFromMilliseconds( time, false) );
		pg.image( _timerFontRenderer.getTextPImage(), p.scaleV(55), p.scaleV(9), _timerFontRenderer.getTextPImage().width, _timerFontRenderer.getTextPImage().height );
		
		pg.popMatrix();
	}
		
	/**
	 * Run final game cleanup
	 */
	protected void finishGame() {
//		_gameMilliseconds = p.millis() - _gameStartTime;
//		p.setGameMode( MatchGame.GAME_OVER );
	}
}
