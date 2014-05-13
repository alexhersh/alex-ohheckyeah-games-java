package org.ohheckyeah.games.bluebear.game.streetstuff;

import processing.core.PShape;

import com.haxademic.core.math.easing.EasingFloat;

public class BlueBearStreetItem {
	
	public float x;
	public float y;
	public PShape graphic;
	public String fileName;
	public boolean hit = false;
	public boolean showing = true;
	protected EasingFloat _scale = new EasingFloat(0, 6);
	
	public BlueBearStreetItem() {
		
	}
	
	public void reset( PShape svg, String file, float startX, float startY ) {
		graphic = svg;
		fileName = file;
		x = startX;
		y = startY;
		hit = false;
		showing = true;
		_scale.setCurrent(0);
		_scale.setTarget(1);
	}
	
	public float scale() {
		return _scale.value();
	}
	
	public void update( float speed ) {
		x -= speed;
		_scale.update();
	}
	
	public void hit() {
		hit = true;
	}
	
	public void hide() {
		showing = false;
	}
	
	public void recycle() {
		graphic = null;
		fileName = null;
		hit = false;
		_scale.setCurrent(0);
		_scale.setTarget(0);
	}
	
}
