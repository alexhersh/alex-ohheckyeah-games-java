package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.assets.CatchyGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.text.StringFormatter;

public class CatchyGameTimer {
	
	protected Catchy p;
	protected int _gameStartTime = 0;
	protected int _gameEndTime = 0;
	protected int _curGameTime = 0;
	protected int GAME_LENGTH = 60 * 1000;
	protected CustomFontText2D _timerFontRenderer;
	
	protected EasingFloat _offsetY;
	protected float _offsetYShowing;
	protected float _offsetYHiding;

	public CatchyGameTimer() {
		p = (Catchy)P.p;
		_timerFontRenderer = new CustomFontText2D( p, p.gameGraphics.font, p.scaleV(60), ColorUtil.colorFromHex("#000000"), CustomFontText2D.ALIGN_LEFT, (int) p.scaleV(p.gameGraphics.timerBanner.width), (int)p.scaleV(80) );
		
		_offsetYShowing = 0;
		_offsetYHiding = p.scaleV(100f);
		_offsetY = new EasingFloat(_offsetYHiding,4);
	}
	
	public void startTimer() {
		_gameStartTime = p.millis();
		_curGameTime = GAME_LENGTH;
		_gameEndTime = 0;
	}
	
	public void update() {
		_curGameTime = GAME_LENGTH - ( p.millis() - _gameStartTime );
		if( _curGameTime < 4000 && _gameEndTime == 0 ) {
			p.setGameMode( Catchy.GAME_FINISHING );
			_gameEndTime = p.millis();
		}
		if( _curGameTime <= 0 ) {
			p.setGameMode( Catchy.GAME_OVER );
		}
	}
	
	public void show() {
		_offsetY.setTarget( _offsetYShowing );
	}
	
	public void hide() {
		_offsetY.setTarget( _offsetYHiding );
	}
	
	/**
	 * Draws the current game's time elapsed
	 */
	public void drawTimer() {
		_offsetY.update();
		
		p.pushMatrix();
		p.translate( 0, _offsetY.value() );
		
		p.shape( p.gameGraphics.timerBanner, 0, 0, p.scaleV(p.gameGraphics.timerBanner.width), p.scaleV(p.gameGraphics.timerBanner.height) );
		_timerFontRenderer.updateText( StringFormatter.timeFromMilliseconds( _curGameTime, false) );
		p.image( _timerFontRenderer.getTextPImage(), p.scaleV(55), p.scaleV(9), _timerFontRenderer.getTextPImage().width, _timerFontRenderer.getTextPImage().height );
		
		p.popMatrix();
	}
		
	/**
	 * Run final game cleanup
	 */
	protected void finishGame() {
//		_gameMilliseconds = p.millis() - _gameStartTime;
//		p.setGameMode( MatchGame.GAME_OVER );
	}
}
