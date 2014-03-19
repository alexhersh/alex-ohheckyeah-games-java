package org.ohheckyeah.games.catchy.assets;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.system.FileUtil;

public class CatchyCharacters {
	
	public PShape blueSquid;
	public PShape greenPanner;
	
	public CatchyCharacters() {
		blueSquid = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/blue-squid.svg" );
		greenPanner = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/green-panner.svg" );
	}
}
