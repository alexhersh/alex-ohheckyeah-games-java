package org.ohheckyeah.games.graduationday.game;

import org.ohheckyeah.games.graduationday.GraduationDay;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class GraduationDayGameMessages {
	
	protected GraduationDay p;
	protected PGraphics pg;
	
	protected float _messageX = 0;
	protected float _messageYHidden = -150;
	protected float _messageYShowing = 0;
	protected float _messageYShowingCenter = 0;
	protected float _messageYShowingLower = 0;
	
	protected ElasticFloat _messageWaitingY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);
	protected ElasticFloat _messageCountdownScale = new ElasticFloat(0, 0.71f, 0.16f);
	protected float _messageWinnerX = 0;
	protected ElasticFloat _messageWinScale = new ElasticFloat(0, 0.71f, 0.16f);
	protected ElasticFloat _messageFailScale = new ElasticFloat(0, 0.71f, 0.16f);
	protected ElasticFloat _messageGameOverScale = new ElasticFloat(0, 0.71f, 0.16f);

	protected int _winFailTime = 0;
	
	public GraduationDayGameMessages() {
		p = (GraduationDay) P.p;
		pg = p.pg;
		
		// calculate responsive sizes
		_messageX = pg.width / 2f;
		_messageYHidden = p.scaleV(-250);
		_messageYShowing = pg.height * .2f;
		_messageYShowingCenter = pg.height * .5f;
		_messageYShowingLower = pg.height * .75f;
	}
	
	public void update() {
		DrawUtil.setDrawCenter(pg);

		_messageWaitingY.update();
		_messageCountdownScale.update();
		_messageWinScale.update();
		_messageFailScale.update();
		_messageGameOverScale.update();
		
		if( _winFailTime != 0 && p.millis() > _winFailTime + 2000 ) {
			_winFailTime = 0;
			hideFail();
			hideWin();
		}

		if( _messageWaitingY.val() > 0 ) {
			pg.shape( p.gameGraphics.textStepIntoZones, _messageX, _messageWaitingY.val(), p.svgWidth(p.gameGraphics.textStepIntoZones), p.svgHeight(p.gameGraphics.textStepIntoZones) );
		}
		if( _messageCountdownScale.val() > 0.1 ) {
			pg.shape( p.gameGraphics.textGetReady, _messageX, _messageYShowingCenter, p.svgWidth(p.gameGraphics.textGetReady) * _messageCountdownScale.val(), p.svgHeight(p.gameGraphics.textGetReady) * _messageCountdownScale.val());
		}
		if( _messageFailScale.val() > 0.1 ) {
			pg.shape( p.gameGraphics.textFail, _messageX, _messageYShowingCenter, p.svgWidth(p.gameGraphics.textFail) * _messageFailScale.val(), p.svgHeight(p.gameGraphics.textFail) * _messageFailScale.val());
		}
		if( _messageWinScale.val() > 0.1 ) {
			pg.shape( p.gameGraphics.textWin, _messageX, _messageYShowingCenter, p.svgWidth(p.gameGraphics.textWin) * _messageWinScale.val(), p.svgHeight(p.gameGraphics.textWin) * _messageWinScale.val() );
		}
		if( _messageGameOverScale.val() > 0.1 ) {
			pg.shape( p.gameGraphics.textGameOver, _messageX, _messageYShowingCenter, p.svgWidth(p.gameGraphics.textGameOver) * _messageGameOverScale.val(), p.svgHeight(p.gameGraphics.textGameOver) * _messageGameOverScale.val() );
		}
	}
	
	public void showWaiting() {
		_messageWaitingY.setTarget(_messageYShowing);
	}
	public void hideWaiting() {
		_messageWaitingY.setTarget(_messageYHidden);
	}
	
	public void showCountdown() {
		_messageCountdownScale.setTarget(1);
	}
	public void hideCountdown() {
		_messageCountdownScale.setTarget(0);
	}
	
	public void showFail() {
		_messageFailScale.setTarget(1f);
		_winFailTime = p.millis();
		hideWin();
	}
	public void hideFail() {
		_messageFailScale.setTarget(0f);
	}
	
	public void setWinnerX( float winnerX ) {		
		_messageWinnerX = winnerX;
	}
	public void showWin() {
		_messageWinScale.setTarget(1f);
		_winFailTime = p.millis();
		hideFail();
	}
	public void hideWin() {
		_messageWinScale.setTarget(0f);
	}
	
	public void showGameOver() {
		_messageGameOverScale.setTarget(1);
	}
	public void hideGameOver() {
		_messageGameOverScale.setTarget(0);
	}
	
	
}
