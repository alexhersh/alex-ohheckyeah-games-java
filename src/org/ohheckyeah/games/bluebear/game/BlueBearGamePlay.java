package org.ohheckyeah.games.bluebear.game;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.BlueBear.GameState;
import org.ohheckyeah.games.bluebear.assets.BlueBearColors;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearBuildingsScroller;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearCloudsScroller;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearSidewalkScroller;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearSkylineScroller;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.hardware.kinect.KinectRegionGrid;
import com.haxademic.core.math.easing.LinearFloat;

public class BlueBearGamePlay {
	
	protected BlueBear p;
	protected PGraphics pg;

	protected KinectRegionGrid _kinectGrid;
	protected ArrayList<BlueBearPlayerControls> _playerControls;
	protected boolean _gameIsActive = false;
	
	protected boolean _isRemoteKinect;
	protected int _countdownTime = 0;
	protected boolean _countdownShowing = false;
	
	protected int _bgColor;
	
	protected BlueBearCloudsScroller _clouds;
	protected BlueBearSkylineScroller _skyline;
	protected BlueBearBuildingsScroller _buildings;
	protected BlueBearSidewalkScroller _sidewalk;
	protected BlueBearRoad _road;
	protected BlueBearCharacter _bear;
	protected BlueBearNemesis _nemesis;
	
	protected float SPEED = 10;
	protected LinearFloat _scrollSpeed = new LinearFloat(0,0.2f);
	
	public BlueBearGamePlay( KinectRegionGrid kinectGrid, boolean isRemoteKinect ) {
		p = (BlueBear) P.p;
		pg = p.pg;
		
		_kinectGrid = kinectGrid;
		_isRemoteKinect = isRemoteKinect;
		_playerControls = new ArrayList<BlueBearPlayerControls>();
		for (int i=0; i < _kinectGrid.kinectRegions.size(); i++) {
			_playerControls.add(new BlueBearPlayerControls(_kinectGrid.getRegion(i), _isRemoteKinect));
		}
				
		_road = new BlueBearRoad();
		_clouds = new BlueBearCloudsScroller();
		_skyline = new BlueBearSkylineScroller();
		_buildings = new BlueBearBuildingsScroller();
		_sidewalk = new BlueBearSidewalkScroller();
		_bear = new BlueBearCharacter();
		_nemesis = new BlueBearNemesis();
		
		reset();
	}
	
	public void reset() {
		_bgColor = BlueBearColors.STAGE_BG;
		_gameIsActive = false;
		// Collections.shuffle( _droppables );
	}
		
	// start/end game methods ------------
	public void startPlayerDetection() {
	}
	
	public void startGame() {
		_scrollSpeed.setTarget(SPEED);
	}
	
	public void gameOver( boolean didWin ) {
		_scrollSpeed.setTarget(0);
	}
	
	// handle countdown timer ------------
	public void playersLockedIn() {
		_gameIsActive = true;
	}
	
	public void showCountdown( int countdownTime ) {
		_countdownTime = countdownTime;
	}
	
	public void hideCountdown() {
	}
	
	public void updateCountdown( int countdownTime ) {
		_countdownTime = countdownTime;
	}
	
	// handle game states ----------------
	public void animateToGameState() {
	}
	
	public void animateToHiddenState() {
	}
	
	public void animateToWinState() {
	}
	
	public void update() {
		if( p.gameState() == GameState.GAME_WAITING_FOR_PLAYERS ) detectPlayers();
		if( p.gameState() == GameState.GAME_PLAYING ) updateControls();
		_scrollSpeed.update();
		drawGraphicsLayers();
	}
	
	public void detectPlayers() {
		boolean hasPlayers = true;
		for( int i=0; i < _playerControls.size(); i++ ) {
			if( _playerControls.get(i).detectPlayer() == false ) {
				hasPlayers = false;
			}
		}
		if( hasPlayers == true ) {
			p.setGameState( GameState.GAME_PRE_COUNTDOWN );
		}
	}
	
	public void updateControls() {
		for( int i=0; i < _playerControls.size(); i++ ) {
			_playerControls.get(i).updateControls();
		}
		_bear.setLane( _playerControls.get(0).lane() );
		_nemesis.setLane( _playerControls.get(1).lane() );
	}
	
	// draw graphics ------------------------------------------------------------------
	protected void drawGraphicsLayers() {
		float speed = _scrollSpeed.value();
		_clouds.update(speed);
		_skyline.update(speed);
		_buildings.update(speed);
		_sidewalk.update(speed);
		_road.update(speed);
		_bear.update(speed);
		_nemesis.update();
	}
	
}