package org.ohheckyeah.games.bluebear.assets.neighborhoods;

import java.util.ArrayList;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.system.FileUtil;

public class BlueBearNeighborhood {
	
	public int backgroundColor;
	public PShape[] backgroundPool;
	public PShape[] skylinePool;
	public PShape[] buildingsPool;
	public PShape[] sidewalkPool;
	public PShape[] roadPool;
	
	public BlueBearNeighborhood( String directory ) {
		backgroundPool = loadScrollingGraphics( "games/bluebear/svg/neighborhoods/"+ directory +"/background/" );
		skylinePool = loadScrollingGraphics( "games/bluebear/svg/neighborhoods/"+ directory +"/skyline/" );
		buildingsPool = loadScrollingGraphics( "games/bluebear/svg/neighborhoods/"+ directory +"/buildings/" );
		sidewalkPool = loadScrollingGraphics( "games/bluebear/svg/neighborhoods/"+ directory +"/sidewalk/" );
		roadPool = loadScrollingGraphics( "games/bluebear/svg/neighborhoods/"+ directory +"/road/" );
	}
	
	protected PShape[] loadScrollingGraphics( String graphicsDir ) {
		// load graphics from directory
		ArrayList<String> graphicFiles = FileUtil.getFilesInDirOfType( FileUtil.getHaxademicDataPath() + graphicsDir, "svg");
		PShape[] graphicsArray = new PShape[graphicFiles.size()];
		for( int i=0; i < graphicsArray.length; i++ ) {
			graphicsArray[i] = P.p.loadShape(graphicsDir + graphicFiles.get(i));
		}
		return graphicsArray;
	}
}
