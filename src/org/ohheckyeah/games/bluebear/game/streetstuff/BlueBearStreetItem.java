package org.ohheckyeah.games.bluebear.game.streetstuff;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.math.easing.LinearFloat;

public class BlueBearStreetItem {
	
	protected BlueBear p;

	public float x;
	public float y;
	public int lane;
	public float speedX;
	public float speedY;
	public float gravity;
	public float rotation;
	public float rotationSpeed;
	public PShape graphic;
	public String fileName;
	public boolean hit = false;
	public boolean showing = true;
	public boolean kicked = false;
	public boolean isPerson = false;
	protected EasingFloat _scale = new EasingFloat(0, 6);
	protected LinearFloat _scaleKick = new LinearFloat(0, 0.025f );
	
	public BlueBearStreetItem() {
		p = (BlueBear)P.p;
	}
	
	public void reset( PShape svg, String file, float startX, float startY, int newLane ) {
		graphic = svg;
		fileName = file;
		x = startX;
		y = startY;
		lane = newLane;
		speedX = 0;
		speedY = 0;
		gravity = p.scaleV( 3 );
		rotation = 0;
		rotationSpeed = 0;
		hit = false;
		showing = true;
		kicked = false;
		_scale.setCurrent(0);
		_scale.setTarget(1);
		isPerson = ( file.indexOf("Character") != -1 || file.indexOf("Moose") != -1 );
	}
	
	public float scale() {
		if( kicked == false ) {
			return _scale.value();
		} else {
			return _scaleKick.value();
		}
	}
	
	public void update( float speed ) {
		if( kicked == false ) {
			_scale.update();
			x -= speed;
		} else {
			_scaleKick.update();
			x += speedX;
			y += speedY;
			speedY += gravity;
			rotation += rotationSpeed;
		}
	}
	
	public void checkRecycle() {
		if( kicked == false ) {
			if( x + graphic.width < 0 ) {
				recycle();
			}
		} else {
			if( y - graphic.height > p.height ) {	// kicked off the bottom of the screen
				recycle();
			}
		}
	}
	
	public void hit() {
		hit = true;
	}
	
	public void hide() {
		showing = false;
	}
	
	public void kick() {
		kicked = true;
		_scaleKick.setCurrent(1);
		_scaleKick.setTarget(0);
		rotationSpeed = MathUtil.randRangeDecimel( -0.1f, 0.1f );
		speedX = p.scaleV( MathUtil.randRangeDecimel(8, 16) );
		speedY = p.scaleV( MathUtil.randRangeDecimel(-30, -18) );
	}
	
	public void recycle() {
		graphic = null;
		fileName = null;
		_scale.setCurrent(0);
		_scale.setTarget(0);
	}
	
}
