package org.ohheckyeah.games.graduationday.game;

import org.ohheckyeah.shared.OHYGameTracking;

import com.haxademic.core.system.FileUtil;

public class GraduationDayTracking
extends OHYGameTracking {
	
	protected String trackingGamesFilePath;

	public GraduationDayTracking() {
		super("GraduationDay");
		initTrackingFile();
	}
	
	protected void initTrackingFile() {
		// create csv file with headers
		trackingGamesFilePath = trackingTextDir + "GraduationDay-game-history.csv";
		if( FileUtil.fileOrPathExists( trackingGamesFilePath ) == false ) {
			FileUtil.writeTextToFile( trackingGamesFilePath, "Date,Number of Players,Game Length,Wins,Fails,Score" + "\n" );
		}
	}
	
	public void trackGameResult( String date, int numPlayers, int gameLength, int wins, int fails, int score ) {
		FileUtil.appendTextToFile( 
				trackingGamesFilePath, 
				date +","+ 
				numPlayers +","+ 
				gameLength +","+ 
				wins +","+ 
				fails +","+ 
				score + "\n" 
		);
	}
	
}
