package org.ohheckyeah.games.tinkerbot.game.display;

import org.ohheckyeah.games.tinkerbot.assets.TinkerBotColors;
import org.ohheckyeah.games.tinkerbot.game.TinkerBotGamePlay;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;

public class TinkerBotLevelTimer
extends TinkerBotBaseDisplay {
	
	protected TinkerBotGamePlay _gamePlay;
	
	protected int TOTAL_BARS = 23;
	protected int _curBars;
	protected int _startTime;
	
	
	public TinkerBotLevelTimer() {
		super();
	}
	
	public void startTimer() {
		_startTime = p.millis();
		_curBars = 0;
	}
	
	public void reset() {
		_startTime = 0;
		_curBars = 0;
	}
	
	public void update() {
		super.update();
		
		// draw it!
		pg.pushMatrix();
		pg.translate( p.scaleH(296), _offsetY.value() );
		
		// draw bg
		DrawUtil.setDrawCorner(pg);
		pg.shape( p.gameGraphics.levelTimerBg, 0, 0, p.scaleH(p.gameGraphics.levelTimerBg.width), p.scaleH(p.gameGraphics.levelTimerBg.height) );
		
		// draw bars
		if( _startTime != 0 ) {
			pg.translate( p.scaleH(38), p.scaleH(20) );
			float percentDone = (float) ( p.millis() - _startTime ) / (float) TinkerBotGameTimer.LEVEL_LENGTH;
			_curBars = P.ceil( percentDone * TOTAL_BARS );
			if( _curBars > TOTAL_BARS ) _curBars = TOTAL_BARS;
			pg.fill( TinkerBotColors.TIMER_BARS_COLOR );
			for( int i=0; i < _curBars; i++ ) {
				pg.rect( i * p.scaleH(17), 0, p.scaleH(13), p.scaleH(55) );
			}
			// last partial bar
			if( _curBars < TOTAL_BARS ) {
				float remainder = 1 - (_curBars - (percentDone * TOTAL_BARS));
				pg.fill( TinkerBotColors.TIMER_BARS_COLOR, remainder * 255 );
				pg.rect( _curBars * p.scaleH(17), 0, p.scaleH(13), p.scaleH(55) );
			}
		}
		
		pg.popMatrix();
	}
	
}
