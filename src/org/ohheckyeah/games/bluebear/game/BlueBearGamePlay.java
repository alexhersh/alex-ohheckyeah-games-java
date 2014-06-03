package org.ohheckyeah.games.bluebear.game;

import org.ohheckyeah.games.bluebear.BlueBear;
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
import org.ohheckyeah.games.bluebear.game.characters.BlueBearPlayerControls;
import org.ohheckyeah.games.bluebear.game.characters.BlueBearPlayerDetectionBg;
import org.ohheckyeah.games.bluebear.game.gameover.BlueBearLoseScreen;
import org.ohheckyeah.games.bluebear.game.gameover.BlueBearWinScreen;
import org.ohheckyeah.games.bluebear.game.nonscrolling.BlueBearBackground;
import org.ohheckyeah.games.bluebear.game.nonscrolling.BlueBearGround;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearScrollerBackground;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearScrollerBuildings;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearScrollerClouds;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearScrollerRoad;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearScrollerSidewalk;
import org.ohheckyeah.games.bluebear.game.scrolling.BlueBearScrollerSkyline;
import org.ohheckyeah.games.bluebear.game.streetstuff.BlueBearStreetItem;
import org.ohheckyeah.games.bluebear.game.streetstuff.BlueBearStreetItems;
import org.ohheckyeah.games.bluebear.game.text.BlueBearCountdownDisplay;
import org.ohheckyeah.games.bluebear.game.text.BlueBearGameMessages;
import org.ohheckyeah.shared.app.OHYBaseGame.GameState;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.hardware.kinect.KinectRegionGrid;
import com.haxademic.core.math.easing.LinearFloat;

public class BlueBearGamePlay {
	
	protected BlueBear p;
	protected PGraphics pg;

	protected KinectRegionGrid _kinectGrid;
	protected boolean _gameIsActive = false;
	protected boolean _gameShouldEnd = false;
	protected boolean _gameLose = false;
	
	protected BlueBearPlayerDetectionBg _playerDetectBackground;
	
	protected boolean _isRemoteKinect;
	protected int _playersDetectedTime = 0;
	protected int _countdownTime = 0;
	protected boolean _countdownShowing = false;
	protected int _gameStartTime = 0;
	protected int _neighborhoodTime = 15 * 1000;
	protected float _winTime = 0;
	protected float _loseTime = 0;
	protected int GAME_OVER_WIN_TIME = 7500;
	protected int GAME_OVER_LOSE_TIME = 5000;
	
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
	protected BlueBearScoreDisplay _scoreDisplay;
	protected BlueBearCountdownDisplay _countdownDisplay;
	protected BlueBearLoseScreen _loseScreen;
	protected BlueBearWinScreen _winScreen;
	protected BlueBearGameMessages _gameMessages;

	public float SPEED = 10;
	protected LinearFloat _scrollSpeed = new LinearFloat(0,0.2f);
	protected boolean _gameplayStarted = false;
	
	protected float _launchTime = 0;
	protected float LAUNCH_TIME = 700;
	protected BlueBearStreetItems _obstacles;
	protected BlueBearStreetItems _goodies;

	
	public BlueBearGamePlay( KinectRegionGrid kinectGrid, boolean isRemoteKinect ) {
		p = (BlueBear) P.p;
		pg = p.pg;
		
		_kinectGrid = kinectGrid;
		_isRemoteKinect = isRemoteKinect;
		
		SPEED = p.scaleV(SPEED);
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
		
		_obstacles = new BlueBearStreetItems();
		_goodies = new BlueBearStreetItems();
		_bear = new BlueBearCharacter( new BlueBearPlayerControls(_kinectGrid.getRegion(0), _isRemoteKinect) );
		_nemesis = new BlueBearNemesis( new BlueBearPlayerControls(_kinectGrid.getRegion(1), _isRemoteKinect), this );
		_playerDetectBackground = new BlueBearPlayerDetectionBg();
		_scoreDisplay = new BlueBearScoreDisplay();
		_countdownDisplay = new BlueBearCountdownDisplay();
		_loseScreen = new BlueBearLoseScreen();
		_winScreen = new BlueBearWinScreen();
		_gameMessages = new BlueBearGameMessages();
	}
	
