package org.ohheckyeah.games.bluebear.assets;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.system.FileUtil;

public class BlueBearGraphics {


	public PShape waitingSpinner;
	public PShape[] scores;
	public PShape roadTile, squirrel, squirrelShadow, bearShadow;
	public PShape blueBearLogo, logoConfetti;
	public PShape logoOHY, logoLegwork, logoModeSet;

	public String font;

	public BlueBearGraphics() {

		// title screen
		blueBearLogo = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/big-blue-logo.svg" );
		logoConfetti = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/logo-confetti.svg" );

		// shell
		scores = new PShape[]{
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bear-life-0.svg" ), 
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bear-life-1.svg" ),
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bear-life-2.svg" ),
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bear-life-3.svg" ),
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bear-life-4.svg" ),
				P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/bear-life-5.svg" )
		};
		
		// scene
		roadTile = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/road-tile.svg" );
		waitingSpinner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/waiting-spinner.svg" );
		bearShadow = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/bear-shadow.svg" );
		squirrel = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/squirrel-spaceship.svg" );
		squirrelShadow = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/squirrel-shadow.svg" );

		// common OHY svgs
		logoOHY = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-ohheckyeah.svg" );
		logoLegwork = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-legwork.svg" );
		logoModeSet = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-mode-set.svg" );

		// fonts
		font = FileUtil.getHaxademicDataPath() + "fonts/nunito/Nunito-Bold.ttf";
	}

}
