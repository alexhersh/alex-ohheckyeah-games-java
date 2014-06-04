package org.ohheckyeah.games.bluebear.game;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.assets.BlueBearColors;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.text.StringFormatter;

public class BlueBearScoreDisplay {
	protected BlueBear p;
	protected PGraphics pg;
	
	protected int _score = 0;
	protected int _maxHealth = 10;
	protected int _health = _maxHealth;

	protected CustomFontText2D _scoreText;
	protected EasingFloat _scoreEaser;

	protected int _gameTime;
	protected CustomFontText2D _timerText;
	
	
	public BlueBearScoreDisplay() {
		p = (BlueBear)P.p;
		pg = p.pg;
	}
	
	protected void buildScoreText() {
		float fontSize = 40;
		_scoreText = new CustomFontText2D( p, p.gameGraphics.font, p.scaleV(fontSize), BlueBearColors.RED_TEXT_COLOR, CustomFontText2D.ALIGN_CENTER, (int) p.scaleV(p.gameGraphics.scoreBg.width), (int)p.scaleV(fontSize * 1.5f) );
		_scoreText.updateText( "0" );
		_scoreEaser = new EasingFloat(0,5);
	}
	
	protected void buildTimerText() {
		float fontSize = 40;
		_timerText = new CustomFontText2D( p, p.gameGraphics.font, p.scaleV(fontSize), BlueBearColors.RED_TEXT_COLOR, CustomFontText2D.ALIGN_CENTER, (int) p.scaleV(p.gameGraphics.scoreBg.width), (int)p.scaleV(fontSize * 1.5f) );
		_timerText.updateText( StringFormatter.timeFromMilliseconds( _gameTime, false) );
	}
	
	public int addScore() {
		_score += 10;
		_scoreEaser.setTarget(_score);
		return _score;
	}
	
	public int addHealth() {
		_health += 2;
		if( _health > _maxHealth ) _health = _maxHealth;
		return _health;
	}
	
	public int hit() {
		_health--;
		if( _health < 0 ) _health = 0;
		return _health;
	}
	
	public void reset( int gameSeconds ) {
		_score = 0;
		_health = _maxHealth;
		if( _scoreText != null ) {
			_scoreText.updateText( ""+_score );
			_scoreEaser.setCurrent(0);
			_scoreEaser.setTarget(0);
		}
		_gameTime = gameSeconds * 1000;
		if( _timerText != null ) _timerText.updateText( StringFormatter.timeFromMilliseconds( _gameTime, false) );
	}

	public void update( int elapsedTime ) {
		DrawUtil.setDrawCorner(pg);
		// draw health
		PShape healthSvg = p.gameGraphics.scores[_health];
		pg.shape( healthSvg, p.scaleV(80), p.scaleV(40), p.svgWidth(healthSvg), p.svgHeight(healthSvg) );
		// draw score
		pg.shape( p.gameGraphics.scoreBg, p.scaleV(450), p.scaleV(40), p.svgWidth(p.gameGraphics.scoreBg), p.svgHeight(p.gameGraphics.scoreBg) );
		if( _scoreText == null ) buildScoreText();
		easeScore();
		pg.image( _scoreText.getTextPImage(), p.scaleV(450), p.scaleV(32), _scoreText.getTextPImage().width, _scoreText.getTextPImage().height );
		// draw timer and update time during gameplay
		pg.shape( p.gameGraphics.scoreBg, pg.width - p.scaleV(233), p.scaleV(40), p.svgWidth(p.gameGraphics.scoreBg), p.svgHeight(p.gameGraphics.scoreBg) );
		if( _timerText == null ) buildTimerText();
		if( p.gameState() == GameState.GAME_PLAYING ) {
			_timerText.updateText( StringFormatter.timeFromMilliseconds( _gameTime - elapsedTime, false ) );
		}
		pg.image( _timerText.getTextPImage(), pg.width - p.scaleV(233), p.scaleV(32), _timerText.getTextPImage().width, _timerText.getTextPImage().height );
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