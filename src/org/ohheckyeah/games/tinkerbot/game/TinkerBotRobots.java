package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;

import processing.core.PGraphics;
import processing.core.PShape;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.math.easing.EasingFloat;
import com.haxademic.core.math.easing.LinearFloat;

public class TinkerBotRobots {

	protected TinkerBot p;
	protected PGraphics pg;

	public enum RobotAnimState {
		WANDERING,
		WAITING,
		MOVING_TO_TARGET,
		OPENING,
		BEAMING,
		CLOSING
	}
	protected RobotAnimState _animState;
	
	// positioning & bar
	protected PShape _barSvg;
	protected float _barSvgWidth;
	protected float _robotX;
	protected EasingFloat _robotXOffset;
	protected float _robotXOffsetHidden;
	
	// end balls
	protected float _ballWidth;
	protected float _ballHeight;

	protected EasingFloat _ballYOffset;
	protected float _ballYOffsetHidden;
	protected float _ballYOffsetShowing;
	
	// face animation
	protected LinearFloat _gunX;
	protected float _gunXHidden;
	protected float _gunXIncOpen;
	protected float _gunXIncClose;

	protected LinearFloat _mouthY;
	protected float _mouthYShowing;
	protected float _mouthYShowingIncOpen;
	protected float _mouthYShowingIncClose;
	
	// vertical movement
	protected LinearFloat _headY;
	protected float _headYIncWander;
	protected float _headYIncDest;
	protected int _wanderTime;
	protected int _animStartTime;
	protected float _shootPosition;
	protected boolean _readyForNewLevel;

	
	
	public TinkerBotRobots() {
		p = (TinkerBot)P.p;
		pg = p.pg;

		_barSvg = p.gameGraphics.robotBar;
		_barSvgWidth = p.svgWidth( p.gameGraphics.robotBar );
		_robotX = pg.width * 0.1f;
		_robotXOffsetHidden = pg.width * -0.3f;
		_robotXOffset = new EasingFloat( _robotXOffsetHidden, 6 );
		
		_ballWidth = p.svgWidth( p.gameGraphics.robotBallTop );
		_ballHeight = p.svgHeight( p.gameGraphics.robotBallTop );
		
		_ballYOffsetHidden = _ballHeight * 1f;
		_ballYOffsetShowing = _ballHeight * 0.1f;
		_ballYOffset = new EasingFloat( _ballYOffsetHidden, 6 );
		
		_animState = RobotAnimState.WAITING;
		_headYIncWander = p.scaleV(3);
		_headYIncDest = p.scaleV(8);
		_headY = new LinearFloat( TinkerBotLayout.PLAYER_Y_CENTER, _headYIncWander );
		_wanderTime = 0;
		_animStartTime = 0;
		_readyForNewLevel = true;
		
		_gunXHidden = p.scaleV(-50);
		_gunXIncOpen = p.scaleV(1);
		_gunXIncClose = p.scaleV(3);
		_gunX = new LinearFloat(_gunXHidden, _gunXIncOpen);
		
		_mouthYShowing = p.scaleV(48);
		_mouthYShowingIncOpen = p.scaleV(6);
		_mouthYShowingIncClose = p.scaleV(2);
		_mouthY = new LinearFloat(0, _mouthYShowingIncOpen);
	}
	
	public void show() {
		_animState = RobotAnimState.WAITING;
		_robotXOffset.setTarget(0);
		_ballYOffset.setTarget(_ballYOffsetShowing);
		_headY.setTarget( TinkerBotLayout.yForPosition( 0 ) );	// reset head to center screen
		_headY.setCurrent( TinkerBotLayout.yForPosition( 0 ) );
	}

	public void hide() {
		_animState = RobotAnimState.WAITING;
		_robotXOffset.setTarget(_robotXOffsetHidden);
		_ballYOffset.setTarget(_ballYOffsetHidden);
		_readyForNewLevel = false;
	}
	
	public void levelStart() {
		_animState = RobotAnimState.WANDERING;
		_readyForNewLevel = false;
		_headY.setInc( _headYIncWander );
	}
	
	public void shootBeam( float yTarget ) {
		_shootPosition = yTarget;

		_animState = RobotAnimState.MOVING_TO_TARGET;
		_headY.setTarget( TinkerBotLayout.yForPosition( _shootPosition ) );
		_headY.setInc( _headYIncDest );
		_animStartTime = p.millis();
	}
	
	protected void openMouth() {
		_animState = RobotAnimState.OPENING;
		_gunX.setInc( _gunXIncOpen );
		_gunX.setTarget(0);
		_mouthY.setInc( _mouthYShowingIncOpen );
		_mouthY.setTarget( _mouthYShowing );
		_animStartTime = p.millis();
	}
	
	protected void beam() {
		_animState = RobotAnimState.BEAMING;
		_animStartTime = p.millis();
	}
	
