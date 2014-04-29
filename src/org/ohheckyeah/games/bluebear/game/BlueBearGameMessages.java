package org.ohheckyeah.games.bluebear.game;

import org.ohheckyeah.games.bluebear.BlueBear;

import processing.core.PGraphics;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class BlueBearGameMessages {
	
	protected BlueBear p;
	protected PGraphics pg;
	
	protected float _messageX = 0;
	protected float _messageYHidden = -150;
	protected float _messageYShowing = 0;
		
	protected BlueBearTextMessage _playerDetectionText;
	protected BlueBearTextMessage _countdownText;
	protected BlueBearTextMessage _winnerText;
	protected BlueBearTextMessage _tieText;
	
	protected ElasticFloat _messageWaitingY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);
	protected ElasticFloat _messageCountdownY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);
	protected float _messageWinnerX = 0;
	protected ElasticFloat _messageWinnerY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);
	protected ElasticFloat _messageTieY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);

	public BlueBearGameMessages() {
		p = (BlueBear) P.p;
		pg = p.pg;
		
		// calculate responsive sizes
		_messageX = p.width / 2f;
		_messageYHidden = p.scaleV(-150);
		_messageYShowing = p.height * .2f;
		
	}
	
	public void update() {
		_messageWaitingY.update();
		_messageCountdownY.update();
		_messageWinnerY.update();
		_messageTieY.update();

		if( _playerDetectionText != null && _messageWaitingY.val() > 0 ) {
			DrawUtil.setDrawCenter(pg);
			pg.pushMatrix();
			pg.translate( _messageX, _messageWaitingY.val() );
			pg.image( _playerDetectionText.image(), 0, 0, _playerDetectionText.image().width, _playerDetectionText.image().height );
			pg.popMatrix();
		}

		if( _countdownText != null && _messageCountdownY.val() > 0 ) {
			DrawUtil.setDrawCenter(pg);
			pg.pushMatrix();
			pg.translate( _messageX, _messageCountdownY.val() );
			pg.image( _countdownText.image(), 0, 0, _countdownText.image().width, _countdownText.image().height );
			pg.popMatrix();
		}

		if( _winnerText != null && _messageWinnerY.val() > 0 ) {
			DrawUtil.setDrawCenter(pg);
			pg.pushMatrix();
			pg.translate( _messageWinnerX, _messageWinnerY.val() );
			pg.image( _winnerText.image(), 0, 0, _winnerText.image().width, _winnerText.image().height );
			pg.popMatrix();
		}
		
		if( _tieText != null && _messageTieY.val() > 0 ) {
			DrawUtil.setDrawCenter(pg);
			pg.pushMatrix();
			pg.translate( _messageX, _messageTieY.val() );
			pg.image( _tieText.image(), 0, 0, _tieText.image().width, _tieText.image().height );
			pg.popMatrix();
		}
	}
	
	public void showWaiting() {
		if( _playerDetectionText == null ) {
			_playerDetectionText = new BlueBearTextMessage( "step into a zone to join", 60, 550, 90, 526 );
		}
		_messageWaitingY.setTarget(_messageYShowing);
	}
	public void hideWaiting() {
		_messageWaitingY.setTarget(_messageYHidden);
	}
	
	public void showCountdown() {
		if( _countdownText == null ) {
			_countdownText = new BlueBearTextMessage( "get ready", 60, 250, 90, 224 );
		}
		_messageCountdownY.setTarget(_messageYShowing);
	}
	public void hideCountdown() {
		_messageCountdownY.setTarget(_messageYHidden);
	}
	
	public void setWinnerX( float winnerX ) {		
		_messageWinnerX = winnerX;
	}
	public void showWinner() {
		if( _winnerText == null ) {
			_winnerText = new BlueBearTextMessage( "winner", 60, 250, 90, 173 );
		}
		_messageWinnerY.setTarget(_messageYShowing);
	}
	public void hideWinner() {
		_messageWinnerY.setTarget(_messageYHidden);
	}
	
	public void showTie() {
		if( _tieText == null ) {
			_tieText = new BlueBearTextMessage( "tie", 60, 125, 90, 64 );
		}
		_messageTieY.setTarget(_messageYShowing);
	}
	public void hideTie() {
		_messageTieY.setTarget(_messageYHidden);
	}
	
}
