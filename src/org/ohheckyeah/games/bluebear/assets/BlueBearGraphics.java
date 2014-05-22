package org.ohheckyeah.games.bluebear.assets;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.system.FileUtil;

public class BlueBearGraphics {


	public PShape waitingSpinner, scoreBg;
	public PShape[] scores;
	public PShape[] countdownNumbers;
	public PShape roadTile, squirrel, squirrelShadow, bearShadow;
	public PShape explosionSmall, explosionLarge;
	public PShape blueBearLogo;
	public PShape blueBearDetected, squirrelDetected, blueBearWin, blueBearLose, conventionCenter;
	public PShape logoOhyTeam, logoOHY;
	public PShape textBroughtToYou, textStepIntoZones, textGetReady, textGameOver, textWin;

	public String font;

	public BlueBearGraphics() {

		// title screen
		blueBearLogo = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/big-blue-logo.svg" );

		// shell
		scores = new PShape[]{
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bear-life-0.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bear-life-1.svg" ),
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bear-life-2.svg" ),
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bear-life-3.svg" ),
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bear-life-4.svg" ),
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bear-life-5.svg" )
		};
		scoreBg = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bg-score.svg" );
		countdownNumbers = new PShape[]{
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/countdown-01.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/countdown-02.svg" ),
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/countdown-03.svg" )
		};
		
		blueBearDetected = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/waiting-bear.svg" );
		squirrelDetected = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/waiting-squirrel.svg" );
		blueBearWin = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/bear-win.svg" );
		blueBearLose = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/bear-lose.svg" );
		
		// shell text
		textBroughtToYou = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/text-brought-to-you-by.svg" );
		textStepIntoZones = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/text-step-into-your-zone.svg" );
		textGetReady = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/text-get-ready.svg" );
		textGameOver = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/text-game-over.svg" );
		textWin = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/text-you-did-it.svg" );
		
		// scene
		roadTile = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/road-tile.svg" );
		waitingSpinner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/waiting-spinner.svg" );
		bearShadow = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/bear-shadow.svg" );
		squirrel = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/squirrel-spaceship.svg" );
		squirrelShadow = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/squirrel-shadow.svg" );
		explosionSmall = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/explosion-small.svg" );
		explosionLarge = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/explosion-large.svg" );
		conventionCenter = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/convention-center.svg" );

		// common OHY svgs
		logoOhyTeam = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-partners-white.svg" );
		logoOHY = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-ohheckyeah.svg" );

		// fonts
		font = FileUtil.getHaxademicDataPath() + "fonts/nunito/Nunito-Bold.ttf";
	}

	public static void drawBgDots( BlueBear p, PGraphics pg, int dotColor ) {
		float _bgDotSize = p.scaleV(45);
		float _bgDotSpaceX = p.scaleV(130);
		float _bgDotSpaceY = p.scaleV(110);
		
		pg.fill( dotColor );
		pg.noStroke();
		int row = 0;
		for( int y=0; y < pg.height + _bgDotSize; y += _bgDotSpaceY ) {
			int startX = (row % 2 == 0) ? 20 : 20 + P.round( _bgDotSpaceX / 2f );
			for( int x=startX; x < pg.width + _bgDotSize; x += _bgDotSpaceX ) {
				pg.ellipse(x, y, _bgDotSize, _bgDotSize);
			}
			row++;
		}
	}
	

}
