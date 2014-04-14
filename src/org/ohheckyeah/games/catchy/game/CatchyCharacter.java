package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.assets.CatchyGraphics.CatchyCharacterDef;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class CatchyCharacter {
	
	protected Catchy p;
	protected CatchyGamePlay catchyGamePlay;
	protected PGraphics pg;
	
	protected CatchyCharacterDef _characterDef;
	protected PShape _character;
	protected PShape _characterCatch;
	protected int _color;
	
	protected float lastPlayerOffset = 0;
	protected EasingFloat _rotation = new EasingFloat(0,4);
	protected EasingFloat _xPosition = new EasingFloat(0, 4);
	protected EasingFloat _scale = new EasingFloat(0, 4);
	protected EasingFloat _bottomPadding = new EasingFloat(0, 4);

	protected boolean _lockedCenter = true;
	protected float _characterX;
	protected float _characterY;
	protected float _catchPointX;
	protected float _catchPointY;
	protected float _characterCatchRadius;
	protected float _characterShadowY;

	protected int _catchTime = 0;
	protected float _catchRadius = 55f;
	
	public CatchyCharacter( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
		
		_color = p.color(0);
	}
	
	public float shadowY() {
		return _characterShadowY;
	}
	
	public void update( float playerOffset ) {
				
		// position character & shadow
		float characterWidth = p.scaleV(_character.width * _scale.value());
		float characterHeight = p.scaleV(_character.height * _scale.value());

		_characterX = ( _lockedCenter == true ) ? catchyGamePlay.gameHalfWidth : catchyGamePlay.gameHalfWidth + playerOffset;
		_xPosition.setTarget( _characterX );
		_characterY = pg.height - _bottomPadding.value() - ( characterHeight * 0.5f );
		_characterShadowY = pg.height - _bottomPadding.value();
		float characterShadowWidth = p.scaleV(p.gameGraphics.shadow.width * _scale.value());
		float characterShadowHeight = p.scaleV(p.gameGraphics.shadow.height * _scale.value());
		float characterSpeed = playerOffset - lastPlayerOffset;


		_rotation.setTarget( characterSpeed * 0.02f );
		_rotation.update();
		_xPosition.update();
		_scale.update();
		_bottomPadding.update();
		lastPlayerOffset = playerOffset;
		
		_characterCatchRadius = (characterHeight / 2f) * 1.0f;
		_catchPointX = _xPosition.value() - _characterCatchRadius * P.sin( -_rotation.value() );
		_catchPointY = _characterY - _characterCatchRadius * P.cos( -_rotation.value() );
		
		// draw
		pg.pushMatrix();
		DrawUtil.setDrawCenter(pg);
		// draw shadow
		pg.shape( p.gameGraphics.shadow, _xPosition.value(), _characterShadowY, characterShadowWidth, characterShadowHeight );
		// draw rotated character
		pg.translate( _xPosition.value(), _characterY, 0 );
		pg.rotateZ( _rotation.value() );
		PShape curCharacterState = (p.millis() < _catchTime + 300) ? _characterCatch : _character;
		pg.shape( curCharacterState, 0, 0, characterWidth, characterHeight );
		pg.popMatrix();
		// drawDebugCatchPoint();
	}
	
	protected void drawDebugCatchPoint() {
		// draw debug catch radius
		pg.pushMatrix();
		pg.fill(255);
		pg.stroke(255,0,0);
		pg.translate( _catchPointX, _catchPointY );
		pg.ellipse( 0, 0, p.scaleV(_catchRadius) * _scale.value(), p.scaleV(_catchRadius) * _scale.value() );
		pg.popMatrix();
	}
	
	public boolean checkCatch( float x, float y ) {
		if( y < _catchPointY && MathUtil.getDistance(x, y, _catchPointX, _catchPointY) < p.scaleV(_catchRadius) ) {
			_catchTime = p.millis();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean checkBump( float x, float y ) {
		//		if( y > _characterTopY + p.scaleV(30f) ) {
		//			if( x > _xPosition.value() - 30 && x < _xPosition.value() + 30 ) {
		//				return true;
		//			} else {
		//				return false;
		//			}
		//		} else {
		return false;
		//		}
	}
	
	public void reset() {
		_characterDef = p.gameGraphics.characterDefs.get( catchyGamePlay.gameIndex % p.gameGraphics.characterDefs.size() );
		_character = _characterDef.characterDefault;
		_characterCatch = _characterDef.characterCatch;
		
		_scale.setTarget(0);
		_scale.setCurrent(0);
	}
	
	public int color() {
		return _characterDef.characterColor;
	}
	
	public void setWaitingState() {
		_lockedCenter = true;
		_scale.setTarget(0);
		_bottomPadding.setTarget( p.scaleV(40) );
	}
	
	public void setSelectedState() {
		_lockedCenter = true;
		_scale.setTarget(3);
		_bottomPadding.setTarget( p.scaleV(60) );
	}
	
	public void setGameplayState() {
		_lockedCenter = false;
		_scale.setTarget(1f);
		_bottomPadding.setTarget( p.scaleV(22) );
	}
	
	public void setWinState( boolean didWin ) {
		_lockedCenter = true;
		if( didWin == true ) {
			_scale.setTarget(3);
			_bottomPadding.setTarget( p.scaleV(40) );
		} else {
			_scale.setTarget(0);
			_bottomPadding.setTarget( p.scaleV(40) );
		}
	
	}

}
