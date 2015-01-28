package org.ohheckyeah.shared.support;

import org.ohheckyeah.shared.app.OHYPhysicalApp;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;

@SuppressWarnings("serial")
public class OHYRemoteControlReceiveTest
extends OHYPhysicalApp  
{

	protected void overridePropsFile() {
		_appConfig.setProperty( "fullscreen", "false" );
		_appConfig.setProperty( "fills_screen", "false" );
		_appConfig.setProperty( "kinect_active", "false" );
		_appConfig.setProperty( "kinect_remote_active", "false" );
		_appConfig.setProperty( "kinect_remote_debug", "true" );
		_appConfig.setProperty( "kinect_remote_sender_ip", "10.0.2.139" );
		_appConfig.setProperty( "kinect_remote_sender_port", "6000" );
		_appConfig.setProperty( "kinect_remote_receiver_ip", "10.0.2.139" );
		_appConfig.setProperty( "kinect_remote_receiver_port", "6100" );
	}
	
	public void setup() {
		super.setup();
		setInputProperties();
		setRemoteControlProperties();
		initReceiverUDP();
	}
	
	public void drawApp() {
		super.drawApp();

		p.background( 0 );
		DrawUtil.setDrawCenter(p);
		p.translate(p.width / 2f, p.height / 2f);
		p.fill(200);
		p.ellipse(0, 0, 50 + 20f * P.sin(p.millis()/500f), 50 + 20f * P.sin(p.millis()/500f));
	}
	
	public void receive( byte[] data, String ip, int port ) {
		String message = new String( data );
		if( _remoteDebugging == true ) P.println( "received: \""+message+"\" from "+ip+" on port "+port );
	}

}
