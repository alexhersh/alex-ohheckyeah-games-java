package org.ohheckyeah.games.bluebear.assets;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.system.FileUtil;

public class BlueBearGraphics {


	public PShape waitingSpinner, scoreBg;
	public PShape[] scores;
	public PShape roadTile, squirrel, squirrelShadow, bearShadow;
	public PShape explosionSmall, explosionLarge;
	public PShape blueBearLogo;
	public PShape logoOhyTeam, logoOHY;
	public PShape textBroughtToYou;

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
		
		// intro text
		textBroughtToYou = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/text-brought-to-you-by.svg" );
		
		
		// scene
		roadTile = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/road-tile.svg" );
		waitingSpinner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/waiting-spinner.svg" );
		bearShadow = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/bear-shadow.svg" );
		squirrel = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/squirrel-spaceship.svg" );
		squirrelShadow = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/squirrel-shadow.svg" );
		explosionSmall = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/explosion-small.svg" );
		explosionLarge = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/explosion-large.svg" );

		// common OHY svgs
		logoOhyTeam = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-partners-white.svg" );
		logoOHY = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-ohheckyeah.svg" );

		// fonts
		font = FileUtil.getHaxademicDataPath() + "fonts/nunito/Nunito-Bold.ttf";
	}

}
