package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.hardware.kinect.KinectRegion;
import com.haxademic.core.math.easing.EasingFloat;

public class CatchyGamePlay {
	
	protected Catchy p;
	public int gameWidth;
	public int gameIndex;
	protected EasingFloat _easedControlX;
	
	protected KinectRegion _kinectRegion;
	public PGraphics pg;
	
	protected PShape _character;
	
	public CatchyGamePlay( int gameIndex, int gameWidth, KinectRegion kinectRegion ) {
		p = (Catchy) P.p;
		this.gameIndex = gameIndex;
		this.gameWidth = gameWidth;
		_kinectRegion = kinectRegion;

		pg = p.createGraphics( gameWidth, p.height, P.OPENGL );
		
		_easedControlX = new EasingFloat( 0.5f, 6f );
		_character = p.gameGraphics.characters.get( gameIndex % p.gameGraphics.characters.size() );
	}
	
	public void update() {
		pg.beginDraw();
		
		// set game bg
		int bgColor = ColorUtil.colorFromHex("#E7E867");
		pg.background(bgColor);
		
		DrawUtil.setDrawCenter(pg);
		
		_easedControlX.setTarget( _kinectRegion.controlX() );
		_easedControlX.update();
		float curControlX = _easedControlX.value();
		
		pg.shape( p.gameGraphics.grass, pg.width * 0.5f * curControlX * 0.5f, pg.height + 2 - p.gameGraphics.grass.height * p.gameScaleV * 0.5f );
		pg.shape( _character, pg.width * curControlX, pg.height - 22 - _character.height * p.gameScaleV * 0.5f, _character.width * p.gameScaleV, _character.height * p.gameScaleV );
		

		_kinectRegion.controlZ();
		
		pg.endDraw();
	}
}