package org.ohheckyeah.games.kacheout.support;

import org.ohheckyeah.shared.app.OHYBaseInputConfig;

@SuppressWarnings("serial")
public class KacheOutKinectConfig 
extends OHYBaseInputConfig {
	public void setup() {
		super.setup("kacheout.properties");
	}
}