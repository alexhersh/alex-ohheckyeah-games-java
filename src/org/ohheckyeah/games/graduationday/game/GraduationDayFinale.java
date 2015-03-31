package org.ohheckyeah.games.graduationday.game;

import org.ohheckyeah.games.graduationday.GraduationDay;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.LinearFloat;

public class GraduationDayFinale {

	protected GraduationDay p;
	protected PGraphics pg;
	
	protected LinearFloat _levelEndOpacity = new LinearFloat(1, 0.05f);

	public GraduationDayFinale() {
		p = (GraduationDay)P.p;
		pg = p.pg;

		showOutro();
	}
	
	public void showIntro() {

	}
	
	public void showOutro() {

	}
	
	public void levelEnd() {
		_levelEndOpacity.setTarget(1f);
	}
	
	public void levelStart() {
		_levelEndOpacity.setTarget(0);
	}
	
	public void update() {	
		drawFireworks();
		drawFlashes();
	}

	protected void drawFireworks() {
		// draw shapes from their center
		DrawUtil.setDrawCenter(pg);
		
		// this should be a series of fireworks objects that are all doing their own thing. 
		// we might not want to use svgs here - we could draw shapes with code
		PShape firework = p.gameGraphics.firework;
		float scaleDown = 0.4f;
		float fireworkW = p.scaleV(firework.width) * scaleDown;
		float fireworkH = p.scaleV(firework.height) * scaleDown;
		
		// draw a simple burst with the svg placeholder
		float radius = fireworkH * (2f + P.sin(p.frameCount/20f));
		pg.pushMatrix();
		pg.translate( pg.width/2, pg.height/4 );
		for (int i = 0; i < 8; i++) {
			float radians = P.TWO_PI/8f * i;
			float x = P.sin(radians) * radius;
			float y = P.cos(radians) * radius;
			pg.pushMatrix();
			pg.translate( x, y );
			pg.rotate(-radians);
			pg.shape( firework, 0, 0, fireworkW, fireworkH );
			pg.popMatrix();
		}
		pg.popMatrix();
	}
	
	protected void drawFlashes() {
		// draw shapes from their center
		DrawUtil.setDrawCenter(pg);
		
		// let's draw flashes with code, since we don't have an svg yet
		// it would be great to be able to configure which part of the screen the flashes show up
	}
}
