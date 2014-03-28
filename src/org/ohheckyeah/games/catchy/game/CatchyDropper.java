package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
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

	public CatchyDropper( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
		_positionX = new EasingFloat(0,6);
		_positionY = new EasingFloat(0,4);
		_dropper = p.gameGraphics.dropper;
	}

	public void update() {

		float topPadding = -22 * p.gameScaleV;
		float shadowPadding = -22 * p.gameScaleV;

		// position dropper & shadow
		float dropperX = catchyGamePlay.gameHalfWidth + _positionX.value();
		float dropperY = 0 + topPadding;
		float dropperWidth = _dropper.width * p.gameScaleV;
		float dropperHeight = _dropper.height * p.gameScaleV;
		float dropperShadowY = pg.height - shadowPadding;
		float dropperShadowWidth = p.gameGraphics.shadow.width * p.gameScaleV;
		float dropperShadowHeight = p.gameGraphics.shadow.height * p.gameScaleV;
		
		// move to next drop position
		if( p.millis() > _lastDropTime + DROP_INTERVAL ) {
			_positionX.setTarget( p.random(-0.5f * catchyGamePlay.gameHalfWidth, 0.5f * catchyGamePlay.gameHalfWidth) );
			_lastDropTime = p.millis();
		}
		// drop!
		float animDownTime = _lastDropTime + DROP_INTERVAL - 200;
		float animUpTime = _lastDropTime + DROP_INTERVAL - 100;
		if( p.millis() > animDownTime && p.millis() < animUpTime ) {
			_positionY.setTarget( 20  * p.gameScaleV );
			_droppedAtPosition = false;
		} else if( p.millis() > animUpTime ) {
			if( _droppedAtPosition == false ) {
				catchyGamePlay.launchNewDroppable( dropperX );
				_droppedAtPosition = true;
			}
			_positionY.setTarget(0);
		}
		
		_positionX.update();
		_positionY.update();

		// draw
		pg.pushMatrix();
		DrawUtil.setDrawCenter(pg);
		// draw shadow
		pg.shape( p.gameGraphics.shadow, dropperX, dropperShadowY, dropperShadowWidth, dropperShadowHeight );
		pg.translate( dropperX, dropperY + (_positionY.value() * p.gameScaleV), 0 );
		pg.shape( _dropper, 0, 0, dropperWidth, dropperHeight );
		pg.popMatrix();
	}

	public void reset() {
		// _dropper = p.gameGraphics.dropper.get( catchyGamePlay.gameIndex % p.gameGraphics.dropper.size() );
	}
}
