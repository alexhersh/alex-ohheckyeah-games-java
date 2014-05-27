package org.ohheckyeah.games.bluebear.support;

import org.ohheckyeah.shared.app.OHYBaseKinectConfig;

@SuppressWarnings("serial")
public class BlueBearKinectConfig 
extends OHYBaseKinectConfig {
	public void setup() {
		super.setup("bluebear.properties");
	}
}