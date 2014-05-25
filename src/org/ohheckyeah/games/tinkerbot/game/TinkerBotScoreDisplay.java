package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;
import org.ohheckyeah.games.tinkerbot.assets.TinkerBotColors;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class TinkerBotScoreDisplay {
	protected TinkerBot p;
	protected PGraphics pg;
	
	protected int _score = 0;
	protected int _health = 5;
	
	protected CustomFontText2D _scoreText;
	protected EasingFloat _scoreEaser;

	
	public TinkerBotScoreDisplay() {
		p = (TinkerBot)P.p;
		pg = p.pg;
	}
	
	protected void buildScoreText() {
		float fontSize = 40;
		_scoreText = new CustomFontText2D( p, p.ohyGraphics.font, p.scaleV(fontSize), TinkerBotColors.RED_TEXT_COLOR, CustomFontText2D.ALIGN_CENTER, (int) p.scaleV(p.gameGraphics.scoreBg.width), (int)p.scaleV(fontSize * 1.5f) );
		_scoreText.updateText( "0" );
		_scoreEaser = new EasingFloat(0,5);
	}
	
	public int addScore() {
		_score += 10;
		_scoreEaser.setTarget(_score);
		return _score;
	}
	
	public int addHealth() {
		_health += 1;
		if( _health > 5 ) _health = 5;
		return _health;
	}
	
	public int hit() {
		_health--;
		if( _health < 0 ) _health = 0;
		return _health;
	}
	
	public void reset() {
		_score = 0;
		_health = 5;
		if( _scoreText != null ) {
			_scoreText.updateText( ""+_score );
			_scoreEaser.setCurrent(0);
			_scoreEaser.setTarget(0);
		}
	}

	public void update() {
		DrawUtil.setDrawCorner(pg);
		// draw score
		pg.shape( p.gameGraphics.scoreBg, p.scaleV(450), p.scaleV(40), p.svgWidth(p.gameGraphics.scoreBg), p.svgHeight(p.gameGraphics.scoreBg) );
		if( _scoreText == null ) buildScoreText();
		easeScore();
		pg.image( _scoreText.getTextPImage(), p.scaleV(450), p.scaleV(32), _scoreText.getTextPImage().width, _scoreText.getTextPImage().height );
	}
	
	public void easeScore() {
		_scoreEaser.update();
		int roundedScore = P.round( _scoreEaser.value() );
		if( roundedScore != _score ) {
			_scoreText.updateText( roundedScore+"" );
		} else {
			_scoreText.updateText( _score+"" );
		}
	}

}