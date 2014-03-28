package org.ohheckyeah.games.catchy.assets;

import java.util.ArrayList;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.system.FileUtil;

public class CatchyGraphics {
	
	public ArrayList<PShape> characters;
	public PShape blueSquid, pinkEye, wheelieRobot, birdie, bowl, greenPanner;
	public PShape grass, bushSmall, bushLarge, mountain, shadow, dropper;
	public PShape gameDivider, catchyLogo, logoConfetti, timerBanner;
	
	public CatchyGraphics() {
		// characters
		blueSquid = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/blue-squid.svg" );
		pinkEye = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/pink-eye.svg" );
		wheelieRobot = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/wheelie-robot.svg" );
		birdie = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/birdie.svg" );
		bowl = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/bowl.svg" );
		greenPanner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/green-panner.svg" );
		
		characters = new ArrayList<PShape>();
		characters.add(blueSquid);
		characters.add(pinkEye);
		characters.add(wheelieRobot);
		characters.add(birdie);
		characters.add(bowl);
		characters.add(greenPanner);
		
		// scene
		grass = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/grass.svg" );
		bushSmall = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/bush-small.svg" );
		bushLarge = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/bush-large.svg" );
		mountain = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/mountain.svg" );
		shadow = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/shadow.svg" );
		dropper = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/dropper.svg" );
		
		// shell
		gameDivider = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/game-divider-line.svg" );
		catchyLogo = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/catchy-logo.svg" );
		logoConfetti = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/logo-confetti.svg" );
		timerBanner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/timer-banner.svg" );
	}
	
	public void shuffleCharacters() {
		PShape cur = null;
		PShape temp = null;
		int swapIndex = 0;
		for( int i=0; i < characters.size(); i++ ) {
			swapIndex = MathUtil.randRange(0, characters.size() - 1);
			temp = characters.get( swapIndex );
			cur = characters.get( i );
			characters.set( swapIndex, cur );
			characters.set( i, temp );
		}

	}
}
