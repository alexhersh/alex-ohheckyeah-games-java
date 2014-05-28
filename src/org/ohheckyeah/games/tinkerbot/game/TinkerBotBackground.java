package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;

public class TinkerBotBackground {

	protected TinkerBot p;
	protected PGraphics pg;

	public TinkerBotBackground() {
		p = (TinkerBot)P.p;
		pg = p.pg;
	}
	
	public void update() {
		DrawUtil.setDrawCenter(pg);
		pg.shape( p.gameGraphics.gameplayBackground, pg.width * 0.5f, pg.height * 0.5f, p.svgWidth(p.gameGraphics.gameplayBackground), p.svgHeight(p.gameGraphics.gameplayBackground) );
	}

}
