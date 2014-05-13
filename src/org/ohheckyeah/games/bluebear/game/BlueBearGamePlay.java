package org.ohheckyeah.games.bluebear.game;

import java.util.ArrayList;

import org.ohheckyeah.games.bluebear.BlueBear;
import org.ohheckyeah.games.bluebear.BlueBear.GameState;
import org.ohheckyeah.games.bluebear.assets.BlueBearColors;
import org.ohheckyeah.games.bluebear.assets.BlueBearSounds;
import org.ohheckyeah.games.bluebear.assets.neighborhoods.BlueBearNeighborhood;
import org.ohheckyeah.games.bluebear.assets.neighborhoods.BlueBearNeighborhoodBaker;
import org.ohheckyeah.games.bluebear.assets.neighborhoods.BlueBearNeighborhoodBroadway;
import org.ohheckyeah.games.bluebear.assets.neighborhoods.BlueBearNeighborhoodDowntown;
import org.ohheckyeah.games.bluebear.assets.neighborhoods.BlueBearNeighborhoodHighlands;
import org.ohheckyeah.games.bluebear.assets.neighborhoods.BlueBearNeighborhoodMountains;
import org.ohheckyeah.games.bluebear.game.characters.BlueBearCharacter;
import org.ohheckyeah.games.bluebear.game.characters.BlueBearNemesis;
import org.ohheckyeah.games.bluebear.game.nonscrolling.BlueBearBackground;
import org.ohheckyeah.games.bluebear.game.nonscrolling.BlueBearGround;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearScrollerBackground;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearScrollerBuildings;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearScrollerClouds;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearScrollerRoad;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearScrollerSidewalk;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearScrollerSkyline;

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
	protected int _gameStartTime = 0;
	protected int _neighborhoodTime = 15 * 1000;
	
	protected int _bgColor;
	
	protected BlueBearNeighborhood[] _neighborhoods;
	protected BlueBearNeighborhood _curNeighborhood;
	protected int _neighborhoodIndex = 0;
	
	protected BlueBearBackground _backgroundColor;
	protected BlueBearGround _groundColor;
	protected BlueBearScrollerRoad _road;
	protected BlueBearScrollerBackground _background;
	protected BlueBearScrollerClouds _clouds;
	protected BlueBearScrollerSkyline _skyline;
	protected BlueBearScrollerBuildings _buildings;
	protected BlueBearScrollerSidewalk _sidewalk;
	protected BlueBearScreenPositions _positions;
	protected BlueBearCharacter _bear;
	protected BlueBearNemesis _nemesis;
	
	protected float SPEED = 10;
	protected LinearFloat _scrollSpeed = new LinearFloat(0,0.2f);
	protected boolean _gameplayStarted = false;
	
	protected float _launchTime = 0;
	protected BlueBearObstacles _obstacles;

	
	public BlueBearGamePlay( KinectRegionGrid kinectGrid, boolean isRemoteKinect ) {
		p = (BlueBear) P.p;
		pg = p.pg;
		
		_kinectGrid = kinectGrid;
		_isRemoteKinect = isRemoteKinect;
		_playerControls = new ArrayList<BlueBearPlayerControls>();
		for (int i=0; i < _kinectGrid.kinectRegions.size(); i++) {
			_playerControls.add(new BlueBearPlayerControls(_kinectGrid.getRegion(i), _isRemoteKinect));
		}
		
		buildNeighborhoods();
		buildGraphicsLayers();
		
		reset();
	}
	
	protected void buildNeighborhoods() {
		_neighborhoods = new BlueBearNeighborhood[5];
		_neighborhoods[0] = new BlueBearNeighborhoodMountains();
		_neighborhoods[1] = new BlueBearNeighborhoodBaker();
		_neighborhoods[2] = new BlueBearNeighborhoodBroadway();
		_neighborhoods[3] = new BlueBearNeighborhoodHighlands();
		_neighborhoods[4] = new BlueBearNeighborhoodDowntown();
	}
	
	protected void buildGraphicsLayers() {
		_positions = new BlueBearScreenPositions();
		
		_road = new BlueBearScrollerRoad();
		_backgroundColor = new BlueBearBackground();
		_groundColor = new BlueBearGround();
		_clouds = new BlueBearScrollerClouds();
		_background = new BlueBearScrollerBackground();
		_skyline = new BlueBearScrollerSkyline();
		_buildings = new BlueBearScrollerBuildings();
		_sidewalk = new BlueBearScrollerSidewalk();
		
		_obstacles = new BlueBearObstacles();
		_bear = new BlueBearCharacter();
		_nemesis = new BlueBearNemesis();
	}
	
	public void reset() {
		_bgColor = BlueBearColors.STAGE_BG;
		_gameIsActive = false;
		
		_obstacles.reset();
		_bear.reset();
		_nemesis.reset();
		
		_road.reset();
		_background.reset();
		_skyline.reset();
		_buildings.reset();
		_sidewalk.reset();

		setLevel(0);
		_gameplayStarted = false;
	}
	
	protected void setLevel( int index ) {
		// reset level
		_neighborhoodIndex = index;
		_curNeighborhood = _neighborhoods[_neighborhoodIndex];
		
		_backgroundColor.setColor( _curNeighborhood.backgroundColor );
		_groundColor.setColor( _curNeighborhood.groundColor );
		
		_obstacles.setGraphicPool( _curNeighborhood.obstaclePool, _curNeighborhood.obstacleFiles );
		
		_road.setGraphicPool( _curNeighborhood.roadPool, _curNeighborhood.roadFiles );
		_background.setGraphicPool( _curNeighborhood.backgroundPool, _curNeighborhood.backgroundFiles );
		_skyline.setGraphicPool( _curNeighborhood.skylinePool, _curNeighborhood.skylineFiles );
		_buildings.setGraphicPool( _curNeighborhood.buildingsPool, _curNeighborhood.buildingsFiles );
		_sidewalk.setGraphicPool( _curNeighborhood.sidewalkPool, _curNeighborhood.sidewalkFiles );
	}
		
	// start/end game methods ------------
	public void startPlayerDetection() {
	}
	
	public void startGame() {
		_scrollSpeed.setTarget(SPEED);
		_gameStartTime = p.millis();
	}
	
	public void gameOver() {
		_launchTime = 0;
		_scrollSpeed.setTarget(0);
		p.setGameState( GameState.GAME_OVER );
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
		updateGameStarted();
		checkLaunch();
		checkObstacleCollisions();
		drawGraphicsLayers();
		updateNeighborhood();
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
	
	protected void updateGameStarted() {
		if( _gameplayStarted == false && SPEED - _scrollSpeed.value() < 0.1f ) {
			_gameplayStarted = true;
			_launchTime = p.millis();
			_bear.startGameplay();
			_nemesis.startGameplay();
		}
	}
	
	protected void updateNeighborhood() {
		if( p.gameState() == GameState.GAME_PLAYING ) {
			int elapsedTime = p.millis() - _gameStartTime;
			int curLevel = P.floor( elapsedTime / _neighborhoodTime );
			if( curLevel != _neighborhoodIndex ) {
				_neighborhoodIndex = curLevel;
				if( _neighborhoodIndex < _neighborhoods.length ) {
					setLevel(_neighborhoodIndex);
				} else {
					gameOver();
				}
			}
		}
	}
	
	protected void checkLaunch() {
		if( _launchTime != 0 && p.millis() > _launchTime + 1000 ) {
			_launchTime = p.millis();
			launch();
		}
	}
	
	protected void launch() {
		_nemesis.launch();
		_obstacles.launch( _nemesis.launchX(), _nemesis.launchY() );
		p.sounds.playSound( BlueBearSounds.LAUNCH );
	}
	
	protected void checkObstacleCollisions() {
		boolean didHit = _obstacles.checkHit( _bear.xLeft(), _bear.xRight(), _bear.y() );
		if( didHit == true ) {
			_bear.hit();
			p.sounds.playSound( BlueBearSounds.HIT );
		}
	}
	
	// draw graphics ------------------------------------------------------------------
	protected void drawGraphicsLayers() {
		float speed = _scrollSpeed.value();
		_backgroundColor.update();
		_groundColor.update();
		
		_clouds.update(speed);
		_skyline.update(speed);
		_road.update(speed);
		_background.update(speed);
		_buildings.update(speed);
		_sidewalk.update(speed);
		
		_obstacles.update(speed);
		
		_bear.update(speed);
		_nemesis.update();
	}
	
}