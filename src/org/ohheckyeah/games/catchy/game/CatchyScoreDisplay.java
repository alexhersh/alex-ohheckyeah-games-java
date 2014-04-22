package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.assets.CatchyColors;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class CatchyScoreDisplay {
	
	protected Catchy p;
	protected CatchyGamePlay catchyGamePlay;
	protected PGraphics pg;

	protected CustomFontText2D _scoreFontRenderer;
	protected int _score;
	protected EasingFloat _scoreEaser;
	protected int _color;

	protected EasingFloat _scoreY;
	protected EasingFloat _scoreScale;
	protected float _scoreYShowing;
	protected float _scoreYHiding;
	
	protected boolean _isRightSide = false;

	public CatchyScoreDisplay( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
		
		
		_scoreFontRenderer = new CustomFontText2D( p, p.gameGraphics.font, p.scaleV(36), CatchyColors.MAIN_TEXT_COLOR, CustomFontText2D.ALIGN_CENTER, (int)p.scaleV(80), (int)p.scaleV(80) );
		_scoreEaser = new EasingFloat(0,5);
		
		_scoreYShowing = p.scaleV(68f);
		_scoreYHiding = p.scaleV(-60f);
		_scoreY = new EasingFloat(_scoreYHiding,5);
		_scoreScale = new EasingFloat(1,7);
		
		reset( p.color(0) );
	}
	
	public void addScore( int points ) {
		_score += points;
		if( _score < 0 ) _score = 0;
		_scoreEaser.setTarget(_score);
	}
	
	public int getScore() {
		return _score;
	}
	
	public void reset( int color ) {
		_score = 0;
		_scoreFontRenderer.updateText( _score+"" );
		_scoreEaser.setCurrent(0);
		_scoreEaser.setTarget(0);
		_color = color;
		_scoreFontRenderer.setTextColor( _color, -1 );
		_scoreScale.setTarget( 1 );
	}
	
	public void show() {
		_scoreY.setTarget( _scoreYShowing );
	}
	
	public void hide() {
		_scoreY.setTarget( _scoreYHiding );
	}
	
	public void easeScore() {
		_scoreEaser.update();
		int roundedScore = P.round( _scoreEaser.value() );
		if( roundedScore != _score ) {
			_scoreFontRenderer.updateText( roundedScore+"" );
		} else {
			_scoreFontRenderer.updateText( _score+"" );
		}
	}
	
	public void setWinState( boolean didWin ) {
		if( didWin == true ) {
			_scoreScale.setTarget( 1.5f );
		}
	}
	
	public void update() {
		_scoreY.update();
		_scoreScale.update();
		
		// check to see if we're on the right side
		if( p.isLastGameplay(catchyGamePlay) == true ) _isRightSide = true;
		
		easeScore();
		
		pg.pushMatrix();
		
		DrawUtil.setDrawCenter( pg );
		pg.noStroke();
		
		if( _isRightSide == false ) {
			pg.translate( p.scaleV(71f * _scoreScale.value()), _scoreY.value() * _scoreScale.value() );
		} else {
			pg.translate( pg.width - p.scaleV(71f * _scoreScale.value()), _scoreY.value() * _scoreScale.value() );
		}
		pg.scale( _scoreScale.value() );
		
		// draw shadow
		pg.fill(0, 25);
		pg.ellipse( p.scaleV(-6f), p.scaleV(11f), p.scaleV(80f), p.scaleV(80f) );
		
		// draw large bg
		pg.fill( _color );
		pg.ellipse( 0, 0, p.scaleV(80f), p.scaleV(80f) );
		
		// draw small shadow
		pg.fill(0, 25);
		pg.ellipse( 0, p.scaleV(3f), p.scaleV(60f), p.scaleV(60f) );
		
		// draw small shadow
		pg.fill(255);
		pg.ellipse( 0, 0, p.scaleV(60f), p.scaleV(60f) );
		
		// draw text score
		pg.image( 
				_scoreFontRenderer.getTextPImage(), 
				0, 
				0, 
				_scoreFontRenderer.getTextPImage().width, 
				_scoreFontRenderer.getTextPImage().height 
		);
		
		pg.popMatrix();
	}
}
