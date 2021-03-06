package org.ohheckyeah.games.tinkerbot.game.display;

import org.ohheckyeah.games.tinkerbot.assets.TinkerBotColors;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class TinkerBotScoreDisplay
extends TinkerBotBaseDisplay {

	public static final int SCORE_POINTS = 100;
	protected int _score = 0;
	
	protected CustomFontText2D _scoreText;
	protected EasingFloat _scoreEaser;
	
	public TinkerBotScoreDisplay() {
		super();
		buildScoreText();
	}
	
	protected void buildScoreText() {
		_scoreText = new CustomFontText2D( p, p.ohyGraphics.font, p.scaleH(FONT_SIZE), TinkerBotColors.MAIN_TEXT_COLOR, CustomFontText2D.ALIGN_CENTER, (int) p.scaleH(197), (int)p.scaleH(FONT_SIZE * 1.5f) );
		_scoreText.updateText( "0" );
		_scoreEaser = new EasingFloat(0,5);
	}
	
	public int addScore() {
		_score += SCORE_POINTS;
		_scoreEaser.setTarget(_score);
		return _score;
	}
	
	public void reset() {
		_score = 0;
		if( _scoreText != null ) {
			_scoreText.updateText( ""+_score );
			_scoreEaser.setCurrent(0);
			_scoreEaser.setTarget(0);
		}
	}

	public void update() {
		super.update();
		
		// move y position
		pg.pushMatrix();
		pg.translate( 0, _offsetY.value() );

		// draw score bg
		DrawUtil.setDrawCorner(pg);
		pg.shape( p.gameGraphics.scoreBg, p.scaleH(992), 0, p.scaleH(p.gameGraphics.scoreBg.width), p.scaleH(p.gameGraphics.scoreBg.height) );
		
		// draw score text
		easeScore();
		pg.image( _scoreText.getTextPImage(), p.scaleH(1009), p.scaleH(7), _scoreText.getTextPImage().width, _scoreText.getTextPImage().height );

		pg.popMatrix();
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