	protected void closeMouth() {
		_animState = RobotAnimState.CLOSING;
		_gunX.setInc( _gunXIncClose );
		_gunX.setTarget(_gunXHidden);
		_mouthY.setInc( _mouthYShowingIncClose );
		_mouthY.setTarget(0);
	}
	
	protected void beamDone() {
		_readyForNewLevel = true;
	}
	
	protected boolean readyForNewLevel() {
		return _readyForNewLevel;
	}
	
	public void update() {
		// easing
		_robotXOffset.update();
		_ballYOffset.update();
		_gunX.update();
		_mouthY.update();
		_headY.update();
		
		// update animation/movement state
		switch ( _animState ) {
			case WAITING:
				break;

			case WANDERING:
				if( p.millis() > _wanderTime + 1000 ) {
					_wanderTime = p.millis();
					int randPosition = MathUtil.randRange( -TinkerBotLayout.HALF_POSITIONS + 1, TinkerBotLayout.HALF_POSITIONS - 1 );
					_headY.setTarget( TinkerBotLayout.yForPosition( randPosition ) );
				}
				break;
				
			case MOVING_TO_TARGET:
				if( p.millis() > _animStartTime + 400 ) openMouth();
				break;
				
			case OPENING:
				if( _gunX.value() > -2 ) beam();
				break;
				
			case BEAMING:
				if( p.millis() > _animStartTime + 1000 ) closeMouth();
				break;
				
			case CLOSING:
				if( _gunX.value() < _gunXHidden + 2 ) beamDone();
				break;
				
			default: break;
		}
		
		// draw bars
		DrawUtil.setDrawCenter(pg);
		
		// left robot ================================================================
		float leftX = _robotX + _robotXOffset.value();
		
		// draw left bar
		pg.pushMatrix();
		pg.translate( leftX, pg.height * 0.5f );
		pg.shape(_barSvg, 0, 0, _barSvgWidth, pg.height );
		pg.popMatrix();
		
		// draw top robot ball
		pg.pushMatrix();
		pg.translate( leftX, 0 - _ballYOffset.value() );
		pg.shape(p.gameGraphics.robotBallTop, 0, 0, _ballWidth, _ballHeight );
		pg.popMatrix();
		
		// draw bottom robot ball
		pg.pushMatrix();
		pg.translate( leftX, pg.height + _ballYOffset.value() );
		pg.shape(p.gameGraphics.robotBallBottom, 0, 0, _ballWidth, _ballHeight );
		pg.popMatrix();
		
		// draw robot head
		pg.pushMatrix();
		pg.translate( leftX, _headY.value() );
		pg.shape(p.gameGraphics.robotGun, _gunX.value(), 0, p.svgWidth(p.gameGraphics.robotGun), p.svgHeight(p.gameGraphics.robotGun) );
		pg.shape(p.gameGraphics.robotMouth, 0, _mouthY.value(), p.svgWidth(p.gameGraphics.robotMouth), p.svgHeight(p.gameGraphics.robotMouth) );
		pg.shape(p.gameGraphics.robotHead, 0, 0, p.svgWidth(p.gameGraphics.robotHead), p.svgHeight(p.gameGraphics.robotHead) );
		pg.popMatrix();
		
		// right robot ================================================================
		float rightX = (pg.width - _robotX) - _robotXOffset.value();
		
		// draw right bar
		pg.pushMatrix();
		pg.translate( rightX, pg.height * 0.5f );
		pg.rotate( P.PI );
		pg.shape(_barSvg, 0, 0, _barSvgWidth, pg.height );
		pg.popMatrix();

		// draw top robot ball
		pg.pushMatrix();
		pg.translate( rightX, 0 - _ballYOffset.value() );
		pg.rotate( P.PI );
		pg.shape(p.gameGraphics.robotBallBottom, 0, 0, _ballWidth, _ballHeight );
		pg.popMatrix();
		
		// draw bottom robot ball
		pg.pushMatrix();
		pg.translate( rightX, pg.height + _ballYOffset.value() );
		pg.rotate( P.PI );
		pg.shape(p.gameGraphics.robotBallTop, 0, 0, _ballWidth, _ballHeight );
		pg.popMatrix();

		// draw robot head
		pg.pushMatrix();
		pg.translate( rightX, _headY.value() );
		pg.shape(p.gameGraphics.robotGun, _gunX.value(), 0, p.svgWidth(p.gameGraphics.robotGun), p.svgHeight(p.gameGraphics.robotGun) );
		pg.shape(p.gameGraphics.robotMouth, 0, _mouthY.value(), p.svgWidth(p.gameGraphics.robotMouth), p.svgHeight(p.gameGraphics.robotMouth) );
		pg.shape(p.gameGraphics.robotHead, 0, 0, p.svgWidth(p.gameGraphics.robotHead), p.svgHeight(p.gameGraphics.robotHead) );
		pg.popMatrix();
		
	}
}
