package org.ohheckyeah.shared.app;

import java.io.File;

import org.ohheckyeah.shared.assets.OHYGraphics;
import org.ohheckyeah.shared.assets.OHYSounds;
import org.ohheckyeah.shared.screens.OHYIntroScreens;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.debug.DebugUtil;
import com.haxademic.core.draw.mesh.PGraphicsKeystone;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.system.FileUtil;

@SuppressWarnings("serial")
public class OHYBaseGame
extends OHYPhysicalApp {
	
	// debug 
	protected boolean _isDebugging = false;
	
	// game canvas & responsive scale
	public PGraphics pg;
	public PGraphicsKeystone _pgKeystone;
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
		_customPropsFile = FileUtil.getHaxademicDataPath() + "properties" + File.separator + propertiesFile;
		super.setup();
		buildCanvas();
		gameScaleV = (float) pg.height / (float) gameOriginalHeight;
		gameScaleH = (float) pg.width / (float) gameOriginalWidth;
		ohyGraphics = new OHYGraphics();
		setInputProperties();
		setRemoteControlProperties();
		if( _isRemoteControl == true ) initReceiverUDP();
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
	
	public float svgWidth( PShape shape ) {
		return scaleV(shape.width);
	}
	
	public float svgHeight( PShape shape ) {
		return scaleV(shape.height);
	}

	protected void buildCanvas() {
		pg = p.createGraphics( p.width, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
		if( p.appConfig.getBoolean( "keystones", false ) == true ) _pgKeystone = new PGraphicsKeystone( p, pg, 13f );
	}
	
	protected void drawCanvasToScreen() {
		if( _pgKeystone == null ) {
			p.image(pg, 0, 0, pg.width, pg.height);
		} else {
			p.noStroke();
			_pgKeystone.update(p.g, true);
		}
	}
}
