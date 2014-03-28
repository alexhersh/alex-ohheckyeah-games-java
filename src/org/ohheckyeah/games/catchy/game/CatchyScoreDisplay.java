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
		
		
		String timerFont = FileUtil.getHaxademicDataPath() + "fonts/AlegreyaSans-Bold.ttf";
		_scoreFontRenderer = new CustomFontText2D( p, timerFont, 36.0f, ColorUtil.colorFromHex("#000000"), CustomFontText2D.ALIGN_CENTER, 80, 80 );
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
			pg.translate( 71f * p.gameScaleV, 68f * p.gameScaleV );
		} else {
			pg.translate( pg.width - 71f * p.gameScaleV, 68f * p.gameScaleV );
		}
		pg.noStroke();
		
		// draw shadow
		pg.fill(0, 25);
		pg.ellipse( -6 * p.gameScaleV, 11 * p.gameScaleV, 80 * p.gameScaleV, 80 * p.gameScaleV );
		
		// draw large bg
		pg.fill( _color );
		pg.ellipse( 0, 0, 80 * p.gameScaleV, 80 * p.gameScaleV );
		
		// draw small shadow
		pg.fill(0, 25);
		pg.ellipse( 0, 3 * p.gameScaleV, 60 * p.gameScaleV, 60 * p.gameScaleV );
		
		// draw small shadow
		pg.fill(255);
		pg.ellipse( 0, 0, 60 * p.gameScaleV, 60 * p.gameScaleV );
		
		// draw text score
		pg.image( 
				_scoreFontRenderer.getTextPImage(), 
				0, 
				7 * p.gameScaleV, 
				_scoreFontRenderer.getTextPImage().width * p.gameScaleV, 
				_scoreFontRenderer.getTextPImage().height * p.gameScaleV 
		);
		
		pg.popMatrix();
	}
}
