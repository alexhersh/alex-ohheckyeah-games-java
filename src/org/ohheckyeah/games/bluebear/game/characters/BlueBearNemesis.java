package org.ohheckyeah.games.bluebear.game.characters;

import org.ohheckyeah.games.bluebear.game.BlueBearGamePlay;
import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.math.easing.LinearFloat;

public class BlueBearNemesis 
extends BlueBearBasePlayer {

	BlueBearGamePlay _gamePlay;
	protected PShape _squirrel;
	
	protected float _curSpeed = 0;
	protected int _laneQueued = -1;

	protected PShape _flameLarge;
	protected PShape _flameSmall;
	protected boolean _flameIsLarge = false;
	protected boolean _flameIsOn = false;
	protected int _flameFrameSwap = 4;
	protected int FLAME_SHOW_WIDTH = 100;
	
	protected PShape _beamTube;
	protected boolean _beamTubeIsDown = false;
	protected LinearFloat _tubeY = new LinearFloat(0, 2f);
	protected int TUBE_MOVE_HEIGHT = 12;

	protected PShape _faceDefault;
	protected PShape _faceBeaming;
	protected int FACE_MOVE_HEIGHT = 50;
	
	protected PShape _beam;
	protected int BEAM_MOVE_HEIGHT = 30;
	protected LinearFloat _beamY = new LinearFloat(0, 5f);

	
	protected float _scale = 0.9f;
	protected float _squirrelShadowOffsetY = -7;
	protected EasingFloat _floatHeight = new EasingFloat(40, 6);
	protected EasingFloat _shadowScale = new EasingFloat(1, 6);
	public static float BASE_FLY_HEIGHT = 100;
	protected float _launchFlyHeight = 130;
	protected float _launchTime = 0;
	protected boolean _launchUp = false;

	public BlueBearNemesis( BlueBearPlayerControls playerControls, BlueBearGamePlay gamePlay ) {
		super( playerControls, 0.7f, 20 );
		_detectionSvg = p.gameGraphics.squirrelDetected;
		_gamePlay = gamePlay;
	
		// init in the top lane
		setLane( _lane );
		_characterPosition.setCurrentY( BlueBearScreenPositions.LANES_Y[_lane] );
		
		BASE_FLY_HEIGHT = p.scaleV(BASE_FLY_HEIGHT);
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
	
	public void setLane( int lane ) {
		boolean laneChanged = ( lane != _lane );
		if( laneChanged ) {
			if( launchDone() ) {
				updateLane( lane );
			} else {
				_laneQueued = lane;
			}
		} else {
			_laneQueued = -1; // cancel lane change queue if player switched back to currently-launching lane
		}
	}
	
	protected void updateLane( int lane ) {
		_laneQueued = -1;
		_characterPosition.setTargetY( BlueBearScreenPositions.LANES_Y[lane] );
		_shadowPosition.setTargetY( BlueBearScreenPositions.LANES_Y[lane] );
		_laneScale.setTarget(1f + lane * 0.1f);
		super.setLane( lane );
		
		boolean readyToDrop = _curSpeed > _gamePlay.SPEED * 0.9f;
		if( readyToDrop ) launch();
	}
	
	protected boolean launchDone() {
		return p.millis() > _launchTime + 1000;
	}
	
	public void reset() {
		_launchTime = 0;
		_curSpeed = 0;
		_laneQueued = -1;
	}
	
	// override base character class for special case of waiting to drop in between lane changes
	public void prepareForGameplay() {
		_lane = 0;
		updateLane( 0 );
	}

	
	protected void launch() {
		_launchUp = true;
		_launchTime = p.millis();
		_gamePlay.launchObstacle();
		tubeDown();
	}
	
	protected void updateLaneQueued() {
		if( _laneQueued != -1 && launchDone() ) {
			updateLane( _laneQueued );
		}
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
		_curSpeed = speed;
		updateLaneQueued();
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
		float flyHeight = ( _launchUp == true ) ? _launchFlyHeight : BASE_FLY_HEIGHT + floatOsc * p.scaleV(9f);
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
//		_beamY.update();
//		_beam.translate(0, _beamY.value());
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
