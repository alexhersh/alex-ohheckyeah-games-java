package org.ohheckyeah.games.catchy.game;

import java.util.ArrayList;

import org.ohheckyeah.games.catchy.Catchy;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.color.ColorUtil;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.draw.util.OpenGLUtil;
import com.haxademic.core.hardware.kinect.KinectRegion;
import com.haxademic.core.math.easing.EasingFloat;

public class CatchyGamePlay {
	
	protected Catchy p;
	public int gameWidth;
	public int gameHalfWidth;
	public int gameIndex;
	protected EasingFloat _easedControlX;
	protected float _playerOffset = 0;
	protected float _autoControl;
	protected boolean _controlsActive = false;
	protected boolean _gameIsActive = false;
	
	protected KinectRegion _kinectRegion;
	protected boolean _hasPlayer = false;
	protected boolean _detectedPlayer = false;
	protected int _detectedPlayerTime = 0;
	protected int _countdownTime = 0;
	protected boolean _countdownShowing = false;
	
	public PGraphics pg;
	
	protected int _bgColor;
	protected CatchyCharacter _character;
	protected CatchyGrass _grass;
	protected CatchyMountainAndBushes _mountain;
	protected CatchyDropper _dropper;
	protected CatchyWaitingSpinner _waitingSpinner;
	protected CatchyCountdownDisplay _countdownDisplay;
	protected float _bushSmallX;
	protected float _bushLargeX;
	
	protected ArrayList<CatchyDroppable> _droppables;
	protected int _droppableIndex = 0;
	
	protected CatchyScoreDisplay _score;
	
	public CatchyGamePlay( int gameIndex, int gameWidth, KinectRegion kinectRegion ) {
		p = (Catchy) P.p;
		this.gameIndex = gameIndex;
		this.gameWidth = gameWidth;
		this.gameHalfWidth = Math.round( gameWidth / 2f );
		_kinectRegion = kinectRegion;

		pg = p.createGraphics( gameWidth, p.height, P.OPENGL );
		pg.smooth(OpenGLUtil.SMOOTH_MEDIUM);
		
		_easedControlX = new EasingFloat( 0.5f, 6f );
		_autoControl = p.random(0.001f, 0.005f);
		
		_character = new CatchyCharacter(this);
		_grass = new CatchyGrass(this);
		_mountain = new CatchyMountainAndBushes(this);
		_dropper = new CatchyDropper(this);
		_droppables = new ArrayList<CatchyDroppable>();
		for( int i=0; i < 30; i++ ) {
			_droppables.add( new CatchyDroppable(this) );
		}
		_score = new CatchyScoreDisplay(this);
		_waitingSpinner = new CatchyWaitingSpinner(this);
		_countdownDisplay = new CatchyCountdownDisplay(this);
		
		reset();
	}
	
	// start/end game methods ------------
	public void startPlayerDetection() {
		_controlsActive = true;
		_hasPlayer = false;
		_detectedPlayer = false;
		_detectedPlayerTime = 0;
		_waitingSpinner.show();
		_grass.setWaitingState();
		_mountain.setWaitingState();
	}
	
	public void startGame() {
		_dropper.startDropping();
	}
	
	public void stopDropping() {
		_dropper.stopDropping();
	}
	
	public void gameOver( boolean didWin ) {
		_controlsActive = false;
		_grass.setWinState();
		_mountain.setWinState( didWin );
		_character.setWinState( didWin );
	}
	
	public int getScore() {
		return _score.getScore();
	}
	
	public boolean hasPlayer() {
		return _hasPlayer;
	}
	
	// handle countdown timer ------------
	public void playersLockedIn() {
		_gameIsActive = true;
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
		_score.show();
		_dropper.showDropper();
		_grass.setGameplayState();
		_mountain.setGameplayState();
		_character.setGameplayState();
	}
	
	public void animateToWinState() {
		_score.hide();
	}
	
	public void reset() {
		_bgColor = ColorUtil.colorFromHex("#E7E867");
		_character.reset();
		_score.reset( _character.color() );
		_dropper.reset();
		_bushSmallX = p.random( 0, gameWidth );
		_bushLargeX = p.random( 0, gameWidth );
		_gameIsActive = false;
	}
	
	public void launchNewDroppable( float x ) {
		CatchyDroppable droppable = _droppables.get( _droppableIndex );
		_droppableIndex = ( _droppableIndex < _droppables.size() - 1 ) ? _droppableIndex + 1 : 0;
		droppable.reset( x, p.scaleV(10f) );
	}
	
	public void checkCatch( CatchyDroppable droppable, float x, float y ) {
		if( droppable.isCatchable() == true && _character.checkCatch(x, y) == true ) {
			droppable.catchSuccess();
			_score.addScore(1);
		}
		if( _character.checkBump(x, y) == true ) {
			droppable.bumped();
		}
	}
	
	public void update() {
		pg.beginDraw();
		pg.background( _bgColor );
		updateControls();
		drawGraphicsLayers();
		pg.endDraw();
	}
	
	protected void updateControls() {
		if( _controlsActive == true ) {
			// update player control for character
			if( _gameIsActive == false ) {
				detectKinectPlayers();
			} else {
				if( p.kinectWrapper != null ) {
					_easedControlX.setTarget( _kinectRegion.controlX() * 2f );					
				} else {
					// fake test controls
					_easedControlX.setTarget( P.sin(p.millis() * _autoControl) );
				}
			}
		}
		_easedControlX.update();
		float curControlX = _easedControlX.value();
		_playerOffset = gameHalfWidth * curControlX;
	}
	
	protected void detectKinectPlayers() {
		if( _detectedPlayer == false ) {
			if( _kinectRegion.pixelCount() >= 20 || p.kinectWrapper == null ) {
				_detectedPlayerTime = p.millis();
				_detectedPlayer = true;
				_waitingSpinner.playerEntered();
			}
		} else {
			if( _kinectRegion.pixelCount() < 20 && p.kinectWrapper != null ) {
				_detectedPlayer = false;
				_hasPlayer = false;
				_waitingSpinner.playerLeft();
				_character.setWaitingState();
			}
		}
		if( _detectedPlayer == true && p.millis() - _detectedPlayerTime > 2000 ) {
			if( _hasPlayer == false ) {
				_hasPlayer = true;
				_waitingSpinner.playerDetected();
				_character.setSelectedState();
			}
		}
	}
	
	// draw graphics ------------------------------------------------------------------
	protected void drawGraphicsLayers() {
		DrawUtil.setDrawCenter(pg);
		_mountain.update(_playerOffset);
		_grass.update(_playerOffset);
		drawDroppables();
		_character.update(_playerOffset);
		_dropper.update();
		_waitingSpinner.update();
		_countdownDisplay.updateWithNumber(_countdownTime);
		DrawUtil.setDrawCorner(pg);
		_score.update();
	}
	
	protected void drawDroppables() {
		for( int i=0; i < _droppables.size(); i++ ) {
			_droppables.get(i).update( _playerOffset, _character.shadowY() );
		}
	}
	
}