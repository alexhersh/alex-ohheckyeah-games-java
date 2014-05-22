package org.ohheckyeah.games.bluebear.game.characters;

import org.ohheckyeah.games.bluebear.BlueBear.GameState;
import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.math.easing.LinearFloat;

public class BlueBearNemesis 
extends BlueBearBasePlayer {

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

	
	protected float _scale = 0.7f;
	protected float _squirrelShadowOffsetY = -7;
	protected EasingFloat _floatHeight = new EasingFloat(40, 6);
	protected EasingFloat _shadowScale = new EasingFloat(1, 6);
	protected float _baseFlyHeight = 80;
	protected float _launchFlyHeight = 100;
	protected float _launchTime = 0;
	protected boolean _launchUp = false;

	public BlueBearNemesis( BlueBearPlayerControls playerControls ) {
		super( playerControls, 0.7f, 20 );
		_detectionSvg = p.gameGraphics.squirrelDetected;
	
		// init in the top lane
		setLane( _lane );
		_characterPosition.setCurrentY( BlueBearScreenPositions.LANES_Y[_lane] );
		
		_baseFlyHeight = p.scaleV(_baseFlyHeight);
		_launchFlyHeight = p.scaleV(_launchFlyHeight);
		_squirrelShadowOffsetY = p.scaleV(_squirrelShadowOffsetY);
		
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
		super.setLane( lane );
		_characterPosition.setTargetY( BlueBearScreenPositions.LANES_Y[_lane] );
		_shadowPosition.setTargetY( BlueBearScreenPositions.LANES_Y[_lane] );
		_laneScale.setTarget(1f + _lane * 0.1f);
	}
	
	public void reset() {
		_launchTime = 0;
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
	
	public void gameOver() {
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
	
	public float launchX() {
		return _characterPosition.x();
	}
	
	public float launchY() {
		return BlueBearScreenPositions.LANES_Y[_lane];
	}
	
	protected void updateGameplay(float speed) {
		updateLaunchMode();
		
		// responsive sizing/placement		
		_laneScale.update();
		float squirrelW = p.scaleV(_squirrel.width * _scale) * _laneScale.value();
		_characterH = p.scaleV(_squirrel.height * _scale) * _laneScale.value();
		
		float characterX = pg.width - p.scaleV(150);
		if( p.gameState() == GameState.GAME_OVER || p.gameState() == GameState.GAME_OVER_OUTRO) characterX = pg.width * 1.25f;
		
		_characterPosition.setTargetX( characterX );
		_characterPosition.update();
		float squirrelY = _characterPosition.y();
		
		_shadowPosition.setTargetX( characterX );
		_shadowPosition.update();
		
		
		// float the squirrel
		float floatOsc = ( _flameIsOn == false ) ? P.sin(p.millis() * 0.005f) : 0f;
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
				_shadowPosition.x(), 
				_shadowPosition.y() + _squirrelShadowOffsetY, 
				p.scaleV(p.gameGraphics.squirrelShadow.width) * _laneScale.value() * _shadowScale.value(), 
				p.scaleV(p.gameGraphics.squirrelShadow.height) * _laneScale.value() * _shadowScale.value()
		);
		pg.shape( _squirrel, _characterPosition.x(), squirrelY - _floatHeight.value(), squirrelW, _characterH );
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
