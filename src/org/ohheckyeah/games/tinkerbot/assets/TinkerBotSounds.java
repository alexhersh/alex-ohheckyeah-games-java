package org.ohheckyeah.games.tinkerbot.assets;

import org.ohheckyeah.shared.assets.OHYSounds;

import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.system.FileUtil;

import ddf.minim.AudioPlayer;

public class TinkerBotSounds
extends OHYSounds {
	
	protected AudioPlayer _waitingSound;
	protected AudioPlayer _gameplaySound;
	protected AudioPlayer _gameOverSound;
	
	public static final String STEP_IN = "step-in";
	public static final String STEP_OUT = "step-out";
	public static final String PLAYER_LOCKED = "player-locked";
	public static final String PLAYERS_DETECTED = "players-detected";
	public static final String COUNTDOWN = "countdown";
	public static final String GAMEPLAY_START = "gameplay-start";
	public static final String LASER = "laser";
	public static final String ERROR = "error";
	
	public TinkerBotSounds() {
		super();
		
		loadSoundtracks();
		loadSoundEffects();
	}
	
	protected void loadSoundtracks() {
		super.loadSoundtracks();
		_introSound = 		p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/soundtrack/title-screen.wav", 512 );
		_waitingSound = 	p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/soundtrack/waiting-loop.wav", 512 );
		_gameplaySound = 	p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/soundtrack/tinkerbot-soundtrack.wav", 512 );
		_gameOverSound = 	p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/soundtrack/game-over.wav", 512 );
	}
	
	public void playIntro() { playSoundtrack( _introSound, true ); }
	public void fadeOutIntro() { _introSound.shiftGain(0, -17, 2000); }
	
	public void playWaiting() { playSoundtrack( _waitingSound, true ); }
	public void playGameplay() { playSoundtrack( _gameplaySound, true ); }
	public void playGameOver() { playSoundtrack( _gameOverSound, false ); }
	
	public void loadSoundEffects() {
		_soundEffects = new AudioPool( p, p.minim );

		_soundEffects.loadAudioFile( TinkerBotSounds.PLAYER_LOCKED, 1, 		FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/trinkle.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.PLAYERS_DETECTED, 1, 	FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/player-detected.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.STEP_IN, 1, 			FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/player-enter.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.STEP_OUT, 1, 			FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/player-leave-2.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.COUNTDOWN, 1f,			FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/countdown.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.GAMEPLAY_START, 1f,	FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/crash.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.LASER, 1f,				FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/laser.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.ERROR, 1f,				FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/error-big-deep-note.wav" );
	}

}
