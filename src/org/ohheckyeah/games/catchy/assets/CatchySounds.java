package org.ohheckyeah.games.catchy.assets;

import org.ohheckyeah.games.catchy.Catchy;

import com.haxademic.core.app.P;
import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.system.FileUtil;

import ddf.minim.AudioPlayer;

public class CatchySounds {
	
	protected Catchy p;
	
	protected AudioPlayer _introSound;
	protected AudioPlayer _waitingSound;
	protected AudioPlayer _gameplaySound;
	protected AudioPlayer _winSound;
	protected AudioPlayer _ohySound;
	
	protected AudioPlayer _curSoundtrack;
	
	protected AudioPool _soundEffects;

	public static final String STEP_IN = "step-in";
	public static final String STEP_OUT = "step-out";
	public static final String PLAYER_LOCKED = "player-locked";
	public static final String PLAYERS_DETECTED = "players-detected";
	public static final String COUNTDOWN = "countdown";
	public static final String CATCH = "catch";
	public static final String CATCH_BAD = "catch-bad";
	public static final String DROP_MISS = "drop-miss";
	public static final String DROP = "drop";
	
	public CatchySounds() {
		p = (Catchy)P.p;
		
		loadSoundtracks();
		loadSoundEffects();
	}
	
	protected void loadSoundtracks() {
		_introSound = p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/catchy/audio/soundtrack/catchy-intro-loop.wav", 512 );
		_waitingSound = p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/catchy/audio/soundtrack/catchy-waiting-loop.wav", 512 );
		_gameplaySound = p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/catchy/audio/soundtrack/ohy-loop.wav", 512 );
		_winSound = p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/catchy/audio/soundtrack/win.wav", 512 );
		_ohySound = p.minim.loadFile( FileUtil.getHaxademicDataPath() + "audio/ohy-clip-2.wav", 512 );
	}
	
	public void playIntro() { playSoundtrack( _introSound ); }
	public void fadeOutIntro() { _introSound.shiftGain(0, -17, 2000); }
	public void playWaiting() { playSoundtrack( _waitingSound ); }
	public void playGameplay() { playSoundtrack( _gameplaySound ); }
	public void playWin() { playSoundtrack( _winSound ); }
	public void playOHY() { playSoundtrack( _ohySound ); }
	
	protected void playSoundtrack( AudioPlayer newSoundTrack ) {
		if( _curSoundtrack != null ) _curSoundtrack.pause();
		_curSoundtrack = newSoundTrack;
		_curSoundtrack.setGain(0);
		_curSoundtrack.play(0);
		if( _curSoundtrack != _winSound && _curSoundtrack != _ohySound )_curSoundtrack.loop(-1);
	}
	
	public void stopSoundtrack() {
		if( _curSoundtrack != null ) _curSoundtrack.pause();
		_curSoundtrack = null;
	}
		
	public void loadSoundEffects() {
		_soundEffects = new AudioPool( p, p.minim );

		_soundEffects.loadAudioFile( CatchySounds.PLAYER_LOCKED, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/players-detected.wav" );
		_soundEffects.loadAudioFile( CatchySounds.PLAYERS_DETECTED, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/long-note.wav" );
		_soundEffects.loadAudioFile( CatchySounds.STEP_IN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/ta-da-flute-2.wav" );
		_soundEffects.loadAudioFile( CatchySounds.STEP_OUT, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/step-out.wav" );
		_soundEffects.loadAudioFile( CatchySounds.COUNTDOWN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/long-note-end.wav" );
		_soundEffects.loadAudioFile( CatchySounds.CATCH, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/ring-2.wav" );
		_soundEffects.loadAudioFile( CatchySounds.CATCH_BAD, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/boing-tom.wav" );
		_soundEffects.loadAudioFile( CatchySounds.DROP_MISS, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/mistake.wav" );
		_soundEffects.loadAudioFile( CatchySounds.DROP, 1.1f, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/drip-low.wav" );
	}

	public void playSound( String id ) {
		_soundEffects.playSound( id );
	}
}
