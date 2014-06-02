package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;
import org.ohheckyeah.games.tinkerbot.assets.TinkerBotColors;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.image.ImageUtil;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.math.easing.LinearFloat;

public class TinkerBotClosingDoors {

	protected TinkerBot p;
	protected PGraphics pg;

	protected EasingFloat _leftOffset;
	protected EasingFloat _topOffset;
	protected EasingFloat _rightOffset;
	protected EasingFloat _bottomOffset;
	
	protected float _leftX;
	protected float _leftY;
	protected float _leftW;
	protected float _leftH;

	protected float _rightX;
	protected float _rightY;
	protected float _rightW;
	protected float _rightH;
	
	protected LinearFloat _overlayOpacity = new LinearFloat(0, 0.01f);

	public TinkerBotClosingDoors() {
		p = (TinkerBot)P.p;
		pg = p.pg;
		
		_leftOffset = new EasingFloat(0, 6);
		_topOffset = new EasingFloat(0, 6);
		float[] offsets = ImageUtil.getOffsetAndSizeToCrop( pg.width, pg.height, p.gameGraphics.doorLeft.width, p.gameGraphics.doorLeft.height, true );
		_leftX = offsets[0];
		_leftY = offsets[1];
		_leftW = offsets[2];
		_leftH = offsets[3];
		
		_rightOffset = new EasingFloat(0, 6);
		_bottomOffset = new EasingFloat(0, 6);
		offsets = ImageUtil.getOffsetAndSizeToCrop( pg.width, pg.height, p.gameGraphics.doorRight.width, p.gameGraphics.doorRight.height, true );
		_rightX = offsets[0];
		_rightY = offsets[1];
		_rightW = offsets[2];
		_rightH = offsets[3];
	}

	public void update() {
		// draw doors
		_leftOffset.update();
		_topOffset.update();
		_rightOffset.update();
		_bottomOffset.update();
		DrawUtil.setDrawCorner(pg);
		pg.shape( p.gameGraphics.doorLeft, _leftX + _leftOffset.value(), _leftY + _topOffset.value(), _leftW, _leftH );
		pg.shape( p.gameGraphics.doorRight, _rightX + _rightOffset.value(), _rightY + _bottomOffset.value(), _rightW, _rightH );
		
		// draw darker color overlay
		_overlayOpacity.update();
		DrawUtil.setDrawCorner(pg);
		pg.fill( TinkerBotColors.DOOR_GAME_OVER_OVERLAY, _overlayOpacity.value() * 255f );
		pg.rect(0, 0, pg.width, pg.height);
	}

	public void show() {
		_leftOffset.setCurrent( -_leftW );
		_leftOffset.setTarget(0);
		_topOffset.setCurrent( -_leftH );
		_topOffset.setTarget(0);
		_rightOffset.setCurrent( _rightW );
		_rightOffset.setTarget(0);
		_bottomOffset.setCurrent( _rightH );
		_bottomOffset.setTarget(0);
		
		_overlayOpacity.setCurrent(0);
		_overlayOpacity.setTarget(0.7f);
	}

	public void hide() {
		_leftOffset.setCurrent( -_leftW );
		_leftOffset.setTarget( -_leftW );
		_topOffset.setCurrent( -_leftH );
		_topOffset.setTarget( -_leftH );
		_rightOffset.setCurrent( _rightW );
		_rightOffset.setTarget( _rightW );
		_bottomOffset.setCurrent( _rightH );
		_bottomOffset.setTarget( _rightH );

		_overlayOpacity.setCurrent(0);
		_overlayOpacity.setTarget(0);
	}

}
