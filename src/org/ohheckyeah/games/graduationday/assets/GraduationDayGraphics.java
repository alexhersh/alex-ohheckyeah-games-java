package org.ohheckyeah.games.graduationday.assets;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.system.FileUtil;

public class GraduationDayGraphics {

	public PShape waitingSpinner;
	public PShape playerProgressBar;
	public PShape graduationDayLogo;
	public PShape textBroughtToYou, textStepIntoZones, textGetReady, textGameOver, textWin, textFail;
	public PShape hat;
	public PShape hand;
	public PShape airplane;
	public PShape firework;
	public PShape[] clouds;

	public GraduationDayGraphics() {

		// title screen
		graduationDayLogo = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/shell/graduation-day-logo.svg" );
		hat = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/shell/hat.svg" );
		hand = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/shell/hand-toss.svg" );

		// shell
		waitingSpinner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/shell/waiting-gear.svg" );

		// shell text
		textBroughtToYou = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/shell/text-brought-to-you-by.svg" );
		textStepIntoZones = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/shell/text-step-into-your-zone.svg" );
		textGetReady = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/shell/text-prepare.svg" );
		textWin = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/shell/text-win.svg" );
		textGameOver = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/shell/text-game-over.svg" );

		// scene
		airplane = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/scene/airplane.svg" );
		clouds = new PShape[]{
			P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/scene/cloud-1.svg" ), 
			P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/scene/cloud-2.svg" ), 
			P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/scene/cloud-3.svg" )
		};
		firework = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/graduationday/svg/scene/firework.svg" );
	}

}
