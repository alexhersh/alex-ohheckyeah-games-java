package org.ohheckyeah.games.catchy.support;

import org.ohheckyeah.shared.app.OHYBaseKinectConfig;

@SuppressWarnings("serial")
public class CatchyKinectConfig 
extends OHYBaseKinectConfig {
	public void setup() {
		super.setup("catchy.properties");
	}
}