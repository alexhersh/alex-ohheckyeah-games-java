package org.ohheckyeah.games.catchy.game;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.haxademic.core.app.P;
import com.haxademic.core.system.FileUtil;

public class CatchyTracking {
	
	// format for RFC 1123 date string -- "Sun, 06 Nov 1994 08:49:37 GMT"
	// we force our locale here as all http dates are in english
	private final static Locale loc = Locale.US;
	private final static String rfc1123Pattern ="EEE, dd MMM yyyyy HH:mm:ss z";
	public final static SimpleDateFormat rfc1123Format = new SimpleDateFormat(rfc1123Pattern, loc);

	protected String outputDir = FileUtil.getHaxademicDataPath() + "games/catchy/output/";
	protected String cameraImageDir = outputDir + "camera/";
	protected String trackingFileDir = outputDir + "text/";
	protected String trackingGamesFilePath = trackingFileDir + "catchy-game-history.csv";
	protected String trackingPlayersFilePath = trackingFileDir + "catchy-player-history.csv";
	
	public CatchyTracking() {
		initTrackingFile();
	}
	
	protected void initTrackingFile() {
		// create directory if it doesn't exist
		if( FileUtil.fileOrPathExists( outputDir ) == false ) FileUtil.createDir( outputDir );
		if( FileUtil.fileOrPathExists( cameraImageDir ) == false ) FileUtil.createDir( cameraImageDir );
		if( FileUtil.fileOrPathExists( trackingFileDir ) == false ) FileUtil.createDir( trackingFileDir );
		// create csv files with headers
		if( FileUtil.fileOrPathExists( trackingGamesFilePath ) == false ) {
			FileUtil.writeTextToFile( trackingGamesFilePath, "Date,Number of Players,Winner Indexes,Winner Score,Low Score" + "\n" );
		}
		if( FileUtil.fileOrPathExists( trackingPlayersFilePath ) == false ) {
			FileUtil.writeTextToFile( trackingPlayersFilePath, "Date,Player Index,Score,Win,Character" + "\n" );
		}
	}
	
	public void trackGameResult( String date, int numPlayers, String winIndexes, int winScore, int lowScore ) {
		FileUtil.appendTextToFile( 
				trackingGamesFilePath, 
				date +","+ 
				numPlayers +","+ 
				winIndexes +","+ 
				winScore +","+ 
				lowScore + "\n" 
		);
	}
	
	public void trackPlayerResult( String date, int gameIndex, int score, boolean didWin, String characterName ) {
		FileUtil.appendTextToFile( 
				trackingPlayersFilePath, 
				date +","+ 
				gameIndex +","+ 
				score +","+ 
				didWin +","+ 
				characterName + "\n" 
		);
	}
	
	public void saveCameraImage( String date ) {
		// save rgb kinect image with the game timestamp
		P.p.kinectWrapper.getRgbImage().save( cameraImageDir + "/" + date + ".png" );
	}
}
