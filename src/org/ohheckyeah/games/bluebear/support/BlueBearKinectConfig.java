package org.ohheckyeah.games.bluebear.support;

import org.ohheckyeah.shared.app.OHYBaseInputConfig;

@SuppressWarnings("serial")
public class BlueBearKinectConfig 
extends OHYBaseInputConfig {
	public void setup() {
		super.setup("bluebear.properties");
	}
}