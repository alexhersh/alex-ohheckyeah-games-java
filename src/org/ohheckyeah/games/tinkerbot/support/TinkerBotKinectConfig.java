package org.ohheckyeah.games.tinkerbot.support;

import org.ohheckyeah.shared.app.OHYBaseInputConfig;

@SuppressWarnings("serial")
public class TinkerBotKinectConfig 
extends OHYBaseInputConfig {
	public void setup() {
		super.setup("tinkerbot.properties");
	}
}