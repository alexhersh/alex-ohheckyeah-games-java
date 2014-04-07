package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class CatchyGameMessages {
	
	protected Catchy p;
	
	protected float _messageX = 0;
	protected float _messageYHidden = -150;
	protected float _messageYShowing = 0;
		
	protected CatchyTextMessage _playerDetectionText;
	protected CatchyTextMessage _countdownText;
	protected CatchyTextMessage _winnerText;
	protected CatchyTextMessage _tieText;
	
	protected ElasticFloat _messageWaitingY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);
	protected ElasticFloat _messageCountdownY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);
	protected float _messageWinnerX = 0;
	protected ElasticFloat _messageWinnerY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);
	protected ElasticFloat _messageTieY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);

	public CatchyGameMessages() {
		p = (Catchy)P.p;
		
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
			DrawUtil.setDrawCenter(p);
			p.pushMatrix();
			p.translate( _messageX, _messageWaitingY.val() );
			p.image( _playerDetectionText.image(), 0, 0, _playerDetectionText.image().width, _playerDetectionText.image().height );
			p.popMatrix();
		}

		if( _countdownText != null && _messageCountdownY.val() > 0 ) {
			DrawUtil.setDrawCenter(p);
			p.pushMatrix();
			p.translate( _messageX, _messageCountdownY.val() );
			p.image( _countdownText.image(), 0, 0, _countdownText.image().width, _countdownText.image().height );
			p.popMatrix();
		}

		if( _winnerText != null && _messageWinnerY.val() > 0 ) {
			DrawUtil.setDrawCenter(p);
			p.pushMatrix();
			p.translate( _messageWinnerX, _messageWinnerY.val() );
			p.image( _winnerText.image(), 0, 0, _winnerText.image().width, _winnerText.image().height );
			p.popMatrix();
		}
		
		if( _tieText != null && _messageTieY.val() > 0 ) {
			DrawUtil.setDrawCenter(p);
			p.pushMatrix();
			p.translate( _messageX, _messageTieY.val() );
			p.image( _tieText.image(), 0, 0, _tieText.image().width, _tieText.image().height );
			p.popMatrix();
		}
	}
	
	public void showWaiting() {
		if( _playerDetectionText == null ) {
			_playerDetectionText = new CatchyTextMessage( "step into a zone to join", 60, 550, 90, 526 );
		}
		_messageWaitingY.setTarget(_messageYShowing);
	}
	public void hideWaiting() {
		_messageWaitingY.setTarget(_messageYHidden);
	}
	
	public void showCountdown() {
		if( _countdownText == null ) {
			_countdownText = new CatchyTextMessage( "get ready", 60, 250, 90, 224 );
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
			_winnerText = new CatchyTextMessage( "winner", 60, 250, 90, 173 );
		}
		_messageWinnerY.setTarget(_messageYShowing);
	}
	public void hideWinner() {
		_messageWinnerY.setTarget(_messageYHidden);
	}
	
	public void showTie() {
		if( _tieText == null ) {
			_tieText = new CatchyTextMessage( "tie", 60, 125, 90, 64 );
		}
		_messageTieY.setTarget(_messageYShowing);
	}
	public void hideTie() {
		_messageTieY.setTarget(_messageYHidden);
	}
	
}
