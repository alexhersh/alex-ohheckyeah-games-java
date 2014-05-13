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
	protected float _squirrelX = 0;
	protected float _squirrelH = 0;
	protected float _squirrelShadowOffsetY = -7;
	protected EasingFloat _laneScale = new EasingFloat(1, 6);
	protected EasingFloat _floatHeight = new EasingFloat(40, 6);
	protected EasingFloat _shadowScale = new EasingFloat(1, 6);
	protected float _baseFlyHeight = 45;
	protected float _launchFlyHeight = 100;
	protected float _launchTime = 0;
	protected boolean _launchUp = false;

	public BlueBearNemesis() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		// init in the top lane
		setLane( _lane );
		_yPosition.setCurrent( _yPosition.target() );
		
		_baseFlyHeight = p.scaleV(_baseFlyHeight);
		_launchFlyHeight = p.scaleV(_launchFlyHeight);
	}
	
	public void setLane( int lane ) {
		_lane = lane;
		_yPosition.setTarget( BlueBearScreenPositions.LANES_Y[_lane] );
		_laneScale.setTarget(1f + _lane * 0.1f);
	}
	
	public void reset() {
		_launchTime = 0;
	}
	
	public void startGameplay() {

	}
	
	public void launch() {
		_launchUp = true;
		_launchTime = p.millis();
	}
	
	public int lane() {
		return _lane;
	}
	
	public float launchX() {
		return _squirrelX;
	}
	
	public float launchY() {
		return _yPosition.target();
	}
	
	public void update() {
		updateLaunchMode();
		
		// responsive sizing/placement		
		_laneScale.update();
		float squirrelW = p.scaleV(p.gameGraphics.squirrel.width * _scale) * _laneScale.value();
		_squirrelH = p.scaleV(p.gameGraphics.squirrel.height * _scale) * _laneScale.value();
		_squirrelX = pg.width - p.scaleV(150);
		_yPosition.update();
		float squirrelY = _yPosition.value() - _squirrelH * 0.5f;
		float shadowY = _yPosition.value() + _squirrelShadowOffsetY;
		
		// float the squirrel
		float floatOsc = P.sin(p.millis() * 0.005f);
		float flyHeight = ( _launchUp == true ) ? _launchFlyHeight : _baseFlyHeight;
		_floatHeight.setTarget( flyHeight + floatOsc * p.scaleV(9f) );
		_floatHeight.update();
		float shadowScaleOsc = floatOsc * 0.5f + 0.5f; // 0-1 scale
		float shadowScale = ( _launchUp == true ) ? 0.2f : 0.8f;
		_shadowScale.setTarget( shadowScale + shadowScaleOsc * 0.2f );
		_shadowScale.update();

		// draw shadow and sprite
		DrawUtil.setDrawCenter(pg);
		pg.shape( 
				p.gameGraphics.squirrelShadow, 
				_squirrelX, 
				shadowY, 
				p.scaleV(p.gameGraphics.squirrelShadow.width) * _laneScale.value() * _shadowScale.value(), 
				p.scaleV(p.gameGraphics.squirrelShadow.height) * _laneScale.value() * _shadowScale.value()
		);
		pg.shape( p.gameGraphics.squirrel, _squirrelX, squirrelY - _floatHeight.value(), squirrelW, _squirrelH );
	}

	protected void updateLaunchMode() {
		if( p.millis() > _launchTime + 300 ) _launchUp = false;
	}
	
}
