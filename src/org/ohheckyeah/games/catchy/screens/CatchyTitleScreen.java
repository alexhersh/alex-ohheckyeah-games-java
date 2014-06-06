package org.ohheckyeah.games.catchy.screens;

import java.util.ArrayList;

import org.ohheckyeah.games.catchy.Catchy;
import org.ohheckyeah.games.catchy.assets.CatchyColors;
import org.ohheckyeah.shared.screens.OHYBaseIntroScreen;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class CatchyTitleScreen
extends OHYBaseIntroScreen {
	
	protected Catchy p;
	
	protected int _bgColor;
	protected int _borderColor;
	
	protected ElasticFloat _logoScale = new ElasticFloat(0, 0.87f, 0.08f);
	
	protected ArrayList<ConfettiParticle> _confetti;
	protected int _confettiLaunchTime = 0;
	protected int _confettiLaunchIndex = 0;

	public CatchyTitleScreen() {
		super();
		p = (Catchy) P.p;
		
		_confetti = new ArrayList<ConfettiParticle>();
		for( int i=0; i < 100; i++ ) {
			_confetti.add( new ConfettiParticle() );
		}
	}
	
	public void reset() {
		_bgColor = CatchyColors.TITLE_SCREEN_BG;
		_logoScale.setValue(0);
		_logoScale.setTarget(1);
	}
	
	public void update() {
		canvas.beginDraw();
		
		// set game bg
		canvas.background( _bgColor );
		
		drawConfetti();
		drawLogo();
		drawBorder();
		
		canvas.endDraw();
	}
	
	protected void drawConfetti() {
		// update existing confetti
		for( int i=0; i < _confetti.size(); i++ ) {
			_confetti.get(i).update();
		}
		// launch new confetti
		if( p.millis() > _confettiLaunchTime + 1000 ) {
			float spacing = p.scaleV(200);
			for( float x = -spacing; x < canvas.width + spacing; x += spacing ) {
				// cycle confetti index
				_confettiLaunchIndex++;
				if( _confettiLaunchIndex >= _confetti.size() ) _confettiLaunchIndex = 0;
				
				// launch new particles with randomness relative to screen size
				float floatXRange = canvas.width / 36f; 
				float floatYRange = canvas.height / 6f; 
				_confetti.get( _confettiLaunchIndex ).launchAt(x + p.random(-floatXRange,floatXRange), p.random(-floatYRange*2,-floatYRange) );
			}
			_confettiLaunchTime = p.millis();
		}
	}
	
	protected void drawLogo() {
		DrawUtil.setDrawCenter(canvas);
		_logoScale.update();
		canvas.shape( p.gameGraphics.catchyLogo, canvas.width * 0.5f, canvas.height * 0.5f, p.scaleH(p.gameGraphics.catchyLogo.width * _logoScale.val()), p.scaleH(p.gameGraphics.catchyLogo.height * _logoScale.val()) );
	}
	
	protected void drawBorder() {
		// draw border
		DrawUtil.setDrawCorner(canvas);
		canvas.noFill();
		
		canvas.stroke( CatchyColors.TITLE_SCREEN_BORDER );
		canvas.strokeWeight( p.scaleV(120) );
		canvas.rect(0, 0, canvas.width, canvas.height);
		
		canvas.stroke( CatchyColors.INTRO_SCREENS_BG );
		canvas.strokeWeight( p.scaleV(60) );
		canvas.rect(0, 0, canvas.width, canvas.height);
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
						
				DrawUtil.setDrawCenter(canvas);
				canvas.pushMatrix();
				canvas.translate(_x + xOsc, _y + yOsc);
				canvas.rotateZ(_rot);
				canvas.shape( p.gameGraphics.logoConfetti, 0, 0, scaledW, scaledH );
				canvas.popMatrix();
				
				if( _y > canvas.height + 20 ) {
					active = false;
				}
			}
		}
	}
}