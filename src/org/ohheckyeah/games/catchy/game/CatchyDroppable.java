package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class CatchyDroppable {
	
	protected Catchy p;
	protected CatchyGamePlay catchyGamePlay;
	protected PGraphics pg;
	protected PShape _graphic;
	
	protected float _x = 0;
	protected float _y = 0;
	protected float _dropSpeed = 4;
	protected EasingFloat _rotation;
	
	protected boolean _active = false;
	
	public CatchyDroppable( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
//		reset(0,0);
	}
	
	public void update( float playerOffset ) {
		if( _active == false ) return;
		
		// draw & move -----
		DrawUtil.setDrawCenter(pg);
//		pg.shape( p.gameGraphics.shadow, _x, dropperShadowY, dropperShadowWidth, dropperShadowHeight );
		pg.shape( _graphic, _x, _y, _graphic.width * p.gameScaleV, _graphic.height * p.gameScaleV );
		_y += _dropSpeed * p.gameScaleV * p.timeFactor.multiplier();
		
		// recycle if we hit the ground -------
		if( _y > pg.height - 20 * p.gameScaleV ) {
			_active = false;
		}
		
		// check for a catch -------
		if( _y > pg.height/2f ) {
			catchyGamePlay.checkCatch( this, _x, _y );
		}
	}
	
	public void catchSuccess() {
		_active = false;
		
	}
	
	public void reset( float x, float y ) {
		_x = x;
		_y = y;
		int randIndex = MathUtil.randRange( 0, p.gameGraphics.droppables.size() - 1 );
		_graphic = p.gameGraphics.droppables.get( randIndex );
		_active = true;
	}
}
