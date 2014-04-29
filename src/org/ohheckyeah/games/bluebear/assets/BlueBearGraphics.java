package org.ohheckyeah.games.bluebear.assets;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.system.FileUtil;

public class BlueBearGraphics {
	
	
	public PShape waitingSpinner;
	public PShape roadTile;
	public PShape blueBearLogo, logoConfetti, redDivider;
	public PShape logoOHY, logoLegwork, logoModeSet;
	
	public String font;
	
	public BlueBearGraphics() {
				
		// scene
		waitingSpinner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/waiting-spinner.svg" );
		roadTile = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/scene/road-tile.svg" );
		
		// title screen
		blueBearLogo = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/big-blue-logo.svg" );
		logoConfetti = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/logo-confetti.svg" );
		
		// common OHY svgs
		redDivider = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/red-squiggle.svg" );
		logoOHY = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-ohheckyeah.svg" );
		logoLegwork = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-legwork.svg" );
		logoModeSet = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-mode-set.svg" );
		
		// add fonts
		font = FileUtil.getHaxademicDataPath() + "fonts/AlegreyaSans-Bold.ttf";
	}
	
	//	public void shuffleCharacters() {
	//		Collections.shuffle( characterDefs );
	//	}
	
}
