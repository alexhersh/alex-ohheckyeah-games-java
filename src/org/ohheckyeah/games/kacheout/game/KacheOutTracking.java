package org.ohheckyeah.games.kacheout.game;

import org.ohheckyeah.shared.OHYGameTracking;

import com.haxademic.core.system.FileUtil;

public class KacheOutTracking
extends OHYGameTracking {
	
	protected String trackingGamesFilePath;
	
	public KacheOutTracking() {
		super("kacheout");
		initTrackingFile();
	}
	
	protected void initTrackingFile() {
		// create csv files with headers
		trackingGamesFilePath = trackingTextDir + "kacheout-game-history.csv";
		if( FileUtil.fileOrPathExists( trackingGamesFilePath ) == false ) {
			FileUtil.writeTextToFile( trackingGamesFilePath, "Date,Number of Players,Winner Index,Game Time" + "\n" );
		}
	}
	
	public void trackGameResult( String date, int numPlayers, String winIndexes, int gameTime ) {
		FileUtil.appendTextToFile( 
				trackingGamesFilePath, 
				date +","+ 
				numPlayers +","+ 
				winIndexes +","+ 
				gameTime + "\n" 
		);
	}
	
}
