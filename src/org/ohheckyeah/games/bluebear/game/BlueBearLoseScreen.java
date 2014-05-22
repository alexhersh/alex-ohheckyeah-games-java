package org.ohheckyeah.games.bluebear.game;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.assets.BlueBearColors;
import org.ohheckyeah.games.bluebear.assets.BlueBearGraphics;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class BlueBearLoseScreen {

	protected BlueBear p;
	protected PGraphics pg;
	
	protected PGraphics _backdrop;
	protected EasingFloat _backdropY = new EasingFloat(0, 6);
	protected boolean _isLose;

	public BlueBearLoseScreen() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		buildLoseScreenBackdrop();
		reset();
	}
	
	protected void buildLoseScreenBackdrop() {
		_backdrop = p.createGraphics( p.width, p.height, P.OPENGL );
		_backdrop.smooth(OpenGLUtil.SMOOTH_HIGH);

		_backdrop.beginDraw();
		_backdrop.clear();
		
		DrawUtil.setDrawCenter(pg);
		_backdrop.background( BlueBearColors.LOSE_SCREEN_BG );
		BlueBearGraphics.drawBgDots( p, _backdrop, BlueBearColors.LOSE_SCREEN_DOTS );

		_backdrop.endDraw();
	}
	
	protected void update() {
		_backdropY.update();
		if( _backdropY.value() < pg.height ) {
			pg.image( _backdrop, 0, _backdropY.value() );
		}
	}
	
	public void reset() {
		_isLose = false;
		_backdropY.setCurrent(pg.height + 10);
		_backdropY.setTarget(pg.height + 10);
	}
	
	public void showLoseScreen() {
		_isLose = true;
		_backdropY.setTarget(0);
	}
	
	public void showWinScreen() {
		
	}
}
