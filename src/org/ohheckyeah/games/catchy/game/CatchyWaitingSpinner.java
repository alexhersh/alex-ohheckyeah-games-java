package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.math.easing.ElasticFloat;

public class CatchyWaitingSpinner {
	
	protected Catchy p;
	protected CatchyGamePlay catchyGamePlay;
	protected PGraphics pg;
	
	protected float _spinnerX = 0;
	protected float _spinnerY = 0;
	protected float _spinnerW = 0;
	protected float _spinnerH = 0;
	
	protected float _rotation = 0;
	protected float _rotationSpeed = 0;
	protected static final float ROTATION_SLOW = 0.1f;
	protected static final float ROTATION_MEDIUM = 0.25f;
	protected static final float ROTATION_FAST = 1f;
	
	protected ElasticFloat _scale = new ElasticFloat(0, 0.71f, 0.16f);

	public CatchyWaitingSpinner( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
		
		// calculate responsive sizes
		_spinnerX = catchyGamePlay.gameHalfWidth;
		_spinnerY = pg.height * 0.5f;
		_spinnerW = p.scaleV(p.gameGraphics.waitingSpinner.width);
		_spinnerH = p.scaleV(p.gameGraphics.waitingSpinner.height);
	}
	
	public void update() {
		_scale.update();
		_rotation -= _rotationSpeed;
		if( _scale.val() > 0 ) {
			pg.pushMatrix();
			pg.translate( _spinnerX, _spinnerY );
			pg.scale( _scale.val() );
			pg.rotate( _rotation );
			pg.shape( p.gameGraphics.waitingSpinner, 0, 0, _spinnerW, _spinnerH );
			pg.popMatrix();
		}
	}
	
	public void show() {
		_scale.setTarget(1);
		_rotationSpeed = ROTATION_SLOW;
	}
	
	public void hide() {
		_scale.setTarget(0);
		_rotationSpeed = ROTATION_SLOW;
	}
	
	public void playerEntered() {
		_scale.setTarget(1.5f);
		_rotationSpeed = ROTATION_MEDIUM;
	}
	
	public void playerLeft() {
		_scale.setTarget(1);
		_rotationSpeed = ROTATION_SLOW;
	}
	
	public void playerDetected() {
		_scale.setTarget(0);
		_rotationSpeed = ROTATION_FAST;
	}
	
}
