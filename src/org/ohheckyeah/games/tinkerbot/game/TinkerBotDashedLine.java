package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.LinearFloat;

public class TinkerBotDashedLine {

	protected TinkerBot p;
	protected PGraphics pg;
	
	protected LinearFloat _alpha = new LinearFloat(0, 0.05f);
	protected LinearFloat _scale = new LinearFloat(0, 0.05f);

	public TinkerBotDashedLine() {
		p = (TinkerBot)P.p;
		pg = p.pg;
	}
	
	public void update( int curGoalPosition ) {
		_alpha.update();
		_scale.update();
		if( _alpha.value() > 0 ) {
			p.gameGraphics.targetLine.disableStyle();
			DrawUtil.setDrawCenter(pg);
			pg.fill( 255, 255 * _alpha.value() );
			pg.shape(
					p.gameGraphics.targetLine, 
					pg.width / 2f, 
					TinkerBotLayout.PLAYER_Y_CENTER + curGoalPosition * TinkerBotLayout.PLAYER_Y_INC,
					p.scaleH( p.gameGraphics.targetLine.width ),
					p.scaleH( p.gameGraphics.targetLine.height ) * _scale.value()
			);
		}
	}
	
	public void show() {
		_alpha.setTarget(1f);
		_scale.setTarget(1f);
	}
	
	public void hide() {
		_alpha.setTarget(0);
		_scale.setTarget(0);
	}
}
