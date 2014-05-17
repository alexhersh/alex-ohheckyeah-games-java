package org.ohheckyeah.games.bluebear.game.characters;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.system.FileUtil;

public class BlueBearCharacter {

	protected BlueBear p;
	protected PGraphics pg;
	protected PImage[] _frames;
	protected PImage[] _framesHurt;
	protected PImage _curFrame;
	protected float _frameIndex = 0;
	protected EasingFloat _yPosition = new EasingFloat(0,6);
	protected float _scale = 0.4f;
	protected int _lane = 0;
	protected float _bearX = 0;
	protected float _bearY = 0;
	protected float _bearW = 0;
	protected float _bearH = 0;
	protected float _shadowY = 0;
	protected float _bearShadowOffsetY = -7;
	protected float _explosionOffsetY = 2;
	protected final int HURT_LENGTH = 400;
	protected int _hurtTime = 0;
	protected EasingFloat _laneScale = new EasingFloat(1, 6);

	public BlueBearCharacter() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
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
		_yPosition.setCurrent( _yPosition.target() );
		
		_bearShadowOffsetY = Math.round(p.scaleV(_bearShadowOffsetY));
		_explosionOffsetY = Math.round(p.scaleV(_explosionOffsetY));
	}
	
	public void reset() {
		_frameIndex = 0;
		_curFrame = _frames[P.floor(_frameIndex)];
	}
	
	public void setLane( int lane ) {
		_lane = lane;
		_yPosition.setTarget( BlueBearScreenPositions.LANES_Y[_lane] );
		_laneScale.setTarget(1f + _lane * 0.1f);
	}
	
	public void hit() {
		_hurtTime = p.millis();
	}
	
	public void startGameplay() {
		
	}
	
	public void startMoving() {

	}
	
	public void stopMoving() {

	}
	
	public float x() {
		return _bearX;
	}
	
	public float xLeft() {
		return _bearX - _bearW * 0.2f;
	}
	
	public float xRight() {
		return _bearX + _bearW * 0.3f;
	}
	
	public float y() {
		return _yPosition.value();
	}
	
	public void update(float speed) {
		_laneScale.update();
		advanceBearFrame(speed);
		
		// responsive sizing/placement
		_bearW = p.scaleV(_curFrame.width * _scale) * _laneScale.value();
		_bearH = p.scaleV(_curFrame.height * _scale) * _laneScale.value();
		_bearX = p.scaleV(130);
		_yPosition.update();
		_bearY = _yPosition.value() - _bearH * 0.5f;
		_shadowY = _yPosition.value() + _bearShadowOffsetY;

		// draw shadow and bear
		DrawUtil.setDrawCenter(pg);
		pg.pushMatrix();
		pg.translate(0, 0, _lane);
		pg.shape( 
				p.gameGraphics.bearShadow, 
				_bearX, 
				_shadowY, 
				p.scaleV(p.gameGraphics.bearShadow.width) * _laneScale.value(), 
				p.scaleV(p.gameGraphics.bearShadow.height) * _laneScale.value() 
		);
		pg.image( _curFrame, _bearX, _bearY, _bearW, _bearH );
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
					_bearX, 
					_shadowY - splodeHeight * 0.5f + _explosionOffsetY, 
					p.scaleV(explosion.width) * _laneScale.value(), 
					splodeHeight
			);

		}
	}
}
