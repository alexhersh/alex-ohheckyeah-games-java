package org.ohheckyeah.games.bluebear.game.scrolling;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.game.BlueBearRoad;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.system.FileUtil;

public class BlueBearScrollingGraphicsLayer {

	protected BlueBear p;
	protected PGraphics pg;

	protected PShape[] _graphicPool;
	protected ArrayList<PShape> _graphicsDisplayed;
	protected int _graphicsStartX = 0;
	protected int _graphicsEndX = 0;

	protected float _paddingW = 0;
	
	public BlueBearScrollingGraphicsLayer(String svgDirPath, float padding) {
		p = (BlueBear)P.p;
		pg = p.pg;

		_graphicsDisplayed = new ArrayList<PShape>();
		_paddingW = p.scaleV(padding);
		
		// load graphics from directory
		ArrayList<String> graphicFiles = FileUtil.getFilesInDirOfType( FileUtil.getHaxademicDataPath() + svgDirPath, "svg");
		_graphicPool = new PShape[graphicFiles.size()];
		for( int i=0; i < _graphicPool.length; i++ ) {
			_graphicPool[i] = p.loadShape(svgDirPath + graphicFiles.get(i));
		}
		
		// TODO: 
		// * make property names generic, not "graphic"
		// * add variable Y for clouds
		// * add variable padding
		// * add parallax speed for skyline & clouds
		// * add Y baseline for all layers
	}
	
	protected int svgWidth( PShape shape ) {
		return P.round(p.scaleV(shape.width));
	}
	
	protected int svgHeight( PShape shape ) {
		return P.round(p.scaleV(shape.height));
	}
	
 	public void update(float speed) {
		_graphicsStartX -= speed;
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
		PShape newBuilding = null;
		while( _graphicsEndX < pg.width ) {
			newBuilding = _graphicPool[MathUtil.randRange(0, _graphicPool.length-1)];
			_graphicsEndX += svgWidth(newBuilding) + _paddingW;
			_graphicsDisplayed.add(newBuilding);
		}
		
		// draw to fill width of screen
		DrawUtil.setDrawCorner(pg);
		pg.pushMatrix();
		int curX = _graphicsStartX;
		for( int i=0; i < _graphicsDisplayed.size(); i++ ) {
			curBuilding = _graphicsDisplayed.get(i);
			pg.shape( curBuilding, curX, BlueBearRoad.ROAD_Y - p.scaleV(10) - svgHeight(curBuilding), svgWidth(curBuilding), svgHeight(curBuilding) );
			curX += svgWidth(curBuilding) + _paddingW;
		}
		pg.popMatrix();
	}
}
