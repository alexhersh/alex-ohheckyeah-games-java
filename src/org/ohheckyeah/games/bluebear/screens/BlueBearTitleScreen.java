package org.ohheckyeah.games.bluebear.screens;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.assets.BlueBearColors;
import org.ohheckyeah.games.bluebear.assets.BlueBearGraphics;
import org.ohheckyeah.shared.screens.OHYBaseIntroScreen;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class BlueBearTitleScreen
extends OHYBaseIntroScreen {
	
	protected BlueBear p;
	protected int _bgColor;
	
	protected ElasticFloat _logoY = new ElasticFloat(0, 0.75f, 0.1f);
	
	public BlueBearTitleScreen() {
		super();
		p = (BlueBear) P.p;
	}
	
	public void reset() {
		_bgColor = BlueBearColors.TITLE_SCREEN_BG;
		_logoY.setValue(canvas.height + p.svgHeight(p.gameGraphics.blueBearLogo));
		_logoY.setTarget(canvas.height / 2f);
	}
	
	public void animateIn() {
		_logoY.setTarget(canvas.height / 2f + p.scaleV(150f));
	}
	
	public void animateOut() {
		_logoY.setTarget( -canvas.height );
	}
	
	public void update() {
		canvas.beginDraw();
		
		canvas.background( _bgColor );
		BlueBearGraphics.drawBgDots( p, canvas, BlueBearColors.TITLE_SCREEN_DOTS );
		drawLogo();
		drawBorder();
		
		canvas.endDraw();
	}
	
	protected void drawBorder() {
		DrawUtil.setDrawCorner(canvas);
		canvas.stroke( BlueBearColors.TITLE_SCREEN_BORDER );
		canvas.strokeWeight( p.scaleV(40) );
		canvas.noFill();
		canvas.rect( 0, 0, canvas.width, canvas.height );
	}
	
	protected void drawLogo() {
		DrawUtil.setDrawCenter(canvas);
		_logoY.update();
		canvas.shape( p.gameGraphics.blueBearLogo, canvas.width * 0.5f, _logoY.val(), p.scaleV(p.gameGraphics.blueBearLogo.width), p.scaleV(p.gameGraphics.blueBearLogo.height) );
	}
	
}