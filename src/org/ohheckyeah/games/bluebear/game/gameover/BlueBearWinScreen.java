package org.ohheckyeah.games.bluebear.game.gameover;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;

public class BlueBearWinScreen {

	protected BlueBear p;
	protected PGraphics pg;
	
	protected PShape _endScrollPiece;
	protected boolean _isWin = false;
	protected float _x = 0;

	public BlueBearWinScreen() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		_endScrollPiece = p.gameGraphics.conventionCenter;
		reset();
	}
	
	public void update( float speed ) {
		if( _isWin == true ) {
			_x -= speed;
			pg.shape( _endScrollPiece, _x, 0, p.svgWidth(_endScrollPiece), p.svgHeight(_endScrollPiece) );
		}
	}
	
	public void reset() {
		_isWin = false;
	}
	
	public void startEnd() {
		_isWin = true;
		_x = pg.width;
	}
	
}