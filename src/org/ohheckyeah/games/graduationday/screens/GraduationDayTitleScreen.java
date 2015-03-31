package org.ohheckyeah.games.graduationday.screens;

import org.ohheckyeah.games.graduationday.GraduationDay;
import org.ohheckyeah.games.graduationday.assets.GraduationDayColors;
import org.ohheckyeah.shared.screens.OHYBaseIntroScreen;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class GraduationDayTitleScreen
extends OHYBaseIntroScreen {
	
	protected GraduationDay p;
	public boolean _bgCached = false;
	
	protected int _bgColor;
	
	protected ElasticFloat _logoScale = new ElasticFloat(0, 0.75f, 0.1f);
	protected ElasticFloat _logoRot = new ElasticFloat(0, 0.75f, 0.1f);
	
	public GraduationDayTitleScreen() {
		super();
		p = (GraduationDay) P.p;
		
		_bgColor = GraduationDayColors.TITLE_SCREEN_BG;
	}
	
	public void reset() {
	}
	
	public void animateIn() {
		_logoScale.setValue(0);
		_logoScale.setTarget(0.7f);
		_logoRot.setValue(-P.TWO_PI * 2f);
		_logoRot.setTarget(0);
	}
	
	public void animateOut() {
		_logoScale.setTarget(0);
	}
	
	public void update() {
		canvas.beginDraw();
		canvas.clear();
		
		drawBackground();
		drawHats();
		drawLogo();
		
		canvas.endDraw();
	}
	
	protected void drawBackground() {
		DrawUtil.setDrawCenter(canvas);
	}
	
	protected void drawHats() {
		// draw shapes from their center
		DrawUtil.setDrawCenter(canvas);
		
		// draw a hand
		float handScale = 0.8f;
		float handW = p.scaleV(p.gameGraphics.hand.width);
		float handH = p.scaleV(p.gameGraphics.hand.height);
		canvas.pushMatrix();
		canvas.translate( canvas.width * 0.5f, canvas.height * 0.85f + P.sin(p.frameCount/10f) );
		canvas.shape( p.gameGraphics.hand, 0, 0, handW * handScale, handH * handScale );
		canvas.popMatrix();
		
		// there's only 1 hat now - can you make another class inside of this class to run your hats?
		float hatScale = 1f;
		float hatW = p.scaleV(p.gameGraphics.hat.width);
		float hatH = p.scaleV(p.gameGraphics.hat.height);
		canvas.pushMatrix();
		canvas.translate( canvas.width * 0.5f, canvas.height * 0.75f );
		canvas.rotate( P.sin(p.frameCount/10f) );
		canvas.shape( p.gameGraphics.hat, 0, 0, hatW * hatScale, hatH * hatScale );
		canvas.popMatrix();
	}
	
	protected void drawLogo() {
		DrawUtil.setDrawCenter(canvas);
		_logoScale.update();
		_logoRot.update();
		if( _logoScale.val() > 0.1f ) {
			canvas.pushMatrix();
			canvas.translate( canvas.width * 0.5f, canvas.height * 0.5f );
			canvas.rotate( _logoRot.val() );
			canvas.shape( p.gameGraphics.graduationDayLogo, 0, 0, p.scaleV(p.gameGraphics.graduationDayLogo.width) * _logoScale.val(), p.scaleV(p.gameGraphics.graduationDayLogo.height) * _logoScale.val() );
			canvas.popMatrix();
		}
	}
	
}