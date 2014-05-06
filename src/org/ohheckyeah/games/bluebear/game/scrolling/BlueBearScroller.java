package org.ohheckyeah.games.bluebear.game.scrolling;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.MathUtil;

public class BlueBearScroller {

	protected BlueBear p;
	protected PGraphics pg;

	protected PShape[] _graphicPool;
	protected ArrayList<PShape> _graphicsOnScreen;
	protected ArrayList<Integer> _graphicsPadding;
	protected int _graphicsStartX = 0;
	protected int _graphicsEndX = 0;

	protected float _baseY = 0;
	protected int _paddingLow = 0;
	protected int _paddingHigh = 0;
	protected float _parallaxFactor = 1;
	
	public BlueBearScroller() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		configureLayer();
	}
	
	protected void configureLayer(){} // overridden by subclasses
	
	protected void initLayer(float baseY, float paddingLow, float paddingHigh, float parallaxFactor) {		
		_graphicsOnScreen = new ArrayList<PShape>();
		_graphicsPadding = new ArrayList<Integer>();
		_baseY = baseY;
		_paddingLow = P.round(p.scaleV(paddingLow));
		_paddingHigh = P.round(p.scaleV(paddingHigh));
		_parallaxFactor = parallaxFactor;
	}
	
	public void reset() {
		_graphicsOnScreen.clear();
		_graphicsPadding.clear();
		setGraphicPool(null);
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
	
	protected int newPadding() {
		return MathUtil.randRange(_paddingLow, _paddingHigh);
	}
	
 	public void update(float speed) {
		_graphicsStartX -= speed * _parallaxFactor;
		_graphicsEndX = _graphicsStartX;
		
		// remove first graphic if it's off-screen, and shift _graphicsStartX by its width
		if( _graphicsOnScreen.size() > 0 ) {
			PShape firstGraphic = _graphicsOnScreen.get(0);
			if( _graphicsStartX < -1 * svgWidth(firstGraphic) ) {
				_graphicsStartX += svgWidth(firstGraphic) + _graphicsPadding.get(0);
				_graphicsEndX = _graphicsStartX;
				_graphicsOnScreen.remove(0);
				_graphicsPadding.remove(0);
			}
		}

		// add all current graphics in array and sum up widths for _graphicsEndX
		PShape curGraphic = null;
		for( int i=0; i < _graphicsOnScreen.size(); i++ ) {
			curGraphic = _graphicsOnScreen.get(i);
			_graphicsEndX += svgWidth(curGraphic) + _graphicsPadding.get(i);
		}
		
		// add new graphics to fill up the screen
		if( _graphicPool != null && _graphicPool.length > 0 ) {
			PShape newGraphic = null;
			int newPad = 0;
			while( _graphicsEndX < pg.width ) {
				newGraphic = _graphicPool[MathUtil.randRange(0, _graphicPool.length-1)];
				newPad = newPadding();
				_graphicsEndX += svgWidth(newGraphic) + newPad;
				_graphicsOnScreen.add(newGraphic);
				_graphicsPadding.add(newPad);
			}
		}
		
		// draw current graphics!
		DrawUtil.setDrawCorner(pg);
		pg.pushMatrix();
		int curX = _graphicsStartX;
		for( int i=0; i < _graphicsOnScreen.size(); i++ ) {
			curGraphic = _graphicsOnScreen.get(i);
			pg.shape( curGraphic, curX, _baseY - svgHeight(curGraphic), svgWidth(curGraphic), svgHeight(curGraphic) );
			curX += svgWidth(curGraphic) + _graphicsPadding.get(i);
		}
		pg.popMatrix();
	}
}
