package org.ohheckyeah.shared.app;

import org.ohheckyeah.shared.assets.OHYGraphics;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
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

	// media
	public OHYGraphics ohyGraphics;

	
	// shared setup methods --------------------------------------------------------------------------------------------
	
	public void setup( String propertiesFile, int gameOriginalHeight ) {
		_customPropsFile = FileUtil.getHaxademicDataPath() + "properties/" + propertiesFile;
		super.setup();
		gameScaleV = p.height / gameOriginalHeight;
		ohyGraphics = new OHYGraphics();
		setKinectProperties();
		setRemoteKinectReceiverProperties();
	}
	
	// Getters ---------------------------------------------------------------------------------------------------------
	
	public GameState gameState() { return _gameState; }

	
	// Responsive scaling & canvas --------------------------------------------------------------------------------------
	
	public float scaleV( float input ) {
		return input * gameScaleV;
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