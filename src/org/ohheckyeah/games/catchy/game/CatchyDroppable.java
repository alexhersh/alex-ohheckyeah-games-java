package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.assets.CatchySounds;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class CatchyDroppable {
	
	protected Catchy p;
	protected CatchyGamePlay catchyGamePlay;
	protected PGraphics pg;
	
	protected PShape _graphic;
	protected boolean _isBad;
	
	protected float _x = 0;
	protected float _y = 0;
	protected float _dropSpeed = 4;
	protected float _xSpeed = 0;
	protected float _rotation;
	protected float _groundY;
	
	protected boolean _active = false;
	protected boolean _catchable = true;
	
	
	protected float characterShadowWidth;
	protected float characterShadowHeight;
	protected final float SCALE = 0.7f;
	protected EasingFloat _scale = new EasingFloat(SCALE,2);
	protected EasingFloat _shadowScale = new EasingFloat(0,4);

	public CatchyDroppable( CatchyGamePlay catchyGamePlay, boolean isBad ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
		
		_isBad = isBad;
		_groundY = pg.height - p.scaleV(60);
		
		characterShadowWidth = p.scaleV(p.gameGraphics.shadow.width);
		characterShadowHeight = p.scaleV(p.gameGraphics.shadow.height);
	}
	
	public void update( float playerOffset, float shadowY ) {
		if( _active == false ) return;
		
		// apply bumped speed
//		_xSpeed *= 0.9;
//		_x += _xSpeed;
		
		// draw shadow
		_shadowScale.update();
		pg.shape( p.gameGraphics.shadow, _x, shadowY, characterShadowWidth * _shadowScale.value(), characterShadowHeight * _shadowScale.value() );
		
		// draw droppable
		_scale.update();
		pg.pushMatrix();
		pg.translate(_x, _y);
		pg.rotate( _rotation );
		pg.shape( _graphic, 0, 0, p.scaleV( _graphic.width * _scale.value() ), p.scaleV( _graphic.height * _scale.value() ) );
		pg.popMatrix();
		_y += p.scaleV(_dropSpeed) * p.timeFactor.multiplier();
		
		// shrink if we hit the ground 
		if( _y > _groundY ) {
			// if( _scale.target() > 0 ) p.sounds.playSound( CatchySounds.DROP_MISS );
			_scale.setTarget(0);
			_shadowScale.setTarget(0);
		}
		
		// check for a catch 
		if( _y > pg.height/2f ) {
			catchyGamePlay.checkCatch( this, _x, _y );
		}
		
		// once object shrinks enough, deactivate droppable
		if( _scale.value() < 0.03f ) {
			_active = false;
		}

	}
	
	public boolean isCatchable() {
		return _catchable;
	}
	
	public boolean isBad() {
		return _isBad;
	}
	
	public void catchSuccess() {
		p.sounds.playSound( CatchySounds.CATCH );
		_scale.setTarget(0);
		_shadowScale.setTarget(0);
		_catchable = false;
	}
	
	public void bumped() {
		//		if( _xSpeed == 0 ) { 
		//			_xSpeed = 10.0f;
		//		}
	}
	
	public void reset( float x, float y ) {
		_x = x;
		_y = y;
		_xSpeed = 0;
		_rotation = p.random( -0.2f, 0.2f );
		if( _isBad == true ) {
			int randIndex = MathUtil.randRange( 0, p.gameGraphics.droppablesBad.size() - 1 );
			_graphic = p.gameGraphics.droppablesBad.get( randIndex );
		} else {
			int randIndex = MathUtil.randRange( 0, p.gameGraphics.droppables.size() - 1 );
			_graphic = p.gameGraphics.droppables.get( randIndex );
		}
		_active = true;
		_catchable = true;
		
		_scale.setCurrent(SCALE);
		_scale.setTarget(SCALE);
		_shadowScale.setCurrent(0);
		_shadowScale.setTarget(1);
	}
}
