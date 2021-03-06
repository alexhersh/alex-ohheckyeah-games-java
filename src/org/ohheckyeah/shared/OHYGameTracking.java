package org.ohheckyeah.shared;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.haxademic.core.app.P;
import com.haxademic.core.system.FileUtil;

public class OHYGameTracking {

	// format for RFC 1123 date string -- "Sun, 06 Nov 1994 08:49:37 GMT"
	// we force our locale here as all http dates are in english
	private final static Locale loc = Locale.US;
	private final static String rfc1123Pattern ="EEE, dd MMM yyyyy HH:mm:ss z";
	public final static SimpleDateFormat rfc1123Format = new SimpleDateFormat(rfc1123Pattern, loc);

	protected String trackingOutputDir = FileUtil.getHaxademicOutputPath() + "games" + File.separator;
	protected String outputDir;
	protected String cameraImageDir;
	protected String trackingTextDir;

	public OHYGameTracking( String gameDir ) {
		// build tracking directory paths
		outputDir = trackingOutputDir + gameDir + File.separator;
		cameraImageDir = outputDir + "camera" + File.separator;
		trackingTextDir = outputDir + "text" + File.separator;
		
		// create directory if it doesn't exist
		if( FileUtil.fileOrPathExists( FileUtil.getHaxademicOutputPath() ) == false ) FileUtil.createDir( FileUtil.getHaxademicOutputPath() );
		if( FileUtil.fileOrPathExists( trackingOutputDir ) == false ) FileUtil.createDir( trackingOutputDir );
		if( FileUtil.fileOrPathExists( outputDir ) == false ) FileUtil.createDir( outputDir );
		if( FileUtil.fileOrPathExists( cameraImageDir ) == false ) FileUtil.createDir( cameraImageDir );
		if( FileUtil.fileOrPathExists( trackingTextDir ) == false ) FileUtil.createDir( trackingTextDir );
	}
	
	public void saveCameraImage( String date ) {
		// save rgb kinect image with the game timestamp
		if(P.p.kinectWrapper == null) return;
		P.p.kinectWrapper.getRgbImage().save( cameraImageDir + File.separator + date + ".png" );
	}

}
