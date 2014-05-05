package org.ohheckyeah.games.bluebear.game.scrolling;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.MathUtil;

public class BlueBearScrollingGraphicsLayer {

	protected BlueBear p;
	protected PGraphics pg;

	protected PShape[] _graphicPool;
	protected ArrayList<PShape> _graphicsDisplayed;
	protected int _graphicsStartX = 0;
	protected int _graphicsEndX = 0;

	protected float _baseY = 0;
	protected float _paddingW = 0;
	protected float _parallaxFactor = 1;
	
	public BlueBearScrollingGraphicsLayer() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		configureLayer();
	}
	
	protected void configureLayer(){} // overridden by subclasses
	
	protected void initLayer(float baseY, float padding, float parallaxFactor) {		
		_graphicsDisplayed = new ArrayList<PShape>();
		_baseY = baseY;
		_paddingW = p.scaleV(padding);
		_parallaxFactor = parallaxFactor;
		
		// TODO: 
		// * add variable padding
		// * add variable Y for clouds
	}
	
	protected int svgWidth( PShape shape ) {
		return P.round(p.scaleV(shape.width));
	}
	
	protected int svgHeight( PShape shape ) {
		return P.round(p.scaleV(shape.height));
	}
	
	public void setGraphicPool( PShape[] pool ) {
		_graphicPool = pool;
	}
	
 	public void update(float speed) {
		_graphicsStartX -= speed * _parallaxFactor;
		_graphicsEndX = _graphicsStartX;
		
		// remove first graphic if it's off-screen, and shift _graphicsStartX by its width
		if( _graphicsDisplayed.size() > 0 ) {
			PShape firstBuilding = _graphicsDisplayed.get(0);
			if( _graphicsStartX < -svgWidth(firstBuilding) ) {
				_graphicsDisplayed.remove(0);
				_graphicsStartX += svgWidth(firstBuilding) + _paddingW;
				_graphicsEndX = _graphicsStartX;
			}
		}

		// add all current graphics in array and sum up widths for _graphicsEndX
		PShape curBuilding = null;
		for( int i=0; i < _graphicsDisplayed.size(); i++ ) {
			curBuilding = _graphicsDisplayed.get(i);
			_graphicsEndX += svgWidth(curBuilding) + _paddingW;
		}
		
		// add new graphics to fill up the screen
		if( _graphicPool != null && _graphicPool.length > 0 ) {
			PShape newBuilding = null;
			while( _graphicsEndX < pg.width ) {
				newBuilding = _graphicPool[MathUtil.randRange(0, _graphicPool.length-1)];
				_graphicsEndX += svgWidth(newBuilding) + _paddingW;
				_graphicsDisplayed.add(newBuilding);
			}
		}
		
		// draw to fill width of screen
		DrawUtil.setDrawCorner(pg);
		pg.pushMatrix();
		int curX = _graphicsStartX;
		for( int i=0; i < _graphicsDisplayed.size(); i++ ) {
			curBuilding = _graphicsDisplayed.get(i);
			pg.shape( curBuilding, curX, _baseY - svgHeight(curBuilding), svgWidth(curBuilding), svgHeight(curBuilding) );
			curX += svgWidth(curBuilding) + _paddingW;
		}
		pg.popMatrix();
	}
}