	public void reset() {
		_bgColor = BlueBearColors.STAGE_BG;
		_gameIsActive = false;
		_gameShouldEnd = false;
		_gameLose = false;
		
		_obstacles.reset();
		_goodies.reset();
		_bear.reset();
		_nemesis.reset();
		_scoreDisplay.reset();
		_loseScreen.reset();
		_winScreen.reset();
		
		_road.reset();
		_background.reset();
		_skyline.reset();
		_buildings.reset();
		_sidewalk.reset();

		setLevel(0);
		_gameplayStarted = false;
		_gameMessages.hideWin();
	}
	
	protected void setLevel( int index ) {
		// reset level
		_neighborhoodIndex = index;
		_curNeighborhood = _neighborhoods[_neighborhoodIndex];
		
		_backgroundColor.setColor( _curNeighborhood.backgroundColor );
		_groundColor.setColor( _curNeighborhood.groundColor );
		
		_obstacles.setGraphicPool( _curNeighborhood.obstaclePool, _curNeighborhood.obstacleFiles );
		_goodies.setGraphicPool( _curNeighborhood.goodiePool, _curNeighborhood.goodieFiles );
		
		_road.setGraphicPool( _curNeighborhood.roadPool, _curNeighborhood.roadFiles );
		_background.setGraphicPool( _curNeighborhood.backgroundPool, _curNeighborhood.backgroundFiles );
		_skyline.setGraphicPool( _curNeighborhood.skylinePool, _curNeighborhood.skylineFiles );
		_buildings.setGraphicPool( _curNeighborhood.buildingsPool, _curNeighborhood.buildingsFiles );
		_sidewalk.setGraphicPool( _curNeighborhood.sidewalkPool, _curNeighborhood.sidewalkFiles );
	}
		
	// start/end game methods ------------
	public void startPlayerDetection() {
		_playerDetectBackground.show();
		_bear.showSpinner();
		_nemesis.showSpinner();
	}
	
	public void startGame() {
		_scrollSpeed.setTarget(SPEED);
		_gameStartTime = p.millis();
		_bear.startMoving();
		_nemesis.startMoving();
	}
	
	public void gameOver() {
		_launchTime = 0;
		_bear.stopMoving();
		_nemesis.stopMoving();
		if( _gameLose ) {
			showLoseSequence();
		} else {
			showWinSequence();
		}
		p.setGameState( GameState.GAME_OVER );
	}
	
	public int gameOverTime() {
		if( _gameLose ) {
			return GAME_OVER_LOSE_TIME;
		} else {
			return GAME_OVER_WIN_TIME;
		}
	}
	
	// handle countdown timer ------------
	public void playersLockedIn() {
		_gameIsActive = true;
		_playerDetectBackground.hide();
		_bear.prepareForGameplay();
		_nemesis.prepareForGameplay();
		_gameMessages.hideWaiting();
		_gameMessages.showCountdown();
	}
	
	public void showCountdown( int countdownTime ) {
		_countdownTime = countdownTime;
		_countdownDisplay.show();
	}
	
	public void hideCountdown() {
		_countdownDisplay.hide();
	}
	
	public void updateCountdown( int countdownTime ) {
		_countdownTime = countdownTime;
	}
	
	// handle game states ----------------
	public void animateToGameState() {
		_gameMessages.hideCountdown();
	}
	
	public void animateToPostGameOverState() {
		_gameMessages.hideLose();
	}
	
	public void animateToPlayerDetection() {
		_gameMessages.showWaiting();
	}
	
	public void update() {
		if( p.gameState() == GameState.GAME_WAITING_FOR_PLAYERS ) detectPlayers();
		if( p.gameState() == GameState.GAME_PLAYING ) updateControls();
		_scrollSpeed.update();
		updateGameStarted();
		if( p.gameState() == GameState.GAME_PLAYING ) {
			checkLaunch();
			checkObstacleCollisions();
			checkGoodieCollisions();
		}
		if( p.gameState() == GameState.GAME_OVER ) checkStopSpeed();
		drawGraphicsLayers();
		updateNeighborhood();
		if( _gameShouldEnd == true ) {
			gameOver();
			_gameShouldEnd = false;
			_gameLose = false;
		}
	}
	
