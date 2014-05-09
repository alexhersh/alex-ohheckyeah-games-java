package org.ohheckyeah.games.bluebear.game.characters;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;

import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class BlueBearNemesis {

	protected BlueBear p;
	protected PGraphics pg;
	protected PImage[] _frames;
	protected PImage _curFrame;
	protected EasingFloat _yPosition = new EasingFloat(0,6);
	protected float _scale = 0.7f;
	protected int _lane = 0;
	protected float _squirrelH = 0;
	protected float _squirrelShadowOffsetY = -7;
	protected EasingFloat _laneScale = new EasingFloat(1, 6);
	protected EasingFloat _floatHeight = new EasingFloat(40, 6);
	protected EasingFloat _shadowScale = new EasingFloat(1, 6);
	protected float baseFlyHeight = 45;
	protected float launchFlyHeight = 70;

	public BlueBearNemesis() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		// init in the top lane
		setLane( _lane );
		_yPosition.setCurrent( _yPosition.target() );
		
		baseFlyHeight = p.scaleV(baseFlyHeight);
		launchFlyHeight = p.scaleV(launchFlyHeight);
	}
	
	public void setLane( int lane ) {
		_lane = lane;
		_yPosition.setTarget( BlueBearScreenPositions.LANES_Y[_lane] );
		_laneScale.setTarget(1f + _lane * 0.1f);
	}
	
	public void update() {
		// responsive sizing/placement		
		_laneScale.update();
		float squirrelW = p.scaleV(p.gameGraphics.squirrel.width * _scale) * _laneScale.value();
		_squirrelH = p.scaleV(p.gameGraphics.squirrel.height * _scale) * _laneScale.value();
		float squirrelX = pg.width - squirrelW * 0.5f - p.scaleV(20);
		_yPosition.update();
		float squirrelY = _yPosition.value() - _squirrelH * 0.5f;
		float shadowY = _yPosition.value() + _squirrelShadowOffsetY;
		
		// float the squirrel
		float floatOsc = P.sin(p.millis() * 0.005f);
		_floatHeight.setTarget( baseFlyHeight + floatOsc * p.scaleV(9f) );
		_floatHeight.update();
		float shadowScaleOsc = floatOsc * 0.5f + 0.5f; // 0-1 scale
		_shadowScale.setTarget( 0.8f + shadowScaleOsc * 0.2f );
		_shadowScale.update();

		// draw shadow and sprite
		DrawUtil.setDrawCenter(pg);
		pg.shape( 
				p.gameGraphics.squirrelShadow, 
				squirrelX, 
				shadowY, 
				p.scaleV(p.gameGraphics.squirrelShadow.width) * _laneScale.value() * _shadowScale.value(), 
				p.scaleV(p.gameGraphics.squirrelShadow.height) * _laneScale.value() * _shadowScale.value()
		);
		pg.shape( p.gameGraphics.squirrel, squirrelX, squirrelY - _floatHeight.value(), squirrelW, _squirrelH );
	}

}
