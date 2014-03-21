package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class CatchyCharacter {
	
	protected Catchy p;
	protected CatchyGamePlay catchyGamePlay;
	protected PGraphics pg;
	protected PShape _character;
	
	protected float lastPlayerOffset = 0;
	protected EasingFloat _rotation;
	
	public CatchyCharacter( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
		_rotation = new EasingFloat(0,4);
	}
	
	public void update( float playerOffset ) {
		
		// position character & shadow
		float characterX = catchyGamePlay.gameHalfWidth + playerOffset;
		float characterY = pg.height - 22 - _character.height * p.gameScaleV * 0.5f;
		float characterWidth = _character.width * p.gameScaleV;
		float characterHeight = _character.height * p.gameScaleV;
		float characterShadowY = pg.height - 22 + p.gameScaleV;
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
		pg.shape( p.gameGraphics.shadow, characterX, characterShadowY, characterShadowWidth, characterShadowHeight );
		// draw rotated character
		pg.translate( characterX, characterY, 0 );
		pg.rotateZ( _rotation.value() );
		pg.shape( _character, 0, 0, characterWidth, characterHeight );
		pg.popMatrix();
	}
	
	public void reset() {
		_character = p.gameGraphics.characters.get( catchyGamePlay.gameIndex % p.gameGraphics.characters.size() );
	}
}
