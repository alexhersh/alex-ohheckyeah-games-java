package org.ohheckyeah.games.bluebear.game;

import org.ohheckyeah.shared.OHYGameTracking;

import com.haxademic.core.system.FileUtil;

public class BlueBearTracking
extends OHYGameTracking {
	
	protected String trackingGamesFilePath;
	
	public BlueBearTracking() {
		super("bluebear");
		initTrackingFile();
	}
	
	protected void initTrackingFile() {
		// create csv files with headers
		trackingGamesFilePath = trackingTextDir + "bluebear-game-history.csv";
		if( FileUtil.fileOrPathExists( trackingGamesFilePath ) == false ) {
			FileUtil.writeTextToFile( trackingGamesFilePath, "Date,Game Length,Remaining Time,Score,Remaining Health,Obstacles Hit,Obstacles Dropped,Bear Lane Changes,Coins Collected,Honeypots Collected" + "\n" );
		}
	}
	
	public void trackGameResult( String date, int gameLengthSeconds, int remainingSeconds, int score, int remainingHealth, int obstaclesHit, int obstaclesDropped, int bearLaneChanges, int coinsGrabbed, int honeypotsGrabbed ) {
		FileUtil.appendTextToFile( 
				trackingGamesFilePath, 
				date +","+ 
				gameLengthSeconds +","+ 
				remainingSeconds +","+ 
				score +","+ 
				remainingHealth +","+ 
				obstaclesHit +","+ 
				obstaclesDropped +","+ 
				bearLaneChanges +","+ 
				coinsGrabbed +","+ 
				honeypotsGrabbed + "\n" 
		);
	}
}
