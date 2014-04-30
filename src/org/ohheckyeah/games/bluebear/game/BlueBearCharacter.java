package org.ohheckyeah.games.bluebear.game;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;
import processing.core.PImage;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.system.FileUtil;

public class BlueBearCharacter {

	protected BlueBear p;
	protected PGraphics pg;
	protected PImage[] _frames;
	protected PImage _curFrame;
	protected float _scale = 0.4f;

	public BlueBearCharacter() {
		p = (BlueBear)P.p;
		pg = p.pg;
		
		// load frames from directory
		String imgPath = "games/bluebear/images/bear-run-sequence/";
		ArrayList<String> frameFiles = FileUtil.getFilesInDirOfType( FileUtil.getHaxademicDataPath() + imgPath, "png");
		_frames = new PImage[frameFiles.size()];
		for( int i=0; i < _frames.length; i++ ) {
			_frames[i] = p.loadImage(imgPath + frameFiles.get(i));
		}
	}

	public void update() {
		int speed = 3;
		_curFrame = _frames[P.floor(p.frameCount/speed % _frames.length)];
		if( _curFrame == null ) return;
		
		// responsive sizing/placement
		int bearW = P.round(p.scaleV(_curFrame.width * _scale));
		int bearH = P.round(p.scaleV(_curFrame.height * _scale));
		int bearX = 20;
		float bearY = pg.height * 0.5f;

		// draw to fill width of screen
		DrawUtil.setDrawCorner(pg);
		pg.pushMatrix();
		pg.image( _curFrame, bearX, bearY, bearW, bearH );
		pg.popMatrix();
	}

}