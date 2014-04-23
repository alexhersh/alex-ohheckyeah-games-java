package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.math.easing.EasingFloat;

public class CatchyMountainAndBushes {
	
	protected Catchy p;
	protected CatchyGamePlay catchyGamePlay;
	protected PGraphics pg;
	
	protected EasingFloat _scale;
	protected float _mountainX;
	protected float _bushSmallX;
	protected float _bushLargeX;

	protected float _bushesOffsetY;

	public CatchyMountainAndBushes( CatchyGamePlay catchyGamePlay ) {
		p = (Catchy)P.p;
		this.catchyGamePlay = catchyGamePlay;
		pg = catchyGamePlay.pg;
		
		_scale = new EasingFloat(0, 4);
		_bushesOffsetY = p.scaleV(28);
	}
	
	public void update( float playerOffset ) {
		_scale.update();
		
		drawMountain( playerOffset );
		drawBushLarge( playerOffset );
		drawBushSmall( playerOffset );
		
	}
	
	protected void drawMountain( float playerOffset ) {
		float mountainW = p.scaleV(p.gameGraphics.mountain.width) * _scale.value();
		float mountainH = p.scaleV(p.gameGraphics.mountain.height) * _scale.value();
		float mountainX = _mountainX + playerOffset * 0.2f;
		float mountainY = pg.height - mountainH/2f;
		
		pg.pushMatrix();
		pg.translate( mountainX, mountainY );
		pg.scale(_scale.value());
		pg.shape( 
				p.gameGraphics.mountain, 
				0,
				0,
				mountainW, 
				mountainH
		);
		pg.popMatrix();
	}
		
	protected void drawBushLarge( float playerOffset ) {
		float bushLargeW = p.scaleV(p.gameGraphics.bushLarge.width) * _scale.value();
		float bushLargeH = p.scaleV(p.gameGraphics.bushLarge.height) * _scale.value();
		float bushLargeX = _bushLargeX + playerOffset * 0.3f;
		float bushLargeY = pg.height - (_bushesOffsetY * _scale.value()) - bushLargeH/2f;
		
		pg.pushMatrix();
		pg.translate( bushLargeX, bushLargeY );
		pg.scale(_scale.value());
		pg.shape( 
				p.gameGraphics.bushLarge, 
				0,
				0,
				bushLargeW, 
				bushLargeH
		);
		pg.popMatrix();
	}

	protected void drawBushSmall( float playerOffset ) {
		float bushSmallW = p.scaleV(p.gameGraphics.bushSmall.width) * _scale.value();
		float bushSmallH = p.scaleV(p.gameGraphics.bushSmall.height) * _scale.value();
		float bushSmallX = _bushSmallX + playerOffset * 0.4f;
		float bushSmallY = pg.height - (_bushesOffsetY * _scale.value()) - bushSmallH/2f;
		
		pg.pushMatrix();
		pg.translate( bushSmallX, bushSmallY );
		pg.scale(_scale.value());
		pg.shape( 
				p.gameGraphics.bushSmall, 
				0,
				0,
				bushSmallW, 
				bushSmallH
		);
		pg.popMatrix();
	}
	
	public void setHiddenState() {
		_scale.setTarget(0f);
	}
	
	public void setWaitingState() {
		_scale.setTarget(0);
		_scale.setCurrent(0);
	}
	
	public void setGameplayState() {
		_mountainX = MathUtil.randRange( 0, catchyGamePlay.gameWidth );
		_bushSmallX = MathUtil.randRange( 0, catchyGamePlay.gameWidth );
		_bushLargeX = MathUtil.randRange( 0, catchyGamePlay.gameWidth );
		
		_scale.setTarget( MathUtil.randRangeDecimel(0.9f, 1.1f) );
	}
	
	public void setWinState( boolean didWin ) {
		if( didWin == true ) {
			_scale.setTarget(1.5f);
		} else {
			_scale.setTarget(0);			
		}
	}

}
