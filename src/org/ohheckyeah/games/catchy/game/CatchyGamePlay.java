package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.hardware.kinect.KinectRegion;
import com.haxademic.core.math.easing.EasingFloat;

public class CatchyGamePlay {
	
	protected Catchy p;
	public int gameWidth;
	public int gameHalfWidth;
	public int gameIndex;
	protected EasingFloat _easedControlX;
	protected float _autoControl;
	protected boolean _controlsActive = false;
	
	protected KinectRegion _kinectRegion;
	public PGraphics pg;
	
	protected int _bgColor;
	protected CatchyCharacter _character;
	protected float _mountainX;
	protected float _mountainH;
	protected float _bushSmallX;
	protected float _bushLargeX;
	
	public CatchyGamePlay( int gameIndex, int gameWidth, KinectRegion kinectRegion ) {
		p = (Catchy) P.p;
		this.gameIndex = gameIndex;
		this.gameWidth = gameWidth;
		this.gameHalfWidth = Math.round( gameWidth / 2f );
		_kinectRegion = kinectRegion;

		pg = p.createGraphics( gameWidth, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_HIGH);
		
		_easedControlX = new EasingFloat( 0.5f, 6f );
		_autoControl = p.random(0.0001f, 0.001f);
		
		_character = new CatchyCharacter(this);
		
		reset();
	}
	
	public void reset() {
		_bgColor = ColorUtil.colorFromHex("#E7E867");
		_character.reset();
		_mountainX = p.random( 0, gameWidth );
		_mountainH = p.random( 0, gameWidth );
		_bushSmallX = p.random( 0, gameWidth );
		_bushLargeX = p.random( 0, gameWidth );
	}
	
	public void update() {
		pg.beginDraw();
		
		// set game bg
		pg.background( _bgColor );
		
		DrawUtil.setDrawCenter(pg);
		
		// update and ease controls
		if( _controlsActive == true ) {
			if( p.kinectWrapper != null ) {
				_easedControlX.setTarget( _kinectRegion.controlX() );
			} else {
				_easedControlX.setTarget( 0.5f + 0.5f * P.sin(p.millis() * _autoControl) );
			}
		}
		_easedControlX.update();
		float curControlX = _easedControlX.value() - 0.5f;
		float playerOffset = gameWidth * curControlX;
		
		// draw graphics layers
		pg.shape( p.gameGraphics.mountain, _mountainX + playerOffset * 0.2f, pg.height + 2 - p.gameGraphics.mountain.height * p.gameScaleV * 0.5f );
		pg.shape( p.gameGraphics.grass, gameHalfWidth + playerOffset * 0.5f, pg.height + 2 - p.gameGraphics.grass.height * p.gameScaleV * 0.5f );
		_character.update(playerOffset);
		pg.shape( p.gameGraphics.bushSmall, _bushSmallX + playerOffset * 1.6f, pg.height + 10 - p.gameGraphics.bushSmall.height * p.gameScaleV * 0.5f );
		pg.shape( p.gameGraphics.bushLarge, _bushLargeX + playerOffset * 1.8f, pg.height + 10 - p.gameGraphics.bushLarge.height * p.gameScaleV * 0.5f );
		

		pg.endDraw();
	}
	
	public void gameStart() {
		_controlsActive = true;
	}
	
	public void gameOver() {
		_controlsActive = false;
	}
	
}