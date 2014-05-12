package org.ohheckyeah.games.bluebear.game;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;

public class BlueBearObstacles {
	protected BlueBear p;
	protected PGraphics pg;

	protected PShape[] _graphicPool;
	protected String[] _graphicPoolFiles;
	protected ArrayList<BlueBearObstacle> _obstacles;
	protected int _launchIndex = 0;
	protected int _poolIndex = 0;

	protected float _baseY = 0;
	protected int _paddingLow = 0;
	protected int _paddingHigh = 0;
	protected float _parallaxFactor = 1;
	
	public BlueBearObstacles() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		// create pool
		_obstacles = new ArrayList<BlueBearObstacle>();
		for( int i=0; i < 30; i++ ) {
			_obstacles.add(new BlueBearObstacle());
		}
	}
	
	public void reset() {
		setGraphicPool(null, null);
	}
	
	public void setGraphicPool( PShape[] pool, String[] poolFiles ) {
		_graphicPool = pool;
		_graphicPoolFiles = poolFiles;
		_launchIndex = 0;
		_poolIndex = 0;
	}

	protected int svgWidth( PShape shape ) {
		return P.round(p.scaleV(shape.width));
	}
	
	protected int svgHeight( PShape shape ) {
		return P.round(p.scaleV(shape.height));
	}
	
	public void launch( float launchX, float launchY ) {
		if( _graphicPool == null ) return;
		_launchIndex = (_launchIndex < _obstacles.size() - 1) ? _launchIndex + 1 : 0;
		_poolIndex = (_poolIndex < _graphicPool.length - 1) ? _poolIndex + 1 : 0;
		_obstacles.get(_launchIndex).reset( _graphicPool[_poolIndex], _graphicPoolFiles[_poolIndex], launchX, launchY );
	}
	
	public boolean checkHit( float bearLeft, float bearRight, float objectY ) {
		boolean didHit = false;
		float obstacleLeft;
		float obstacleRight;
		for( int i=0; i < _obstacles.size(); i++ ) {
			BlueBearObstacle obstacle = _obstacles.get(i);
			if( obstacle.graphic != null ) {
				if( Math.abs( obstacle.y - objectY ) < p.scaleV(10) ) {
					obstacleLeft = obstacle.x - svgWidth(obstacle.graphic) / 2f;
					obstacleRight = obstacle.x + svgWidth(obstacle.graphic) / 2f;
					boolean leftSideCollide = obstacleLeft > bearLeft && obstacleLeft < bearRight;
					boolean rightSideCollide = obstacleRight > bearLeft && obstacleRight < bearRight;
					if( obstacle.hit == false ) {
						if( leftSideCollide || rightSideCollide ) {
							didHit = true;
							obstacle.hit();
						}
					}
				}
			}
		}
		return didHit;
	}
	
 	public void update( float speed ) {
		
		// remove left-most graphic if it's off-screen
		if( _obstacles.size() > 0 ) {
			BlueBearObstacle firstObstacle = _obstacles.get(0);
			if( firstObstacle.graphic != null ) {
				if( firstObstacle.x < -1 * svgWidth(firstObstacle.graphic) ) {
					firstObstacle.recycle();
					_obstacles.remove(0);
				}
			}
		}
		
		// draw obstacles
		DrawUtil.setDrawCenter(pg);
		pg.pushMatrix();
		for( int i=0; i < _obstacles.size(); i++ ) {
			BlueBearObstacle obstacle = _obstacles.get(i);
			if( obstacle.graphic != null ) {
				obstacle.update( speed );
				pg.shape( obstacle.graphic, obstacle.x, obstacle.y - svgHeight(obstacle.graphic) * 0.5f, svgWidth(obstacle.graphic), svgHeight(obstacle.graphic) );
			}
		}
		pg.popMatrix();
	}

	public class BlueBearObstacle {
		
		public float x;
		public float y;
		public PShape graphic;
		public String fileName;
		public boolean hit = false;
		
		public BlueBearObstacle() {
			
		}
		
		public void reset( PShape svg, String file, float startX, float startY ) {
			graphic = svg;
			fileName = file;
			x = startX;
			y = startY;
			hit = false;
		}
		
		public void update( float speed ) {
			x -= speed;
		}
		
		public void hit() {
			hit = true;
		}
		
		public void recycle() {
			graphic = null;
			fileName = null;
			hit = false;
		}
		
	}

}
