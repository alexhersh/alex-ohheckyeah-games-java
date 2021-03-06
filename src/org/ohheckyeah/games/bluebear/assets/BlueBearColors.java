package org.ohheckyeah.games.bluebear.assets;

import com.haxademic.core.draw.color.ColorGroup;
import com.haxademic.core.draw.color.ColorUtil;

public class BlueBearColors
extends ColorGroup {
	
	public final static int STAGE_BG = ColorUtil.colorFromHex("#ffffff");
	public final static int MAIN_TEXT_COLOR = ColorUtil.colorFromHex("#000000");
	public final static int RED_TEXT_COLOR = ColorUtil.colorFromHex("#EB2227");
	public final static int CREDITS_BG = ColorUtil.colorFromHex("#f6921e");
	public final static int CREDITS_DOTS = ColorUtil.colorFromHex("#f9a137");
	public final static int INTRO_SCREENS_BG = ColorUtil.colorFromHex("#ffffff");
	public final static int TITLE_SCREEN_BG = ColorUtil.colorFromHex("#fffbd4");
	public final static int TITLE_SCREEN_BORDER = ColorUtil.colorFromHex("#224194");
	public final static int TITLE_SCREEN_DOTS = ColorUtil.colorFromHex("#f9fff1");
	public final static int DETECT_SCREEN_TOP = ColorUtil.colorFromHex("#da6baf");
	public final static int DETECT_SCREEN_BOTTOM = ColorUtil.colorFromHex("#d95caa");
	public final static int LOSE_SCREEN_BG = ColorUtil.colorFromHex("#70C8F3");
	public final static int LOSE_SCREEN_DOTS = ColorUtil.colorFromHex("#82CEF4");
	
	public BlueBearColors( int set ) {
		super(set);
	}
	
}
