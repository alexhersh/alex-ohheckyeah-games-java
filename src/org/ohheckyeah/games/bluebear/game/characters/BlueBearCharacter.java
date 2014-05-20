package org.ohheckyeah.games.bluebear.game.characters;

import java.util.ArrayList;

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

	public BlueBearCharacter( BlueBearPlayerControls playerControls ) {
		super( playerControls, 0.3f );
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
	}
	
	public void reset() {
		_frameIndex = 0;
		_curFrame = _frames[P.floor(_frameIndex)];
	}
	
	public void setLane( int lane ) {
		super.setLane( lane );
		_characterPosition.setTargetY( BlueBearScreenPositions.LANES_Y[_lane] );
		_shadowPosition.setTargetY( BlueBearScreenPositions.LANES_Y[_lane] + _bearShadowOffsetY );
		_laneScale.setTarget(1f + _lane * 0.1f);
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
	
	protected void updateGameplay(float speed) {
		advanceBearFrame(speed);
		
		// responsive sizing/placement
		float characterX = p.scaleV(130);
		_characterW = p.scaleV(_curFrame.width * _scale) * _laneScale.value();
		_characterH = p.scaleV(_curFrame.height * _scale) * _laneScale.value();
		_characterPosition.setTargetX( characterX );
		_characterPosition.update();
		float characterY = _characterPosition.y() - _characterH * 0.5f;
		
		_shadowPosition.setTargetX( characterX );
		_shadowPosition.update();

		// draw shadow and bear
		DrawUtil.setDrawCenter(pg);
		pg.pushMatrix();
		pg.translate(0, 0, _lane);
		pg.shape( 
				p.gameGraphics.bearShadow, 
				_shadowPosition.x(), 
				_shadowPosition.y(), 
				p.scaleV(p.gameGraphics.bearShadow.width) * _laneScale.value(), 
				p.scaleV(p.gameGraphics.bearShadow.height) * _laneScale.value() 
		);
		pg.image( _curFrame, _characterPosition.x(), characterY, _characterW, _characterH );
		showExplosion();
		pg.popMatrix();
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
