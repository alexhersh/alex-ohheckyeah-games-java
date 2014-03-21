package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;

public class CatchyCharacter {
	
	protected Catchy p;
	protected CatchyGamePlay catchyGamePlay;
	protected PGraphics pg;
	protected PShape _character;
	
	protected float lastPlayerOffset = 0;
	
	public CatchyCharacter( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
	}
	
	public void update( float playerOffset ) {
		DrawUtil.setDrawCenter(pg);
		
		float characterX = catchyGamePlay.gameHalfWidth + playerOffset;
		float characterY = pg.height - 22 - _character.height * p.gameScaleV * 0.5f;
		float characterWidth = _character.width * p.gameScaleV;
		float characterHeight = _character.height * p.gameScaleV;
		float characterShadowY = pg.height - 22 + p.gameScaleV;
		float characterShadowWidth = p.gameGraphics.shadow.width * p.gameScaleV;
		float characterShadowHeight = p.gameGraphics.shadow.height * p.gameScaleV;
		float characterSpeed = playerOffset - lastPlayerOffset;
		lastPlayerOffset = playerOffset;
		
		pg.pushMatrix();
		// draw shadow
		pg.shape( p.gameGraphics.shadow, characterX, characterShadowY, characterShadowWidth, characterShadowHeight );
		// draw rotated character
		pg.translate( characterX, characterY, 0 );
		pg.rotateZ( characterSpeed * 0.02f );
		pg.shape( _character, 0, 0, characterWidth, characterHeight );
		pg.popMatrix();
	}
	
	public void reset() {
		_character = p.gameGraphics.characters.get( catchyGamePlay.gameIndex % p.gameGraphics.characters.size() );
	}
}
