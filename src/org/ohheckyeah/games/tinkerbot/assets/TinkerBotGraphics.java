package org.ohheckyeah.games.tinkerbot.assets;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.system.FileUtil;

public class TinkerBotGraphics {


	public PShape waitingSpinner;
	public PShape scoreBg, timeBg, levelTimerBg;
	public PShape[] scores;
	public PShape[] countdownNumbers;
	public PShape playerBar, playerBarError, targetLine;
	public PShape tinkerBotLogo;
	public PShape gameplayBackground, gameplayBackgroundWaiting;
	public PShape win;
	public PShape textBroughtToYou, textStepIntoZones, textGetReady, textGameOver, textWin;

	public String font;

	public TinkerBotGraphics() {

		// title screen
		tinkerBotLogo = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/tinker-bot-logo.svg" );

		// shell
		waitingSpinner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/waiting-gear.svg" );
		gameplayBackgroundWaiting = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/gameplay-background-waiting.svg" );
		gameplayBackground = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/gameplay-background.svg" );
		scoreBg = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bg-score.svg" );
		win = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/bear-win.svg" );
		
		scoreBg = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-bg-score.svg" );
		timeBg = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-bg-time.svg" );
		levelTimerBg = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-bg-level-timer.svg" );
		
		// shell text
		textBroughtToYou = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-brought-to-you-by.svg" );
		textStepIntoZones = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-step-into-your-zone.svg" );
		textGetReady = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/text-get-ready.svg" );
		textGameOver = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/text-game-over.svg" );
		textWin = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/text-you-did-it.svg" );
		
		// scene
		playerBar = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-bar.svg" );
		playerBarError = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-bar-error.svg" );
		targetLine = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/target-line.svg" );
	}

}
