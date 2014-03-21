package org.ohheckyeah.games.catchy.assets;

import java.util.ArrayList;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.system.FileUtil;

public class CatchyGraphics {
	
	public ArrayList<PShape> characters;
	public PShape blueSquid, pinkEye, wheelieRobot;
	public PShape grass, bushSmall, bushLarge, mountain;
	public PShape divider;
	
	public CatchyGraphics() {
		// characters
		blueSquid = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/blue-squid.svg" );
		pinkEye = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/pink-eye.svg" );
		wheelieRobot = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/wheelie-robot.svg" );
		
		characters = new ArrayList<PShape>();
		characters.add(blueSquid);
		characters.add(pinkEye);
		characters.add(wheelieRobot);
		
		// scene
		grass = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/grass.svg" );
		bushSmall = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/bush-small.svg" );
		bushLarge = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/bush-large.svg" );
		mountain = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/mountain.svg" );
		
		// shell
		divider = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/game-divider-line.svg" );
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
