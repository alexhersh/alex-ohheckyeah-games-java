package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class CatchyDropper {

	protected Catchy p;
	protected CatchyGamePlay catchyGamePlay;
	protected PGraphics pg;
	protected PShape _dropper;

	protected EasingFloat _positionX;
	protected EasingFloat _positionY;
	
	protected EasingFloat _offsetY;
	protected float _offsetYShowing;
	protected float _offsetYHiding;
	
	protected int _dropperState = 0;
	protected final int STATE_READY = 0;
	protected final int STATE_ALMOST = 1;
	protected final int STATE_DROP = 2;
	
	protected int _lastDropTime = 0;
	protected final int DROP_INTERVAL = 1 * 1000;
	protected float _screenSplitX = 0;
	protected boolean _droppedAtPosition = false;
	protected int _numColumns = 5;
	protected int _numColsOutward = 0;
	protected float _columnWidth;
	protected int _curColumn = 0;
	protected boolean _active = false;

	public CatchyDropper( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
		_positionX = new EasingFloat(0,5);
		_positionY = new EasingFloat(0,4);
		_dropper = p.gameGraphics.dropperReady;
		
		_offsetYShowing = p.scaleV(50);
		_offsetYHiding = p.scaleV(-100f);
		_offsetY = new EasingFloat(_offsetYHiding,7);
		
		// calculate columns for horizontal dropping spread 
		_columnWidth = (catchyGamePlay.gameWidth * 0.75f) / _numColumns;
		_numColsOutward = (int) ((_numColumns-1) / 2f);
	}

	public void update() {

		float topPadding = p.scaleV(-22);
		float shadowPadding = p.scaleV(-22);

		// position dropper & shadow
		float dropperX = catchyGamePlay.gameHalfWidth + _positionX.value();
		float dropperY = 0 + topPadding;
		float dropperWidth = p.scaleV(_dropper.width);
		float dropperHeight = p.scaleV(_dropper.height);
		float dropperShadowY = pg.height - shadowPadding;
		float dropperShadowWidth = p.scaleV(p.gameGraphics.shadow.width);
		float dropperShadowHeight = p.scaleV(p.gameGraphics.shadow.height);
		
		// move to next drop position
		if( _active == true ) {
			if( p.millis() > _lastDropTime + DROP_INTERVAL ) {
				if( MathUtil.randRange(0, 100) > 25 ) {
					if( MathUtil.randBoolean(p) == true ) {
						_curColumn++;
						if( _curColumn > _numColsOutward ) _curColumn -= _numColsOutward;
					} else {
						_curColumn--;
						if( _curColumn < -_numColsOutward ) _curColumn += _numColsOutward;
					}
				} else {
					// do nothing - stay in place
				}
				_positionX.setTarget( _curColumn * _columnWidth );
				_lastDropTime = p.millis();
				_dropperState = STATE_READY;
				_dropper = p.gameGraphics.dropperReady;
			}
			// drop!
			float animAlmostTime = _lastDropTime + DROP_INTERVAL - 750;
			float animDownTime = _lastDropTime + DROP_INTERVAL - 500;
			float animUpTime = _lastDropTime + DROP_INTERVAL - 400;
			
			if( p.millis() > animAlmostTime && _dropperState == STATE_READY ) {
				_dropperState = STATE_ALMOST;
				_dropper = p.gameGraphics.dropperAlmost;
			} else if( p.millis() > animDownTime && p.millis() < animUpTime ) {
				_positionY.setTarget( p.scaleV(20) );
				_droppedAtPosition = false;
			} else if( p.millis() > animUpTime ) {
				if( _droppedAtPosition == false ) {
					catchyGamePlay.launchNewDroppable( dropperX );
					_droppedAtPosition = true;
					_dropperState = STATE_DROP;
					_dropper = p.gameGraphics.dropperDrop;
				}
				_positionY.setTarget(0);
			}
		}
		
		_positionX.update();
		_positionY.update();
		_offsetY.update();

		// draw
		pg.pushMatrix();
		DrawUtil.setDrawCenter(pg);
		// draw shadow
		pg.shape( p.gameGraphics.shadow, dropperX, dropperShadowY, dropperShadowWidth, dropperShadowHeight );
		pg.translate( dropperX, dropperY + p.scaleV(_positionY.value() + _offsetY.value()), 0 );
		pg.shape( _dropper, 0, 0, dropperWidth, dropperHeight );
		pg.popMatrix();
	}
	
	public void showDropper() {
		_offsetY.setTarget( _offsetYShowing );
		_dropperState = STATE_READY;
		_dropper = p.gameGraphics.dropperReady;
	}
	
	public void startDropping() {
		_active = true;
		_lastDropTime = p.millis();
		_dropperState = STATE_READY;
		_dropper = p.gameGraphics.dropperReady;
	}
	
	public void stopDropping() {
		_active = false;
		_offsetY.setTarget( _offsetYHiding );
		_dropperState = STATE_READY;
		_dropper = p.gameGraphics.dropperReady;
		_positionY.setTarget(0);
	}
	
	public void reset() {
		_dropperState = STATE_READY;
		_dropper = p.gameGraphics.dropperReady;
		_curColumn = 0;
		_positionX.setTarget(0);
		_positionY.setTarget(0);
	}
}
