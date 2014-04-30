package org.ohheckyeah.games.bluebear.assets;

import com.haxademic.core.draw.color.ColorGroup;
import com.haxademic.core.draw.color.ColorUtil;

public class BlueBearColors
extends ColorGroup {
	
	public final static int STAGE_BG = ColorUtil.colorFromHex("#ffffff");
	public final static int MAIN_TEXT_COLOR = ColorUtil.colorFromHex("#000000");
	public final static int CREDITS_BG = ColorUtil.colorFromHex("#94C65B");
	public final static int INTRO_SCREENS_BG = ColorUtil.colorFromHex("#ffffff");
	public final static int TITLE_SCREEN_BG = ColorUtil.colorFromHex("#94C65B");
	public final static int TITLE_SCREEN_BORDER = ColorUtil.colorFromHex("#393427");
	
	public BlueBearColors( int set ) {
		super(set);
	}
	
}
