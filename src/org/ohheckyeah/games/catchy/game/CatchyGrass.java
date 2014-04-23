package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.math.easing.EasingFloat;

public class CatchyGrass {
	
	protected Catchy p;
	protected CatchyGamePlay catchyGamePlay;
	protected PGraphics pg;
	
	protected EasingFloat _scale = new EasingFloat(0, 7);
	
	

	public CatchyGrass( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
		
	}
	
	public void update( float playerOffset ) {
		_scale.update();
		
		float grassW = p.scaleV(p.gameGraphics.grass.width) * _scale.value();
		if( grassW < pg.width * 1.2f ) grassW = pg.width * 1.2f;
		float grassH = p.scaleV(p.gameGraphics.grass.height) * _scale.value();
		float grassX = catchyGamePlay.gameHalfWidth + playerOffset * 0.35f;
		float grassY = pg.height - grassH/2f;
		
		pg.pushMatrix();
		pg.translate( grassX, grassY );
		pg.shape( 
				p.gameGraphics.grass, 
				0,
				0,
				grassW * _scale.value(), 
				grassH * _scale.value()
		);
		pg.popMatrix();
	}
		
	public void setHiddenState() {
		_scale.setTarget(0f);
	}
	
	public void setWaitingState() {
		_scale.setTarget(2);
	}
	
	public void setGameplayState() {
		_scale.setTarget(1f);
	}
	
	public void setWinState() {
		_scale.setTarget(2);
	}
	
}
