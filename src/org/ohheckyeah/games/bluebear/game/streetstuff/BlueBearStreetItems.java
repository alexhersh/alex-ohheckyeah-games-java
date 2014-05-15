package org.ohheckyeah.games.bluebear.game.streetstuff;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;

public class BlueBearStreetItems {
	protected BlueBear p;
	protected PGraphics pg;

	protected PShape[] _graphicPool;
	protected String[] _graphicPoolFiles;
	protected ArrayList<BlueBearStreetItem> _obstacles;
	protected final int OBSTACLES_POOL_SIZE = 30;
	protected int _launchIndex = 0;
	protected int _poolIndex = 0;
	
	protected float _obstacleSpacing = 0;

	protected float _baseY = 0;
	protected int _paddingLow = 0;
	protected int _paddingHigh = 0;
	protected float _parallaxFactor = 1;
	
	public BlueBearStreetItems() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		// create pool
		_obstacles = new ArrayList<BlueBearStreetItem>();
		for( int i=0; i < OBSTACLES_POOL_SIZE; i++ ) {
			_obstacles.add(new BlueBearStreetItem());
		}
	}
	
	public void reset() {
		setGraphicPool(null, null);
		for( int i=0; i < _obstacles.size(); i++ ) {
			BlueBearStreetItem obstacle = _obstacles.get(i);
			obstacle.recycle();
		}
	}
	
	public void setGraphicPool( PShape[] pool, String[] poolFiles ) {
		_graphicPool = pool;
		_graphicPoolFiles = poolFiles;
		_launchIndex = 0;
		_poolIndex = 0;
	}
	
	public float obstacleSpacing() {
		return _obstacleSpacing;
	}

	public void launch( float launchX, float launchY ) {
		if( _graphicPool == null ) return;
		_launchIndex = (_launchIndex < _obstacles.size() - 1) ? _launchIndex + 1 : 0;
		_poolIndex = (_poolIndex < _graphicPool.length - 1) ? _poolIndex + 1 : 0;
		_obstacles.get(_launchIndex).reset( _graphicPool[_poolIndex], _graphicPoolFiles[_poolIndex], launchX, launchY );
		
		// once we've launched 2 obstacles, calculate the spacing for goodies
		if( _obstacleSpacing == 0 && _launchIndex > 1 ) {
			_obstacleSpacing = _obstacles.get(_launchIndex).x - _obstacles.get(_launchIndex - 1).x;
		}
	}
	
	public BlueBearStreetItem checkHit( float bearLeft, float bearRight, float objectY ) {
		BlueBearStreetItem didHit = null;
		float obstacleLeft;
		float obstacleRight;
		for( int i=0; i < _obstacles.size(); i++ ) {
			BlueBearStreetItem obstacle = _obstacles.get(i);
			if( obstacle.graphic != null ) {
				if( Math.abs( obstacle.y - objectY ) < p.scaleV(10) ) {
					obstacleLeft = obstacle.x - p.svgWidth(obstacle.graphic) / 2f;
					obstacleRight = obstacle.x + p.svgWidth(obstacle.graphic) / 2f;
					boolean leftSideCollide = obstacleLeft > bearLeft && obstacleLeft < bearRight;
					boolean rightSideCollide = obstacleRight > bearLeft && obstacleRight < bearRight;
					if( obstacle.hit == false ) {
						if( leftSideCollide || rightSideCollide ) {
							didHit = obstacle;
							obstacle.hit();
						}
					}
				}
			}
		}
		return didHit;
	}
	
 	public void update( float speed ) {
		// draw obstacles
		DrawUtil.setDrawCenter(pg);
		pg.pushMatrix();
		for( int i=0; i < _obstacles.size(); i++ ) {
			BlueBearStreetItem obstacle = _obstacles.get(i);
			if( obstacle.graphic != null && obstacle.showing == true ) {
				obstacle.update( speed );
				pg.pushMatrix();
				pg.translate( obstacle.x, obstacle.y - p.svgHeight(obstacle.graphic) * 0.5f * obstacle.scale() );
				pg.rotate( obstacle.rotation );
				pg.shape( obstacle.graphic, 0, 0, p.svgWidth(obstacle.graphic) * obstacle.scale(), p.svgHeight(obstacle.graphic) * obstacle.scale() );
				pg.popMatrix();
				obstacle.checkRecycle();
			}
		}
		pg.popMatrix();
	}


}
