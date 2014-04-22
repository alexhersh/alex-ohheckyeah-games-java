package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.assets.CatchyColors;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.text.CustomFontText2D;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class CatchyCountdownDisplay {

	protected Catchy p;
	protected CatchyGamePlay catchyGamePlay;
	protected PGraphics pg;

	protected CustomFontText2D _countdownFontRenderer;
	protected ElasticFloat _scale = new ElasticFloat(0, 0.71f, 0.16f);
	protected int _color;
	protected int _timer = -1;

	public CatchyCountdownDisplay( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;

		_countdownFontRenderer = new CustomFontText2D( p, p.gameGraphics.font, p.scaleV(120), CatchyColors.MAIN_TEXT_COLOR, CustomFontText2D.ALIGN_CENTER, (int)p.scaleV(150), (int)p.scaleV(150) );
	}

	public void updateWithNumber( int timer ) {
		_scale.update();
		
		// bounce the countdown scale on number change
		if( timer > 0 && timer != _timer ) {
			_countdownFontRenderer.updateText( timer+"" );
			_timer = timer;
			_scale.setTarget(2f);
			_scale.update();
			_scale.setTarget(1);
		}

		if( _scale.val() > 0.001f ) {
			DrawUtil.setDrawCenter( pg );
			pg.pushMatrix();
	
			pg.translate( catchyGamePlay.gameHalfWidth, p.height / 2f );
			
			pg.scale( _scale.val() );
			pg.noStroke();
			
			// draw small shadow
			pg.fill(0, 25);
			pg.ellipse( 0, p.scaleV(3), p.scaleV(150), p.scaleV(150) );
	
			// draw small shadow
			pg.fill(255);
			pg.ellipse( 0, 0, p.scaleV(150), p.scaleV(150) );
	
			// draw text score
			pg.image( 
					_countdownFontRenderer.getTextPImage(), 
					0, 
					0, 
					_countdownFontRenderer.getTextPImage().width, 
					_countdownFontRenderer.getTextPImage().height 
					);
	
			pg.popMatrix();
		}
	}
	
	public void show() {
		_scale.setTarget(1);
	}
	
	public void hide() {
		_scale.setTarget(0);
	}
}
