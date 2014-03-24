package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.system.FileUtil;
import com.haxademic.core.text.StringFormatter;

public class CatchyGameTimer {
	
	protected Catchy p;
	protected int _gameStartTime = 0;
	protected int _curGameTime = 0;
	protected int GAME_LENGTH = 3 * 1000;
	protected CustomFontText2D _timerFontRenderer;

	public CatchyGameTimer() {
		p = (Catchy)P.p;
		String timerFont = FileUtil.getHaxademicDataPath() + "fonts/coders_crux.ttf";
		_timerFontRenderer = new CustomFontText2D( p, timerFont, 40.0f, ColorUtil.colorFromHex("#000000"), CustomFontText2D.ALIGN_LEFT, 250, 80 );
	}
	
	public void startTimer() {
		_gameStartTime = p.millis();
		_curGameTime = GAME_LENGTH;
	}
	
	public void update() {
		_curGameTime = GAME_LENGTH - ( p.millis() - _gameStartTime );
		if( _curGameTime <= 0 ) {
			p.setGameMode( Catchy.GAME_OVER );
		}
	}
	
	/**
	 * Draws the current game's time elapsed
	 */
	public void drawTimer() {
		_timerFontRenderer.updateText( StringFormatter.timeFromMilliseconds( _curGameTime, false) );
		p.image( _timerFontRenderer.getTextPImage(), 0, 0 );
	}
		
	/**
	 * Run final game cleanup
	 */
	protected void finishGame() {
//		_gameMilliseconds = p.millis() - _gameStartTime;
//		p.setGameMode( MatchGame.GAME_OVER );
	}
}
