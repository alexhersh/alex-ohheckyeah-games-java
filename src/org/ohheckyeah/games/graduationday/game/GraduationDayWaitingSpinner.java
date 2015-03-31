package org.ohheckyeah.games.graduationday.game;

import org.ohheckyeah.games.graduationday.GraduationDay;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class GraduationDayWaitingSpinner {
	
	protected GraduationDay p;
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
	
	protected final int WAITING_COLOR = ColorUtil.colorFromHex("#ffffff");
	protected final int DETECTED_COLOR = ColorUtil.colorFromHex("#2DFFA0");
	protected final int LOCKED_COLOR = ColorUtil.colorFromHex("#2DFFA0");
	protected int _drawColor = 0;
	
	protected ElasticFloat _scale = new ElasticFloat(0, 0.71f, 0.16f);

	public GraduationDayWaitingSpinner( int spinnerX ) {
		p = (GraduationDay)P.p;
		pg = p.pg;
		
		_spinnerX = spinnerX;
		_spinnerY = pg.height * 0.58f;
		_spinnerW = p.scaleV(p.gameGraphics.waitingSpinner.width);
		_spinnerH = p.scaleV(p.gameGraphics.waitingSpinner.height);
		
		p.gameGraphics.waitingSpinner.disableStyle();
	}
	
	public void update() {
		DrawUtil.setDrawCenter(pg);

		_scale.update();
		_rotation += _rotationSpeed;
		if( _scale.val() > 0 ) {
			pg.noStroke();
			if( _rotationSpeed == ROTATION_MEDIUM ) {
				if( p.frameCount % 6 < 3 ) {
					pg.fill( _drawColor );
				} else {
					pg.fill( WAITING_COLOR );
				}
			} else {
				pg.fill( _drawColor );
			}
			pg.pushMatrix();
			pg.translate( _spinnerX, _spinnerY );
			pg.scale( _scale.val() );
//			pg.rotate( _rotation );
			pg.shape( p.gameGraphics.waitingSpinner, 0, 0, _spinnerW, _spinnerH );
			pg.popMatrix();
		}
	}
	
	public float y() {
		return _spinnerY;
	}
	
	public void show() {
		_scale.setTarget(0.6f);
		_rotationSpeed = ROTATION_SLOW;
		_drawColor = WAITING_COLOR;
	}
	
	public void hide() {
		_scale.setTarget(0);
		_rotationSpeed = ROTATION_SLOW;
		_drawColor = LOCKED_COLOR;
	}
	
	public void playerEntered() {
		_scale.setTarget(1.5f);
		_scale.update();
		_scale.setTarget(0.7f);
		_rotationSpeed = ROTATION_MEDIUM;
		_drawColor = DETECTED_COLOR;
	}
	
	public void playerLeft() {
		_scale.setTarget(0.0f);
		_scale.update();
		_scale.setTarget(0.6f);
		_rotationSpeed = ROTATION_SLOW;
		_drawColor = WAITING_COLOR;
	}
	
	public void playerDetected() {
		_scale.setTarget(0.85f);
		_rotationSpeed = ROTATION_FAST;
		_drawColor = LOCKED_COLOR;
	}
	
}