	public void detectPlayers() {
		boolean hasPlayers = true;
		if( _bear.detectPlayer() == false ) hasPlayers = false;
		if( _nemesis.detectPlayer() == false ) hasPlayers = false;
		if( hasPlayers == true ) {
			if( _playersDetectedTime == 0 ) {
				_playersDetectedTime = p.millis();
			}
			if( p.millis() > _playersDetectedTime + 2000 ) {
				_playersDetectedTime = 0;
				p.setGameState( GameState.GAME_PRE_COUNTDOWN );
			}
		} else {
			_playersDetectedTime = 0;
		}
	}
	
	public void updateControls() {
		_bear.updateControls();
		_nemesis.updateControls();
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
					_gameShouldEnd = true;
				}
			}
		}
	}
	
	protected void checkLaunch() {
		if( _launchTime != 0 && p.millis() > _launchTime + LAUNCH_TIME ) {
			_launchTime = p.millis();
			launchGoodie();
		}
	}
	
	public void launchObstacle() {
		// launch obstacle
		_obstacles.launch( _nemesis.launchX(), _nemesis.launchY(), _nemesis.lane() );
		p.sounds.playSound( BlueBearSounds.LAUNCH );
	}
	
	protected void launchGoodie() {
		// launch a goodie in a different lane
		int goodieLane = BlueBearScreenPositions.randomLane();
		while( goodieLane == _nemesis.lane() ) {
			goodieLane = BlueBearScreenPositions.randomLane();
		}
		float goodieOffsetX = _obstacles.obstacleSpacing();
		if( goodieOffsetX == 0 ) goodieOffsetX = p.scaleV(200);
		_goodies.launch( _nemesis.launchX() + goodieOffsetX + goodieOffsetX / 2f, BlueBearScreenPositions.LANES_Y[goodieLane], goodieLane );
	}
	
	protected void checkObstacleCollisions() {
		BlueBearStreetItem didHit = _obstacles.checkHit( _bear.xLeft(), _bear.xRight(), _bear.y() );
		if( didHit != null ) {
			didHit.kick( _bear.x() < didHit.x );
			_bear.hit();
			p.sounds.playSound( BlueBearSounds.HIT );
			boolean gameOver = _scoreDisplay.hit() == 0;
			if( gameOver ) {
				_gameShouldEnd = true;
				_gameLose = true;
			}
		}
	}
	
	protected void checkGoodieCollisions() {
		BlueBearStreetItem didHit = _goodies.checkHit( _bear.xLeft(), _bear.xRight(), _bear.y() );
		if( didHit != null ) {
			didHit.hide();
			if( didHit.fileName.indexOf("health") == -1 ) {
				_scoreDisplay.addScore();
				p.sounds.playSound( BlueBearSounds.SCORE );
			} else {
				_scoreDisplay.addHealth();
				p.sounds.playSound( BlueBearSounds.HEALTH_UP );
			}
		}
	}
	
	protected void showWinSequence() {
		_bear.win();
		_winScreen.startEnd();
		p.sounds.playWin();
		_winTime = p.millis();
	}
	
	protected void checkStopSpeed() {
		if( _winTime != 0 && p.millis() > _winTime + 1500 ) {
			_bear.winJump();
			_scrollSpeed.setTarget(0);
			_gameMessages.showWin();
			_winTime = 0;
		}
		if( _loseTime != 0 && p.millis() > _loseTime + 2000 ) {
			_bear.loseSad();
			_loseScreen.showLoseScreen();
			_gameMessages.showLose();
			p.sounds.playLose();
			_loseTime = 0;
		}
	}
	
	protected void showLoseSequence() {
		_bear.lose();
		_loseTime = p.millis();
		_scrollSpeed.setTarget(0);
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
		
		_obstacles.update(speed, true, _bear.x());
		_goodies.update(speed, false, _bear.x());
		
		_countdownDisplay.updateWithNumber(_countdownTime);
		_scoreDisplay.update();
		_playerDetectBackground.update();
		_loseScreen.update();
		_winScreen.update(speed);

		_bear.update(speed);
		_nemesis.update(speed);
		
		_gameMessages.update();
	}
	
}