package org.ohheckyeah.games.catchy.assets;

import java.util.ArrayList;
import java.util.Collections;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.system.FileUtil;

public class CatchyGraphics {
	
	public ArrayList<CatchyCharacterDef> characterDefs;
	public CatchyCharacterDef blueSquid, greenPanner, pinkEye, wheelieRobot, birdie, bowl;
	
	public ArrayList<PShape> droppables;
	public PShape apple, coin, diamond, iceCreamCone;
	public ArrayList<PShape> droppablesBad;
	public PShape bomb, brick, dirtySock, sword;
	public PShape grass, bushSmall, bushLarge, mountain, shadow, dropper, waitingSpinner;
	public PShape gameDivider, catchyLogo, logoConfetti, timerBanner;
	
	public String font;
	
	public CatchyGraphics() {
		// characters
		blueSquid = new CatchyCharacterDef("blue-squid", ColorUtil.colorFromHex("#107DAE"));
		greenPanner = new CatchyCharacterDef("green-panner", ColorUtil.colorFromHex("#708424"));
		pinkEye = new CatchyCharacterDef("pink-eye", ColorUtil.colorFromHex("#D47AA3"));
		wheelieRobot = new CatchyCharacterDef("wheelie-robot", ColorUtil.colorFromHex("#97B1BF"));
		birdie = new CatchyCharacterDef("birdie", ColorUtil.colorFromHex("#F2CA18"));
		bowl = new CatchyCharacterDef("bowl", ColorUtil.colorFromHex("#A02134"));
		
		characterDefs = new ArrayList<CatchyCharacterDef>();
		characterDefs.add(blueSquid);
		characterDefs.add(greenPanner);
		characterDefs.add(pinkEye);
		characterDefs.add(wheelieRobot);
		characterDefs.add(birdie);
		characterDefs.add(bowl);
				
		// droppables
		apple = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/droppables/apple.svg" );
		coin = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/droppables/coin.svg" );
		diamond = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/droppables/diamond.svg" );
		iceCreamCone = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/droppables/ice-cream-cone.svg" );

		droppables = new ArrayList<PShape>();
		droppables.add(apple);
		droppables.add(coin);
		droppables.add(diamond);
		droppables.add(iceCreamCone);
		
		bomb = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/droppables/bomb.svg" );
		brick = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/droppables/brick.svg" );
		dirtySock = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/droppables/dirty-sock.svg" );
		sword = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/droppables/sword.svg" );

		droppablesBad = new ArrayList<PShape>();
		droppablesBad.add(bomb);
		droppablesBad.add(brick);
		droppablesBad.add(dirtySock);
		droppablesBad.add(sword);

		// scene
		grass = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/grass.svg" );
		bushSmall = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/bush-small.svg" );
		bushLarge = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/bush-large.svg" );
		mountain = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/mountain.svg" );
		shadow = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/shadow.svg" );
		dropper = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/dropper.svg" );
		waitingSpinner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/waiting-spinner.svg" );
		
		// shell
		gameDivider = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/game-divider-line.svg" );
		catchyLogo = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/catchy-logo.svg" );
		logoConfetti = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/logo-confetti.svg" );
		timerBanner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/timer-banner.svg" );
		
		// add fonts
		font = FileUtil.getHaxademicDataPath() + "fonts/AlegreyaSans-Bold.ttf";
	}
	
	public void shuffleCharacters() {
		Collections.shuffle( characterDefs );
		//
		//		CatchyCharacterDef cur = null;
		//		CatchyCharacterDef temp = null;
		//		int swapIndex = 0;
		//		for( int i=0; i < characterDefs.size(); i++ ) {
		//			swapIndex = MathUtil.randRange(0, characterDefs.size() - 1);
		//			temp = characterDefs.get( swapIndex );
		//			cur = characterDefs.get( i );
		//			characterDefs.set( swapIndex, cur );
		//			characterDefs.set( i, temp );
		//		}
	}
	
	public class CatchyCharacterDef {
		
		public PShape characterDefault;
		public PShape characterCatch;
		public int characterColor;
		
		public CatchyCharacterDef( String characterName, int color ) {
			characterDefault = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/"+ characterName +".svg" );
			characterCatch = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/"+ characterName +"-catch.svg" );
			characterColor = color;
		}
	}
}
