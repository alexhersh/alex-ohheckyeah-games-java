package org.ohheckyeah.shared.support;

import org.ohheckyeah.shared.app.OHYBaseRemoteControl;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;

@SuppressWarnings("serial")
public class OHYRemoteControlTest
extends OHYBaseRemoteControl  
{

	protected void overridePropsFile() {
		_appConfig.setProperty( "fullscreen", "false" );
		_appConfig.setProperty( "fills_screen", "false" );
		_appConfig.setProperty( "kinect_active", "false" );
		_appConfig.setProperty( "kinect_remote_active", "true" );
		_appConfig.setProperty( "kinect_remote_debug", "true" );
		_appConfig.setProperty( "kinect_remote_sender_ip", "10.0.2.139" );
		_appConfig.setProperty( "kinect_remote_sender_port", "6000" );
		_appConfig.setProperty( "kinect_remote_receiver_ip", "10.0.2.139" );
		_appConfig.setProperty( "kinect_remote_receiver_port", "6100" );
	}
	
	public void setup() {
		super.setup( null );
	}
	
	public void drawApp() {
		super.drawApp();

		p.background( 0 );
		DrawUtil.setDrawCenter(p);
		p.translate(p.width / 2f, p.height / 2f);
		p.fill(200, 255, 200);
		p.ellipse(0, 0, 50 + 20f * P.sin(p.millis()/500f), 50 + 20f * P.sin(p.millis()/500f));
	}
	
	protected String buildKinectString() {
		return "" + p.millis();
	}

}
