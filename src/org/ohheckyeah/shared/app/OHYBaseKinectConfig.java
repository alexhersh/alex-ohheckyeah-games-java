package org.ohheckyeah.shared.app;

import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.system.FileUtil;

@SuppressWarnings("serial")
public class OHYBaseKinectConfig
extends OHYKinectApp {

	protected void overridePropsFile() {
		_appConfig.setProperty( "kinect_active", "true" );
		_appConfig.setProperty( "width", "640" );
		_appConfig.setProperty( "height", "480" );
	}
		
	public void setup( String propertiesFile ) {
		_customPropsFile = FileUtil.getHaxademicDataPath() + "properties/" + propertiesFile;
		super.setup();
		setKinectProperties();
	}

	public void drawApp() {		
		// reset drawing 
		DrawUtil.resetGlobalProps( p );
		p.shininess(1000f); 
		p.lights();
		p.background(0);
		
		DrawUtil.setDrawCorner(p);
		DrawUtil.setColorForPImage(p);
		// p.image( p.kinectWrapper.getRgbImage(), 0, 0);
		// _kinectGrid.update();
		_kinectGrid.updateDebug();
		// P.println( _kinectGrid.getRegion(0).controlX() + " , " + _kinectGrid.getRegion(0).controlZ() );
	}
	
	public void keyPressed() {
		super.keyPressed();
		if (p.key == ' ') {
			_kinectGrid.toggleDebugOverhead();
		}
	}

}
