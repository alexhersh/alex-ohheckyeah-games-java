package org.ohheckyeah.games.bluebear.game.characters;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.game.BlueBearScreenPositions;

import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class BlueBearNemesis {

	protected BlueBear p;
	protected PGraphics pg;
	protected PImage[] _frames;
	protected PImage _curFrame;
	protected EasingFloat _yPosition = new EasingFloat(0,6);
	protected float _scale = 0.7f;
	protected int _lane = 0;
	protected int _enemyH = 0;

	public BlueBearNemesis() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		// init in the top lane
		setLane( _lane );
		_yPosition.setCurrent( _yPosition.target() );
	}
	
	public void setLane( int lane ) {
		_lane = lane;
		_yPosition.setTarget( BlueBearScreenPositions.LANES_Y[_lane] - _enemyH );
	}
	
	public void update() {
		// responsive sizing/placement		
		int enemyW = P.round(p.scaleV(p.gameGraphics.nemesis.width * _scale));
		_enemyH = P.round(p.scaleV(p.gameGraphics.nemesis.height * _scale));
		int enemyX = pg.width - enemyW - 20;
		_yPosition.update();
		float enemyY = _yPosition.value();

		// draw  sprite
		DrawUtil.setDrawCorner(pg);
		pg.shape( p.gameGraphics.nemesis, enemyX, enemyY, enemyW, _enemyH );
	}

}
