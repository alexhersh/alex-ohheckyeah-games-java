package org.ohheckyeah.games.tinkerbot.game.display;

import org.ohheckyeah.games.tinkerbot.assets.TinkerBotColors;
import org.ohheckyeah.games.tinkerbot.game.TinkerBotGamePlay;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;

import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.text.StringFormatter;

public class TinkerBotGameTimer
extends TinkerBotBaseDisplay {
	
	protected TinkerBotGamePlay _gamePlay;
	
	protected int _gameStartTime = 0;
	protected int _gameEndTime = 0;
	protected int _curGameTime = 0;
	protected boolean _active = false;
	public static int GAME_LENGTH_SECONDS;
	protected int GAME_LENGTH;
	public static int LEVEL_LENGTH = 7000;
	protected int _levelStartTime = 0;
	protected CustomFontText2D _timerFontRenderer;
	
	public TinkerBotGameTimer( TinkerBotGamePlay gamePlay, int gameSeconds ) {
		super();
		_gamePlay = gamePlay;
		
		_timerFontRenderer = new CustomFontText2D( p, p.ohyGraphics.font, p.scaleV(FONT_SIZE), TinkerBotColors.MAIN_TEXT_COLOR, CustomFontText2D.ALIGN_CENTER, (int) p.scaleV(197), (int)p.scaleV(FONT_SIZE * 1.5f) );
		
		GAME_LENGTH_SECONDS = gameSeconds;
		GAME_LENGTH = GAME_LENGTH_SECONDS * 1000;
	}
	
	public void startTimer() {
		_gameStartTime = p.millis();
		_curGameTime = GAME_LENGTH;
		_gameEndTime = 0;
		_levelStartTime = p.millis();
		_active = true;
	}
	
	public void update() {
		super.update();
		
		// update game time and check game state
		_curGameTime = GAME_LENGTH - ( p.millis() - _gameStartTime );
		if( _curGameTime < 0 && _active == true ) {
			_active = false;
			_gameEndTime = 0;
			p.setGameState( GameState.GAME_OVER );
		}

		// check level
		if( p.gameState() == GameState.GAME_PLAYING ) {
			if( p.millis() > _levelStartTime + LEVEL_LENGTH ) {
				newLevel();
			}
		}
		
		// draw it!
		pg.pushMatrix();
		pg.translate( 0, _offsetY.value() );
		
		DrawUtil.setDrawCorner(pg);
		pg.shape( p.gameGraphics.timeBg, p.scaleV(752), 0, p.scaleV(p.gameGraphics.timeBg.width), p.scaleV(p.gameGraphics.timeBg.height) );
		int time = ( _curGameTime <= 0 ) ? 0 : _curGameTime;
		_timerFontRenderer.updateText( StringFormatter.timeFromMilliseconds( time, false) );
		pg.image( _timerFontRenderer.getTextPImage(), p.scaleV(768), p.scaleV(9), _timerFontRenderer.getTextPImage().width, _timerFontRenderer.getTextPImage().height );
		
		pg.popMatrix();
	}
	
	public void newLevel() {
		_levelStartTime = p.millis();
		_gamePlay.newLevel();
	}
	
	public void show() {
		super.show();
		_curGameTime = GAME_LENGTH;
	}
	
}
