package org.ohheckyeah.games.bluebear.game;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.system.FileUtil;

public class BlueBearBuildingsScroller {

	protected BlueBear p;
	protected PGraphics pg;

	protected PShape[] _buildingPool;
	protected ArrayList<PShape> _buildingsDisplayed;
	protected int _buildingsStartX = 0;
	protected int _buildingsEndX = 0;
	
	public BlueBearBuildingsScroller() {
		p = (BlueBear)P.p;
		pg = p.pg;

		
		_buildingsDisplayed = new ArrayList<PShape>();
		
		// load buildings from directory
		String imgPath = "games/bluebear/svg/neighborhoods/baker/buildings/";
		ArrayList<String> buildingFiles = FileUtil.getFilesInDirOfType( FileUtil.getHaxademicDataPath() + imgPath, "svg");
		_buildingPool = new PShape[buildingFiles.size()];
		for( int i=0; i < _buildingPool.length; i++ ) {
			_buildingPool[i] = p.loadShape(imgPath + buildingFiles.get(i));
		}
	}
	
	protected int svgWidth( PShape shape ) {
		return P.round(p.scaleV(shape.width));
	}
	
	protected int svgHeight( PShape shape ) {
		return P.round(p.scaleV(shape.height));
	}
	
 	public void update() {
		int speed = 5;
		_buildingsStartX -= speed;
		_buildingsEndX = _buildingsStartX;
		
		// remove first building if it's off-screen, and shift _buildingsStartX by its width
		if( _buildingsDisplayed.size() > 0 ) {
			PShape firstBuilding = _buildingsDisplayed.get(0);
			if( _buildingsStartX < -svgWidth(firstBuilding) ) {
				_buildingsDisplayed.remove(0);
				_buildingsStartX += svgWidth(firstBuilding);
				_buildingsEndX = _buildingsStartX;
			}
		}

		// add all current buildings in array and sum up widths for _buildingsEndX
		PShape curBuilding = null;
		for( int i=0; i < _buildingsDisplayed.size(); i++ ) {
			curBuilding = _buildingsDisplayed.get(i);
			_buildingsEndX += svgWidth(curBuilding);
		}
		
		// find existing extents of buildings in array
		PShape newBuilding = null;
		while( _buildingsEndX < pg.width ) {
			newBuilding = _buildingPool[MathUtil.randRange(0, _buildingPool.length-1)];
			_buildingsEndX += svgWidth(newBuilding);
			_buildingsDisplayed.add(newBuilding);
		}
		
		// draw to fill width of screen
		DrawUtil.setDrawCorner(pg);
		pg.pushMatrix();
		int curX = _buildingsStartX;
		for( int i=0; i < _buildingsDisplayed.size(); i++ ) {
			curBuilding = _buildingsDisplayed.get(i);
			pg.shape( curBuilding, curX, BlueBearRoad.ROAD_Y - p.scaleV(10) - svgHeight(curBuilding), svgWidth(curBuilding), svgHeight(curBuilding) );
			curX += svgWidth(curBuilding);
		}
		pg.popMatrix();
	}
}
