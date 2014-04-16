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
		_introSound = p._minim.loadFile( FileUtil.getHaxademicDataPath() + "games/catchy/audio/soundtrack/catchy-intro-loop.wav", 512 );
		_introSound.loop(-1);
		_introSound.pause();
		
		_waitingSound = p._minim.loadFile( FileUtil.getHaxademicDataPath() + "games/catchy/audio/soundtrack/catchy-waiting-loop.wav", 512 );
		_waitingSound.loop(-1);
		_waitingSound.pause();
		
		_gameplaySound = p._minim.loadFile( FileUtil.getHaxademicDataPath() + "games/catchy/audio/soundtrack/ohy-loop.wav", 512 );
		_gameplaySound.loop(-1);
		_gameplaySound.pause();
		
		_winSound = p._minim.loadFile( FileUtil.getHaxademicDataPath() + "games/catchy/audio/soundtrack/win.wav", 512 );
	}
	
	public void playIntro() { playSoundtrack( _introSound ); }
	public void playWaiting() { playSoundtrack( _waitingSound ); }
	public void playGameplay() { playSoundtrack( _gameplaySound ); }
	public void playWin() { playSoundtrack( _winSound ); }
	
	protected void playSoundtrack( AudioPlayer newSoundTrack ) {
		if( _curSoundtrack != null ) _curSoundtrack.pause();
		_curSoundtrack = newSoundTrack;
		_curSoundtrack.play(0);
	}
	
	public void stopSoundtrack() {
		if( _curSoundtrack != null ) _curSoundtrack.pause();
		_curSoundtrack = null;
	}
		
	public void loadSoundEffects() {
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/big-deep-note.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/boing-tom.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/chirp-flute.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/crash.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/flute-cascade-down.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/long-note.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/low-notes-up.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/mistake.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/notes-down-lower.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/notes-down.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/notes-up.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/ring-2.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/ring.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/ta-da-flute.wav" );
//		_sounds.loadAudioFile( CatchySounds.WIN, 1, FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/ta-da.wav" );
		
		_soundEffects = new AudioPool( p, p._minim );

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
