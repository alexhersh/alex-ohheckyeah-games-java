package org.ohheckyeah.games.catchy.game;

import java.util.ArrayList;

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
	protected float _playerOffset = 0;
	protected float _autoControl;
	protected boolean _controlsActive = false;
	
	protected KinectRegion _kinectRegion;
	public PGraphics pg;
	
	protected int _bgColor;
	protected CatchyCharacter _character;
	protected CatchyDropper _dropper;
	protected float _mountainX;
	protected float _mountainH;
	protected float _bushSmallX;
	protected float _bushLargeX;
	
	protected ArrayList<CatchyDroppable> _droppables;
	protected int _droppableIndex = 0;
	
	protected CatchyScoreDisplay _score;
	
	public CatchyGamePlay( int gameIndex, int gameWidth, KinectRegion kinectRegion ) {
		p = (Catchy) P.p;
		this.gameIndex = gameIndex;
		this.gameWidth = gameWidth;
		this.gameHalfWidth = Math.round( gameWidth / 2f );
		_kinectRegion = kinectRegion;

		pg = p.createGraphics( gameWidth, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
		
		_easedControlX = new EasingFloat( 0.5f, 6f );
		_autoControl = p.random(0.0001f, 0.001f);
		
		_character = new CatchyCharacter(this);
		_dropper = new CatchyDropper(this);
		_droppables = new ArrayList<CatchyDroppable>();
		for( int i=0; i < 30; i++ ) {
			_droppables.add( new CatchyDroppable(this) );
		}
		_score = new CatchyScoreDisplay(this);
		
		reset();
	}
	
	// public methods ------------------------------------------------------------------
	public void gameStart() {
		_controlsActive = true;
	}
	
	public void stopDropping() {
		_dropper.stopDropping();
	}
	
	public void gameOver() {
		_controlsActive = false;
	}
	
	public void reset() {
		_bgColor = ColorUtil.colorFromHex("#E7E867");
		_character.reset();
		_score.reset( _character.color() );
		_dropper.reset();
		_mountainX = p.random( 0, gameWidth );
		_mountainH = p.random( 0, gameWidth );
		_bushSmallX = p.random( 0, gameWidth );
		_bushLargeX = p.random( 0, gameWidth );
		
	}
	
	public void launchNewDroppable( float x ) {
		CatchyDroppable droppable = _droppables.get( _droppableIndex );
		_droppableIndex = ( _droppableIndex < _droppables.size() - 1 ) ? _droppableIndex + 1 : 0;
		droppable.reset( x, p.scaleV(10f) );
	}
	
	public void checkCatch( CatchyDroppable droppable, float x, float y ) {
		if( _character.checkCatch(x, y) == true ) {
			droppable.catchSuccess();
			_score.addScore(10);
		}
	}
	
	public void update() {
		pg.beginDraw();
		pg.background( _bgColor );
		updateControls();
		drawGraphicsLayers();
		pg.endDraw();
	}
	
	protected void updateControls() {
		if( _controlsActive == true ) {
			if( p.kinectWrapper != null ) {
				_easedControlX.setTarget( _kinectRegion.controlX() * 2f );
			} else {
				_easedControlX.setTarget( 0.5f * P.sin(p.millis() * _autoControl) );
			}
		}
		_easedControlX.update();
		float curControlX = _easedControlX.value();
		_playerOffset = gameHalfWidth * curControlX;
	}
	
	// draw graphics ------------------------------------------------------------------
	protected void drawGraphicsLayers() {
		DrawUtil.setDrawCorner(pg);
		drawMountain();
		drawBushes();
		drawGrass();
		drawDroppables();
		DrawUtil.setDrawCenter(pg);
		_character.update(_playerOffset);
		_dropper.update();
		DrawUtil.setDrawCorner(pg);
		_score.drawScore();
	}
	
	protected void drawMountain() {
		float mountainW = p.scaleV(p.gameGraphics.mountain.width);
		float mountainH = p.scaleV(p.gameGraphics.mountain.height);
		pg.shape( 
				p.gameGraphics.mountain, 
				_mountainX - mountainW/2f + _playerOffset * 0.2f, 
				pg.height - mountainH, 
				mountainW, 
				mountainH
		);
	}
	
	protected void drawGrass() {
		float grassW = p.scaleV(p.gameGraphics.grass.width);
		float grassH = p.scaleV(p.gameGraphics.grass.height);
		pg.shape( 
				p.gameGraphics.grass, 
				gameHalfWidth - grassW/2f + _playerOffset * 0.5f, 
				pg.height - grassH,
				grassW, 
				grassH
		);
	}
	
	protected void drawDroppables() {
		for( int i=0; i < _droppables.size(); i++ ) {
			_droppables.get(i).update(_playerOffset);
		}
	}
	
	protected void drawBushes() {
		// small
		float bushSmallW = p.scaleV(p.gameGraphics.bushSmall.width);
		float bushSmallH = p.scaleV(p.gameGraphics.bushSmall.height);
		pg.shape( 
				p.gameGraphics.bushSmall, 
				_bushSmallX - bushSmallW/2f + _playerOffset * 1.6f, 
				pg.height - bushSmallH, 
				bushSmallW, 
				bushSmallH
		);
		// large
		float bushLargeW = p.scaleV(p.gameGraphics.bushLarge.width);
		float bushLargeH = p.scaleV(p.gameGraphics.bushLarge.height);
		pg.shape( 
				p.gameGraphics.bushLarge, 
				_bushLargeX - bushLargeW/2f + _playerOffset * 1.8f, 
				pg.height - bushLargeH, 
				bushLargeW, 
				bushLargeH
		);

	}
	
}