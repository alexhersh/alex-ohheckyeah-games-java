package org.ohheckyeah.games.catchy.assets;

import java.util.Vector;

import toxi.color.TColor;

import com.haxademic.core.draw.color.ColorGroup;
import com.haxademic.core.draw.color.ColorUtil;

public class CatchyColors
extends ColorGroup {
	
	public final static int STAGE_BG = ColorUtil.colorFromHex("#E7E867");
	public final static int MAIN_TEXT_COLOR = ColorUtil.colorFromHex("#000000");
	public final static int CREDITS_BG = ColorUtil.colorFromHex("#94C65B");
	public final static int INTRO_SCREENS_BG = ColorUtil.colorFromHex("#ffffff");
	public final static int TITLE_SCREEN_BG = ColorUtil.colorFromHex("#94C65B");
	public final static int TITLE_SCREEN_BORDER = ColorUtil.colorFromHex("#393427");
	
	public CatchyColors( int set ) {
		super(set);
	}
	
	/**
	 * Overrides base class to provide a list of color options for catchy
	 */
	public void getColors( int set ) {
		_colorSets = new Vector<Vector<TColor>>();
		_colorSets.add( createGroupWithHexes( "02ffff", "00fa00", "fdfd04", "fd5002", "fe007c" ) );
		_colorSets.add( createGroupWithHexes( "d865fe", "00fffe", "28fb00", "feff02", "ff00ff" ) );
		_colorSets.add( createGroupWithHexes( "02b8ff", "fffa02", "ff0091", "858585", "c182f4" ) );
	}
	
}
