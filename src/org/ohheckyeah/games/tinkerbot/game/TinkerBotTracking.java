package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.shared.OHYGameTracking;

import com.haxademic.core.system.FileUtil;

public class TinkerBotTracking
extends OHYGameTracking {
	
	protected String trackingGamesFilePath;

	public TinkerBotTracking() {
		super("tinkerbot");
		initTrackingFile();
	}
	
	protected void initTrackingFile() {
		// create csv file with headers
		trackingGamesFilePath = trackingTextDir + "tinkerbot-game-history.csv";
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
