package org.ohheckyeah.games.kacheout.support;

import org.ohheckyeah.shared.app.OHYBaseKinectConfig;

@SuppressWarnings("serial")
public class KacheOutKinectConfig 
extends OHYBaseKinectConfig {
	public void setup() {
		super.setup("kacheout.properties");
	}
}