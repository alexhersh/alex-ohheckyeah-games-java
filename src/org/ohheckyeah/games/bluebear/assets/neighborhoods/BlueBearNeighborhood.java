package org.ohheckyeah.games.bluebear.assets.neighborhoods;

import java.util.ArrayList;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.system.FileUtil;

public class BlueBearNeighborhood {
	
	public int backgroundColor;
	public int groundColor;
	public PShape[] backgroundPool;
	public String[] backgroundFiles;
	public PShape[] skylinePool;
	public String[] skylineFiles;
	public PShape[] buildingsPool;
	public String[] buildingsFiles;
	public PShape[] sidewalkPool;
	public String[] sidewalkFiles;
	public PShape[] roadPool;
	public String[] roadFiles;
	public PShape[] cloudsPool;
	public String[] cloudsFiles;
	public PShape[] obstaclePool;
	public String[] obstacleFiles;
	public PShape[] goodiePool;
	public String[] goodieFiles;
	protected int[] _randIndexes;
	
	public String[] curGraphicsFiles; // questionable way of handling this, but does the job for now.
	
	public BlueBearNeighborhood( String directory ) {
		backgroundPool = loadScrollingGraphics( "games/bluebear/svg/neighborhoods/"+ directory +"/background/" );
		backgroundFiles = curGraphicsFiles;
		skylinePool = loadScrollingGraphics( "games/bluebear/svg/neighborhoods/"+ directory +"/skyline/" );
		skylineFiles = curGraphicsFiles;
		buildingsPool = loadScrollingGraphics( "games/bluebear/svg/neighborhoods/"+ directory +"/buildings/" );
		buildingsFiles = curGraphicsFiles;
		sidewalkPool = loadScrollingGraphics( "games/bluebear/svg/neighborhoods/"+ directory +"/sidewalk/" );
		sidewalkFiles = curGraphicsFiles;
		roadPool = loadScrollingGraphics( "games/bluebear/svg/neighborhoods/"+ directory +"/road/" );
		roadFiles = curGraphicsFiles;
		cloudsPool = loadScrollingGraphics( "games/bluebear/svg/neighborhoods/"+ directory +"/clouds/" );
		cloudsFiles = curGraphicsFiles;
		obstaclePool = loadScrollingGraphics( "games/bluebear/svg/neighborhoods/"+ directory +"/obstacles/" );
		obstacleFiles = curGraphicsFiles;
		goodiePool = loadScrollingGraphics( "games/bluebear/svg/scene/goodies/" );
		goodieFiles = curGraphicsFiles;
	}
	
	protected PShape[] loadScrollingGraphics( String graphicsDir ) {
		// load graphics from directory
		ArrayList<String> graphicFiles = FileUtil.getFilesInDirOfType( FileUtil.getHaxademicDataPath() + graphicsDir, "svg");
		curGraphicsFiles = new String[graphicFiles.size()];
		
		// create shuffled array of indexes
		_randIndexes = new int[graphicFiles.size()];
		for( int i=0; i < _randIndexes.length; i++ ) _randIndexes[i] = i;
		for( int i=0; i < _randIndexes.length; i++ ) {
			int tmpVal = _randIndexes[i];
			int randIndex = MathUtil.randRange(0, _randIndexes.length - 1);
			_randIndexes[i] = _randIndexes[randIndex];
			_randIndexes[randIndex] = tmpVal;
		}
		
		// load shapes in random order into array
		PShape[] graphicsArray = new PShape[graphicFiles.size()];
		for( int i=0; i < graphicsArray.length; i++ ) {
			graphicsArray[_randIndexes[i]] = P.p.loadShape(graphicsDir + graphicFiles.get(i));
			curGraphicsFiles[_randIndexes[i]] = graphicFiles.get(i);
		}
		return graphicsArray;
	}
}
