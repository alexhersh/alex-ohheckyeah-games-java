package org.ohheckyeah.games.tinkerbot.game;

import org.ohheckyeah.games.tinkerbot.TinkerBot;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class TinkerBotGameMessages {
	
	protected TinkerBot p;
	protected PGraphics pg;
	
	protected float _messageX = 0;
	protected float _messageYHidden = -150;
	protected float _messageYShowing = 0;
	protected float _messageYShowingCenter = 0;
	protected float _messageYShowingLower = 0;
	
	protected ElasticFloat _messageWaitingY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);
	protected ElasticFloat _messageCountdownY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);
	protected float _messageWinnerX = 0;
	protected ElasticFloat _messageWinY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);
	protected ElasticFloat _messageFailY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);

	public TinkerBotGameMessages() {
		p = (TinkerBot) P.p;
		pg = p.pg;
		
		// calculate responsive sizes
		_messageX = p.width / 2f;
		_messageYHidden = p.scaleV(-250);
		_messageYShowing = pg.height * .2f;
		_messageYShowingCenter = pg.height * .5f;
		_messageYShowingLower = pg.height * .75f;
	}
	
	public void update() {
		DrawUtil.setDrawCenter(pg);

		_messageWaitingY.update();
		_messageCountdownY.update();
		_messageWinY.update();
		_messageFailY.update();

		if( _messageWaitingY.val() > 0 ) {
			pg.shape( p.gameGraphics.textStepIntoZones, _messageX, _messageWaitingY.val(), p.svgWidth(p.gameGraphics.textStepIntoZones), p.svgHeight(p.gameGraphics.textStepIntoZones) );
		}
		if( _messageCountdownY.val() > 0 ) {
			pg.shape( p.gameGraphics.textGetReady, _messageX, _messageCountdownY.val(), p.svgWidth(p.gameGraphics.textGetReady), p.svgHeight(p.gameGraphics.textGetReady) );
		}
		if( _messageFailY.val() > 0 ) {
			pg.shape( p.gameGraphics.textFail, _messageX, _messageFailY.val(), p.svgWidth(p.gameGraphics.textFail), p.svgHeight(p.gameGraphics.textFail) );
		}
		if( _messageWinY.val() > 0 ) {
			pg.shape( p.gameGraphics.textWin, _messageX, _messageWinY.val(), p.svgWidth(p.gameGraphics.textWin), p.svgHeight(p.gameGraphics.textWin) );
		}
	}
	
	public void showWaiting() {
		_messageWaitingY.setTarget(_messageYShowing);
	}
	public void hideWaiting() {
		_messageWaitingY.setTarget(_messageYHidden);
	}
	
	public void showCountdown() {
		_messageCountdownY.setTarget(_messageYShowingCenter);
	}
	public void hideCountdown() {
		_messageCountdownY.setTarget(_messageYHidden);
	}
	
	public void showFail() {
		_messageFailY.setTarget(_messageYShowingCenter);
	}
	public void hideFail() {
		_messageFailY.setTarget(_messageYHidden);
	}
	
	public void setWinnerX( float winnerX ) {		
		_messageWinnerX = winnerX;
	}
	public void showWin() {
		_messageWinY.setTarget(_messageYShowingCenter);
	}
	public void hideWin() {
		_messageWinY.setTarget(_messageYHidden);
	}
	
	
}
