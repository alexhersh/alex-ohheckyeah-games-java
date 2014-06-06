package org.ohheckyeah.shared.screens;

import org.ohheckyeah.shared.OHYTextMessage;
import org.ohheckyeah.shared.assets.OHYColors;

import processing.core.PImage;

import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.system.FileUtil;


public class OHYPartnersCreditsScreen
extends OHYBaseIntroScreen {

	
	protected int _bgColor = OHYColors.INTRO_SCREENS_BG;
	protected OHYTextMessage _text;
	protected PImage _partnerImage;
	protected float _sponsorScale = 0;
	protected float yMove = 0;
	protected float yMoveInc = 0;
	protected float yCurMoveInc = 0;
	
	public OHYPartnersCreditsScreen() {
		super();
		loadPartnersImage();
	}
	
	protected void loadPartnersImage() {
		String partnersImagePath = p.appConfig.getString("partners_image", null);
		if( partnersImagePath != null ) {
			_partnerImage = p.loadImage( FileUtil.getHaxademicDataPath() + partnersImagePath );
			
			// letterbox partners to fill screen
			float imageFillRatioW = (float) pg.width / _partnerImage.width;
			float imageFillRatioH = (float) pg.height / _partnerImage.height;
			_sponsorScale = (imageFillRatioW > imageFillRatioH) ? imageFillRatioH : imageFillRatioW;
		}
	}
	
	public void reset() {
		
	}
	
	public void animateIn() {
		if( _text == null ) _text = buildIntroText( "From Your Friends at:" );
		yMove = canvas.height * 0.3f + _text.image().height;
		yMoveInc = p.scaleV(0.17f);
		yCurMoveInc = yMoveInc;
	}
	
	public void update() {
		if( yMove > 0 ) {
			yMove -= yCurMoveInc;
			yCurMoveInc += yMoveInc;
		}
		
		canvas.beginDraw();
		canvas.clear();
		DrawUtil.setDrawCenter(canvas);
		
		canvas.background(_bgColor);
		drawPartners();
		drawMessage();
		
		canvas.endDraw();
	}
	
	protected void drawMessage() {
		canvas.pushMatrix();
		canvas.translate( canvas.width * 0.5f, yMove - _text.image().height );
		canvas.image( _text.image(), 0, 0, _text.image().width, _text.image().height );
		canvas.popMatrix();
	}
	
	protected void drawPartners() {
		canvas.pushMatrix();
		canvas.translate( canvas.width * 0.5f, canvas.height * 0.5f + yMove );
		canvas.image( _partnerImage, 0, 0, _partnerImage.width * _sponsorScale, _partnerImage.height * _sponsorScale );
		canvas.popMatrix();
	}
	
}