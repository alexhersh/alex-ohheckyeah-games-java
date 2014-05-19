package org.ohheckyeah.games.bluebear.game.characters;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.BlueBear.GameState;
import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.math.easing.LinearFloat;

public class BlueBearNemesis {

	protected BlueBear p;
	protected PGraphics pg;
	
	protected PShape _squirrel;
	
	protected PShape _flameLarge;
	protected PShape _flameSmall;
	protected boolean _flameIsLarge = false;
	protected boolean _flameIsOn = false;
	protected int _flameFrameSwap = 4;
	protected int FLAME_SHOW_WIDTH = 100;
	
	protected PShape _beamTube;
	protected boolean _beamTubeIsDown = false;
	protected LinearFloat _tubeY = new LinearFloat(0, 2f);
	protected int TUBE_MOVE_HEIGHT = 8;

	protected PShape _faceDefault;
	protected PShape _faceBeaming;
	protected int FACE_MOVE_HEIGHT = 50;
	
	protected PShape _beam;
	protected int BEAM_MOVE_HEIGHT = 30;
	protected LinearFloat _beamY = new LinearFloat(0, 5f);

	
	protected EasingFloat _yPosition = new EasingFloat(0,6);
	protected float _scale = 0.7f;
	protected int _lane = 0;
	protected float _squirrelX = 0;
	protected float _squirrelH = 0;
	protected float _squirrelShadowOffsetY = -7;
	protected EasingFloat _laneScale = new EasingFloat(1, 6);
	protected EasingFloat _floatHeight = new EasingFloat(40, 6);
	protected EasingFloat _shadowScale = new EasingFloat(1, 6);
	protected float _baseFlyHeight = 80;
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
		
		initSquirrel();
	}
	
	protected void initSquirrel() {
		_squirrel = p.gameGraphics.squirrel;
		
		_flameLarge = _squirrel.getChild("flame-large");
		_flameLarge.translate(FLAME_SHOW_WIDTH, 0);
		_flameSmall = _squirrel.getChild("flame-small");
		_flameSmall.translate(FLAME_SHOW_WIDTH, 0);

		_beamTube = _squirrel.getChild("beam-port");
		_beamTube.translate(0, 0);

		_faceDefault = _squirrel.getChild("face-default");
		_faceBeaming = _squirrel.getChild("face-beam");
		_faceBeaming.translate(0, FACE_MOVE_HEIGHT);

		_beam = _squirrel.getChild("beam");
		_beam.translate(0, 0);
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
	
	public void startMoving() {
		_flameIsOn = true;
		_flameSmall.translate(-FLAME_SHOW_WIDTH, 0);
	}
	
	public void stopMoving() {
		_flameSmall.translate(FLAME_SHOW_WIDTH, 0);
		_flameIsOn = false;
		if( _flameIsLarge == true ) {
			_flameIsLarge = false;
			_flameLarge.translate(FLAME_SHOW_WIDTH, 0);
		}
	}
	
	public void launch() {
		_launchUp = true;
		_launchTime = p.millis();
		tubeDown();
	}
	
	protected void updateLaunchMode() {
		if( _launchUp == true && p.millis() > _launchTime + 300 ) {
			_launchUp = false;
			tubeUp();
		}
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
		float squirrelW = p.scaleV(_squirrel.width * _scale) * _laneScale.value();
		_squirrelH = p.scaleV(_squirrel.height * _scale) * _laneScale.value();
		_squirrelX = pg.width - p.scaleV(150);
		_yPosition.update();
		float squirrelY = _yPosition.value();// - _squirrelH * 0.5f;
		float shadowY = _yPosition.value() + _squirrelShadowOffsetY;
		
		// float the squirrel
		float floatOsc = P.sin(p.millis() * 0.005f);
		float flyHeight = ( _launchUp == true ) ? _launchFlyHeight : _baseFlyHeight + floatOsc * p.scaleV(9f);
		flyHeight *= _laneScale.value();
		_floatHeight.setTarget( flyHeight );
		_floatHeight.update();
		float shadowScaleOsc = floatOsc * 0.5f + 0.5f; // 0-1 scale
		float shadowScale = ( _launchUp == true ) ? 0.2f : 0.8f;
		_shadowScale.setTarget( shadowScale + shadowScaleOsc * 0.2f );
		_shadowScale.update();

		// animate sub-squirrel parts
		animateSquirrel();
		
		// draw shadow and sprite
		DrawUtil.setDrawCenter(pg);
		pg.pushMatrix();
		pg.translate(0, 0, _lane);
		pg.shape( 
				p.gameGraphics.squirrelShadow, 
				_squirrelX, 
				shadowY, 
				p.scaleV(p.gameGraphics.squirrelShadow.width) * _laneScale.value() * _shadowScale.value(), 
				p.scaleV(p.gameGraphics.squirrelShadow.height) * _laneScale.value() * _shadowScale.value()
		);
		pg.shape( _squirrel, _squirrelX, squirrelY - _floatHeight.value(), squirrelW, _squirrelH );
		pg.popMatrix();
	}
	
	protected void animateSquirrel() {
		// animate flame
		if( p.gameState() == GameState.GAME_PLAYING ) {
			if( _flameIsOn == true && p.frameCount % _flameFrameSwap == 0 ) {
				_flameIsLarge = !_flameIsLarge;
				if( _flameIsLarge == true ) {
					_flameLarge.translate(-FLAME_SHOW_WIDTH, 0);
				} else {
					_flameLarge.translate(FLAME_SHOW_WIDTH, 0);
				}
			}
		}
		
		// update drop tube y
		_tubeY.update();
		_beamTube.translate(0, _tubeY.value());
		
		// update beam y
		_beamY.update();
		_beam.translate(0, _beamY.value());
	}
	
	protected void tubeDown() {
		_tubeY.setCurrent(TUBE_MOVE_HEIGHT);
		_tubeY.setTarget(0);
		
		_beamY.setCurrent(BEAM_MOVE_HEIGHT);
		_beamY.setTarget(0);
		
		_faceDefault.translate(0, FACE_MOVE_HEIGHT);
		_faceBeaming.translate(0, -FACE_MOVE_HEIGHT);
	}
	
	protected void tubeUp() {
		_tubeY.setCurrent(-TUBE_MOVE_HEIGHT);
		_tubeY.setTarget(0);

		_beamY.setCurrent(-BEAM_MOVE_HEIGHT);
		_beamY.setTarget(0);

		_faceDefault.translate(0, -FACE_MOVE_HEIGHT);
		_faceBeaming.translate(0, FACE_MOVE_HEIGHT);
	}

}
