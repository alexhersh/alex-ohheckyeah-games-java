package org.ohheckyeah.games.catchy.game;

import org.ohheckyeah.games.catchy.Catchy;

import com.haxademic.core.app.P;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.math.easing.ElasticFloat;

public class CatchyGameMessages {
	
	protected Catchy p;
	
	protected float _messageX = 0;
	protected float _messageYHidden = 0;
	protected float _messageYShowing = 0;
		
	protected CatchyTextMessage _playerDetectionText;
	protected CatchyTextMessage _countdownText;
	protected CatchyTextMessage _winnerText;
	
	protected ElasticFloat _messageWaitingY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);
	protected ElasticFloat _messageCountdownY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);
	protected ElasticFloat _messageWinnerY = new ElasticFloat(_messageYHidden, 0.71f, 0.16f);

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
		_messageWaitingY.update();

		if( _messageWaitingY.val() > 0 ) {
			DrawUtil.setDrawCenter(p);
			p.pushMatrix();
			p.translate( _messageX, _messageWaitingY.val() );
			p.image( _playerDetectionText.image(), 0, 0, _playerDetectionText.image().width, _playerDetectionText.image().height );
			p.popMatrix();
		}

		if( _messageCountdownY.val() > 0 ) {
			DrawUtil.setDrawCenter(p);
			p.pushMatrix();
			p.translate( _messageX, _messageCountdownY.val() );
			p.image( _countdownText.image(), 0, 0, _countdownText.image().width, _countdownText.image().height );
			p.popMatrix();
		}

		if( _messageWinnerY.val() > 0 ) {
			DrawUtil.setDrawCenter(p);
			p.pushMatrix();
			p.translate( _messageX, _messageWinnerY.val() );
			p.image( _winnerText.image(), 0, 0, _winnerText.image().width, _winnerText.image().height );
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
			_countdownText = new CatchyTextMessage( "get ready", 60, 250, 90, 231 );
		}
		_messageCountdownY.setTarget(_messageYShowing);
	}
	public void hideCountdown() {
		_messageCountdownY.setTarget(_messageYHidden);
	}
	
	public void showWinner() {
		if( _winnerText == null ) {
			_winnerText = new CatchyTextMessage( "winner", 60, 175, 90, 154 );
		}
		_messageWinnerY.setTarget(_messageYShowing);
	}
	public void hideWinner() {
		_messageWinnerY.setTarget(_messageYHidden);
	}
	
}
