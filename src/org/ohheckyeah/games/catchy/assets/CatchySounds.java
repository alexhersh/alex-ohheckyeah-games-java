package org.ohheckyeah.games.catchy.assets;

import org.ohheckyeah.shared.assets.OHYSounds;

import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.system.FileUtil;

import ddf.minim.AudioPlayer;

public class CatchySounds
extends OHYSounds {
	
	protected AudioPlayer _waitingSound;
	protected AudioPlayer _gameplaySound;
	protected AudioPlayer _winSound;

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
		super();
		
		loadSoundtracks();
		loadSoundEffects();
	}
	
	protected void loadSoundtracks() {
		super.loadSoundtracks();
		_introSound = 		p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/catchy/audio/soundtrack/catchy-intro-loop.wav", 512 );
		_waitingSound = 	p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/catchy/audio/soundtrack/catchy-waiting-loop.wav", 512 );
		_gameplaySound = 	p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/catchy/audio/soundtrack/ohy-loop.wav", 512 );
		_winSound = 		p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/catchy/audio/soundtrack/win.wav", 512 );
	}
	
	public void playIntro() { playSoundtrack( _introSound, true ); }
	public void fadeOutIntro() { _introSound.shiftGain(0, -17, 2000); }
	
	public void playWaiting() { playSoundtrack( _waitingSound, true); }
	public void playGameplay() { playSoundtrack( _gameplaySound, true ); }
	public void playWin() { playSoundtrack( _winSound, false ); }
	

	public void loadSoundEffects() {
		_soundEffects = new AudioPool( p, p.minim );

		_soundEffects.loadAudioFile( CatchySounds.PLAYER_LOCKED, 1, 	FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/players-detected.wav" );
		_soundEffects.loadAudioFile( CatchySounds.PLAYERS_DETECTED, 1, 	FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/long-note.wav" );
		_soundEffects.loadAudioFile( CatchySounds.STEP_IN, 1, 			FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/ta-da-flute-2.wav" );
		_soundEffects.loadAudioFile( CatchySounds.STEP_OUT, 1, 			FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/step-out.wav" );
		_soundEffects.loadAudioFile( CatchySounds.COUNTDOWN, 1, 		FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/long-note-end.wav" );
		_soundEffects.loadAudioFile( CatchySounds.CATCH, 1, 			FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/ring-2.wav" );
		_soundEffects.loadAudioFile( CatchySounds.CATCH_BAD, 1, 		FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/boing-tom.wav" );
		_soundEffects.loadAudioFile( CatchySounds.DROP_MISS, 1, 		FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/mistake.wav" );
		_soundEffects.loadAudioFile( CatchySounds.DROP, 1.1f, 			FileUtil.getHaxademicDataPath() + "games/catchy/audio/sfx/drip-low.wav" );
	}

}
