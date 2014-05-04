package org.ohheckyeah.games.bluebear.game;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.system.FileUtil;

public class BlueBearCharacter {

	protected BlueBear p;
	protected PGraphics pg;
	protected PImage[] _frames;
	protected PImage _curFrame;
	protected float _frameIndex = 0;
	protected EasingFloat _yPosition = new EasingFloat(0,6);
	protected float _scale = 0.5f;
	protected int _lane = 0;
	protected int _bearH = 0;

	public BlueBearCharacter() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		// load frame images from directory
		String imgPath = "games/bluebear/images/bear-run-sequence/";
		ArrayList<String> frameFiles = FileUtil.getFilesInDirOfType( FileUtil.getHaxademicDataPath() + imgPath, "png");
		_frames = new PImage[frameFiles.size()];
		for( int i=0; i < _frames.length; i++ ) {
			_frames[i] = p.loadImage(imgPath + frameFiles.get(i));
		}
		
		// init in the top lane
		setLane( _lane );
		_yPosition.setCurrent( _yPosition.target() );
	}
	
	public void setLane( int lane ) {
		_lane = lane;
		_yPosition.setTarget( BlueBearRoad.ROAD_Y + (BlueBearRoad.LANE_H / 2f) + (BlueBearRoad.LANE_H * _lane) - _bearH );
	}
	
	public void update(float speed) {
		speed *= 0.35f;
		float frameInc = 1 - 1f / speed;
		_frameIndex += ( frameInc > 0 ) ? frameInc : _frameIndex;
		_curFrame = _frames[P.floor(_frameIndex % _frames.length)];
		if( _curFrame == null ) return;
		
		// responsive sizing/placement
		int bearW = P.round(p.scaleV(_curFrame.width * _scale));
		_bearH = P.round(p.scaleV(_curFrame.height * _scale));
		int bearX = 20;
		_yPosition.update();
		float bearY = _yPosition.value();

		// draw to fill width of screen
		DrawUtil.setDrawCorner(pg);
		pg.pushMatrix();
		pg.image( _curFrame, bearX, bearY, bearW, _bearH );
		pg.popMatrix();
	}

}
