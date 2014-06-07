package org.ohheckyeah.games.bluebear.support;

import org.ohheckyeah.games.bluebear.assets.BlueBearColors;
import org.ohheckyeah.games.bluebear.game.BlueBearTracking;
import org.ohheckyeah.shared.app.OHYBaseRemoteControl;

import processing.core.PApplet;
import processing.core.PShape;

import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.system.FileUtil;

@SuppressWarnings("serial")
public class BlueBearRemoteControl
extends OHYBaseRemoteControl  
{

	protected PShape _logo;
	
	public static void main(String args[]) {
		_isFullScreen = true;
		PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "org.ohheckyeah.games.bluebear.support.BlueBearRemoteControl" });
	}

	public void setup() {
		super.setup( "bluebear.properties", new BlueBearTracking() );
		_logo = p.loadShape( FileUtil.getHaxademicDataPath() + "games/bluebear/svg/shell/big-blue-logo.svg" );
	}
	
	protected void overridePropsFile() {
		_appConfig.setProperty( "fullscreen", "true" );
	}
	
	public void drawApp() {
		super.drawApp();
		
		// draw branded game screen 
		p.background( BlueBearColors.TITLE_SCREEN_BG );
		DrawUtil.setDrawCenter(p);
		float drawRatio = ( p.width / _logo.width ) * 0.7f;
		p.shape(_logo, p.width * 0.5f, p.height * 0.5f, _logo.width * drawRatio, _logo.height * drawRatio );
	}
}
