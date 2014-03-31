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
	protected EasingFloat _rotation;
	
	protected float _characterX;
	protected float _characterY;
	protected float _characterTopY;

	protected int _catchTime = 0;
	
	public CatchyCharacter( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
		
		_rotation = new EasingFloat(0,4);
		_color = p.color(240,150,190);
	}
	
	public void update( float playerOffset ) {
		
		float bottomPadding = p.scaleV(22);
		
		// position character & shadow
		_characterX = catchyGamePlay.gameHalfWidth + playerOffset;
		_characterY = pg.height - bottomPadding - _character.height * p.scaleV(0.5f);
		_characterTopY = _characterY - _character.height * p.scaleV(0.5f);
		float characterWidth = p.scaleV(_character.width);
		float characterHeight = p.scaleV(_character.height);
		float characterShadowY = pg.height - bottomPadding;
		float characterShadowWidth = p.scaleV(p.gameGraphics.shadow.width);
		float characterShadowHeight = p.scaleV(p.gameGraphics.shadow.height);
		float characterSpeed = playerOffset - lastPlayerOffset;


		_rotation.setTarget( characterSpeed * 0.02f );
		_rotation.update();
		lastPlayerOffset = playerOffset;
		
		// draw
		pg.pushMatrix();
		DrawUtil.setDrawCenter(pg);
		// draw shadow
		pg.shape( p.gameGraphics.shadow, _characterX, characterShadowY, characterShadowWidth, characterShadowHeight );
		// draw rotated character
		pg.translate( _characterX, _characterY, 0 );
		pg.rotateZ( _rotation.value() );
		PShape curCharacterState = (p.millis() < _catchTime + 300) ? _characterCatch : _character;
		pg.shape( curCharacterState, 0, 0, characterWidth, characterHeight );
		pg.popMatrix();
	}
	
	public boolean checkCatch( float x, float y ) {
		if( MathUtil.getDistance(x, y, _characterX, _characterTopY) < p.scaleV(30f) ) {
			_catchTime = p.millis();
			return true;
		} else {
			return false;
		}
	}
	
	public void reset() {
		_characterDef = p.gameGraphics.characterDefs.get( catchyGamePlay.gameIndex % p.gameGraphics.characterDefs.size() );
		_character = _characterDef.characterDefault;
		_characterCatch = _characterDef.characterCatch;
	}
	
	public int color() {
		return _characterDef.characterColor;
	}
}
