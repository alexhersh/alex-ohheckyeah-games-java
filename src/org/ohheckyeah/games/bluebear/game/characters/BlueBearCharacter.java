package org.ohheckyeah.games.bluebear.game.characters;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear.GameState;
import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;

import processing.core.PImage;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.system.FileUtil;

public class BlueBearCharacter
extends BlueBearBasePlayer {

	protected PImage[] _frames;
	protected PImage[] _framesHurt;
	protected PImage _curFrame;
	protected float _frameIndex = 0;
	protected float _scale = 0.4f;
	protected float _bearShadowOffsetY = -7;
	protected float _explosionOffsetY = 2;
	protected final int HURT_LENGTH = 400;
	protected int _hurtTime = 0;
	protected float _winWalkSpeed = 5;
	protected float _winWalkProgress = 0;
	protected boolean _didWin = false;
	protected boolean _winJump = false;
	protected boolean _didLose = false;
	protected boolean _loseSad = false;

	public BlueBearCharacter( BlueBearPlayerControls playerControls ) {
		super( playerControls, 0.3f, 0 );
		_detectionSvg = p.gameGraphics.blueBearDetected;

		
		// load frame images from directory
		String imgPath = "games/bluebear/images/bear-run-sequence/";
		String imgPathHurt = "games/bluebear/images/bear-run-sequence-hurt/";
		ArrayList<String> frameFiles = FileUtil.getFilesInDirOfType( FileUtil.getHaxademicDataPath() + imgPath, "png");
		ArrayList<String> frameHurtFiles = FileUtil.getFilesInDirOfType( FileUtil.getHaxademicDataPath() + imgPathHurt, "png");
		_frames = new PImage[frameFiles.size()];
		_framesHurt = new PImage[frameHurtFiles.size()];
		for( int i=0; i < _frames.length; i++ ) _frames[i] = p.loadImage(imgPath + frameFiles.get(i));
		for( int i=0; i < _framesHurt.length; i++ ) _framesHurt[i] = p.loadImage(imgPathHurt + frameHurtFiles.get(i));
		
		// init in the top lane
		setLane( _lane );
		_characterPosition.setCurrentY( BlueBearScreenPositions.LANES_Y[_lane] );
		
		_bearShadowOffsetY = Math.round(p.scaleV(_bearShadowOffsetY));
		_explosionOffsetY = Math.round(p.scaleV(_explosionOffsetY));
		_winWalkSpeed = p.scaleV(_winWalkSpeed);
	}
	
	public void reset() {
		_frameIndex = 0;
		_curFrame = _frames[P.floor(_frameIndex)];
		_didWin = false;
		_didLose = false;
		_winWalkProgress = 0;
	}
	
	public void setLane( int lane ) {
		_characterPosition.setTargetY( BlueBearScreenPositions.LANES_Y[lane] );
		_shadowPosition.setTargetY( BlueBearScreenPositions.LANES_Y[lane] + _bearShadowOffsetY );
		_laneScale.setTarget(1f + lane * 0.1f);
		super.setLane( lane );
	}
	
	public void hit() {
		_hurtTime = p.millis();
	}
	
	public float x() {
		return _characterPosition.x();
	}
	
	public float xLeft() {
		return _characterPosition.x() - _characterW * 0.2f;
	}
	
	public float xRight() {
		return _characterPosition.x() + _characterW * 0.3f;
	}
	
	public float y() {
		return _characterPosition.y();
	}
	public void prepareForGameplay() {
		super.prepareForGameplay();
		_winJump = false;
		_loseSad = false;
	}
	
	protected void updateGameplay(float speed) {
		advanceBearFrame(speed);
		
		// ALL OF THIS STATE CHECKING IS TERRIBLE!!! TODO: FIX THIS MESS

		// responsive sizing/placement
		float characterX = p.scaleV(130);
		if( p.gameState() == GameState.GAME_OVER || p.gameState() == GameState.GAME_OVER_OUTRO) {
			if( _didWin == true ) {
				if( _winJump == true ) {
					characterX = pg.width * 0.5f;
				} else {
					_winWalkProgress += _winWalkSpeed;
					characterX += _winWalkProgress; 
				}
			} else {
				if( _loseSad == true ) {
					characterX = pg.width * 0.5f;
				} else {
					// leave x as-is
				}
			}
		}
		
		if( _winJump == true ) { // || p.gameState() == GameState.GAME_OVER || p.gameState() == GameState.GAME_OVER_OUTRO || p.gameState() == GameState.GAME_INTRO ) {
			_characterPosition.setTargetY( pg.height * 0.5f + p.svgHeight(p.gameGraphics.blueBearWin ) * 0.5f );
		}
		if( _loseSad == true ) {
			if( p.gameState() == GameState.GAME_OVER )
				_characterPosition.setTargetY( pg.height * 0.4f + p.svgHeight(p.gameGraphics.blueBearLose ) * 0.5f );
			else if( p.gameState() == GameState.GAME_OVER_OUTRO )
				_characterPosition.setTargetY( pg.height * 2.2f );
		}

		_characterW = p.scaleV(_curFrame.width * _scale) * _laneScale.value();
		_characterH = p.scaleV(_curFrame.height * _scale) * _laneScale.value();
		_characterPosition.setTargetX( characterX );
		_characterPosition.update();
		
		float characterY = _characterPosition.y() - _characterH * 0.5f;
		
		_shadowPosition.setTargetX( characterX );
		_shadowPosition.update();

		// draw shadow and bear
		DrawUtil.setDrawCenter(pg);
		if( _winJump == true ) {
			characterX = pg.width * 0.5f;
			pg.shape( p.gameGraphics.bearShadow, _shadowPosition.x(), _shadowPosition.y(), p.scaleV(p.gameGraphics.bearShadow.width) * _laneScale.value(), p.scaleV(p.gameGraphics.bearShadow.height) * _laneScale.value() );
			pg.shape( p.gameGraphics.blueBearWin, _characterPosition.x(), characterY, p.scaleV(p.gameGraphics.blueBearWin.width) * _laneScale.value(), p.scaleV(p.gameGraphics.blueBearWin.height) * _laneScale.value() );
		} else if( _loseSad == true ) {
			characterX = pg.width * 0.5f;
			pg.shape( p.gameGraphics.blueBearLose, _characterPosition.x(), characterY, p.scaleV(p.gameGraphics.blueBearLose.width), p.scaleV(p.gameGraphics.blueBearLose.height) );
		} else {
			pg.pushMatrix();
			pg.translate(0, 0, _lane);
			pg.shape( p.gameGraphics.bearShadow, _shadowPosition.x(), _shadowPosition.y(), p.scaleV(p.gameGraphics.bearShadow.width) * _laneScale.value(), p.scaleV(p.gameGraphics.bearShadow.height) * _laneScale.value() );
			pg.image( _curFrame, _characterPosition.x(), characterY, _characterW, _characterH );
			if( p.gameState() == GameState.GAME_PLAYING ) showExplosion();
			pg.popMatrix();
		}
	}
	
	protected void advanceBearFrame(float speed) {
		if( speed > 0 ) {
			speed *= 0.35f;
			float frameInc = 1 - 1f / speed;
			frameInc *= 0.5f;
			if(frameInc > 0) _frameIndex += frameInc;
			_frameIndex = _frameIndex % _frames.length;
			_curFrame = _frames[P.floor(_frameIndex)];
			if( _hurtTime != 0 ) {
				// switch to hurt graphics
				if( _hurtTime + HURT_LENGTH > p.millis() ) {
					if( p.frameCount % 3 != 0 ) { // flicker
						_curFrame = _framesHurt[P.floor(_frameIndex)];
					}
				} else {
					_hurtTime = 0;
				}
			}
		}
		if( _curFrame == null ) return;
	}
	
	public void win() {
		_didWin = true;
	}
	
	public void winJump() {
		_winJump = true;
	}
	
	public void lose() {
		_didLose = true;
		_hurtTime = p.millis() + 4000;
	}
	
	public void loseSad() {
		_loseSad = true;
	}
	
	protected void showExplosion() {
		if( _hurtTime != 0 ) {
			// choose explosion graphic based on half the hurt time
			PShape explosion = p.gameGraphics.explosionLarge;
			if( _hurtTime + HURT_LENGTH / 2f > p.millis() ) {
				explosion = p.gameGraphics.explosionSmall;
			}
			// draw explosion
			float splodeHeight = p.scaleV(explosion.height) * _laneScale.value();
			pg.shape( 
					explosion, 
					_characterPosition.x(), 
					_characterPosition.y() - splodeHeight * 0.5f + _explosionOffsetY, 
					p.scaleV(explosion.width) * _laneScale.value(), 
					splodeHeight
			);

		}
	}
}
