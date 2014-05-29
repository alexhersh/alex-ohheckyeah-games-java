package org.ohheckyeah.games.tinkerbot.game.display;

import org.ohheckyeah.games.tinkerbot.TinkerBot;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.math.easing.EasingFloat;

public class TinkerBotBaseDisplay {
	
	protected TinkerBot p;
	protected PGraphics pg;

	protected EasingFloat _offsetY;
	protected float _offsetYShowing;
	protected float _offsetYHiding;
	
	protected float FONT_SIZE = 55;

	public TinkerBotBaseDisplay() {
		p = (TinkerBot)P.p;
		pg = p.pg;
		
		_offsetYShowing = p.scaleV(15);
		_offsetYHiding = p.scaleV(-150);
		_offsetY = new EasingFloat(_offsetYHiding,4);
	}
	
	public void update() {
		_offsetY.update();
	}
	
	public void show() {
		_offsetY.setTarget( _offsetYShowing );
	}
	
	public void hide() {
		_offsetY.setTarget( _offsetYHiding );
	}

}
