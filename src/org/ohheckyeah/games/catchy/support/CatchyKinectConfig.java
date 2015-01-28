package org.ohheckyeah.games.catchy.support;

import org.ohheckyeah.shared.app.OHYBaseInputConfig;

@SuppressWarnings("serial")
public class CatchyKinectConfig 
extends OHYBaseInputConfig {
	public void setup() {
		super.setup("catchy.properties");
	}
}