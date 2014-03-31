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
	protected int _color;
	
	protected float lastPlayerOffset = 0;
	protected EasingFloat _rotation;
	
	protected float _characterX;
	protected float _characterY;
	protected float _characterTopY;
	
	public CatchyCharacter( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
		
		_rotation = new EasingFloat(0,4);
		_color = p.color(240,150,190);
	}
	
	public void update( float playerOffset ) {
		
		float bottomPadding = 22 * p.gameScaleV;
		
		// position character & shadow
		_characterX = catchyGamePlay.gameHalfWidth + playerOffset;
		_characterY = pg.height - bottomPadding - _character.height * 0.5f * p.gameScaleV;
		_characterTopY = _characterY - _character.height * 0.5f * p.gameScaleV;
		float characterWidth = _character.width * p.gameScaleV;
		float characterHeight = _character.height * p.gameScaleV;
		float characterShadowY = pg.height - bottomPadding;
		float characterShadowWidth = p.gameGraphics.shadow.width * p.gameScaleV;
		float characterShadowHeight = p.gameGraphics.shadow.height * p.gameScaleV;
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
		pg.shape( _character, 0, 0, characterWidth, characterHeight );
		pg.popMatrix();
	}
	
	public boolean checkCatch( float x, float y ) {
		if( MathUtil.getDistance(x, y, _characterX, _characterTopY) < 30f * p.gameScaleV ) {
			return true;
		} else {
			return false;
		}
	}
	
	public void reset() {
		_characterDef = p.gameGraphics.characterDefs.get( catchyGamePlay.gameIndex % p.gameGraphics.characterDefs.size() );
		_character = _characterDef.characterDefault;
	}
	
	public int color() {
		return _characterDef.characterColor;
	}
}
