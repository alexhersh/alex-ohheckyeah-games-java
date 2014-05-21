package org.ohheckyeah.games.bluebear.game.text;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.game.BlueBearGamePlay;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat3D;

public class BlueBearCountdownDisplay {

	protected BlueBear p;
	protected BlueBearGamePlay catchyGamePlay;
	protected PGraphics pg;

	protected ElasticFloat3D[] _positions;
	protected int _color;
	protected int _timer = -1;
	protected boolean _active = false;

	public BlueBearCountdownDisplay() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		_positions = new ElasticFloat3D[ p.gameGraphics.countdownNumbers.length ];
		for( int i=0; i < _positions.length; i++ ) {
			_positions[i] = new ElasticFloat3D( 0, 0, 0, 0.71f, 0.16f );
		}
	}

	public void updateWithNumber( int timer ) {

		DrawUtil.setDrawCenter( pg );
		pg.pushMatrix();
		pg.translate( pg.width * 0.5f, p.height * 0.5f );

		// update and draw numbers
		int curTimeIndex = timer - 1;
		ElasticFloat3D elasticFloat;
		for( int i=0; i < _positions.length; i++ ) {
			elasticFloat = _positions[i];
			if( curTimeIndex == i ) {
				elasticFloat.setTarget(0f, 0f, 1f);
			} else if( i > curTimeIndex ) {
				elasticFloat.setTarget(0f, p.scaleV(-100), 0f);
			} else if( i < curTimeIndex ) {
				elasticFloat.setTarget(0f, p.scaleV(100), 0f);
			}
			_positions[i].update();
			
			// draw (use z as scale
			if( elasticFloat.z() > 0.1f ) {
				pg.shape( 
						p.gameGraphics.countdownNumbers[i], 
						0, 
						elasticFloat.y(), 
						p.svgWidth(p.gameGraphics.countdownNumbers[i]) * elasticFloat.z(), 
						p.svgHeight(p.gameGraphics.countdownNumbers[i]) * elasticFloat.z()
				);
			}
		}

		pg.popMatrix();
	}
	
	public void show() {
		// reset all elastic objects
		for(ElasticFloat3D elasticFloat : _positions) {
			elasticFloat.setCurrent(0f, p.scaleV(100), 0f);
			elasticFloat.setTarget(0f, p.scaleV(100), 0f);
		}
	}
	
	public void hide() {
	}
}
