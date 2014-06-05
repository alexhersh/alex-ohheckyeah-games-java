package org.ohheckyeah.games.tinkerbot.screens;

import org.ohheckyeah.games.tinkerbot.TinkerBot;
import org.ohheckyeah.games.tinkerbot.assets.TinkerBotColors;
import org.ohheckyeah.shared.screens.OHYBaseIntroScreen;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class TinkerBotTitleScreen
extends OHYBaseIntroScreen {
	
	protected TinkerBot p;
	public boolean _bgCached = false;
	
	protected int _bgColor;
	
	protected ElasticFloat _logoScale = new ElasticFloat(0, 0.75f, 0.1f);
	protected ElasticFloat _logoRot = new ElasticFloat(0, 0.75f, 0.1f);
	
	public TinkerBotTitleScreen() {
		super();
		p = (TinkerBot) P.p;
		
		_bgColor = TinkerBotColors.TITLE_SCREEN_BG;
	}
	
	public void reset() {
	}
	
	public void animateIn() {
		_logoScale.setValue(0);
		_logoScale.setTarget(1.3f);
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
		drawLogo();
		
		canvas.endDraw();
	}
	
	protected void drawBackground() {
		DrawUtil.setDrawCenter(canvas);
		canvas.image( p.gameGraphics.gameplayBackgroundImage, canvas.width * 0.5f, canvas.height * 0.5f, p.gameGraphics.gameplayBackgroundImage.width, p.gameGraphics.gameplayBackgroundImage.height );
	}
	
	protected void drawLogo() {
		DrawUtil.setDrawCenter(canvas);
		_logoScale.update();
		_logoRot.update();
		if( _logoScale.val() > 0 ) {
			canvas.pushMatrix();
			canvas.translate( canvas.width * 0.5f, canvas.height * 0.5f );
			canvas.rotate( _logoRot.val() );
			canvas.shape( p.gameGraphics.tinkerBotLogo, 0, 0, p.scaleV(p.gameGraphics.tinkerBotLogo.width) * _logoScale.val(), p.scaleV(p.gameGraphics.tinkerBotLogo.height) * _logoScale.val() );
			canvas.popMatrix();
		}
	}
	
}