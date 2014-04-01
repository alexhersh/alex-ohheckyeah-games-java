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
		_dropper = p.gameGraphics.dropper;
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
			}
			// drop!
		
			float animDownTime = _lastDropTime + DROP_INTERVAL - 500;
			float animUpTime = _lastDropTime + DROP_INTERVAL - 400;
			if( p.millis() > animDownTime && p.millis() < animUpTime ) {
				_positionY.setTarget( p.scaleV(20) );
				_droppedAtPosition = false;
			} else if( p.millis() > animUpTime ) {
				if( _droppedAtPosition == false ) {
					catchyGamePlay.launchNewDroppable( dropperX );
					_droppedAtPosition = true;
				}
				_positionY.setTarget(0);
			}
		}
		
		_positionX.update();
		_positionY.update();

		// draw
		pg.pushMatrix();
		DrawUtil.setDrawCenter(pg);
		// draw shadow
		pg.shape( p.gameGraphics.shadow, dropperX, dropperShadowY, dropperShadowWidth, dropperShadowHeight );
		pg.translate( dropperX, dropperY + p.scaleV(_positionY.value()), 0 );
		pg.shape( _dropper, 0, 0, dropperWidth, dropperHeight );
		pg.popMatrix();
	}
	
	public void stopDropping() {
		_active = false;
	}

	public void reset() {
		_curColumn = 0;
		_active = true;
		// _dropper = p.gameGraphics.dropper.get( catchyGamePlay.gameIndex % p.gameGraphics.dropper.size() );
	}
}
