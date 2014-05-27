package org.ohheckyeah.games.tinkerbot.support;

import org.ohheckyeah.shared.app.OHYBaseKinectConfig;

@SuppressWarnings("serial")
public class TinkerBotKinectConfig 
extends OHYBaseKinectConfig {
	public void setup() {
		super.setup("tinkerbot.properties");
	}
}