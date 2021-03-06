package org.ohheckyeah.games.tinkerbot.assets;

import org.ohheckyeah.games.tinkerbot.TinkerBot;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.system.FileUtil;

public class TinkerBotGraphics {


	public PShape waitingSpinner, gearBackgroundMask;
	public PShape scoreBg, timeBg, levelTimerBg;
	public PShape[] playerParts;
	public PShape[] playerPartsError;
	public PShape playerBar, playerBarError, targetLine;
	public PShape robotBallBottom, robotBallTop, robotBar;
	public PShape robotGun, robotHead, robotMouth;
	public PShape robotGunRight, robotHeadRight, robotMouthRight;
	public PShape robotBeamBar, robotBeamEnd;
	public PShape robotBoltTop, robotBoltBottom;
	public PShape tinkerBotLogo;
	public PShape gameplayBackground, gameplayBackgroundWaiting;
	public PShape textBroughtToYou, textStepIntoZones, textGetReady, textGameOver, textWin, textFail;
	public PShape doorLeft, doorRight;

	public String font;

	public PGraphics gameplayBackgroundImage;

	public TinkerBotGraphics() {

		// title screen
		tinkerBotLogo = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/tinker-bot-logo.svg" );

		// shell
		waitingSpinner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/waiting-gear.svg" );
		gearBackgroundMask = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/gear-background-mask.svg" );
		gameplayBackgroundWaiting = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/gameplay-background-waiting.svg" );
		gameplayBackground = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/gameplay-background.svg" );
		
		scoreBg = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-bg-score.svg" );
		timeBg = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-bg-time.svg" );
		levelTimerBg = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-bg-level-timer.svg" );
		
		doorLeft = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/door-left.svg" );
		doorRight = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/door-right.svg" );
		
		// shell text
		textBroughtToYou = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-brought-to-you-by.svg" );
		textStepIntoZones = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-step-into-your-zone.svg" );
		textGetReady = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-prepare.svg" );
		textWin = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-win.svg" );
		textFail = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-fail-error.svg" );
		textGameOver = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/shell/text-game-over.svg" );
		
		// scene
		playerBar = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-bar.svg" );
		playerBarError = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-bar-error.svg" );
		targetLine = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/target-line.svg" );
		
		robotBallBottom = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-ball-bottom.svg" );
		robotBallTop = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-ball-top.svg" );
		robotBar = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-bar.svg" );
		
		robotGun = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-gun.svg" );
		robotGunRight = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-gun-right.svg" );
		robotHead = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-head.svg" );
		robotHeadRight = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-head-right.svg" );
		robotMouth = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-mouth.svg" );
		robotMouthRight = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-mouth-right.svg" );
		
		robotBeamBar = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-beam-bar.svg" );
		robotBeamEnd = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-beam-end.svg" );

		robotBoltTop = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-bolt-top.svg" );
		robotBoltBottom = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/robot-bolt-bottom.svg" );
		
		playerParts = new PShape[]{
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-01.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-02.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-03.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-04.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-05.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-06.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-07.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-08.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-09.svg" )
		};
		playerPartsError = new PShape[]{
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-01-error.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-02-error.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-03-error.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-04-error.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-05-error.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-06-error.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-07-error.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-08-error.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/tinkerbot/svg/scene/player-part-09-error.svg" )
		};
		
		// cache gameplay background
		TinkerBot p = (TinkerBot) P.p;
		gameplayBackgroundImage = p.createGraphics( p.width, p.height, P.OPENGL );
		gameplayBackgroundImage.smooth(OpenGLUtil.SMOOTH_HIGH);

		DrawUtil.setDrawCenter(gameplayBackgroundImage);
		gameplayBackgroundImage.beginDraw();
		if( p.gameScaleV > p.gameScaleH ) {
			gameplayBackgroundImage.shape( gameplayBackground, gameplayBackgroundImage.width / 2f, gameplayBackgroundImage.height / 2f, p.scaleV(gameplayBackground.width), p.scaleV(gameplayBackground.height) );
		} else {
			gameplayBackgroundImage.shape( gameplayBackground, gameplayBackgroundImage.width / 2f, gameplayBackgroundImage.height / 2f, p.scaleH(gameplayBackground.width), p.scaleH(gameplayBackground.height) );
		}
		gameplayBackgroundImage.endDraw();

	}

}
