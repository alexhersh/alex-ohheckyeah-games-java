package org.ohheckyeah.shared.app;

import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.system.FileUtil;

@SuppressWarnings("serial")
public class OHYBaseKinectConfig
extends OHYKinectApp {

	protected void overridePropsFile() {
		_appConfig.setProperty( "kinect_active", "true" );
		_appConfig.setProperty( "fills_screen", "true" );
//		_appConfig.setProperty( "width", "640" );
//		_appConfig.setProperty( "height", "480" );
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
		
		float scaleRatio = p.height / 480;
		p.translate( ( p.width / 2f ) - 640, 0);
		p.scale(scaleRatio);
		
		DrawUtil.setDrawCorner(p);
		DrawUtil.setColorForPImage(p);
		_kinectGrid.updateDebug();
		// p.image( p.kinectWrapper.getRgbImage(), 0, 0);
		// _kinectGrid.update();
		// P.println( _kinectGrid.getRegion(0).controlX() + " , " + _kinectGrid.getRegion(0).controlZ() );
	}
	
	public void keyPressed() {
		super.keyPressed();
		if (p.key == ' ') {
			_kinectGrid.toggleDebugOverhead();
		}
	}

}
