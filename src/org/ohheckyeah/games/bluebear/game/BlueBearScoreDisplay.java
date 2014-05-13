package org.ohheckyeah.games.bluebear.game;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;

public class BlueBearScoreDisplay {
	protected BlueBear p;
	protected PGraphics pg;
	
	protected int _score = 0;
	protected int _health = 5;
	
	public BlueBearScoreDisplay() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
	}
	
	public int addScore() {
		_score += 10;
		P.println("_score = ",_score);
		return _score;
	}
	
	public int addHealth() {
		_health += 1;
		if( _health > 5 ) _health = 5;
		P.println("_health = ",_health);
		return _health;
	}
	
	public int hit() {
		_health--;
		if( _health < 0 ) _health = 0;
		return _health;
	}
	
	public void reset() {
		_score = 0;
		_health = 5;
	}

	public void update() {
		DrawUtil.setDrawCorner(pg);
		PShape scoreSvg = p.gameGraphics.scores[_health];
		pg.shape( scoreSvg, p.scaleV(80), p.scaleV(40), p.svgWidth(scoreSvg), p.svgHeight(scoreSvg) );
	}
}