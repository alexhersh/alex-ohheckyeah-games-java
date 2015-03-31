package org.ohheckyeah.games.graduationday.game;

import org.ohheckyeah.games.graduationday.GraduationDay;
import org.ohheckyeah.games.graduationday.assets.GraduationDayColors;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.LinearFloat;

public class GraduationDayBackground {

	protected GraduationDay p;
	protected PGraphics pg;
	
	protected LinearFloat _levelEndOpacity = new LinearFloat(1, 0.05f);

	public GraduationDayBackground() {
		p = (GraduationDay)P.p;
		pg = p.pg;

		showOutro();
	}
	
	public void showIntro() {

	}
	
	public void showOutro() {

	}
	
	public void gameEnd() {
		_levelEndOpacity.setTarget(1f);
	}
	
	public void gameStart() {
		_levelEndOpacity.setTarget(0);
	}
	
	public void update() {	
		drawBackground();
		drawClouds();
		drawAirplane();
		drawOverlay();
	}

	protected void drawBackground() {
		
	}
	
	protected void drawClouds() {
		// draw shapes from their center
		DrawUtil.setDrawCenter(pg);
		
		// show some cute clouds - they probably shouldn't move this fast
		for (int i = 0; i < p.gameGraphics.clouds.length; i++) {
			PShape cloud = p.gameGraphics.clouds[i];
			float cloudW = p.scaleV(cloud.width);
			float cloudH = p.scaleV(cloud.height);
			pg.pushMatrix();
			pg.translate( (pg.width/3f * i + p.frameCount * 2f) % pg.width, pg.height/2 + P.sin(p.frameCount/(50f*i)) * p.scaleV(15f) );
			pg.shape( cloud, 0, 0, cloudW, cloudH );
			pg.popMatrix();
		}
	}
	
	protected void drawAirplane() {
		// draw shapes from their center
		DrawUtil.setDrawCenter(pg);
		
		// how about some particles for the exhaust?
		
		// draw a plane - perhaps break up the svg and connect the plane to the banner with dynamic lines
		// extra credit: make the banner flap in the wind with triangle strip texturing (whoa)
		float planeW = p.scaleV(p.gameGraphics.airplane.width);
		float planeH = p.scaleV(p.gameGraphics.airplane.height);
		pg.pushMatrix();
		pg.translate( pg.width/2, pg.height/2 + P.sin(p.frameCount/50f) * p.scaleV(15f) );
		pg.shape( p.gameGraphics.airplane, 0, 0, planeW, planeH );
		pg.popMatrix();
	}
	
	protected void drawOverlay() {
		DrawUtil.setDrawCorner(pg);
		// draw color overlay on game over
		_levelEndOpacity.update();
		if( _levelEndOpacity.value() > 0.01f ) {
			pg.fill( GraduationDayColors.STAGE_BG, _levelEndOpacity.value() * 255f );
			pg.rect(0, 0, pg.width, pg.height);
		}
	}
}
