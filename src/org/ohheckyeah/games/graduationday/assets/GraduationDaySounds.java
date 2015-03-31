package org.ohheckyeah.games.graduationday.assets;

import org.ohheckyeah.shared.assets.OHYSounds;

import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.system.FileUtil;

import ddf.minim.AudioPlayer;

public class GraduationDaySounds
extends OHYSounds {
	
	protected AudioPlayer _soundtrack;
	
	public static final String STEP_IN = "step-in";
	public static final String STEP_OUT = "step-out";
	public static final String PLAYER_LOCKED = "player-locked";
	public static final String PLAYERS_DETECTED = "players-detected";
	public static final String COUNTDOWN = "countdown";
	public static final String GAMEPLAY_START = "gameplay-start";
	public static final String LASER = "laser";
	public static final String ERROR = "error";
	
	public GraduationDaySounds() {
		super();
		
		loadSoundtracks();
		loadSoundEffects();
	}
	
	protected void loadSoundtracks() {
		super.loadSoundtracks();
		_soundtrack = p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/graduationday/audio/soundtrack/graduation_day_game_theme.wav", 512 );
		_introSound = p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/graduationday/audio/soundtrack/graduation_day_game_theme.wav", 512 );
	}
	
	
	public void playIntro() { playSoundtrack( _introSound, true ); }
	public void fadeOutIntro() { _introSound.shiftGain(0, -17, 2000); }
	
	public void playWaiting() { playSoundtrack( _soundtrack, true ); }
	public void playGameplay() { playSoundtrack( _soundtrack, true ); }
	public void playGameOver() { playSoundtrack( _soundtrack, false ); }
	
	public void loadSoundEffects() {
		_soundEffects = new AudioPool( p, p.minim );

		_soundEffects.loadAudioFile( GraduationDaySounds.PLAYER_LOCKED, 1, 		FileUtil.getHaxademicDataPath() + "games/graduationday/audio/sfx/trinkle.wav" );
		_soundEffects.loadAudioFile( GraduationDaySounds.PLAYERS_DETECTED, 1, 	FileUtil.getHaxademicDataPath() + "games/graduationday/audio/sfx/player-detected.wav" );
		_soundEffects.loadAudioFile( GraduationDaySounds.STEP_IN, 1, 			FileUtil.getHaxademicDataPath() + "games/graduationday/audio/sfx/player-enter.wav" );
		_soundEffects.loadAudioFile( GraduationDaySounds.STEP_OUT, 1, 			FileUtil.getHaxademicDataPath() + "games/graduationday/audio/sfx/player-leave-2.wav" );
		_soundEffects.loadAudioFile( GraduationDaySounds.COUNTDOWN, 1f,			FileUtil.getHaxademicDataPath() + "games/graduationday/audio/sfx/countdown.wav" );
		_soundEffects.loadAudioFile( GraduationDaySounds.GAMEPLAY_START, 1f,	FileUtil.getHaxademicDataPath() + "games/graduationday/audio/sfx/crash.wav" );
		_soundEffects.loadAudioFile( GraduationDaySounds.LASER, 1f,				FileUtil.getHaxademicDataPath() + "games/graduationday/audio/sfx/laser.wav" );
		_soundEffects.loadAudioFile( GraduationDaySounds.ERROR, 1f,				FileUtil.getHaxademicDataPath() + "games/graduationday/audio/sfx/error-big-deep-note.wav" );
	}

}
