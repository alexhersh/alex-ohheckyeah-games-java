package org.ohheckyeah.shared.app;

import org.ohheckyeah.shared.assets.OHYGraphics;
import org.ohheckyeah.shared.assets.OHYSounds;
import org.ohheckyeah.shared.screens.OHYIntroScreens;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.debug.DebugUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.system.FileUtil;

@SuppressWarnings("serial")
public class OHYBaseGame
extends OHYKinectApp {
	
	// debug 
	protected boolean _isDebugging = false;
	
	// game canvas & responsive scale
	public PGraphics pg;
	public float gameScaleV = 1;
	public float gameScaleH = 1;

	// game state
	public enum GameState {
		GAME_INTRO,
		GAME_INTRO_OUTRO,
		GAME_WAITING_FOR_PLAYERS,
		GAME_PRE_COUNTDOWN,
		GAME_COUNTDOWN,
		GAME_PLAYING,
		GAME_FINISHING,
		GAME_OVER,
		GAME_OVER_OUTRO
	}
	protected GameState _gameState;
	protected GameState _gameStateQueued;	// wait until beginning on the next frame to switch states to avoid mid-frame conflicts

	public enum PlayerDetectedState {
		PLAYER_WAITING,
		PLAYER_LOST,
		PLAYER_DETECTED,
		PLAYER_LOCKED
	}

	// media / screens
	public OHYGraphics ohyGraphics;
	public OHYSounds ohySounds;
	protected OHYIntroScreens _introScreens;

	
	// shared setup methods --------------------------------------------------------------------------------------------
	
	public void setup( String propertiesFile, int gameOriginalHeight, int gameOriginalWidth ) {
		_customPropsFile = FileUtil.getHaxademicDataPath() + "properties/" + propertiesFile;
		super.setup();
		gameScaleV = (float) p.height / (float) gameOriginalHeight;
		gameScaleH = (float) p.width / (float) gameOriginalWidth;
		ohyGraphics = new OHYGraphics();
		setKinectProperties();
		setRemoteKinectProperties();
		if( _isRemoteKinect == true ) initReceiverUDP();
	}
	
	// Getters ---------------------------------------------------------------------------------------------------------
	
	public GameState gameState() { return _gameState; }
	public void setGameState( GameState state ) {
		DebugUtil.printErr("override setGameState()");
	}
	
	// Responsive scaling & canvas --------------------------------------------------------------------------------------
	
	public float scaleV( float input ) {
		return input * gameScaleV;
	}

	public float scaleH( float input ) {
		return input * gameScaleH;
	}
	
	public int svgWidth( PShape shape ) {
		return P.round(scaleV(shape.width));
	}
	
	public int svgHeight( PShape shape ) {
		return P.round(scaleV(shape.height));
	}

	protected void buildCanvas() {
		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
	}
	
}
