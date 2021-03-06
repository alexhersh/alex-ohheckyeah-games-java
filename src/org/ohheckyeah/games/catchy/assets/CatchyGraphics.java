package org.ohheckyeah.games.catchy.assets;

import java.util.ArrayList;
import java.util.Collections;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.system.FileUtil;

public class CatchyGraphics {
	
	public ArrayList<CatchyCharacterDefinition> characterDefs;
	public CatchyCharacterDefinition blueSquid, greenPanner, pinkEye, wheelieRobot, birdie, bowl;
	
	public ArrayList<PShape> droppables;
	public PShape apple, coin, diamond, iceCreamCone, candy;
	public ArrayList<PShape> droppablesBad;
	public PShape bomb, brick, dirtySock, sword;
	public PShape grass, bushSmall, bushLarge, mountain, shadow, dropperReady, dropperAlmost, dropperDrop, waitingSpinner;
	public PShape gameDivider, catchyLogo, logoConfetti, timerBanner, redDivider, blackDivider;
	public PShape logoOHY, logoLegwork, logoModeSet;
	
	public String font;
	
	public CatchyGraphics() {
		// characters
		blueSquid = new CatchyCharacterDefinition("blue-squid", ColorUtil.colorFromHex("#107DAE"));
		greenPanner = new CatchyCharacterDefinition("green-panner", ColorUtil.colorFromHex("#708424"));
		pinkEye = new CatchyCharacterDefinition("pink-eye", ColorUtil.colorFromHex("#D47AA3"));
		wheelieRobot = new CatchyCharacterDefinition("wheelie-robot", ColorUtil.colorFromHex("#97B1BF"));
		birdie = new CatchyCharacterDefinition("birdie", ColorUtil.colorFromHex("#F2CA18"));
		bowl = new CatchyCharacterDefinition("bowl", ColorUtil.colorFromHex("#A02134"));
		
		characterDefs = new ArrayList<CatchyCharacterDefinition>();
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
		candy = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/droppables/candy.svg" );

		droppables = new ArrayList<PShape>();
		droppables.add(apple);
		droppables.add(coin);
		droppables.add(diamond);
		droppables.add(iceCreamCone);
		droppables.add(candy);
		
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
		dropperReady = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/dropper-ready.svg" );
		dropperAlmost = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/dropper-almost.svg" );
		dropperDrop = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/dropper-drop.svg" );
		waitingSpinner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/scene/waiting-spinner.svg" );
		
		// shell
		gameDivider = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/game-divider-line.svg" );
		catchyLogo = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/catchy-logo.svg" );
		logoConfetti = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/logo-confetti.svg" );
		timerBanner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/timer-banner.svg" );
		redDivider = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/red-squiggle.svg" );
		blackDivider = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/shell/black-squiggle.svg" );
		logoOHY = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-ohheckyeah.svg" );
		logoLegwork = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-legwork.svg" );
		logoModeSet = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-mode-set.svg" );
		
		// add fonts
		font = FileUtil.getHaxademicDataPath() + "fonts/AlegreyaSans-Bold.ttf";
	}
	
	public void shuffleCharacters() {
		Collections.shuffle( characterDefs );
	}
	
}
