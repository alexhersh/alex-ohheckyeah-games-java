package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.system.FileUtil;

public class CatchyScoreDisplay {
	
	protected Catchy p;
	protected CatchyGamePlay catchyGamePlay;
	protected PGraphics pg;

	protected CustomFontText2D _scoreFontRenderer;
	protected int _score;
	protected EasingFloat _scoreEaser;
	protected int _color;
	
	protected boolean _isRightSide = false;

	public CatchyScoreDisplay( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
		
		
		_scoreFontRenderer = new CustomFontText2D( p, p.gameGraphics.font, p.scaleV(36), ColorUtil.colorFromHex("#000000"), CustomFontText2D.ALIGN_CENTER, (int)p.scaleV(80), (int)p.scaleV(80) );
		_scoreEaser = new EasingFloat(0,5);
		reset( p.color(0) );
	}
	
	public void addScore( int points ) {
		_score += points;
		_scoreEaser.setTarget(_score);
	}
	
	public void reset( int color ) {
		_score = 0;
		_scoreFontRenderer.updateText( _score+"" );
		_scoreEaser.setCurrent(0);
		_scoreEaser.setTarget(0);
		_color = color;
		_scoreFontRenderer.setTextColor( _color, -1 );
	}
	
	public void update() {
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
	public void drawScore() {
		// check to see if we're on the right side
		if( p.isLastGameplay(catchyGamePlay) == true ) _isRightSide = true;
		
		easeScore();
		
		DrawUtil.setDrawCenter( pg );
		pg.pushMatrix();
		
		if( _isRightSide == false ) {
			pg.translate( p.scaleV(71f), p.scaleV(68f) );
		} else {
			pg.translate( pg.width - p.scaleV(71f), p.scaleV(68f) );
		}
		pg.noStroke();
		
		// draw shadow
		pg.fill(0, 25);
		pg.ellipse( p.scaleV(-6), p.scaleV(11), p.scaleV(80), p.scaleV(80) );
		
		// draw large bg
		pg.fill( _color );
		pg.ellipse( 0, 0, p.scaleV(80), p.scaleV(80) );
		
		// draw small shadow
		pg.fill(0, 25);
		pg.ellipse( 0, p.scaleV(3), p.scaleV(60), p.scaleV(60) );
		
		// draw small shadow
		pg.fill(255);
		pg.ellipse( 0, 0, p.scaleV(60), p.scaleV(60) );
		
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
