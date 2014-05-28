package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.system.FileUtil;

public class TinkerBotBackground {

	protected TinkerBot p;
	protected PGraphics pg;

	public TinkerBotBackground() {
		p = (TinkerBot)P.p;
		pg = p.pg;
	}
	
	public void update() {
		DrawUtil.setDrawCenter(pg);
		pg.image( p.gameGraphics.gameplayBackgroundImage, pg.width * 0.5f, pg.height * 0.5f, p.gameGraphics.gameplayBackgroundImage.width, p.gameGraphics.gameplayBackgroundImage.height );
	}

}
