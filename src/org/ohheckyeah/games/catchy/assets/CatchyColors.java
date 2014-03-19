package org.ohheckyeah.games.catchy.assets;

import java.util.Vector;

import toxi.color.TColor;

import com.haxademic.core.draw.color.ColorGroup;

public class CatchyColors
extends ColorGroup {
	
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
