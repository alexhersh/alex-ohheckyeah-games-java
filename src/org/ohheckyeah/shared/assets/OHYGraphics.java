package org.ohheckyeah.shared.assets;

import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.system.FileUtil;

public class OHYGraphics {


	public PShape logoOhyTeam, logoOHY;
	public String font;

	public OHYGraphics() {

		// common OHY svgs
		logoOhyTeam = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-partners-white.svg" );
		logoOHY = P.p.loadShape( FileUtil.getHaxademicDataPath() + "svg/logo-ohheckyeah.svg" );

		// fonts
		font = FileUtil.getHaxademicDataPath() + "fonts/nunito/Nunito-Bold.ttf";
	}
}
