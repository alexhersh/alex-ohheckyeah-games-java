package org.ohheckyeah.games.catchy.assets;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.system.FileUtil;

public class CatchyCharacterDefinition {

	public PShape characterDefault;
	public PShape characterCatch;
	public PShape characterSkeleton;
	public int characterColor;

	public CatchyCharacterDefinition( String characterName, int color ) {
		characterDefault = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/"+ characterName +".svg" );
		characterCatch = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/"+ characterName +"-catch.svg" );
		characterSkeleton = P.p.loadShape( FileUtil.getHaxademicDataPath() + "games/catchy/svg/characters/"+ characterName +"-skeleton.svg" );
		characterColor = color;
	}
}
