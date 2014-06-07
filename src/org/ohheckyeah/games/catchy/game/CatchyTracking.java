package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.shared.OHYGameTracking;

import com.haxademic.core.system.FileUtil;

public class CatchyTracking
extends OHYGameTracking {
	
	protected String trackingGamesFilePath;
	protected String trackingPlayersFilePath;
	
	public CatchyTracking() {
		super("catchy");
		initTrackingFile();
	}
	
	protected void initTrackingFile() {
		// create csv files with headers
		trackingGamesFilePath = trackingTextDir + "catchy-game-history.csv";
		if( FileUtil.fileOrPathExists( trackingGamesFilePath ) == false ) {
			FileUtil.writeTextToFile( trackingGamesFilePath, "Date,Number of Players,Winner Indexes,Winner Score,Low Score" + "\n" );
		}
		trackingPlayersFilePath = trackingTextDir + "catchy-player-history.csv";
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
}
