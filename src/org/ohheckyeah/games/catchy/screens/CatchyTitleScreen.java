package org.ohheckyeah.games.catchy.screens;

import java.util.ArrayList;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.assets.CatchyColors;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class CatchyTitleScreen {
	
	protected Catchy p;
	public int titleWidth;
	public int titleHeight;
	public int borderWidth;
	public int borderHeight;
	public PGraphics pg;
	
	protected int _bgColor;
	protected int _borderColor;
	
	protected ElasticFloat _logoScale = new ElasticFloat(0, 0.87f, 0.08f);
	
	protected ArrayList<ConfettiParticle> _confetti;
	protected int _confettiLaunchTime = 0;
	protected int _confettiLaunchIndex = 0;

	public CatchyTitleScreen() {
		p = (Catchy) P.p;
		titleWidth = p.width - (int) p.scaleV(140);
		titleHeight = p.height - (int) p.scaleV(120);
		borderWidth = p.width - (int) p.scaleV(40);
		borderHeight = p.height - (int) p.scaleV(20);

		pg = p.createGraphics( titleWidth, titleHeight, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_HIGH);
		
		_confetti = new ArrayList<ConfettiParticle>();
		for( int i=0; i < 100; i++ ) {
			_confetti.add( new ConfettiParticle() );
		}
	}
	
	public void reset() {
		_bgColor = CatchyColors.TITLE_SCREEN_BG;
		_borderColor = CatchyColors.TITLE_SCREEN_BORDER;
		_logoScale.setValue(0);
		_logoScale.setTarget(1);
	}
	
	public void update() {
		pg.beginDraw();
		
		// set game bg
		pg.background( _bgColor );
		
		drawConfetti();
		drawLogo();
		drawBorder();
		
		pg.endDraw();
	}
	
	protected void drawConfetti() {
		// update existing confetti
		for( int i=0; i < _confetti.size(); i++ ) {
			_confetti.get(i).update();
		}
		// launch new confetti
		if( p.millis() > _confettiLaunchTime + 1000 ) {
			float spacing = p.scaleV(200);
			for( float x = -spacing; x < pg.width + spacing; x += spacing ) {
				// cycle confetti index
				_confettiLaunchIndex++;
				if( _confettiLaunchIndex >= _confetti.size() ) _confettiLaunchIndex = 0;
				
				// launch new particles with randomness relative to screen size
				float floatXRange = p.width / 36f; 
				float floatYRange = p.height / 6f; 
				_confetti.get( _confettiLaunchIndex ).launchAt(x + p.random(-floatXRange,floatXRange), p.random(-floatYRange*2,-floatYRange) );
			}
			_confettiLaunchTime = p.millis();
		}
	}
	
	protected void drawLogo() {
		DrawUtil.setDrawCenter(pg);
		_logoScale.update();
		pg.shape( p.gameGraphics.catchyLogo, pg.width * 0.5f, pg.height * 0.5f, p.gameGraphics.catchyLogo.width * p.scaleV(_logoScale.val()), p.gameGraphics.catchyLogo.height * p.scaleV(_logoScale.val()) );
	}
	
	protected void drawBorder() {
		// draw border
		DrawUtil.setDrawCorner(pg);
		pg.noFill();
		pg.stroke( _borderColor );
		pg.strokeWeight( 12 );
		pg.rect(0, 0, pg.width, pg.height);
	}
	
	public class ConfettiParticle {
		
		public float _x = 0;
		public float _y = 0;
		public float _ySpeed = 3;
		public float _rot = 0;
		public float _rotSpeed = 0;
		public float _incSpeed = 0;
		public boolean active = false;
		protected float scaledW;
		protected float scaledH;
		
		public ConfettiParticle() {
			scaledW = p.scaleV(p.gameGraphics.logoConfetti.width);
			scaledH = p.scaleV(p.gameGraphics.logoConfetti.height);
		}
		
		public void launchAt( float x, float y ) {
			_x = x;
			_y = y;
			_rotSpeed = p.random(-0.1f, 0.1f);
			_incSpeed = p.random(0.001f, 0.005f);
			active = true;
		}
		
		public void update() {
			if( active == true ) { 
	
				_rot += _rotSpeed;
				_y += p.gameScaleV * _ySpeed * p.timeFactor.multiplier();
				float xOsc = 6 * P.sin(_incSpeed * p.millis());
				float yOsc = 2 * P.cos(_incSpeed * p.millis());
						
				DrawUtil.setDrawCenter(pg);
				pg.pushMatrix();
				pg.translate(_x + xOsc, _y + yOsc);
				pg.rotateZ(_rot);
				pg.shape( p.gameGraphics.logoConfetti, 0, 0, scaledW, scaledH );
				pg.popMatrix();
				
				if( _y > pg.height + 20 ) {
					active = false;
				}
			}
		}
	}
}